/**
 * This file contains the IStreamConsumerEvents class which is used for
 * callbacks from classes derived from StreamConsumer.
 */
package org.datasift;

/**
 * @author MediaSift
 * @version 0.1
 */
public interface IStreamConsumerEvents {
	/**
	 * Called when the socket is connected.
	 * @param consumer
	 * @throws EInvalidData
	 */
	public void onConnect(StreamConsumer consumer);

	/**
	 * Called for each interaction consumed.
	 * @param consumer
	 * @param interaction
	 * @throws EInvalidData
	 */
	public void onInteraction(StreamConsumer consumer, Interaction interaction)
			throws EInvalidData;

	/**
	 * Called for each deletion notification consumed.
	 * @param consumer
	 * @param interaction
	 * @throws EInvalidData
	 */
	public void onDeleted(StreamConsumer consumer, Interaction interaction)
			throws EInvalidData;
	
	/**
	 * Called for each status message consumed.
	 * @param consumer
	 * @param type
	 * @param info
	 */
	public void onStatus(StreamConsumer consumer, String type, JSONdn info);

	/**
	 * Called for each deletion notification consumed.
	 * @param consumer
	 * @param interaction
	 * @throws EInvalidData
	 */
	public void onWarning(StreamConsumer consumer, String message)
			throws EInvalidData;

	/**
	 * Called for each deletion notification consumed.
	 * @param consumer
	 * @param interaction
	 * @throws EInvalidData
	 */
	public void onError(StreamConsumer consumer, String message)
			throws EInvalidData;

	/**
	 * Called when the consumer stops for some reason.
	 * @param consumer
	 * @param reason
	 */
	public void onStopped(StreamConsumer consumer, String reason);
	
	/**
	 * Called when the socket is disconnected.
	 * @param consumer
	 * @throws EInvalidData
	 */
	public void onDisconnect(StreamConsumer consumer);
}
