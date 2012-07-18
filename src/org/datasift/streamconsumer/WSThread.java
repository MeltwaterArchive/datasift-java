/**
 *
 */
package org.datasift.streamconsumer;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.datasift.EAPIError;
import org.datasift.EInvalidData;
import org.datasift.Interaction;
import org.datasift.JSONdn;
import org.datasift.StreamConsumer;
import org.datasift.User;
import org.json.JSONException;

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
	private URI _uri = null;
	private List<String> _subscriptions = null;

	public WSThread(WS http, User user) throws WebSocketException, URISyntaxException {
		this(http, user, new ArrayList<String>());
	}
	
	public WSThread(WS http, User user, List<String> subscriptions) throws WebSocketException, URISyntaxException {
		_consumer = http;
		_user = user;
		_subscriptions = subscriptions;
		_uri = new URI("ws://" + _user.getWebsocketBaseURL() + (_consumer.isHistoric() ? "historics/multi?hashes="+ Arrays.deepToString(_subscriptions.toArray()).replace("[", "").replace("]", ""): ""));
	}

	public void setAutoReconnect(boolean auto_reconnect) {
		_auto_reconnect = auto_reconnect;
	}

	public synchronized int getConsumerState() {
		return _consumer.getState();
	}

	public synchronized void setStopped() throws EInvalidData {
		_consumer.setStopped();
	}

	public synchronized void processLine(String line) {
		try {
			// Extract the hash
			JSONdn data = new JSONdn(line);
			if (data.has("status")) {
				String status = data.getStringVal("status");
				if (status.equals("error") || status.equals("failure")) {
					_consumer.onError(data.getStringVal("message"));
					_consumer.stop();
				} else if (status.equals("warning")) {
					_consumer.onWarning(data.getStringVal("message"));
				}else if (status.equals("success")) {
					_consumer.onStatus(status, data);
				}else {
					// Dunno what that is, make it an error
					_consumer.onError("Unhandled content received: " + line);
				}
			} else if (data.has("data")) {
				Interaction i = new Interaction(data.getJSONObject("data").toString());
				if (i.has("deleted")) {
					_consumer.onMultiDeleted(data.getStringVal("hash"), i);
				} else {
					_consumer.onMultiInteraction(data.getStringVal("hash"), i);
				}
			} else {
				_consumer.onError("Unhandled content received: " + line);
			}
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

	public synchronized void stopped() {
		try {
			_consumer.setStopped();
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
		do_subscribe(hash);
		if (!_subscriptions.contains(hash)) {
			_subscriptions.add(hash);
		}
	}
	
	private synchronized void do_subscribe(String hash) throws EAPIError {
		try {
			if (_ws != null && getConsumerState() == StreamConsumer.STATE_RUNNING) {
				_ws.send("{\"action\":\"subscribe\",\"hash\":\"" + hash + "\"}");
			}
		} catch (WebSocketException e) {
			throw new EAPIError(e.getMessage());
		}
	}

	public synchronized void unsubscribe(String hash) throws EAPIError {
		do_unsubscribe(hash);
		_subscriptions.remove(hash);
	}
	
	private synchronized void do_unsubscribe(String hash) throws EAPIError {
		try {
			if (_ws != null && getConsumerState() == StreamConsumer.STATE_RUNNING) {
				_ws.send("{\"action\":\"unsubscribe\",\"hash\":\"" + hash + "\"}");
			}
		} catch (WebSocketException e) {
			throw new EAPIError(e.getMessage());
		}
	}
	
	public synchronized void sendStop() throws EInvalidData {
		try {
			if (_ws != null && getConsumerState() == StreamConsumer.STATE_RUNNING) {
				_ws.send("{\"action\":\"stop\"}");
			}
		} catch (WebSocketException e) {
			// Swallow this and just mark us as stopped
			stopped();
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
					if (_ws != null) {
						_ws.close();
					}
					_ws = null;
					_ws = new WebSocketConnection(_uri);
					_ws.addHeader("Authorization: " + _user.getUsername() + ":" + _user.getAPIKey());
					_ws.addHeader("User-Agent: " + _user.getUserAgent());
					
					// Register Event Handlers
					_ws.setEventHandler(new WebSocketEventHandler() {
						public void onOpen()
						{
							// Socket connected, tell the event handler
							_consumer.onConnect();
							// Send subscribes
							for (String hash : _subscriptions) {
								try {
									do_subscribe(hash);
								} catch (EAPIError e) {
									try {
										_consumer.onWarning("Failed to subscribe to " + hash);
									} catch (EInvalidData e1) {
										// Ignored
									}
								}
							}
						}

						public void onMessage(WebSocketMessage message)
						{
							// Message received
							String line = message.getText();
							if (line.length() > 10) {
								processLine(line);
							}
						}

						public void onClose()
						{
							// Socket closed - tell the event handler
							_consumer.onDisconnect();
							// Decide what to do next
							switch (getConsumerState()) {
							case StreamConsumer.STATE_RUNNING:
								if (_auto_reconnect) {
									restartConsumer();
								} else {
									stopped();
								}
								break;
							case StreamConsumer.STATE_STOPPING:
								stopped();
								break;
							}
						}
					});

					// Establish WebSocket Connection
					_ws.connect();

					// Wait for the state to change
					while (getConsumerState() == StreamConsumer.STATE_RUNNING) {
						Thread.sleep(500);
					}

					// If the state is not stopping or stopped, we got disconnected!
					if (getConsumerState() != StreamConsumer.STATE_STOPPING && getConsumerState() != StreamConsumer.STATE_STOPPED) {
						// Send the stop message
						stopConsumer();
						reason = "Socket disconnected";
					} else {
						// The stop was requested
						reason = "Stop requested";
					}

					// Wait a maximum of 30 seconds while the stop process happens
					int stopCounter = 60;
					while (stopCounter > 0 && getConsumerState() == StreamConsumer.STATE_STOPPING) {
						Thread.sleep(500);
						stopCounter--;
					}
					if (stopCounter == 0) {
						// Timed out waiting for the stop ack, tell the user
						synchronized (this) {
							try {
								_consumer.onWarning("Timed out waiting for the server to respond to the stop request, disconnecting!");
							} catch (EInvalidData e) {
								// Ignored
							}
						}
					}
				} catch (WebSocketException e) {
					_auto_reconnect = false;
					reason = e.getMessage();
				} catch (InterruptedException e) {
					reason = "Socket disconnected";
				} finally {
					try {
						_ws.close();
					} catch (Exception e) {
						// Deliberately ignored - usually thrown due to the
						// connection not being open, which we really don't
						// care about knowing at this point!
					}
				}
			}

			if (reason.length() == 0
					&& (getConsumerState() == StreamConsumer.STATE_RUNNING || getConsumerState() == StreamConsumer.STATE_RESTARTING)
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
					reconnect_delay = 0;
				}
				
				if (reconnect_delay > 0) {
					try {
						_consumer.onWarning("Connection failed, retrying in " + reconnect_delay + " seconds");
					} catch (EInvalidData e) {
						// Ignored
					}
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
