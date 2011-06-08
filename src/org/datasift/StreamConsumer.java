/**
 * This file contains the StreamConsumer class.
 */
package org.datasift;

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

	/**
	 * Consumer states.
	 */
	public static final int STATE_STOPPED = 0;
	public static final int STATE_STARTING = 1;
	public static final int STATE_RUNNING = 2;
	public static final int STATE_STOPPING = 3;

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

		throw new EInvalidData("Unknown consumer type: " + type);
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
	 * The event handler.
	 */
	protected IStreamConsumerEvents _eventHandler = null;

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
		// Set the user
		_user = user;

		// Set the definition
		_definition = definition;

		// Compile the definition to ensure it's valid for use
		if (_definition.getHash().isEmpty()) {
			_definition.compile();
		} else {
			_definition.validate();
		}
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
	 * This is called when the consumer is stopped.
	 * 
	 * @param reason
	 *            The reason the consumer stopped.
	 * @throws EInvalidData
	 */
	public void onStopped(String reason) throws EInvalidData {
		if (_eventHandler != null) {
			_eventHandler.onStopped(this, reason);
		} else {
			throw new EInvalidData(
					"You must provide an onStopped method or an eventHandler object");
		}
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
		onStart();
	}

	/**
	 * Called when the consumer should start consuming the stream.
	 * 
	 * @abstract
	 */
	abstract protected void onStart();

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
}
