/**
 * This file contains the StreamConsumer class.
 */
package org.datasift;

import java.util.ArrayList;

import org.datasift.streamconsumer.*;

/**
 * @author MediaSift
 * @version 0.1
 */
abstract public class StreamConsumer {
	/**
	 * Consumer types.
	 */
	public static final String TYPE_HTTP = "Http";
	public static final String TYPE_HTTP_MULTI = "HttpMulti";
	public static final String TYPE_WS = "Ws";

	/**
	 * Consumer states.
	 */
	public static final int STATE_STOPPED = 0;
	public static final int STATE_STARTING = 1;
	public static final int STATE_RUNNING = 2;
	public static final int STATE_STOPPING = 3;
	public static final int STATE_RESTARTING = 4;

	/**
	 * Factory method that takes a Definition object.
	 * 
	 * @param user
	 * @param type
	 * @param definition
	 * @param eventHandler
	 * @return
	 * @throws EAccessDenied
	 * @throws ECompileFailed
	 * @throws EInvalidData
	 */
	public static StreamConsumer factory(User user, String type,
			Definition definition, IStreamConsumerEvents eventHandler)
			throws EInvalidData, ECompileFailed, EAccessDenied {
		if (type == StreamConsumer.TYPE_HTTP) {
			return new Http(user, definition, eventHandler);
		}

		throw new EInvalidData("Unknown or inappropriate consumer type: "
				+ type);
	}

	/**
	 * Factory method that takes a Definition object.
	 * 
	 * @param user
	 * @param type
	 * @param definition
	 * @param eventHandler
	 * @return
	 * @throws EAccessDenied
	 * @throws ECompileFailed
	 * @throws EInvalidData
	 */
	public static StreamConsumer factory(User user, String type,
			ArrayList<String> hashes, IMultiStreamConsumerEvents eventHandler)
			throws EInvalidData, ECompileFailed, EAccessDenied {
		if (type == StreamConsumer.TYPE_HTTP_MULTI) {
			return new HttpMulti(user, hashes, eventHandler);
		}

		throw new EInvalidData("Unknown or inappropriate consumer type: "
				+ type);
	}

	/**
	 * Factory method that takes no definition or CSDL.
	 * 
	 * @param user
	 * @param type
	 * @param eventHandler
	 * @return
	 * @throws EAccessDenied
	 * @throws ECompileFailed
	 * @throws EInvalidData
	 * @throws EAPIError 
	 */
	public static StreamConsumer factory(User user, String type,
			IMultiStreamConsumerEvents eventHandler)
			throws EInvalidData, ECompileFailed, EAccessDenied, EAPIError {
		if (type == StreamConsumer.TYPE_WS) {
			return new WS(user, eventHandler);
		}

		throw new EInvalidData("Unknown or inappropriate consumer type: "
				+ type);
	}

	/**
	 * Factory method that takes a CSDL definition.
	 * 
	 * @param user
	 * @param type
	 * @param csdl
	 * @param eventHandler
	 * @return
	 * @throws EInvalidData
	 * @throws ECompileFailed
	 * @throws EAccessDenied
	 */
	public static StreamConsumer factory(User user, String type, String csdl,
			IStreamConsumerEvents eventHandler) throws EInvalidData,
			ECompileFailed, EAccessDenied {
		return StreamConsumer.factory(user, type, new Definition(user, csdl),
				eventHandler);
	}
	
	/**
	 * Factory method that takes a Definition object.
	 * 
	 * @param user
	 * @param type
	 * @param definition
	 * @param eventHandler
	 * @return
	 * @throws EAccessDenied
	 * @throws ECompileFailed
	 * @throws EInvalidData
	 * @throws EAPIError 
	 */
	public static StreamConsumer historicFactory(User user, String type,
			Historic historic, IStreamConsumerEvents eventHandler)
			throws EInvalidData, ECompileFailed, EAccessDenied, EAPIError {
		if (type == StreamConsumer.TYPE_HTTP) {
			return new Http(user, new Definition(user, "", historic.getHash()), eventHandler, true);
		}

		throw new EInvalidData("Unknown or inappropriate consumer type: "
				+ type);
	}

	/**
	 * The user that owns this consumer.
	 */
	protected User _user = null;

	/**
	 * The definition this consumer is consuming.
	 */
	protected Definition _definition = null;

