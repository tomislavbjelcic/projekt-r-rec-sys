package hr.fer.projektr.utilitymatrix;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import hr.fer.projektr.util.RecSysUtil;

public class ItemSimilarityPairs {
	
	private static class UnorderedIntPair {
		int x;
		int y;
		
		UnorderedIntPair(int x, int y) {
			setPair(x, y);
		}
		
		void setPair(int x, int y) {
			this.x = Math.min(x, y);
			this.y = this.x == x ? y : x;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj == null)
				return false;
			if (this == obj)
				return true;
			if (!(obj instanceof UnorderedIntPair))
				return false;
			
			UnorderedIntPair other = (UnorderedIntPair) obj;
			return this.x == other.x && this.y == other.y;
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(x, y);
		}
	}
	private Map<UnorderedIntPair, Double> similarities = new HashMap<>();
	private UnorderedIntPair getterPair = new UnorderedIntPair(0, 0);
	
	public Double putSimilarity(int itemId1, int itemId2, double sim) {
		UnorderedIntPair p = new UnorderedIntPair(itemId1, itemId2);
		return similarities.put(p, sim);
	}
	
	public boolean isSimilarityCalculated(int itemId1, int itemId2) {
		getterPair.setPair(itemId1, itemId2);
		return similarities.containsKey(getterPair);
	}
	
	public Double getSimilarity(int itemId1, int itemId2) {
		getterPair.setPair(itemId1, itemId2);
		return similarities.get(getterPair);
	}
	
	public void putAllSimilaritiesForItem(int itemId, UtilityMatrix m, Map<Integer, Double> userAvgRatings) {
		int itemIdx = m.getColIndexForItemID(itemId);
		if (itemIdx == -1)
			return;
		
		int itemCount = m.itemCount();
		for (int i=0; i<itemCount; i++) {
			if (i==itemIdx)
				continue;
			int itemIdOther = m.getItemIDforColIndex(i);
			double sim = RecSysUtil.itemSimilarity(itemId, itemIdOther, m, userAvgRatings);
			putSimilarity(itemId, itemIdOther, sim);
		}
	}

}
