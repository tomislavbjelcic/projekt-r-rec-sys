package hr.fer.projektr.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import hr.fer.projektr.logger.Logger;
import hr.fer.projektr.utilitymatrix.ItemSimilarityPairs;
import hr.fer.projektr.utilitymatrix.UtilityMatrix;

public class RecSysUtil {
	
	private static final double SIM_THRESH = 0.2;
	private static final Map<Integer, Double> m1 = new HashMap<>();
	private static final Map<Integer, Double> m2 = new HashMap<>();
	
	public static double itemSimilarity(int itemId1, int itemId2, UtilityMatrix m, 
			Map<Integer, Double> userAvgRatings) {
		int c1 = m.getColIndexForItemID(itemId1);
		int c2 = m.getColIndexForItemID(itemId2);
		if (c1 == -1 || c2 == -1)
			return 0.0;
		
		m.getAllUserRatingsForItemID(itemId1, m1);
		m.getAllUserRatingsForItemID(itemId2, m2);
		
		double numeratorSum = 0.0;
		double sqSum1 = 0.0;
		double sqSum2 = 0.0;
		int to = m.userCount();
		for (var entry : m1.entrySet()) {
			int userId = entry.getKey();
			Double R = m2.get(userId);
			if (R == null)
				continue;
			
			double r1 = entry.getValue().doubleValue();
			double r2 = R.doubleValue();
			
			Double Avg = userAvgRatings.get(userId);
			double avg = Avg == null ? 0.0 : Avg.doubleValue();
			double add1 = r1 - avg;
			double add2 = r2 - avg;
			numeratorSum += (add1 * add2);
			sqSum1 += (add1 * add1);
			sqSum2 += (add2 * add2);
		}
		/*
		for (int i=0; i<to; i++) {
			double r1 = itemVector1.getEntry(i);
			double r2 = itemVector2.getEntry(i);
			if (r1 == 0.0 || r2 == 0.0)
				continue;
			int userId = m.getUserIDforRowIndex(i);
			Double Avg = userAvgRatings.get(userId);
			double avg = Avg == null ? 0.0 : Avg.doubleValue();
			double add1 = r1 - avg;
			double add2 = r2 - avg;
			numeratorSum += (add1 * add2);
			sqSum1 += (add1 * add1);
			sqSum2 += (add2 * add2);
			
		}
		*/
		
		double denominator = Math.sqrt(sqSum1 * sqSum2);
		double result = denominator == 0.0 ? denominator : numeratorSum / denominator;
		m1.clear();
		m2.clear();
		return result;
	}
	
	public static double estimateRating(int userId, int itemId, UtilityMatrix m,
			ItemSimilarityPairs simPairs, Map<Integer, Double> userAvgRatings,
			Map<Integer, Double> itemAvgRatings) {
		double existingRating = m.getRating(userId, itemId);
		if (existingRating != 0.0)
			return existingRating;
		Double userIdAvgRating = userAvgRatings.get(userId);
		Double itemIdAvgRating = itemAvgRatings.get(itemId);
		boolean userHasRated = userIdAvgRating != null;
		boolean itemHasBeenRated = itemIdAvgRating != null;
		if (!userHasRated) {
			return itemHasBeenRated ? itemIdAvgRating.doubleValue() : 3.0;
		}
		if (!itemHasBeenRated) {
			return userIdAvgRating.doubleValue();
		}
		
		int itemCount = m.itemCount();
		double num = 0.0;
		double denom = 0.0;
		for (int i=0; i<itemCount; i++) {
			int itid = m.getItemIDforColIndex(i);
			if (itemId == itid)
				continue;
			double r = m.getRating(userId, itid);
			if (r == 0.0)
				continue;
			
			/*
			Double sim = simPairs.getSimilarity(itemId, itid);
			if (sim == null) {
				double similarity = itemSimilarity(itemId, itid, m, userAvgRatings);
				simPairs.putSimilarity(itemId, itid, similarity);
				sim = similarity;
			}
			
			double simil = sim.doubleValue();
			*/
			double simil = itemSimilarity(itemId, itid, m, userAvgRatings);
			if (simil < SIM_THRESH)
				continue;
			num += (simil * r);
			denom += simil;
		}
		
		double est = denom == 0.0 ? userIdAvgRating.doubleValue() : num / denom;
		return est;
	}
	
	/*
	public static void putAllSimilaritiesForItem(int itemId, UtilityMatrix m, ItemSimilarityPairs sims, Map<Integer, Double> userAvgRatings) {
		int itemIdx = m.getColIndexForItemID(itemId);
		if (itemIdx == -1)
			return;
		
		int itemCount = m.itemCount();
		for (int i=0; i<itemCount; i++) {
			if (i==itemIdx)
				continue;
			int itemIdOther = m.getItemIDforColIndex(i);
			double sim = RecSysUtil.itemSimilarity(itemId, itemIdOther, m, userAvgRatings);
			sims.putSimilarity(itemId, itemIdOther, sim);
		}
	}
	*/
	
	public static void precomputeSimilarities(UtilityMatrix m, ItemSimilarityPairs sims, Map<Integer, Double> userAvgRatings, Path p, 
			Logger logger) {
		try(BufferedReader br = Files.newBufferedReader(p, StandardCharsets.UTF_8)) {
			int itemId = -1;
			long counter = 0L;
			boolean validItem = true;
			int itemCount = m.itemCount();
			while (true) {
				String line = br.readLine();
				if (line == null) break;
				
				int lineLen = line.length();
				char last = line.charAt(lineLen - 1);
				if (last != ':')
					continue;
				
				itemId = Integer.parseInt(line.substring(0, lineLen-1));
				validItem = m.getColIndexForItemID(itemId) != -1;
				if (!validItem) {
					logger.log(String.format("Preskačem izračune sličnosti za item ID %d jer nije dodan.", itemId));
					continue;
				}
				
				for (int i=0; i<itemCount; i++) {
					int itid = m.getItemIDforColIndex(i);
					if (itemId == itid)
						continue;
					
					if (sims.isSimilarityCalculated(itemId, itid))
						continue;
					
					double sim = itemSimilarity(itemId, itid, m, userAvgRatings);
					counter++;
					logger.log(String.format("Izračunao sličnost između itema %d i %d: %f. Izračunato ih je %d",
							itemId, itid, sim, counter));
					sims.putSimilarity(itemId, itid, sim);
				}
				
				
			}
			
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
}
