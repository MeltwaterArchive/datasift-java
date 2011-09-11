/**
 * 
 */
package org.datasift.streamconsumer;

import java.util.ArrayList;

import org.datasift.*;

/**
 * @author stuart
 * 
 */
public class HttpMulti extends StreamConsumer {
	/**
	 * This is the thread that consumes the HTTP stream.
	 */
	private HttpMultiThread thread = null;
	
	/**
	 * Constructor.
	 * 
	 * @param User
	 *            user
	 * @param ArrayList<String>
	 *            hashes
	 * @param IStreamConsumerEvents
	 *            eventHandler
	 * @throws EInvalidData
	 * @throws ECompileFailed
	 * @throws EAccessDenied
	 */
	public HttpMulti(User user, ArrayList<String> hashes,
			IMultiStreamConsumerEvents eventHandler) throws EInvalidData,
			ECompileFailed, EAccessDenied {
		super(user, eventHandler);
		thread = new HttpMultiThread(this, user, hashes);
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
	protected void onStart() {
		if (!isRunning()) {
			_state = StreamConsumer.STATE_RUNNING;
			thread.start();
		}
	}
}
