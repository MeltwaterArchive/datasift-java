/**
 * This simple example demonstrates creating a historic data and writing the output to a CSV file.
 */
package org.datasift.examples;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.datasift.*;

/**
 * @author MediaSift
 * @version 0.1
 */
public class HistoricDump implements IStreamConsumerEvents {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length != 5) {
			System.err
					.println("Usage: HistoricDump <keywords> <start_date> <end_date> <sources> <csv_filename>");
			System.err.println("Where...");
			System.err
					.println("  <keywords>     comma separated list of keywords");
			System.err.println("  <start_date>   start date (yyyymmddhhmmss)");
			System.err.println("  <end_date>     end date (yyyymmddhhmmss)");
			System.err
					.println("  <sources>      comma separated list of sources (twitter,digg,etc)");
			System.err.println("  <csv_filename> filename for the csv output");
			System.exit(1);
		}

		// Config
		String csdl = "interaction.type == \"twitter\" and language.tag == \"en\" and interaction.content contains_any \""
				+ args[0].replace("\"", "\\\"") + "\"";

		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");

		try {
			// Authenticate
			System.err.println("Creating user...");
			User user = new User(Config.username, Config.api_key);

			// Create the definition
			System.err.println("Creating definition...");
			Definition d = user.createDefinition(csdl);

			// Create the historic
			System.err.println("Creating historic...");
			Historic h = d.createHistoric(df.parse(args[1]), df.parse(args[2]),
					args[3], 100);

			// Display the playback ID
			System.err.println("Playback ID: " + h.getHash());

			// Create the consumer
			System.err.println("Getting the consumer...");
			StreamConsumer consumer = h.getConsumer(StreamConsumer.TYPE_HTTP,
					new HistoricDump(h, args[4]));

			// And start consuming
			System.err.println("Consuming...");
			System.err.println("--");
			consumer.consume();
		} catch (EInvalidData e) {
			System.err.print("InvalidData: ");
			System.err.println(e.getMessage());
		} catch (ECompileFailed e) {
			System.err.print("CompileFailed: ");
			System.err.println(e.getMessage());
		} catch (EAccessDenied e) {
			System.err.print("AccessDenied: ");
			System.err.println(e.getMessage());
		} catch (EAPIError e) {
			System.err.print("EAPIError: ");
			System.err.println(e.getMessage());
		} catch (ParseException e) {
			System.err.print("ParseException: ");
			System.err.println(e.getMessage());
		}
	}

	private Historic _h = null;
	private FileWriter _fw = null;
	private long _counter = 0L;

	public HistoricDump(Historic h, String fn) {
		_h = h;
		try {
			_fw = new FileWriter(fn);
			_fw.append("ID,Created At,Username,Content\n");
		} catch (IOException e) {
			System.err
					.println("ERR: Failed to create and/or write to the CSV file: "
							+ e.getMessage());
			System.exit(1);
		}
	}

	/**
	 * Called when the connection has been established.
	 * 
	 * @param StreamConsumer
	 *            consumer The consumer object.
	 */
	public void onConnect(StreamConsumer c) {
		System.err.println("Connected, starting historic query");

		// Start the historic query
		try {
			_h.start();
			System.err.println("--");
		} catch (EInvalidData e) {
			System.err.print("EInvalidData: ");
			System.err.println(e.getMessage());
		} catch (EAccessDenied e) {
			System.err.print("EAccessDenied: ");
			System.err.println(e.getMessage());
		} catch (EAPIError e) {
			System.err.print("EAPIError: ");
			System.err.println(e.getMessage());
		}
	}

	/**
	 * Called when the connection has disconnected.
	 * 
	 * @param StreamConsumer
	 *            consumer The consumer object.
	 */
	public void onDisconnect(StreamConsumer c) {
		System.err.println("Disconnected");
		System.err.println("--");
	}

	/**
	 * Handle incoming data.
	 * 
	 * @param StreamConsumer
	 *            consumer The consumer object.
	 * @param Interaction
	 *            interaction The interaction data.
	 * @throws EInvalidData
	 */
	public void onInteraction(StreamConsumer c, Interaction i) {
		try {
			try {
				_fw.append(i.getStringVal("interaction.id"));
			} catch (EInvalidData e) {
				// Ignored
			}

			_fw.append(",\"");

			try {
				_fw.append(i.getStringVal("interaction.created_at").replace(
						"\"", "\\\""));
			} catch (EInvalidData e) {
				// Ignored
			}

			_fw.append("\",\"");

			try {
				_fw.append(i.getStringVal("interaction.author.username")
						.replace("\"", "\\\""));
			} catch (EInvalidData e) {
				// Ignored
			}

			_fw.append("\",\"");

			try {
				_fw.append(i.getStringVal("interaction.content")
						.replace("\"", "\\\"").replace('\n', ' '));
			} catch (EInvalidData e) {
				// Ignored
			}

			_fw.append("\"\n");
			_fw.flush();

			_counter += 1;
			if (_counter % 1000 == 0) {
				System.err.print("\r" + String.valueOf(_counter++));
			}
		} catch (IOException e) {
			System.err
					.println("ERR: Failed to write interaction to the CSV file: "
							+ e.getMessage());
		}
	}

	/**
	 * Handle delete notifications.
	 * 
	 * @param StreamConsumer
	 *            consumer The consumer object.
	 * @param Interaction
	 *            interaction The interaction data.
	 * @throws EInvalidData
	 */
	public void onDeleted(StreamConsumer c, Interaction i) throws EInvalidData {
		// For the purposes of this example we do nothing with deletes, but in
		// your implementation you will need to properly process deletes by
		// removing them from your storage system.
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
		if (type.equalsIgnoreCase("info") && info.has("progress")) {
			try {
				System.err.println(info.getStringVal("progress") + "% done");
			} catch (EInvalidData e) {
				// Failed to get the progress string, ignore it
			}
		} else {
			try {
				System.err.println("STATUS: " + type + " "
						+ info.getStringVal("message"));
			} catch (EInvalidData e1) {
				System.err.println("STATUS: " + type);
			}

			try {
				_h.reloadData();
				System.err.println("STATUS: " + _h.getStatus() + " - "
						+ String.valueOf(_h.getProgress()) + "% done");
				if (_h.getStatus().equals("succeeded")) {
					consumer.stop();
				}
			} catch (EInvalidData e) {
				System.err.println("reloadData: EInvalidData: "
						+ e.getMessage());
			} catch (EAccessDenied e) {
				System.err.println("reloadData: EAccessDenied: "
						+ e.getMessage());
			} catch (EAPIError e) {
				System.err.println("reloadData: EAPIError: " + e.getMessage());
			}
		}
	}

	/**
	 * Called when a warning is received in the data stream.
	 * 
	 * @param DataSift_StreamConsumer
	 *            consumer The consumer object.
	 * @param string
	 *            message The warning message.
	 */
	public void onWarning(StreamConsumer consumer, String message)
			throws EInvalidData {
		System.err.println("Warning: " + message);
	}

	/**
	 * Called when an error is received in the data stream.
	 * 
	 * @param DataSift_StreamConsumer
	 *            consumer The consumer object.
	 * @param string
	 *            message The error message.
	 */
	public void onError(StreamConsumer consumer, String message)
			throws EInvalidData {
		System.err.println("Error: " + message);
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
		System.err.print("Stopped: " + reason);
		try {
			_fw.close();
		} catch (IOException e) {
			System.err.println("ERR: Failed to close the CSV file: "
					+ e.getMessage());
		}
	}
}
