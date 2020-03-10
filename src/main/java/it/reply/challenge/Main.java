package it.reply.challenge;

import java.util.List;

import it.reply.challenge.utils.FileUtils;

public class Main {

	public static void main(String[] args) {

		// Reader
		String dataset = null;
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
