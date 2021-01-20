package hr.fer.projektr.utilitymatrix;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

import hr.fer.projektr.logger.Logger;

public class TrainingSetFileVisitor extends SimpleFileVisitor<Path> {
	
	private static class AvgInfo {
		long sum = 0L;
		long count = 0L;
	}
	
	private static final String FILE_REGEX = "mv_(\\d)+\\.txt";
	private UtilityMatrix utilityMatrix;
	private Logger logger;
	private long itemsVisited = 0L;
	private long ratingsAdded = 0L;
	private long mostRatings = 0L;
	private int cnt = 0;
	private Map<Integer, Double> userAvgRatings = new HashMap<>();
	private Map<Integer, AvgInfo> userAvgRatingsTmp = new HashMap<>();
	private Map<Integer, Double> itemAvgRatings = new HashMap<>();
	
	public TrainingSetFileVisitor(UtilityMatrix utilityMatrix, Logger logger) {
		this.utilityMatrix = utilityMatrix;
		this.logger = logger;
	}
	
	private static int getItemIdFromFileName(String fileName) {
		String idstr = fileName.substring(3, fileName.length() - 4);
		int id = Integer.parseInt(idstr);
		return id;
	}
	
	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		String fileName = file.getFileName().toString();
		boolean matchesRegex = fileName.matches(FILE_REGEX);
		if (!matchesRegex) {
			logger.log(String.format("Datoteka %s nije u ispravnom formatu. Prekidam obilazak...", file.toString()));
			return FileVisitResult.TERMINATE;
		}
		
		int itemId = getItemIdFromFileName(fileName);
		int itemCount = utilityMatrix.itemCount();
		int colIdx = utilityMatrix.addItemID(itemId);
		if (colIdx == -1) {
			logger.log("Krenuo sam obrađivati " + fileName + " ali dostignut je maksimalan broj itema. Prekidam obilazak...");
			return FileVisitResult.TERMINATE;
		}
		if (colIdx < itemCount) {
			logger.log("Item ID " + itemId + " već postoji učitan u matrici. Nastavljam obilazak...");
			return FileVisitResult.CONTINUE;
		}
		
		try (BufferedReader br = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
			itemsVisited++;
			logger.log("Otvaram datoteku " + fileName + ". Otvorio ih već " + itemsVisited);
			
			String firstLine = br.readLine();
			int itemIdCheck = Integer.parseInt(firstLine.substring(0, firstLine.length() - 1));
			if (itemIdCheck != itemId) {
				logger.log("Datoteka " + fileName + " nema prikladan item ID: " + itemIdCheck + ". Prekidam obilazak...");
				return FileVisitResult.TERMINATE;
			}
			
			
			long sum = 0L;
			long counter = 0L;
			while(true) {
				String line = br.readLine();
				if (line == null) break;
				
				cnt++;
				if (cnt < 10)
					continue;
				cnt=0;
				
				String[] splitted = line.split(",");
				int userId = Integer.parseInt(splitted[0]);
				long ratingLong = Long.parseLong(splitted[1]);
				double rating = ratingLong;
				
				
				
				utilityMatrix.addUserID(userId);
				boolean b = utilityMatrix.setRating(userId, itemId, rating);
				if (!b) {
					continue;
				}
				counter++;
				AvgInfo ai = userAvgRatingsTmp.get(userId);
				if (ai == null) {
					ai = new AvgInfo();
					userAvgRatingsTmp.put(userId, ai);
				}
				ai.count++;
				ai.sum += ratingLong;
				sum += ratingLong;
			}
			double avg = counter == 0L ? 0.0 : (sum * 1.0) / counter;
			itemAvgRatings.put(itemId, avg);
			mostRatings = Math.max(counter, mostRatings);
			logger.log("Dodano " + ratingsAdded + " ocjena. Film sa najvise ocjena ih ima " + mostRatings);
		}
		
		return FileVisitResult.CONTINUE;
	}
	
	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
		userAvgRatingsTmp.forEach((k, v) -> {
			if (v.count == 0L)
				return;
			double avg = (v.sum * 1.0) / v.count;
			userAvgRatings.put(k, avg);
		});
		return FileVisitResult.TERMINATE;
	}
	
	public Map<Integer, Double> getUserAvgRatings() {
		return userAvgRatings;
	}
	
	public Map<Integer, Double> getItemAvgRatings() {
		return itemAvgRatings;
	}
	
}
