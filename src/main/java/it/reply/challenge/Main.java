package it.reply.challenge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.reply.challenge.model.Developer;
import it.reply.challenge.model.Dipendente;
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
		challenge(INPUT_A);
		challenge(INPUT_B);
		challenge(INPUT_C);
		challenge(INPUT_D);
		challenge(INPUT_E);
		challenge(INPUT_F);
	}
	
	public static void challenge(String dataset) {

		// Reader
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
			e.order = d - (h + 2);
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
			e.order = m - (h + devNum + 3);
			e.company = line[0];
			e.bonus = Integer.valueOf(line[1]);
			mans.add(e);
		}
		
		// Solver
		Dipendente[][] soluzione = new Dipendente[h][w];
		int score = solver(devs, mans, map, soluzione);
		
		// Validator
		//TODO
		
		// Serializer
		for(int i = 0; i<h; i++) {			
			for(int j = 0; j<w; j++) {
				Dipendente d = soluzione[i][j];
				if(d != null) {
					d.flagPosizionato = true;
					d.x = j;
					d.y = i;
				}
			}
		}
		StringBuilder sb = new StringBuilder();
		for(Developer a : devs) {
			if(a.flagPosizionato) {
				sb.append(a.x + " " + a.y);
			} else {
				sb.append("X");
			}
			sb.append("\n");
		}
		for(Manager a : mans) {
			if(a.flagPosizionato) {
				sb.append(a.x + " " + a.y);
			} else {
				sb.append("X");
			}
			sb.append("\n");
		}
		
		// Writer
		FileUtils.writeFile(dataset, sb.toString(), score);
	}

	private static int solver(List<Developer> devs, List<Manager> mans, CellEnum[][] map, Dipendente[][] soluzione) {
	
		// Ordina per compagnia, così probabilmente li metto vicini e ho bonus
		List<Developer> devsSorted = new ArrayList<>(devs);
		List<Manager> mansSorted = new ArrayList<>(mans);
		
		// Li posiziono nel primo modo possibile
		boolean finitiDev = false;
		boolean finitiMag = false;
		
		for(int i = 0; i<map.length && !finitiDev && !finitiMag; i++) {
			for(int j = 0; j<map[i].length && !finitiDev && !finitiMag; j++) {
				switch(map[i][j]) {
				case DEV:
					Developer d = findBestDev(devsSorted, soluzione, j, i);
					if(d != null) {
						d.flagPosizionato = true;
						d.x = j;
						d.y = i;
						soluzione[i][j] = d;
					} else {
						finitiDev = true;
					}
					break;
				case MAN:
					Manager m = findBestMan(mansSorted, soluzione, j, i);
					if(m != null) {
						m.flagPosizionato = true;
						m.x = j;
						m.y = i;
						soluzione[i][j] = m;
					} else {
						finitiMag = true;
					}
					break;
				}
			}

		}		
	
		return 0;
	}
	
	private static Developer findBestDev(List<Developer> devs, Dipendente[][] soluzione, int x, int y) {
		List<Dipendente> dips = new ArrayList<>();
		try {
			dips.add(soluzione[x-1][y]);
		} catch(Exception e) {}
		try {
			dips.add(soluzione[x+1][y]);
		} catch(Exception e) {}
		try {
			dips.add(soluzione[x][y-1]);
		} catch(Exception e) {}
		try {
			dips.add(soluzione[x][y+1]);
		} catch(Exception e) {}
		dips.removeAll(Collections.singleton(null));
		if(dips.isEmpty()) {
			return devs.get(0);
		}
		int max = 0;
		Developer best = null;
		for(Developer d1 : devs) {
			if(!d1.flagPosizionato) {
				int sum = 0;
				for(Dipendente d2 : dips) {
					sum += calcolaTP(d1, d2);
				}
				if(sum > max) {
					best = d1;
				}
			}
		}
		return best;
	}
	
	private static Manager findBestMan(List<Manager> mans, Dipendente[][] soluzione, int x, int y) {
		List<Dipendente> dips = new ArrayList<>();
		try {
			dips.add(soluzione[x-1][y]);
		} catch(Exception e) {}
		try {
			dips.add(soluzione[x+1][y]);
		} catch(Exception e) {}
		try {
			dips.add(soluzione[x][y-1]);
		} catch(Exception e) {}
		try {
			dips.add(soluzione[x][y+1]);
		} catch(Exception e) {}
		dips.removeAll(Collections.singleton(null));
		if(dips.isEmpty()) {
			return mans.get(0);
		}
		int max = 0;
		Manager best = null;
		for(Manager d1 : mans) {
			if(!d1.flagPosizionato) {
				int sum = 0;
				for(Dipendente d2 : dips) {
					sum += calcolaTP(d1, d2);
				}
				if(sum > max) {
					best = d1;
				}
			}
		}
		return best;
	}

	/**
	 * Calcola il work potential
	 */
	private static int calcolaWP(Developer d1, Developer d2) {
		List<String> a;
		List<String> b;

		a = new ArrayList<>(d1.skills);
		b = new ArrayList<>(d2.skills);
		a.removeAll(b);
		int sharedSkills = d1.skills.size() - a.size();
		
		a = new ArrayList<>(d1.skills);
		b = new ArrayList<>(d2.skills);
		a.removeAll(d2.skills);
		b.removeAll(d1.skills);
		int distinctSkills = a.size() + b.size();
		
		return sharedSkills * distinctSkills;
	}
	
	/**
	 * Calcola il bonus potential
	 */
	private static int calcolaBP(Dipendente d1, Dipendente d2) {
		if(d1.company.equals(d2.company)) {
			return d1.bonus * d2.bonus;
		}
		return 0;
	}
	
	/**
	 * Calcola il total potential
	 */
	private static int calcolaTP(Dipendente d1, Dipendente d2) {
		return calcolaBP(d1, d2)
				+ (d1 instanceof Developer && d2 instanceof Developer?
							calcolaWP((Developer)d1, (Developer)d2)
						: 	0);
	}
	
}