	/**
	 * Whether we should auto-reconnect if the connection gets dropped.
	 */
	protected boolean _auto_reconnect = true;

	/**
	 * The current state.
	 */
	protected int _state = StreamConsumer.STATE_STOPPED;
	
	/**
	 * True if this is a historic consumer.
	 */
	protected boolean _is_historic = false;

	/**
	 * The event handler.
	 */
	protected IStreamConsumerEvents _eventHandler = null;

	/**
	 * The multi-stream event handler.
	 */
	protected IMultiStreamConsumerEvents _multiEventHandler = null;

	/**
	 * Constructor. Do not use this directly, use the factory method instead.
	 * 
	 * @param User
	 *            user The user this consumer will run as.
	 * @param Definition
	 *            definition The definition that this consumer will consume.
	 * @throws EAccessDenied
	 * @throws ECompileFailed
	 * @throws EInvalidData
	 */
	protected StreamConsumer(User user, Definition definition)
			throws EInvalidData, ECompileFailed, EAccessDenied {
		Init(user, definition);
	}

	/**
	 * Constructor. Do not use this directly, use the factory method instead.
	 * 
	 * @param User
	 *            user The user this consumer will run as.
	 * @param Definition
	 *            definition The definition that this consumer will consume.
	 * @param IStreamConsumerEvents
	 *            eventHandler The class that will receive events.
	 * @throws EAccessDenied
	 * @throws ECompileFailed
	 * @throws EInvalidData
	 */
	protected StreamConsumer(User user, Definition definition,
			IStreamConsumerEvents eventHandler) throws EInvalidData,
			ECompileFailed, EAccessDenied {
		// Set the event handler
		_eventHandler = eventHandler;

		// Call the common init function
		Init(user, definition);
	}

	/**
	 * Constructor. Do not use this directly, use the factory method instead.
	 * 
	 * @param User
	 *            user The user this consumer will run as.
	 * @param Definition
	 *            definition The definition that this consumer will consume.
	 * @param IStreamConsumerEvents
	 *            eventHandler The class that will receive events.
	 * @param boolean
	 *            isHistoric True if this is a historic consumer.
	 * @throws EAccessDenied
	 * @throws ECompileFailed
	 * @throws EInvalidData
	 */
	protected StreamConsumer(User user, Definition definition,
			IStreamConsumerEvents eventHandler, boolean isHistoric) throws EInvalidData,
			ECompileFailed, EAccessDenied {
		// Set the event handler
		_eventHandler = eventHandler;

		// Call the common init function
		Init(user, definition, isHistoric);
	}

	/**
	 * Constructor. Do not use this directly, use the factory method instead.
	 * 
	 * @param User
	 *            user The user this consumer will run as.
	 * @param Definition
	 *            definition The definition that this consumer will consume.
	 * @param IStreamConsumerEvents
	 *            eventHandler The class that will receive events.
	 * @throws EAccessDenied
	 * @throws ECompileFailed
	 * @throws EInvalidData
	 */
	protected StreamConsumer(User user, IMultiStreamConsumerEvents eventHandler)
			throws EInvalidData, ECompileFailed, EAccessDenied {
		// Set the event handler
		_multiEventHandler = eventHandler;

		// Call the common init function
		Init(user, null);
	}

	/**
	 * Initialise the object.
	 * 
	 * @param user
	 *            The user this consumer should run as.
	 * @param definition
	 *            The definition this consumer will consume.
	 * @throws EAccessDenied
	 * @throws ECompileFailed
	 * @throws EInvalidData
	 */
	protected void Init(User user, Definition definition) throws EInvalidData,
			ECompileFailed, EAccessDenied {
		Init(user, definition, false);
	}

	/**
	 * Initialise the object.
	 * 
	 * @param user
	 *            The user this consumer should run as.
	 * @param definition
	 *            The definition this consumer will consume.
	 * @throws EAccessDenied
	 * @throws ECompileFailed
	 * @throws EInvalidData
	 */
	protected void Init(User user, Definition definition, boolean isHistoric) throws EInvalidData,
			ECompileFailed, EAccessDenied {
		// Set the user
		_user = user;

		// Set the definition
		_definition = definition;

		// If we have a definition, compile it to ensure it's valid for use
		if (_definition != null) {
			if (_definition.getHash().isEmpty()) {
				_definition.compile();
			}
		}
		
		// Set whether this is a historic consumer
		_is_historic = isHistoric;
	}

