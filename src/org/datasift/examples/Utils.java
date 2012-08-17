package org.datasift.examples;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Utils {
	/**
	 * Read a file and return the contents.
	 * 
	 * @param  String filePath The file to read.
	 * @return String          The file contents.
	 * @throws java.io.IOException
	 */
	public static String readFileAsString(String filePath)
			throws java.io.IOException {
		byte[] buffer = new byte[(int) new File(filePath).length()];
		BufferedInputStream f = null;
		try {
			f = new BufferedInputStream(new FileInputStream(filePath));
			f.read(buffer);
		} finally {
			if (f != null)
				try {
					f.close();
				} catch (IOException ignored) {
				}
		}
		return new String(buffer);
	}

	/**
	 * Repeat the given string the given number of times.
	 * 
	 * @param  String s    The string to repeat.
	 * @param  int    reps The numebr of times to repeat it.
	 * @return String      The resulting string.
	 */
	public static String repeatString(String s, int reps) {
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
}
