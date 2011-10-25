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
	private HttpMultiThread _thread = null;
	
	/**
	 * This is the list of hashes to which we're currently subscribed.
	 */
	private ArrayList<String> _hashes = null; 
	
	/**
	 * Constructor.
	 * 
	 * @param User
	 *            user
	 * @param ArrayList<String>
	 *            hashes
	 * @param IMultiStreamConsumerEvents
	 *            eventHandler
	 * @throws EInvalidData
	 * @throws ECompileFailed
	 * @throws EAccessDenied
	 */
	public HttpMulti(User user, ArrayList<String> hashes,
			IMultiStreamConsumerEvents eventHandler) throws EInvalidData,
			ECompileFailed, EAccessDenied {
		super(user, eventHandler);
		_hashes = hashes;
		restartThread();
	}

	public void setAutoReconnect(boolean auto_reconnect) {
		_thread.setAutoReconnect(auto_reconnect);
	}
	
	@Override
	public void subscribe(String hash) {
		if (!_hashes.contains(hash)) {
			_hashes.add(hash);
			restartThread();
		}
	}
	
	@Override
	public void unsubscribe(String hash) {
		if (_hashes.contains(hash)) {
			_hashes.remove(hash);
			restartThread();
		}
	}
	
	private void restartThread() {
		// Put the current thread in a temporary var
		HttpMultiThread tmp = _thread;
		// Release the main var
		_thread = null;
		// Create a new Thread object
		_thread = new HttpMultiThread(this, _user, _hashes);
		// If we're supposed to be running, start the thread
		if (_state == StreamConsumer.STATE_RUNNING) {
			_thread.start();
		}
		// If we have an old thread, kill it
		if (tmp != null) {
			tmp.requestKill();
		}
	}
	
	public boolean isRunning() {
		if (_thread == null) {
			return false;
		}
		return _thread.isAlive();
	}

	@Override
	protected void onStart(boolean auto_reconnect) {
		setAutoReconnect(auto_reconnect);
		if (!isRunning()) {
			_state = StreamConsumer.STATE_RUNNING;
			_thread.start();
		}
	}
}
