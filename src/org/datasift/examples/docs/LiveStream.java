package org.datasift.examples.docs;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.datasift.Definition;
import org.datasift.EInvalidData;
import org.datasift.IStreamConsumerEvents;
import org.datasift.Interaction;
import org.datasift.StreamConsumer;
import org.datasift.User;
import org.datasift.Config;

/**
 *
 * @author courtney
 */
public class LiveStream implements IStreamConsumerEvents {//Step 1 - must implement IStreamConsumerEvents 

    public static void main(String[] args) {
        try {
            //step 2 - create a user object for authentication
            User user = new User(Config.username, Config.api_key);
            //step 3 - create a definition
            Definition def = user.createDefinition("interaction.type !=\"twitter\"");
            //step 4 - Get a stream consumer
            StreamConsumer consumer = def.getConsumer(StreamConsumer.TYPE_HTTP, new LiveStream());
            consumer.consume();//start consuming the stream

        } catch (Exception ex) {
            System.err.println();
            Logger.getLogger("net.datasift.example").log(Level.WARNING, "An error occured: " + ex.getMessage());
        }

    }

    /**
     * Handle incoming data. This method will be invoked every time an interaction is received
     * It is one of the two methods available in our IStreamConsumerEvents interface
     * @param StreamConsumer
     *            consumer The consumer object.
     * @param JSONObject
     *            interaction The interaction data.
     * @throws EInvalidData
     */
    public void onInteraction(StreamConsumer c, Interaction i)
            throws EInvalidData {
        try {
            //System.out.print(i.getStringVal("interaction.author.username"));
            //System.out.print(": ");
            System.out.println(i.getStringVal("interaction.content"));
        } catch (EInvalidData e) {
            // The interaction did not contain either a type or content.
            System.out.println("Exception: " + e.getMessage());
            System.out.print("Interaction: ");
            System.out.println(i);
        }
        System.out.println("--");
    }

	/**
	 * Handle delete notifications.
	 * 
	 * @param StreamConsumer
	 *            consumer The consumer object.
	 * @param JSONObject
	 *            interaction The interaction data.
	 * @throws EInvalidData
	 */
	public void onDeleted(StreamConsumer c, Interaction i)
			throws EInvalidData {
		// Ignored for this example
	}

    /**
     * Called when the consumer has stopped.
     * 
     * @param DataSift_StreamConsumer
     *            $consumer The consumer object.
     * @param string
     *            $reason The reason the consumer stopped.
     */
    public void onStopped(StreamConsumer consumer, String reason) {
        System.out.print("Stopped: ");
        System.out.println(reason);
    }
    
	/**
	 * Called when a warning is received in the data stream.
	 * 
	 * @param DataSift_StreamConsumer consumer The consumer object.
	 * @param string message The warning message.
	 */
	public void onWarning(StreamConsumer consumer, String message)
			throws EInvalidData {
		System.out.println("Warning: " + message);
	}

	/**
	 * Called when an error is received in the data stream.
	 * 
	 * @param DataSift_StreamConsumer consumer The consumer object.
	 * @param string message The error message.
	 */
	public void onError(StreamConsumer consumer, String message)
			throws EInvalidData {
		System.out.println("Error: " + message);
	}
}