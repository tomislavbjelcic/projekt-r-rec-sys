package hr.fer.projektr.utilitymatrix;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.apache.commons.math3.linear.RealVector;
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
	
	public int getUserIDforRowIndex(int rowIndex) {
		Objects.checkIndex(rowIndex, nextRowIndex);
		return rowIndexToUserID.get(rowIndex).intValue();
	}
	
	public int getItemIDforColIndex(int colIndex) {
		Objects.checkIndex(colIndex, nextColIndex);
		return colIndexToItemID.get(colIndex).intValue();
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
	
	/*
	public RealVector getRowVectorForUserID(int userId) {
		int r = this.getRowIndexForUserID(userId);
		return r == -1 ? null : storage.getRowVector(r);
	}
	*/
	
	public void getAllUserRatingsForItemID(int itemId, Map<Integer, Double> m) {
		int c = this.getColIndexForItemID(itemId);
		if (c == -1)
			return;
		
		int userCount = this.userCount();
		for (int i=0; i<userCount; i++) {
			double r = storage.getEntry(i, c);
			if (r == 0.0)
				continue;
			
			int userId = this.getUserIDforRowIndex(i);
			m.put(userId, r);
			
		}
	}
	
	/*
	public Map<Integer, Double> getAverageRatingsForUsers() {
		Map<Integer, Double> avgRatingsMap = new HashMap<>();
		for (var entry : rowIndexToUserID.entrySet()) {
			int row = entry.getKey();
			int userId = entry.getValue();
			RealVector rowVector = storage.getRowVector(row);
			int dim = rowVector.getDimension();
			int count = 0;
			double sum = 0.0;
			for (int i=0; i<dim; i++) {
				double r = rowVector.getEntry(i);
				if (r == 0.0)
					continue;
				sum += r;
				count++;
			}
			if (count > 0) {
				double avg = sum / count;
				avgRatingsMap.put(userId, avg);
			}
		}
		return avgRatingsMap;
	}
	
	public Map<Integer, Double> getAverageRatingsForItems() {
		Map<Integer, Double> avgRatingsMap = new HashMap<>();
		for (var entry : colIndexToItemID.entrySet()) {
			int col = entry.getKey();
			int itemId = entry.getValue();
			RealVector colVector = storage.getColumnVector(col);
			int dim = colVector.getDimension();
			int count = 0;
			double sum = 0.0;
			for (int i=0; i<dim; i++) {
				double r = colVector.getEntry(i);
				if (r == 0.0)
					continue;
				sum += r;
				count++;
			}
			if (count > 0) {
				double avg = sum / count;
				avgRatingsMap.put(itemId, avg);
			}
		}
		return avgRatingsMap;
	}
	*/
	
	
	

}
