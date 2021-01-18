package hr.fer.projektr.util;

import java.util.Map;

import org.apache.commons.math3.linear.RealVector;

import hr.fer.projektr.utilitymatrix.UtilityMatrix;

public class RecSysUtil {
	
	public static double itemSimilarity(int itemId1, int itemId2, UtilityMatrix m, 
			Map<Integer, Double> userAvgRatings) {
		RealVector itemVector1 = m.getColVectorForItemID(itemId1);
		RealVector itemVector2 = m.getColVectorForItemID(itemId2);
		if (itemVector1 == null || itemVector2 == null)
			return 0.0;
		
		return 0.0;
	}
	
}
