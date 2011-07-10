/**
 * This example gets the cost associated with the stream given on the command
 * line or piped/typed into STDIN. It  presents it in a nice ASCII table.]
 * Note that the CSDL must be enclosed in quotes if given on the command line.
 *
 * php cost.php 'interaction.content contains "football"'
 *  or
 * cat football.csdl | php cost.php
 *
 * NB: Most of the error handling (exception catching) has been removed for
 * the sake of simplicity. Nearly everything in this library may throw
 * exceptions, and production code should catch them. See the documentation
 * for full details.
 */
package org.datasift.examples;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.datasift.*;

/**
 * @author MediaSift
 * @version 0.1
 */
public class CostBreakdown {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Please specify the filename of a CSDL definition.");
			return;
		}
		
		try {
			
			String csdl = CostBreakdown.readFileAsString(args[0]);
			
			// Authenticate
			System.out.println("Creating user...");
			User user = new User(Config.username, Config.api_key);

			// Create the definition
			System.out.println("Creating definition...");
			System.out.println("  " + csdl);
			Definition def = user.createDefinition(csdl);

			// Get the cost
			Cost c = def.getCostBreakdown();
			HashMap<String,CostItem> costs = c.getCosts();

			// Build an array of TableRows
			ArrayList<TableRow> costtable = new ArrayList<TableRow>();
			
			// Keep track of the maxlen of each column
			int maxlen_Target = "Target".length();
			int maxlen_TimesUsed = "Times used".length();
			int maxlen_Complexity = "Complexity".length();

			// Create a row for the total at the end
			int totalCost = c.getTotalCost();
			TableRow totalRow = new TableRow("Total", 0, totalCost);
			if (totalRow.getTargetLength() > maxlen_Target) {
				maxlen_Target = totalRow.getTargetLength();
			}
			if (totalRow.getComplexityLength() > maxlen_Complexity) {
				maxlen_Complexity = totalRow.getComplexityLength();
			}

			// Create a row for each cost item and its targets and add it to the row array
			for (String key : costs.keySet()) {
				CostItem ci = costs.get(key);
				TableRow r = new TableRow(key, ci.getCount(), ci.getCost());
				
				if (r.getTargetLength() > maxlen_Target) {
					maxlen_Target = r.getTargetLength();
				}
				
				if (r.getTimesUsedLength() > maxlen_TimesUsed) {
					maxlen_TimesUsed = r.getTimesUsedLength();
				}
				
				if (r.getComplexityLength() > maxlen_Complexity) {
					maxlen_Complexity = r.getComplexityLength();
				}

				costtable.add(r);
				
				if (ci.hasTargets()) {
					HashMap<String,CostItem> targets = ci.getTargets();
					for (String target : targets.keySet()) {
						CostItem ci1 = targets.get(target);
						TableRow r1 = new TableRow("  " + target, ci1.getCount(), ci1.getCost());
						
						if (r1.getTargetLength() > maxlen_Target) {
							maxlen_Target = r1.getTargetLength();
						}
						
						if (r1.getTimesUsedLength() > maxlen_TimesUsed) {
							maxlen_TimesUsed = r1.getTimesUsedLength();
						}
						
						if (r1.getComplexityLength() > maxlen_Complexity) {
							maxlen_Complexity = r1.getComplexityLength();
						}

						costtable.add(r1);
					}
				}
			}
			
			System.out.println();

			// Top border
			System.out.print("/-");
			System.out.print(repeatString("-", maxlen_Target));
			System.out.print("---");
			System.out.print(repeatString("-", maxlen_TimesUsed));
			System.out.print("---");
			System.out.print(repeatString("-", maxlen_Complexity));
			System.out.println("-\\");
			
			// Header row
			System.out.print("| ");
			System.out.print(String.format("%1$-" + maxlen_Target + "s", "Target"));
			System.out.print(" | ");
			System.out.print(String.format("%1$-" + maxlen_TimesUsed + "s", "Times used"));
			System.out.print(" | ");
			System.out.print(String.format("%1$-" + maxlen_Complexity + "s", "Complexity"));
			System.out.println(" |");

			// Header bottom border
			System.out.print("|-");
			System.out.print(repeatString("-", maxlen_Target));
			System.out.print("-+-");
			System.out.print(repeatString("-", maxlen_TimesUsed));
			System.out.print("-+-");
			System.out.print(repeatString("-", maxlen_Complexity));
			System.out.println("-|");

			// Now the rows
			for (Object row : costtable.toArray()) {
				System.out.print("| ");
				System.out.print(((TableRow)row).getTarget(maxlen_Target));
				System.out.print(" | ");
				System.out.print(((TableRow)row).getTimesUsed(maxlen_TimesUsed));
				System.out.print(" | ");
				System.out.print(((TableRow)row).getComplexity(maxlen_Complexity));
				System.out.println(" |");
			}

			// Total top border
			System.out.print("|-");
			System.out.print(repeatString("-", maxlen_Target));
			System.out.print("-+-");
			System.out.print(repeatString("-", maxlen_TimesUsed));
			System.out.print("-+-");
			System.out.print(repeatString("-", maxlen_Complexity));
			System.out.println("-|");

			// Total
			System.out.print("| ");
			System.out.print(String.format("%1$#" + (maxlen_Target + 3 + maxlen_TimesUsed) + "s", totalRow.getTarget(totalRow.getTargetLength())));
			System.out.print(" = ");
			System.out.print(totalRow.getComplexity(maxlen_Complexity));
			System.out.println(" |");
			
			// Bottom border
			System.out.print("\\-");
			System.out.print(repeatString("-", maxlen_Target));
			System.out.print("---");
			System.out.print(repeatString("-", maxlen_TimesUsed));
			System.out.print("---");
			System.out.print(repeatString("-", maxlen_Complexity));
			System.out.println("-/");
			System.out.println();
			
			int tierNum = 0;
			String tierDesc = "";

			if (totalCost > 1000)
			{
				tierNum = 3;
				tierDesc = "high complexity";
			}
			else if (totalCost > 100)
			{
				tierNum = 2;
				tierDesc = "medium complexity";
			}
			else
			{
				tierNum = 1;
				tierDesc = "simple complexity";
			}

			System.out.println("A total cost of " + totalRow.getComplexity(totalRow.getComplexityLength()) + " puts this stream in tier " + tierNum + ", " + tierDesc);
			
		} catch (EInvalidData e) {
			System.out.print("InvalidData: ");
			System.out.println(e.getMessage());
		} catch (ECompileFailed e) {
			System.out.print("CompileFailed: ");
			System.out.println(e.getMessage());
		} catch (EAccessDenied e) {
			System.out.print("AccessDenied: ");
			System.out.println(e.getMessage());
		} catch (EAPIError e) {
			System.out.print("APIError: ");
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.print("IOException: ");
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * 
	 * @param filePath
	 * @return
	 * @throws java.io.IOException
	 */
	private static String readFileAsString(String filePath) throws java.io.IOException{
	    byte[] buffer = new byte[(int) new File(filePath).length()];
	    BufferedInputStream f = null;
	    try {
	        f = new BufferedInputStream(new FileInputStream(filePath));
	        f.read(buffer);
	    } finally {
	        if (f != null) try { f.close(); } catch (IOException ignored) { }
	    }
	    return new String(buffer);
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
		private String _target = "";
		private int _timesused = 0;
		private int _complexity = 0;
		private NumberFormat _f = null;
		
		public TableRow(String target, int timesused, int complexity) {
			_target = target;
			_timesused = timesused;
			_complexity = complexity;
			_f = new DecimalFormat("#,###,###");
		}
		
		public int getTargetLength() {
			return _target.length();
		}
		
		public String getTarget(int width) {
			return String.format("%1$-" + width + "s", _target);
		}
		
		public int getTimesUsedLength() {
			return  _f.format(_timesused).length();
		}
		
		public String getTimesUsed(int width) {
			return String.format("%1$#" + width + "s", _f.format(_timesused));
		}
		
		public int getComplexityLength() {
			return _f.format(_complexity).length();
		}
		
		public String getComplexity(int width) {
			return String.format("%1$#" + width + "s", _f.format(_complexity));
		}
	}
}