	/**
	 * Get the current state.
	 * 
	 * @return int
	 */
	public int getState() {
		return _state;
	}
	
	/**
	 * Get whether this is a historic consumer.
	 * 
	 * @return boolean
	 */
	public boolean isHistoric() {
		return _is_historic;
	}

	/**
	 * Set the current state to stopped.
	 * @throws EInvalidData 
	 */
	public void setStopped() throws EInvalidData {
		if (_state == STATE_STOPPING) {
			_state = STATE_STOPPED;
		} else {
			throw new EInvalidData("The state must be STOPPING before it can be set to STOPPED.");
		}
	}

	/**
	 * This is called when the consumer has successfully connected.
	 */
	public void onConnect() {
		if (_eventHandler != null) {
			_eventHandler.onConnect(this);
		} else if (_multiEventHandler != null) {
			_multiEventHandler.onConnect(this);
		}
		// If we don't have a handler for this event, swallow it!
	}

	/**
	 * This is called when the consumer has successfully connected.
	 */
	public void onDisconnect() {
		if (_eventHandler != null) {
			_eventHandler.onDisconnect(this);
		} else if (_multiEventHandler != null) {
			_multiEventHandler.onDisconnect(this);
		}
		// If we don't have a handler for this event, swallow it!
	}

	/**
	 * This is called for each interaction received from the stream and should
	 * be implemented in extending classes if they don't use an
	 * IStreamConsumerEvents object.
	 * 
	 * @param Interaction
	 *            interaction The interaction data structure.
	 * @throws EInvalidData
	 */
	public void onInteraction(Interaction interaction) throws EInvalidData {
		if (_eventHandler != null) {
			_eventHandler.onInteraction(this, interaction);
		} else {
			throw new EInvalidData(
					"You must provide an onInteraction method or an eventHandler object");
		}
	}

	/**
	 * This is called for each interaction received from the stream and should
	 * be implemented in extending classes if they don't use an
	 * IStreamConsumerEvents object.
	 * 
	 * @param Interaction
	 *            interaction The interaction data structure.
	 * @throws EInvalidData
	 */
	public void onDeleted(Interaction interaction) throws EInvalidData {
		if (_eventHandler != null) {
			_eventHandler.onDeleted(this, interaction);
		} else {
			throw new EInvalidData(
					"You must provide an onDeleted method or an eventHandler object");
		}
	}

	/**
	 * This is called for each interaction received from the stream and should
	 * be implemented in extending classes if they don't use an
	 * IStreamConsumerEvents object.
	 * 
	 * @param Interaction
	 *            interaction The interaction data structure.
	 * @throws EInvalidData
	 */
	public void onMultiInteraction(String hash, Interaction interaction)
			throws EInvalidData {
		if (_multiEventHandler != null) {
			_multiEventHandler.onInteraction(this, hash, interaction);
		} else {
			throw new EInvalidData(
					"You must provide an onMultiInteraction method or a multiEventHandler object");
		}
	}

	/**
	 * This is called for each deletion notification received from the stream
	 * and should be implemented in extending classes if they don't use an
	 * IStreamConsumerEvents object.
	 * 
	 * @param Interaction
	 *            interaction The interaction data structure.
	 * @throws EInvalidData
	 */
	public void onMultiDeleted(String hash, Interaction interaction)
			throws EInvalidData {
		if (_multiEventHandler != null) {
			_multiEventHandler.onDeleted(this, hash, interaction);
		} else {
			throw new EInvalidData(
					"You must provide an onMultiDeleted method or a multiEventHandler object");
		}
	}
	
	/**
	 * Called for each status message received down the stream.
	 * 
	 * @param type
	 * @param info
	 * @throws EInvalidData
	 */
	public void onStatus(String type, JSONdn info) throws EInvalidData {
		if (_eventHandler != null) {
			_eventHandler.onStatus(this, type, info);
		} else if (_multiEventHandler != null) {
			_multiEventHandler.onStatus(this, type, info);
		} else {
			throw new EInvalidData("You must provide an onStatus method or an eventHandler object");
		}
	}

