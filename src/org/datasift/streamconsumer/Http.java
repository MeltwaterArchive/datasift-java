/**
 * 
 */
package org.datasift.streamconsumer;

import org.datasift.*;

/**
 * @author stuart
 * 
 */
public class Http extends StreamConsumer {
	/**
	 * This is the thread that consumes the HTTP stream.
	 */
	private HttpThread thread = null;

	/**
	 * Constructor.
	 * 
	 * @param User
	 *            user
	 * @param Definition
	 *            definition
	 * @param IStreamConsumerEvents
	 *            eventHandler
	 * @throws EInvalidData
	 * @throws ECompileFailed
	 * @throws EAccessDenied
	 */
	public Http(User user, Definition definition,
			IStreamConsumerEvents eventHandler) throws EInvalidData,
			ECompileFailed, EAccessDenied {
		super(user, definition, eventHandler);
		thread = new HttpThread(this, user, definition);
	}

	public void setAutoReconnect(boolean auto_reconnect) {
		thread.setAutoReconnect(auto_reconnect);
	}
	
	public void setProxy(String proxy, int port) {
		thread.setProxy(proxy, port);
	}	
	
	public void setProxyCredentials(char[] proxy_user, char[] proxy_password) {
		thread.setProxyCredentials(proxy_user, proxy_password);
	}

	public boolean isRunning() {
		if (thread == null) {
			return false;
		}
		return thread.isAlive();
	}

	@Override
	protected void onStart(boolean auto_reconnect) {
		setAutoReconnect(auto_reconnect);
		if (!isRunning()) {
			_state = StreamConsumer.STATE_RUNNING;
			thread.start();
		}
	}
}
