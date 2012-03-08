/**
 *
 */
package org.datasift.streamconsumer;

import java.io.BufferedReader;
import java.io.IOException;
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
			Interaction i = new Interaction(line);
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
					_consumer.onDeleted(i);
				} else {
					_consumer.onInteraction(i);
				}
			}
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
			BufferedReader reader = null;
			if (getConsumerState() == StreamConsumer.STATE_RUNNING) {
				// Attempt to connect and start processing incoming interactions
				DefaultHttpClient client = new DefaultHttpClient();
				try {
					HttpGet get = new HttpGet("http://"
							+ _user.getStreamBaseURL() + _definition.getHash());
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
								String line = reader.readLine();
								if (line == null) {
									// If the line is null then the connection has closed
									// Break out the loop and auto reconnect if enabled
									break;
	
								} else if (line.length() > 10) {
									// If the line is longer than a length
									// indicator, process it
									processLine(line);
								}
							}
						} else if (statusCode >= 400 && statusCode < 500
								&& statusCode != 420) {
							// Connection was refused
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
				} catch (EInvalidData e) {
					reason = e.getMessage();
					stopConsumer();
				} catch (EAccessDenied e) {
					reason = e.getMessage();
					stopConsumer();
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

		onStopped(reason);
	}
}
