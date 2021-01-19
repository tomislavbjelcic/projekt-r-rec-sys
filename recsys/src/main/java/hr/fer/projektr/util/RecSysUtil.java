package hr.fer.projektr.util;

import java.util.Map;

import org.apache.commons.math3.linear.RealVector;

import hr.fer.projektr.utilitymatrix.ItemSimilarityPairs;
import hr.fer.projektr.utilitymatrix.UtilityMatrix;

public class RecSysUtil {
	
	private static final double SIM_THRESH = 0.25;
	
	public static double itemSimilarity(int itemId1, int itemId2, UtilityMatrix m, 
			Map<Integer, Double> userAvgRatings) {
		RealVector itemVector1 = m.getColVectorForItemID(itemId1);
		RealVector itemVector2 = m.getColVectorForItemID(itemId2);
		if (itemVector1 == null || itemVector2 == null)
			return 0.0;
		
		double numeratorSum = 0.0;
		double sqSum1 = 0.0;
		double sqSum2 = 0.0;
		int to = m.userCount();
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
		
		double denominator = Math.sqrt(sqSum1 * sqSum2);
		double result = denominator == 0.0 ? denominator : numeratorSum / denominator;
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
			
			Double sim = simPairs.getSimilarity(itemId, itid);
			if (sim == null) {
				double similarity = itemSimilarity(itemId, itid, m, userAvgRatings);
				simPairs.putSimilarity(itemId, itid, similarity);
				sim = similarity;
			}
			
			double simil = sim.doubleValue();
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
	
}
