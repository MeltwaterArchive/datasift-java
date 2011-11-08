/**
 * 
 */
package org.datasift.streamconsumer;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.datasift.*;
import org.json.JSONException;

/**
 * @author MediaSift
 * @version 0.1
 */
public class HttpThread extends Thread {
	private Http _consumer = null;
	private User _user = null;
	private Definition _definition = null;
	private boolean _auto_reconnect = false;

	public HttpThread(Http http, User user, Definition definition) {
		_consumer = http;
		_user = user;
		_definition = definition;
	}

	public void setAutoReconnect(boolean auto_reconnect) {
		_auto_reconnect = auto_reconnect;
	}

	public synchronized int getConsumerState() {
		return _consumer.getState();
	}

	public synchronized void processLine(String line) {
		try {
			_consumer.onInteraction(new Interaction(line));
		} catch (JSONException e) {
			// Ignore
		} catch (EInvalidData e) {
			// Ignore
		}
	}

	public synchronized void stopConsumer() {
		try {
			_consumer.stop();
		} catch (EInvalidData e) {
		}
	}

	public synchronized void onStopped(String reason) {
		try {
			_consumer.onStopped(reason);
		} catch (EInvalidData e) {
			// Ignore
		}
	}

	public void run() {
		int reconnect_delay = 0;
		String reason = "";
		do {
			// Delay before attempting a reconnect
			if (getConsumerState() == StreamConsumer.STATE_RUNNING
					&& reconnect_delay > 0) {
				try {
					Thread.sleep(reconnect_delay * 1000);
				} catch (Exception e) {
				}
			}

			// If we're still running...
			if (getConsumerState() == StreamConsumer.STATE_RUNNING) {
				// Attempt to connect and start processing incoming interactions
				try {
					DefaultHttpClient client = new DefaultHttpClient();
					HttpGet get = new HttpGet("http://"
							+ _user.getStreamBaseURL() + _definition.getHash());
					get.addHeader("authorization", _user.getUsername() + ":" + _user.getAPIKey());
					HttpResponse response = client.execute(get);
					int statusCode = response.getStatusLine().getStatusCode();
					if (statusCode == 200) {
						// Reset the reconnect delay
						reconnect_delay = 0;
						// Get a stream reader
						BufferedReader reader = new BufferedReader(
								new InputStreamReader(response.getEntity()
										.getContent()));
						// While we're running, get a line
						while (getConsumerState() == StreamConsumer.STATE_RUNNING) {
							String line = reader.readLine();
							System.out.println(line);
							if (line == null) {
								// If the line is null then the connection has closed
								// Break out the loop and auto reconnect if enabled
								break;
								
							} else if (line.length() > 100) {
								// If the line length is bigger than a tick or an
								// empty line, process it
								processLine(line);
							}
						}
					} else if (statusCode == 404) {
						// Hash not found!
						reason = "Hash not found!";
						_consumer.stop();
					} else {
						// Connection failed, back off a bit and try again
						// Timings from
						// http://support.datasift.net/help/kb/rest-api/http-streaming-api
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
				// Timings from
				// http://support.datasift.net/help/kb/rest-api/http-streaming-api
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

		onStopped(reason);
	}
}
