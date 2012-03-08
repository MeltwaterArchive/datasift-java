/**
 */
package org.datasift.examples;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import org.datasift.*;

/**
 * @author MediaSift
 * @version 0.1
 */
public class JDBCFootball implements IStreamConsumerEvents {

	static String  table = "CREATE TABLE example(id  VARCHAR(30),content  VARCHAR(500),PRIMARY KEY(id));";
	//JDBC stuff
	private static Connection conn = null;
	private static Statement statement = null;
	private static PreparedStatement preparedStatement = null;
	/**
	 * @param args
	 */
	public static void main(String[] args) throws SQLException {
		try {
			//for MySQL use com.mysql.jdbc.Driver see http://www.vogella.de/articles/MySQLJava/article.html#javaconnection
			Class.forName("org.hsqldb.jdbcDriver");
		} catch (Exception e) {
			System.out.println("ERROR: failed to load HSQLDB JDBC driver.");
			e.printStackTrace();
			return;
		}
		conn = DriverManager.getConnection("jdbc:hsqldb:mem:aname", "sa", "");
		statement = conn.createStatement();
		statement.execute(table);
		try {
			// Authenticate
			System.out.println("Creating user...");
			User user = new User(Config.username, Config.api_key);

			// Create the definition
			String csdl = "interaction.content contains \"football\"";
			System.out.println("Creating definition...");
			System.out.println("  " + csdl);
			Definition def = user.createDefinition(csdl);

			// Create the consumer
			System.out.println("Getting the consumer...");
			StreamConsumer consumer = def.getConsumer(StreamConsumer.TYPE_HTTP,
					new JDBCFootball());

			// And start consuming
			System.out.println("Consuming...");
			System.out.println("--");
			consumer.consume();
		} catch (EInvalidData e) {
			System.out.print("InvalidData: ");
			System.out.println(e.getMessage());
		} catch (ECompileFailed e) {
			System.out.print("CompileFailed: ");
			System.out.println(e.getMessage());
		} catch (EAccessDenied e) {
			System.out.print("AccessDenied: ");
			System.out.println(e.getMessage());
		}
	}
	/**
	 * This determines the number of interactions to consume before stopping.
	 */
	private int _num = 10;

	/**
	 * Handle incoming data.
	 * 
	 * @param StreamConsumer
	 *            consumer The consumer object.
	 * @param JSONObject
	 *            interaction The interaction data.
	 * @throws EInvalidData
	 */
	public void onInteraction(StreamConsumer c, Interaction i)
			throws EInvalidData {
		try {
			preparedStatement = conn.prepareStatement("insert into  example values (?, ?);");
			preparedStatement.setString(1, i.getStringVal("interaction.id"));
			preparedStatement.setString(2, i.toString());
			System.out.println(i);
		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());
		}
		System.out.println("--");

		// Stop after 10
		if (_num-- == 1) {
			System.out.println("Stopping consumer...");
			c.stop();
		}
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
