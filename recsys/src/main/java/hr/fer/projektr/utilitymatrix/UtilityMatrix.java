package hr.fer.projektr.utilitymatrix;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.apache.commons.math3.linear.OpenMapRealMatrix;
import org.apache.commons.math3.linear.SparseRealMatrix;

public class UtilityMatrix {
	
	private SparseRealMatrix storage;
	private BidiMap<Integer, Integer> rowIndexToUserID = new DualHashBidiMap<>();
	private BidiMap<Integer, Integer> colIndexToItemID = new DualHashBidiMap<>();
	private int nextRowIndex = 0;
	private int nextColIndex = 0;
	
	public UtilityMatrix(int maxRows, int maxCols) {
		storage = new OpenMapRealMatrix(maxRows, maxCols);
	}
	
	public SparseRealMatrix getStorage() {
		return storage;
	}
	
	public int maxUserCount() {
		return storage.getRowDimension();
	}
	
	public int maxItemCount() {
		return storage.getColumnDimension();
	}
	
	public int userCount() {
		return nextRowIndex;
	}
	
	public int itemCount() {
		return nextColIndex;
	}
	
	public int addUserID(int userId) {
		int possibleRowIndex = getRowIndexForUserID(userId);
		if (possibleRowIndex != -1)
			return possibleRowIndex;
		if (nextRowIndex >= maxUserCount())
			return -1;
		int rowIndex = nextRowIndex;
		rowIndexToUserID.put(rowIndex, userId);
		nextRowIndex++;
		return rowIndex;
	}
	
	public int addItemID(int itemId) {
		int possibleColIndex = getColIndexForItemID(itemId);
		if (possibleColIndex != -1)
			return possibleColIndex;
		if (nextColIndex >= maxItemCount())
			return -1;
		int colIndex = nextColIndex;
		colIndexToItemID.put(colIndex, itemId);
		nextColIndex++;
		return colIndex;
	}
	
	public int getRowIndexForUserID(int userId) {
		Integer ri = rowIndexToUserID.getKey(userId);
		return ri == null ? -1 : ri.intValue();
	}
	
	
	public int getColIndexForItemID(int itemId) {
		Integer ci = colIndexToItemID.getKey(itemId);
		return ci == null ? -1 : ci.intValue();
	}
	
	public boolean setRating(int userId, int itemId, double rating) {
		int r = this.getRowIndexForUserID(userId);
		int c = this.getColIndexForItemID(itemId);
		if (r == -1 || c == -1)
			return false;
		storage.setEntry(r, c, rating);
		return true;
	}
	
	public double getRating(int userId, int itemId) {
		int r = this.getRowIndexForUserID(userId);
		int c = this.getColIndexForItemID(itemId);
		if (r == -1 || c == -1)
			return 0.0;
		return storage.getEntry(r, c);
	}
	
	
	
	

}
