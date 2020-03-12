package it.reply.challenge;

import java.util.ArrayList;
import java.util.List;

import it.reply.challenge.model.Developer;
import it.reply.challenge.model.Manager;
import it.reply.challenge.utils.FileUtils;

public class Main {
	
	private static final String INPUT_A = "a_solar.txt";
	private static final String INPUT_B = "b_dream.txt";
	private static final String INPUT_C = "c_soup.txt";
	private static final String INPUT_D = "d_maelstrom.txt";
	private static final String INPUT_E = "e_igloos.txt";
	private static final String INPUT_F = "f_glitch.txt";
	
	enum CellEnum {
		UNAV,
		DEV,
		MAN
	}
	
	public static void main(String[] args) {

		// Reader
		String dataset = INPUT_A;
		List<String> file = FileUtils.readFile("in/" + dataset);
		
		// Parser
		int w = Integer.valueOf(file.get(0).split(" ")[0]);
		int h = Integer.valueOf(file.get(0).split(" ")[1]);
		CellEnum[][] map = new CellEnum[h][w];
		for(int H=1; H<=h; H++) {
			String line = file.get(H);
			for(int W=0; W<w; W++) {
				CellEnum cellType = null;
				switch(line.charAt(W)) {
				case '#': cellType = CellEnum.UNAV; break;
				case '_': cellType = CellEnum.DEV; break;
				case 'M': cellType = CellEnum.MAN; break;
				default: System.exit(1);
				}
				map[H-1][W] = cellType;
			}
		}
		int devNum = Integer.valueOf(file.get(h + 1));
		List<Developer> devs = new ArrayList<>(devNum);
		for(int d = h + 2; d < devNum + h + 2; d++) {
			String[] line = file.get(d).split(" ");
			Developer e = new Developer();
			e.company = line[0];
			e.bonus = Integer.valueOf(line[1]);
			int skillNum = Integer.valueOf(line[2]);
			e.skills = new ArrayList<>(skillNum);
			for(int i=3; i < skillNum+3; i++) {
				e.skills.add(line[i]);
			}
			devs.add(e);
		}
		
		int manNum = Integer.valueOf(file.get(h + devNum + 2));
		List<Manager> mans = new ArrayList<>(manNum);
		for(int m = h + devNum + 3; m < manNum + h + devNum + 3; m++) {
			String[] line = file.get(m).split(" ");
			Manager e = new Manager();
			e.company = line[0];
			e.bonus = Integer.valueOf(line[1]);
			mans.add(e);
		}
		
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
