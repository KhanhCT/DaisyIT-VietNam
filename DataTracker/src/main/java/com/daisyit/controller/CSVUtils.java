package com.daisyit.controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class CSVUtils {
	private static final char DEFAULT_SEPARATOR = ',';

	public static void writeLine(Writer w, List<String> values) throws IOException {
		writeLine(w, values, DEFAULT_SEPARATOR, ' ');
	}

	public static void writeLine(Writer w, List<String> values, char separators) throws IOException {
		writeLine(w, values, separators, ' ');
	}

	private static String followCVSformat(String value) {

		String result = value;
		// if (result.contains("\"")) {
		// result = result.replace("\"", "\"\"");
		// }
		return result;

	}

	public static void writeLine(Writer w, List<String> values, char separators, char customQuote) throws IOException {

		boolean first = true;

		// default customQuote is empty

		if (separators == ' ') {
			separators = DEFAULT_SEPARATOR;
		}

		StringBuilder sb = new StringBuilder();
		for (String value : values) {
			if (!first) {
				sb.append(separators);
			}
			if (customQuote == ' ') {
				sb.append(followCVSformat(value));
			} else {
				sb.append(customQuote).append(followCVSformat(value)).append(customQuote);
			}

			first = false;
		}
		sb.append("\n");
		w.append(sb.toString());

	}

	public static List<String[]> readCSV(String csvFile) {
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		 List<String[]> list = new ArrayList<>();
		try {
			br = new BufferedReader(new FileReader(csvFile));
			//br = new BufferedReader(new InputStreamReader(new FileInputStream(csvFile), "UTF8"));
			br.readLine();
			while ((line = br.readLine()) != null) {
				String[] tmp = line.split(cvsSplitBy);	
				list.add(tmp);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}

}
