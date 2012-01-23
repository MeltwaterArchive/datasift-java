/**
 *
 */
package org.datasift.streamconsumer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.datasift.*;
import org.json.JSONException;

/**
 * @author MediaSift
 * @version 0.1
 */
public class HttpMultiThread extends Thread {
	private HttpMulti _consumer = null;
	private User _user = null;
	private ArrayList<String> _hashes = null;
	private boolean _auto_reconnect = false;
	private boolean _kill_requested = false;

	public HttpMultiThread(HttpMulti http, User user, ArrayList<String> hashes) {
		_consumer = http;
		_user = user;
		_hashes = hashes;
	}

	public void setAutoReconnect(boolean auto_reconnect) {
		_auto_reconnect = auto_reconnect;
	}

	public synchronized int getConsumerState() {
		return _consumer.getState();
	}

	public synchronized void processLine(String line) {
		try {
			// Extract the hash
			JSONdn data = new JSONdn(line);
			_consumer.onMultiInteraction(data.getStringVal("hash"), new Interaction(data.getJSONObject("data").toString()));
		} catch (JSONException e) {
			// Ignore
		} catch (EInvalidData e) {
			// Ignore
		}
	}

	public synchronized void requestKill() {
		_kill_requested = true;
	}

	public synchronized void stopConsumer() {
		try {
			_consumer.stop();
		} catch (EInvalidData e) {
		}
	}

	public synchronized void onStopped(String reason) {
		if (!_kill_requested) {
			try {
				_consumer.onStopped(reason);
			} catch (EInvalidData e) {
				// Ignore
			}
		}
	}

	public void run() {
		int reconnect_delay = 0;
		String reason = "";
		do {
			if (_kill_requested) return;

			// Delay before attempting a reconnect
			if (getConsumerState() == StreamConsumer.STATE_RUNNING
					&& reconnect_delay > 0) {
				try {
					Thread.sleep(reconnect_delay * 1000);
				} catch (Exception e) {
				}
			}

			// If we're still running...
			BufferedReader reader = null;
			if (getConsumerState() == StreamConsumer.STATE_RUNNING) {
				// Attempt to connect and start processing incoming interactions
				try {
					DefaultHttpClient client = new DefaultHttpClient();
					String url = "http://"
							+ _user.getStreamBaseURL() + "multi?hashes=" + _hashes.toString().replace(", ", ",").replace("[", "").replace("]", "");
					HttpGet get = new HttpGet(url);
					get.addHeader("authorization", _user.getUsername() + ":" + _user.getAPIKey());
					HttpResponse response = client.execute(get);
					int statusCode = response.getStatusLine().getStatusCode();
					if (statusCode == 200) {
						// Reset the reconnect delay
						reconnect_delay = 0;
						// Get a stream reader
						reader = new BufferedReader(
								new InputStreamReader(response.getEntity()
										.getContent()));
						// While we're running, get a line
						while (getConsumerState() == StreamConsumer.STATE_RUNNING) {
							if (_kill_requested) return;
							String line = reader.readLine();
							// If the line length is bigger than a tick or an
							// empty line, process it
							if (line.length() > 100) {
								processLine(line);
							}
						}
					} else if (statusCode == 404) {
						// Hash not found!
						reason = "Hash not found!";
						stopConsumer();
					} else {
						// Connection failed, back off a bit and try again
						// Timings from http://dev.datasift.com/docs/streaming-api
						if (reconnect_delay == 0) {
							reconnect_delay = 10;
						} else if (reconnect_delay < 240) {
							reconnect_delay *= 2;
						} else {
							reason = "Connection failed: "
									+ statusCode
									+ " "
									+ response.getStatusLine()
											.getReasonPhrase();
							_consumer.stop();
						}
					}
				} catch (Exception e) {
					reason = "";
				}
			}

			if (reason.length() == 0
					&& getConsumerState() == StreamConsumer.STATE_RUNNING
					&& _auto_reconnect) {
				// Connection failed or timed out
				// Timings from http://dev.datasift.com/docs/streaming-api
				if (reconnect_delay == 0) {
					reconnect_delay = 1;
				} else if (reconnect_delay < 16) {
					reconnect_delay++;
				} else {
					reason = "Connection failed due to a network error";
					stopConsumer();
				}
			}

			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					// Deliberately ignored - this exception means it's not
					// open so can't be closed which is not something we care
					// about!
				}
			}
		} while (getConsumerState() == StreamConsumer.STATE_RUNNING
				&& _auto_reconnect);

		if (reason.length() == 0) {
			if (getConsumerState() == StreamConsumer.STATE_STOPPING) {
				reason = "Stop requested";
			} else {
				reason = "Connection dropped, reason unknown";
			}
		}

		if (_kill_requested) return;

		onStopped(reason);
	}
}
