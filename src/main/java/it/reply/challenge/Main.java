package it.reply.challenge;

import java.util.List;

import it.reply.challenge.utils.FileUtils;

public class Main {
	
	private static final String INPUT_A = "a_solar.txt";
	private static final String INPUT_B = "b_dream.txt";
	private static final String INPUT_C = "c_soup.txt";
	private static final String INPUT_D = "d_maelstrom.txt";
	private static final String INPUT_E = "e_igloos.txt";
	private static final String INPUT_F = "f_glitch.txt";
	
	public static void main(String[] args) {

		// Reader
		String dataset = INPUT_A;
		List<String> file = FileUtils.readFile("in/" + dataset);
		
		// Parser
		//TODO
		
		// Solver
		//TODO
		
		// Validator
		//TODO
		
		// Serializer
		//TODO
		
		// Writer
		FileUtils.writeFile(dataset, null, 0);
	}
}
