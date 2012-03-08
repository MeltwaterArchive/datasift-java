/**
 *
 */
package org.datasift.streamconsumer;

import java.io.BufferedReader;
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
			
			Interaction i = new Interaction(data.getJSONObject("data").toString());

			if (i.has("status")) {
				String status = i.getStringVal("status");
				if (status == "error") {
					_consumer.onError(i.getStringVal("message"));
				} else if (status == "warning") {
					_consumer.onWarning(i.getStringVal("message"));
				} else {
					// Should be a tick, ignore it
				}
			} else {
				if (i.has("deleted")) {
					_consumer.onMultiDeleted(data.getStringVal("hash"), i);
				} else {
					_consumer.onMultiInteraction(data.getStringVal("hash"), i);
				}
			}
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
				DefaultHttpClient client = new DefaultHttpClient();
				String url = "http://"
						+ _user.getStreamBaseURL() + "multi?hashes=" + _hashes.toString().replace(", ", ",").replace("[", "").replace("]", "");
				HttpGet get = new HttpGet(url);
				try {
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
							// If the line is longer than a length indicator,
							// process it
							if (line.length() > 10) {
								processLine(line);
							}
						}
					} else if (statusCode >= 400 && statusCode < 500
							&& statusCode != 420) {
						// Connection was refused, but not because we were
						// rate-limited
						reader = new BufferedReader(new InputStreamReader(
								response.getEntity().getContent()));
						// Read the status line
						String line = reader.readLine();
						// Parse the line and get the error message if
						// present
						JSONdn data = new JSONdn(line);
						if (data.has("message")) {
							reason = data.getStringVal("message");
						} else {
							reason = "Connection refused: "
									+ statusCode
									+ " "
									+ response.getStatusLine()
											.getReasonPhrase();
						}
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
							stopConsumer();
						}
					}
				} catch (Exception e) {
					reason = "";
				} finally {
					// Clean up the connection
					try {
						get.abort();
						client.getConnectionManager().shutdown();
					} catch (Exception e) {
						// Ignore any issues with this - we can't really do
						// anything sensible with problems shutting down the
						// connection.
					}
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
