package hr.fer.projektr.utilitymatrix;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import hr.fer.projektr.logger.Logger;

class TrainingSetFileVisitor extends SimpleFileVisitor<Path> {
	
	private static final String FILE_REGEX = "mv_(\\d)+\\.txt";
	private UtilityMatrix utilityMatrix;
	private Logger logger;
	
	TrainingSetFileVisitor(UtilityMatrix utilityMatrix, Logger logger) {
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
			logger.log("Otvaram datoteku " + fileName);
			
			String firstLine = br.readLine();
			int itemIdCheck = Integer.parseInt(firstLine.substring(0, firstLine.length() - 1));
			if (itemIdCheck != itemId) {
				logger.log("Datoteka " + fileName + " nema prikladan item ID: " + itemIdCheck + ". Prekidam obilazak...");
				return FileVisitResult.TERMINATE;
			}
			
			while(true) {
				String line = br.readLine();
				if (line == null) break;
				String[] splitted = line.split(",");
				int userId = Integer.parseInt(splitted[0]);
				double rating = Double.parseDouble(splitted[1]);
				
				utilityMatrix.addUserID(userId);
				utilityMatrix.setRating(userId, itemId, rating);
			}
		}
		
		return FileVisitResult.CONTINUE;
	}
	
}
