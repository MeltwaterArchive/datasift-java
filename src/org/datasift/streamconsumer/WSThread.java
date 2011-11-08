/**
 * 
 */
package org.datasift.streamconsumer;

import java.net.URI;
import java.net.URISyntaxException;

import org.datasift.*;
import org.json.JSONException;

// From http://code.google.com/p/weberknecht/
// Slightly modified to enable sending of the DataSift auth header
import de.roderick.weberknecht.WebSocket;
import de.roderick.weberknecht.WebSocketConnection;
import de.roderick.weberknecht.WebSocketEventHandler;
import de.roderick.weberknecht.WebSocketException;
import de.roderick.weberknecht.WebSocketMessage;

/**
 * @author MediaSift
 * @version 0.1
 */
public class WSThread extends Thread {
	private WS _consumer = null;
	private User _user = null;
	private boolean _auto_reconnect = false;
	private WebSocket _ws = null;
	
	public WSThread(WS http, User user) throws WebSocketException, URISyntaxException {
		_consumer = http;
		_user = user;
		
		_ws = new WebSocketConnection(new URI("ws://" + _user.getStreamBaseURL()));
		_ws.addHeader("Authorization: " + _user.getUsername() + ":" + _user.getAPIKey());
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
	
	public synchronized void restartConsumer() {
		_consumer.restart();
	}

	public synchronized void stopConsumer() {
		try {
			_consumer.stop();
		} catch (EInvalidData e) {
		}
	}
	
	public synchronized void onRestarted() {
		_consumer.onRestarted();
	}

	public synchronized void onStopped(String reason) {
		try {
			_consumer.onStopped(reason);
		} catch (EInvalidData e) {
			// Ignore
		}
	}

	public synchronized void subscribe(String hash) throws EAPIError {
		try {
			_ws.send("{\"action\":\"subscribe\",\"hash\":\"" + hash + "\"}");
		} catch (WebSocketException e) {
			throw new EAPIError(e.getMessage());
		}
	}
	
	public synchronized void unsubscribe(String hash) throws EAPIError {
		try {
			_ws.send("{\"action\":\"unsubscribe\",\"hash\":\"" + hash + "\"}");
		} catch (WebSocketException e) {
			throw new EAPIError(e.getMessage());
		}
	}

	public void run() {
		if (getConsumerState() == StreamConsumer.STATE_RESTARTING) {
			onRestarted();
		}
		int reconnect_delay = 0;
		String reason = "";
		do {
			// Delay before attempting a reconnect
			if ((getConsumerState() == StreamConsumer.STATE_RUNNING ||
				getConsumerState() == StreamConsumer.STATE_RESTARTING)
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
					// Register Event Handlers
					_ws.setEventHandler(new WebSocketEventHandler() {
						public void onOpen()
						{
							// Socket connected
						}
						                
						public void onMessage(WebSocketMessage message)
						{
							// Message received
							String line = message.getText();
							System.out.println(line);
							if (line.length() > 100) {
								processLine(line);
							}
						}
						                
						public void onClose()
						{
							// Socket closed
							if (getConsumerState() == StreamConsumer.STATE_RUNNING) {
								if (_auto_reconnect) {
									restartConsumer();
								} else {
									stopConsumer();
								}
							}
						}
					});
					
					// Establish WebSocket Connection
					_ws.connect();
					
					while (getConsumerState() == StreamConsumer.STATE_RUNNING) {
						Thread.sleep(5000);
					}

					reason = "Socket disconnected";
				} catch (WebSocketException e) {
					_auto_reconnect = false;
					reason = e.getMessage();
				} catch (InterruptedException e) {
					reason = "Socket disconnected";
				}
			}

			if (reason.length() == 0
					&& (getConsumerState() == StreamConsumer.STATE_RUNNING || getConsumerState() == StreamConsumer.STATE_RESTARTING)
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
		} while ((getConsumerState() == StreamConsumer.STATE_RUNNING || getConsumerState() == StreamConsumer.STATE_RESTARTING)
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
