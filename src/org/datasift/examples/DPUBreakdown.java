/**
 * This example gets the DPU associated with the stream given on the command
 * line. It  presents it in a nice ASCII table.
 */
package org.datasift.examples;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.datasift.Config;
import org.datasift.DPU;
import org.datasift.DPUItem;
import org.datasift.Definition;
import org.datasift.EAPIError;
import org.datasift.EAccessDenied;
import org.datasift.ECompileFailed;
import org.datasift.EInvalidData;
import org.datasift.User;

/**
 * @author MediaSift
 * @version 0.1
 */
public class DPUBreakdown {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Please specify the filename of a CSDL definition.");
			return;
		}

		try {
			String csdl = Utils.readFileAsString(args[0]);

			// Authenticate
			System.out.println("Creating user...");
			User user = new User(Config.username, Config.api_key);

			// Create the definition
			System.out.println("Creating definition...");
			System.out.println("  " + csdl);
			Definition def = user.createDefinition(csdl);

			// Get the cost
			DPU c = def.getDPUBreakdown();
			HashMap<String, DPUItem> dpus = c.getDPU();

			// Build an array of TableRows
			ArrayList<TableRow> dputable = new ArrayList<TableRow>();

			// Keep track of the maxlen of each column
			int maxlen_Target = "Target".length();
			int maxlen_TimesUsed = "Times used".length();
			int maxlen_Complexity = "Complexity".length();

			// Create a row for the total at the end
			double totalDPU = c.getTotal();
			TableRow totalRow = new TableRow("Total", 0, totalDPU);
			if (totalRow.getTargetLength() > maxlen_Target) {
				maxlen_Target = totalRow.getTargetLength();
			}
			if (totalRow.getComplexityLength() > maxlen_Complexity) {
				maxlen_Complexity = totalRow.getComplexityLength();
			}

			// Create a row for each DPU item and its targets and add it to the
			// row array
			for (String key : dpus.keySet()) {
				DPUItem ci = dpus.get(key);
				TableRow r = new TableRow(key, ci.getCount(), ci.getDPU());

				if (r.getTargetLength() > maxlen_Target) {
					maxlen_Target = r.getTargetLength();
				}

				if (r.getTimesUsedLength() > maxlen_TimesUsed) {
					maxlen_TimesUsed = r.getTimesUsedLength();
				}

				if (r.getComplexityLength() > maxlen_Complexity) {
					maxlen_Complexity = r.getComplexityLength();
				}

				dputable.add(r);

				if (ci.hasTargets()) {
					HashMap<String, DPUItem> targets = ci.getTargets();
					for (String target : targets.keySet()) {
						DPUItem ci1 = targets.get(target);
						TableRow r1 = new TableRow("  " + target,
								ci1.getCount(), ci1.getDPU());

						if (r1.getTargetLength() > maxlen_Target) {
							maxlen_Target = r1.getTargetLength();
						}

						if (r1.getTimesUsedLength() > maxlen_TimesUsed) {
							maxlen_TimesUsed = r1.getTimesUsedLength();
						}

						if (r1.getComplexityLength() > maxlen_Complexity) {
							maxlen_Complexity = r1.getComplexityLength();
						}

						dputable.add(r1);
					}
				}
			}

			System.out.println();

			// Top border
			System.out.print("/-");
			System.out.print(Utils.repeatString("-", maxlen_Target));
			System.out.print("---");
			System.out.print(Utils.repeatString("-", maxlen_TimesUsed));
			System.out.print("---");
			System.out.print(Utils.repeatString("-", maxlen_Complexity));
			System.out.println("-\\");

			// Header row
			System.out.print("| ");
			System.out.print(String.format("%1$-" + maxlen_Target + "s",
					"Target"));
			System.out.print(" | ");
			System.out.print(String.format("%1$-" + maxlen_TimesUsed + "s",
					"Times used"));
			System.out.print(" | ");
			System.out.print(String.format("%1$-" + maxlen_Complexity + "s",
					"Complexity"));
			System.out.println(" |");

			// Header bottom border
			System.out.print("|-");
			System.out.print(Utils.repeatString("-", maxlen_Target));
			System.out.print("-+-");
			System.out.print(Utils.repeatString("-", maxlen_TimesUsed));
			System.out.print("-+-");
			System.out.print(Utils.repeatString("-", maxlen_Complexity));
			System.out.println("-|");

			// Now the rows
			for (Object row : dputable.toArray()) {
				System.out.print("| ");
				System.out.print(((TableRow) row).getTarget(maxlen_Target));
				System.out.print(" | ");
				System.out.print(((TableRow) row)
						.getTimesUsed(maxlen_TimesUsed));
				System.out.print(" | ");
				System.out.print(((TableRow) row)
						.getComplexity(maxlen_Complexity));
				System.out.println(" |");
			}

			// Total top border
			System.out.print("|-");
			System.out.print(Utils.repeatString("-", maxlen_Target));
			System.out.print("-+-");
			System.out.print(Utils.repeatString("-", maxlen_TimesUsed));
			System.out.print("-+-");
			System.out.print(Utils.repeatString("-", maxlen_Complexity));
			System.out.println("-|");

			// Total
			System.out.print("| ");
			System.out.print(String.format("%1$"
					+ (maxlen_Target + 3 + maxlen_TimesUsed) + "s",
					totalRow.getTarget(totalRow.getTargetLength())));
			System.out.print(" = ");
			System.out.print(totalRow.getComplexity(maxlen_Complexity));
			System.out.println(" |");

			// Bottom border
			System.out.print("\\-");
			System.out.print(Utils.repeatString("-", maxlen_Target));
			System.out.print("---");
			System.out.print(Utils.repeatString("-", maxlen_TimesUsed));
			System.out.print("---");
			System.out.print(Utils.repeatString("-", maxlen_Complexity));
			System.out.println("-/");
			System.out.println();
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
			e.printStackTrace();
		} catch (IOException e) {
			System.out.print("IOException: ");
			System.out.println(e.getMessage());
		}
	}

	public static class TableRow {
		private String _target = "";
		private int _timesused = 0;
		private double _complexity = 0;
		private NumberFormat _f = null;
		private NumberFormat _fc = null;

		public TableRow(String target, int timesused, double complexity) {
			_target = target;
			_timesused = timesused;
			_complexity = complexity;
			_f = new DecimalFormat("#,###,###");
			_fc = new DecimalFormat("#,###,###.##");
		}

		public int getTargetLength() {
			return _target.length();
		}

		public String getTarget(int width) {
			return String.format("%1$-" + width + "s", _target);
		}

		public int getTimesUsedLength() {
			return _f.format(_timesused).length();
		}

		public String getTimesUsed(int width) {
			return String.format("%1$" + width + "s", _f.format(_timesused));
		}

		public int getComplexityLength() {
			return _fc.format(_complexity).length();
		}

		public String getComplexity(int width) {
			return String.format("%1$" + width + "s", _fc.format(_complexity));
		}
	}
}
