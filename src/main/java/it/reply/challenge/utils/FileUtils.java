package it.reply.challenge.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
	
	private static final String OUTPUT_PATH = "src/main/resources/out/";
	private static final String OUTPUT_FILE_EXTENSION = ".out.txt";
	private static final String INPUT_FILE_EXTENSION = ".txt";

	private FileUtils() {
	}
	
	public static List<String> readFile(String fileName) {
		List<String> output = new ArrayList<>();
		BufferedReader br = null;
		InputStream is = null;
		try {
			is = FileUtils.class.getClassLoader().getResourceAsStream(fileName);
			if(is == null) {
				throw new IllegalArgumentException("File not found: " + fileName);
			}
			br = new BufferedReader(new InputStreamReader(is));

			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {
				output.add(sCurrentLine);
			}
		} catch (NullPointerException e) {
			System.err.println("File not found in classpath: " + fileName);
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} finally {
			try {
				if (br != null) {
					br.close();
				}
				if (is != null) {
					is.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return output;
	}
	
	public static void writeFile(String fileName, String solution, int score) {
		BufferedWriter bufferedWriter = null;
		FileWriter fileWriter = null;
		try {
			String outputFile = fileName.replace(INPUT_FILE_EXTENSION, "_" + score + OUTPUT_FILE_EXTENSION);
			fileWriter = new FileWriter(OUTPUT_PATH + outputFile);
			bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.append(solution);
			System.out.println("Soluzion wrote to: " + outputFile);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(bufferedWriter != null) {
					bufferedWriter.close();
				}
				fileWriter.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