	/**
	 * This is called when the consumer is stopped.
	 * 
	 * @param reason
	 *            The reason the consumer stopped.
	 * @throws EInvalidData
	 */
	public void onStopped(String reason) throws EInvalidData {
		if (_eventHandler != null) {
			_eventHandler.onStopped(this, reason);
		} else if (_multiEventHandler != null) {
			_multiEventHandler.onStopped(this, reason);
		} else {
			throw new EInvalidData(
					"You must provide an onStopped method or an eventHandler object");
		}
	}
	
	/**
	 * This is called when a warning is received in the data stream.
	 * 
	 * @param message The warning message.
	 * @throws EInvalidData
	 */
	public void onWarning(String message) throws EInvalidData {
		if (_eventHandler != null) {
			_eventHandler.onWarning(this, message);
		} else if (_multiEventHandler != null) {
			_multiEventHandler.onWarning(this, message);
		}
		// If we don't have a handler for this event, swallow it!
	}
	
	/**
	 * This is called when an error is received in the data stream.
	 * 
	 * @param message The error message.
	 * @throws EInvalidData
	 */
	public void onError(String message) throws EInvalidData {
		if (_eventHandler != null) {
			_eventHandler.onError(this, message);
		} else if (_multiEventHandler != null) {
			_multiEventHandler.onError(this, message);
		}
		// If we don't have a handler for this event, swallow it!
	}

	/**
	 * Start consuming with auto_reconnect enabled.
	 */
	public void consume() {
		consume(true);
	}

	/**
	 * Once an instance of a StreamConsumer is ready for use, call this to start
	 * consuming. Extending classes should implement onStart to handle actually
	 * starting.
	 */
	public void consume(boolean auto_reconnect) {
		_auto_reconnect = auto_reconnect;

		// Start consuming
		_state = StreamConsumer.STATE_STARTING;
		onStart(_auto_reconnect);
	}

	/**
	 * Called when the consumer should start consuming the stream.
	 * 
	 * @abstract
	 */
	abstract protected void onStart(boolean auto_reconnect);

	/**
	 * This method can be called at any time to *request* that the consumer stop
	 * consuming. This method sets the state to STATE_STOPPING and it's up to
	 * the consumer implementation to notice that this has changed, stop
	 * consuming and call the onStopped method.
	 * 
	 * @throws EInvalidData
	 * @throws DataSift_Exception_InalidData
	 */
	public void stop() throws EInvalidData {
		if (_state == StreamConsumer.STATE_RUNNING) {
			_state = StreamConsumer.STATE_STOPPING;
		} else {
			throw new EInvalidData(
					"Consumer state must be RUNNING before it can be stopped");
		}
	}

	/**
	 * Default implementation of onStop. It's unlikely that this method will
	 * ever be used in isolation, but rather it should be called as the final
	 * step in the extending class's implementation.
	 * 
	 * @throws EInvalidData
	 */
	protected void onStop(String reason) throws EInvalidData {
		if (_state != StreamConsumer.STATE_STOPPING && reason.length() == 0) {
			reason = "Unexpected";
		}

		_state = StreamConsumer.STATE_STOPPED;
		onStopped(reason);
	}

	/**
	 * Subscribe to a stream.
	 * 
	 * @param def
	 * @throws EAccessDenied
	 * @throws EInvalidData
	 * @throws EAPIError
	 */
	public void subscribe(Definition def) throws EInvalidData, EAccessDenied,
			EAPIError {
		subscribe(def.getHash());
	}

	/**
	 * Subscribe to a stream.
	 * 
	 * @param hash
	 * @throws EAPIError
	 */
	public void subscribe(String hash) throws EAPIError {
		throw new EAPIError(
				"This consumer does not support dynamic stream subscription.");
	}

	/**
	 * Unsubscribe from a stream.
	 * 
	 * @param def
	 * @throws EAccessDenied
	 * @throws EInvalidData
	 * @throws EAPIError
	 */
	public void unsubscribe(Definition def) throws EInvalidData, EAccessDenied,
			EAPIError {
		unsubscribe(def.getHash());
	}

	/**
	 * Unsubscribe from a stream.
	 * 
	 * @param hash
	 * @throws EAPIError
	 */
	public void unsubscribe(String hash) throws EAPIError {
		throw new EAPIError(
				"This consumer does not support dynamic stream subscription.");
	}

}
