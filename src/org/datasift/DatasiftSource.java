/**
 * Datasift Flume Source<BR/>
 * 
 * <B>Revision History:</B>
 * 
 * <PRE>
 * ====================================================================================
 * Date        By                           Description
 * MM/DD/YYYY
 * ----------  ---------------------------  -------------------------------------------
 * 08/18/2013  alan.delrio@hotmail.com      Initial Version
 * ====================================================================================
 * </PRE>
 * 
 * @author Alan del Rio
 * 
 */
package org.datasift;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.EventDrivenSource;
import org.apache.flume.channel.ChannelProcessor;
import org.apache.flume.conf.Configurable;
import org.apache.flume.event.EventBuilder;
import org.apache.flume.source.AbstractSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import org.datasift.streamconsumer.Http;
/**
 * A Datasift Source, which pulls data from Datasift streaming API. Currently,
 * this only supports pulling from the streaming API
 */
public class DatasiftSource extends AbstractSource
    implements EventDrivenSource, Configurable {
  
   public static final String DATASIFT_USER_ID = "datasiftUserID";
   public static final String DATASIFT_API_ID = "datasiftApiID";
   public static final String DATASIFT_HASH_ID = "datasiftHashID";
   public static final String PROXY_HOST = "proxyHost";
   public static final String PROXY_PORT = "proxyPort";  
   public static final String DATASIFT_RECONNECT = "reconnect";     
  private static final Logger logger =
      LoggerFactory.getLogger(DatasiftSource.class);

  /** Information necessary for accessing the  Datasift API */  
  private String sUserID;
  private String sApiID;
  private String sHashID;
  private String sProxyHost;
  private int iProxyPort;
  private boolean bReconnect=false;
  private StreamConsumer consumer;
  
  /**
   * The initialization method for the Source. The context contains all the
   * Flume configuration info, and can be used to retrieve any configuration
   * values necessary to set up the Source.
   */
  public void configure(Context context) {
    this.sUserID = context.getString(DATASIFT_USER_ID );
    this.sApiID = context.getString(DATASIFT_API_ID);
    this.sHashID = context.getString(DATASIFT_HASH_ID);
    this.sProxyHost = context.getString(PROXY_HOST);
    this.iProxyPort = context.getInteger(PROXY_PORT);
    this.iProxyPort = context.getInteger(PROXY_PORT);
    this.bReconnect = context.getBoolean(DATASIFT_RECONNECT);
  }

  /**
   * Start processing events. This uses the Twitter Streaming API to sample
   * Twitter, and process tweets.
   */
  @Override
  public void start() {
    // The channel is the piece of Flume that sits between the Source and Sink,
    // and is used to process events.
    final ChannelProcessor channel = getChannelProcessor();  
    final Map<String, String> headers = new HashMap<String, String>();

	try {
		User user = new User(this.sUserID, this.sApiID);		
		consumer = user.getConsumer(StreamConsumer.TYPE_HTTP, this.sHashID, 
				new ConsumeStream(headers,channel));
	} catch (Exception e){
	    logger.debug(e.toString());		
	}    
    if (this.sProxyHost.equalsIgnoreCase("NONE")==false){
		Http httpSC= (Http)consumer; 
		httpSC.setProxy(this.sProxyHost,this.iProxyPort);
    }
    
   logger.debug("Starting up Datasift");
   consumer.consume(this.bReconnect);
   super.start();
  }
  
  /**
   * Stops the Source's event processing and shuts down the Datasift stream
   */
  @Override
  public void stop() {
    logger.debug("Shutting down Datasift");
    //STOP Connection
    try {
		consumer.stop();
	} catch (Exception e) {
	    logger.debug(e.toString());
	}
    super.stop();
  }
  public class ConsumeStream implements IStreamConsumerEvents {
	
	private Map<String, String> headers;
	private final ChannelProcessor channel;
	
	ConsumeStream(Map<String, String> headers, final ChannelProcessor channel){
		this.headers=headers;
		this.channel=channel;	
	}

	/**
	 * Called when the connection has been established.
	 * 
	 * @param StreamConsumer consumer The consumer object.
	 */
	public void onConnect(StreamConsumer c) {
		System.out.println("Connected");
		System.out.println("--");
	}
	
	/**
	 * Called when the connection has disconnected.
	 * 
	 * @param StreamConsumer consumer The consumer object.
	 */
	public void onDisconnect(StreamConsumer c) {
		System.out.println("Disconnected");
		System.out.println("--");
	}


	public void onInteraction(StreamConsumer c, Interaction i)
			throws EInvalidData {
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
		Date dt = null;
		try {
			dt = sdf.parse(i.getStringVal("interaction.created_at"));
	        this.headers.put("timestamp", String.valueOf(dt.getTime()));
	        Event event = EventBuilder.withBody(i.toString().getBytes(), headers);        
	        this.channel.processEvent(event);			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} 
	}

	/**
	 * Handle delete notifications.
	 * 
	 * @param StreamConsumer
	 *            c The consumer object.
	 * @param Interaction
	 *            i The interaction data.
	 * @throws EInvalidData
	 */
	public void onDeleted(StreamConsumer c, Interaction i)
			throws EInvalidData {
		try {
			System.out.print("Deleted: ");
			System.out.print(i.getStringVal("interaction.id"));
		} catch (EInvalidData e) {
			// The interaction did not contain either a type or content.
			System.out.println("Exception: " + e.getMessage());
			System.out.print("Deletion: ");
			System.out.println(i);
		}
		System.out.println("--");
	}

	/**
	 * Handle status notifications
	 * 
	 * @param StreamConsumer
	 *            consumer The consumer object.
	 * @param String
	 *            type The status notification type.
	 * @param JSONdn
	 *            info The notification data.
	 */
	public void onStatus(StreamConsumer consumer, String type, JSONdn info) {
		System.out.print("STATUS: ");
		System.out.println(type);
	}

	/**
	 * Called when the consumer has stopped.
	 * 
	 * @param DataSift_StreamConsumer
	 *            consumer The consumer object.
	 * @param string
	 *            reason The reason the consumer stopped.
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
}
