package hr.fer.projektr;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import hr.fer.projektr.logger.Logger;
import hr.fer.projektr.logger.StandardOutputLogger;
import hr.fer.projektr.util.RecSysUtil;
import hr.fer.projektr.utilitymatrix.ItemSimilarityPairs;
import hr.fer.projektr.utilitymatrix.TrainingSetFileVisitor;
import hr.fer.projektr.utilitymatrix.UtilityMatrix;
import hr.fer.projektr.utilitymatrix.UtilityMatrixLoader;

public class Main {
	
	private static final Logger logger = new StandardOutputLogger();
	private static final int WHOLE_DATASET_ITEM_COUNT = 17770;
	private static final int WHOLE_DATASET_USER_COUNT = 480189;
	
	public static void main(String[] args) {
		int argc = args.length;
		if (argc < 1) {
			logger.log("Glavni program očekuje barem jedan argument: putanju do direktorija sa podacima.");
			return;
		}
		if (argc > 3) {
			logger.log("Previše argumenata.");
			return;
		}
		
		String trainingSetPathStr = args[0];
		Path trainingSetPath = Paths.get(trainingSetPathStr).toAbsolutePath().normalize();
		int users = argc > 1 ? Integer.parseInt(args[1]) : WHOLE_DATASET_USER_COUNT;
		int items = argc > 2 ? Integer.parseInt(args[2]) : WHOLE_DATASET_ITEM_COUNT;
		logger.log("Argumenti glavnog programa uspješno učitani.\n"
				+ String.format("Putanja: %s; Korisnika: %d; Proizvoda: %d", trainingSetPath, 
						users, items));
		
		UtilityMatrixLoader loader = new UtilityMatrixLoader(users, items, logger);
		UtilityMatrix m = loader.loadUtilityMatrix(trainingSetPath);
		logger.log("Ucitavanje gotovo. Racunam prosjeke...");
		TrainingSetFileVisitor visitor = loader.getVisitor();
		Map<Integer, Double> userAvgRatings = visitor.getUserAvgRatings();
		Map<Integer, Double> itemAvgRatings = visitor.getItemAvgRatings();
		logger.log("Racunanje prosjeka gotovo.");
		
		ItemSimilarityPairs isp = new ItemSimilarityPairs();
		int itemCount = m.itemCount();
		int userCount = m.userCount();
		logger.log("Izracun prosjeka gotov. Racunam ocjene...");
		
		Path qualifyingPath = trainingSetPath.resolve("../qualifying.txt");
		try(BufferedReader br = Files.newBufferedReader(qualifyingPath, StandardCharsets.UTF_8)) {
			int itemId = -1;
			long counter = 0L;
			while (true) {
				String line = br.readLine();
				if (line == null) break;
				
				int lineLen = line.length();
				char last = line.charAt(lineLen - 1);
				if (last == ':') {
					itemId = Integer.parseInt(line.substring(0, lineLen-1));
					continue;
				}
				
				String[] splitted = line.split(",");
				int userId = Integer.parseInt(splitted[0]);
				counter++;
				
				double r = RecSysUtil.estimateRating(userId, itemId, m, isp, userAvgRatings, itemAvgRatings);
				String msg = String.format("User %d Item %d Ocjena: %f. Izračunato %d ocjena", userId, itemId, r, counter);
				logger.log(msg);
				
				
			}
			
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		/*
		int counter = 0;
		for (int u=0; u<userCount; u++) {
			int uid = m.getUserIDforRowIndex(u);
			for (int i=0; i<itemCount; i++) {
				int iid = m.getItemIDforColIndex(i);
				double r = RecSysUtil.estimateRating(uid, iid, m, isp, userAvgRatings, itemAvgRatings);
				counter++;
				logger.log(String.format("(%d, %d) ocjena: %f, prosao ih %d", uid, iid, r, counter));
			}
		}*/
		
		/*
		for (int i=0; i<itemCount; i++) {
			int itemId1 = m.getItemIDforColIndex(i);
			for (int j=i+1; j<itemCount; j++) {
				int itemId2 = m.getItemIDforColIndex(j);
				double sim = RecSysUtil.itemSimilarity(itemId1, itemId2, m, userAvgRatings);
				String msg = String.format("Sim(%d, %d): %f", itemId1, itemId2, sim);
				logger.log(msg);
			}
		}
		int u = 3;
		int i = 3;
		double est = RecSysUtil.estimateRating(u, i, m, isp, userAvgRatings, itemAvgRatings);
		logger.log(String.format("Estimate for user %d item %d: %f", u, i, est));
		*/
	}
	
}
