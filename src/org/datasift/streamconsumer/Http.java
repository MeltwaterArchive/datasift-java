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
		this(user, definition, eventHandler, false);
	}

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
			IStreamConsumerEvents eventHandler, boolean isHistoric) throws EInvalidData,
			ECompileFailed, EAccessDenied {
		super(user, definition, eventHandler, isHistoric);
		thread = new HttpThread(this, user, definition);
	}

	public void setAutoReconnect(boolean auto_reconnect) {
		thread.setAutoReconnect(auto_reconnect);
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
