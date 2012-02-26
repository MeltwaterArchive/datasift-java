/**
 * This file contains the IMultiStreamConsumerEvents class which is used for
 * multi-stream aware callbacks from classes derived from StreamConsumer.
 */
package org.datasift;

/**
 * @author MediaSift
 * @version 0.1
 */
public interface IMultiStreamConsumerEvents {
	/**
	 * Called for each interaction consumed.
	 * @param consumer
	 * @param hash
	 * @param interaction
	 * @throws EInvalidData
	 */
	public void onInteraction(StreamConsumer consumer, String hash, Interaction interaction)
			throws EInvalidData;

	/**
	 * Called for each deletion notification consumed.
	 * @param consumer
	 * @param hash
	 * @param interaction
	 * @throws EInvalidData
	 */
	public void onDeleted(StreamConsumer consumer, String hash, Interaction interaction)
			throws EInvalidData;

	/**
	 * Called when the consumer stops for some reason.
	 * @param consumer
	 * @param reason
	 */
	public void onStopped(StreamConsumer consumer, String reason);
}
