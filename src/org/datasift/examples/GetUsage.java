/**
 * This example will get the usage statistics for the configured user and
 * display them in a pretty table. If a stream hash is passed on the command
 * line, statistics for that stream are displayed.
 *
 * NB: Most of the error handling (exception catching) has been removed for
 * the sake of simplicity. Nearly everything in this library may throw
 * exceptions, and production code should catch them. See the documentation
 * for full details.
 */
package org.datasift.examples;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import org.datasift.*;

/**
 * @author MediaSift
 * @version 0.1
 */
public class GetUsage {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			
			// Authenticate
			System.out.println("Creating user...");
			User user = new User(Config.username, Config.api_key);

			// Have we got a hash?
			String hash = (args.length > 0 ? args[0] : "");

			// Get the usage
			Usage usage = user.getUsage(hash);

			// Build an array of TableRows
			ArrayList<TableRow> table = new ArrayList<TableRow>();
			
			// Keep track of the maxlen of each column
			int maxlen_Type = (hash.length() == 0 ? "Stream" : "Type").length();
			int maxlen_Processed = "Processed".length();
			int maxlen_Delivered = "Delivered".length();

			// Create a row for the total at the end
			TableRow totalRow = new TableRow("Total", usage.getProcessed(), usage.getDelivered());
			if (totalRow.getProcessedLength() > maxlen_Type) {
				maxlen_Type = totalRow.getProcessedLength();
			}
			if (totalRow.getDeliveredLength() > maxlen_Delivered) {
				maxlen_Delivered = totalRow.getDeliveredLength();
			}

			// Create a row for each cost item and its targets and add it to the row array
			for (String key : usage.getItems()) {
				TableRow r = new TableRow(key, usage.getProcessed(key), usage.getDelivered(key));
				
				if (r.getTypeLength() > maxlen_Type) {
					maxlen_Type = r.getTypeLength();
				}
				
				if (r.getProcessedLength() > maxlen_Processed) {
					maxlen_Processed = r.getProcessedLength();
				}
				
				if (r.getDeliveredLength() > maxlen_Delivered) {
					maxlen_Delivered = r.getDeliveredLength();
				}

				table.add(r);
			}
			
			System.out.println();

			// Top border
			System.out.print("/-");
			System.out.print(repeatString("-", maxlen_Type));
			System.out.print("---");
			System.out.print(repeatString("-", maxlen_Processed));
			System.out.print("---");
			System.out.print(repeatString("-", maxlen_Delivered));
			System.out.println("-\\");
			
			// Header row
			System.out.print("| ");
			System.out.print(String.format("%1$-" + maxlen_Type + "s", "Target"));
			System.out.print(" | ");
			System.out.print(String.format("%1$-" + maxlen_Processed + "s", "Times used"));
			System.out.print(" | ");
			System.out.print(String.format("%1$-" + maxlen_Delivered + "s", "Complexity"));
			System.out.println(" |");

			// Header bottom border
			System.out.print("|-");
			System.out.print(repeatString("-", maxlen_Type));
			System.out.print("-+-");
			System.out.print(repeatString("-", maxlen_Processed));
			System.out.print("-+-");
			System.out.print(repeatString("-", maxlen_Delivered));
			System.out.println("-|");

			// Now the rows
			for (Object row : table.toArray()) {
				System.out.print("| ");
				System.out.print(((TableRow)row).getType(maxlen_Type));
				System.out.print(" | ");
				System.out.print(((TableRow)row).getProcessed(maxlen_Processed));
				System.out.print(" | ");
				System.out.print(((TableRow)row).getDelivered(maxlen_Delivered));
				System.out.println(" |");
			}

			// Total top border
			System.out.print("|-");
			System.out.print(repeatString("-", maxlen_Type));
			System.out.print("-+-");
			System.out.print(repeatString("-", maxlen_Processed));
			System.out.print("-+-");
			System.out.print(repeatString("-", maxlen_Delivered));
			System.out.println("-|");

			// Total
			System.out.print("| ");
			System.out.print(totalRow.getType(maxlen_Type));
			System.out.print(" | ");
			System.out.print(totalRow.getProcessed(maxlen_Processed));
			System.out.print(" | ");
			System.out.print(totalRow.getDelivered(maxlen_Delivered));
			System.out.println(" |");
			
			// Bottom border
			System.out.print("\\-");
			System.out.print(repeatString("-", maxlen_Type));
			System.out.print("---");
			System.out.print(repeatString("-", maxlen_Processed));
			System.out.print("---");
			System.out.print(repeatString("-", maxlen_Delivered));
			System.out.println("-/");
			System.out.println();
			
		} catch (EInvalidData e) {
			System.out.print("InvalidData: ");
			System.out.println(e.getMessage());
		} catch (EAccessDenied e) {
			System.out.print("AccessDenied: ");
			System.out.println(e.getMessage());
		} catch (EAPIError e) {
			System.out.print("APIError: ");
			System.out.println(e.getMessage());
		}
	}
	
	private static String repeatString(String s, int reps)
	{
		if (reps < 0) {
			return "";
		}
		
		if (s == null) {
			return null;
		}
		
		StringBuilder stringBuilder = new StringBuilder(s.length() * reps);
		
		for (int i = 0; i < reps; i++) {
			stringBuilder.append(s);
		}
		
		return stringBuilder.toString();
	}

	public static class TableRow {
		private String _type = "";
		private int _processed = 0;
		private int _delivered = 0;
		private NumberFormat _f = null;
		
		public TableRow(String type, int processed, int delivered) {
			_type = type;
			_processed = processed;
			_delivered = delivered;
			_f = new DecimalFormat("#,###,###");
		}
		
		public int getTypeLength() {
			return _type.length();
		}
		
		public String getType(int width) {
			return String.format("%1$-" + width + "s", _type);
		}
		
		public int getProcessedLength() {
			return  _f.format(_processed).length();
		}
		
		public String getProcessed(int width) {
			return String.format("%1$#" + width + "s", _f.format(_processed));
		}
		
		public int getDeliveredLength() {
			return _f.format(_delivered).length();
		}
		
		public String getDelivered(int width) {
			return String.format("%1$#" + width + "s", _f.format(_delivered));
		}
	}
}
