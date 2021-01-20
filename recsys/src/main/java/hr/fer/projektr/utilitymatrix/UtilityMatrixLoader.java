package hr.fer.projektr.utilitymatrix;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;

import hr.fer.projektr.logger.Logger;

public class UtilityMatrixLoader {
	
	private static final String FILE_REGEX = "mv_(\\d)+\\.txt";
	private int colLimit;
	private int rowLimit;
	private Logger logger;
	private TrainingSetFileVisitor visitor;
	
	public UtilityMatrixLoader(int rowLimit, int colLimit, Logger logger) {
		this.configureDimensionsLimit(rowLimit, colLimit);
		this.logger = Objects.requireNonNull(logger);
	}
	
	public void configureDimensionsLimit(int rowLimit, int colLimit) {
		if (rowLimit < 1)
			throw new NotStrictlyPositiveException(rowLimit);
		if (colLimit < 1)
			throw new NotStrictlyPositiveException(colLimit);
		this.rowLimit = rowLimit;
		this.colLimit = colLimit;
	}

	public UtilityMatrix loadUtilityMatrix(Path trainingSetPath) {
		Objects.requireNonNull(trainingSetPath);
		if (!Files.exists(trainingSetPath))
			throw new RuntimeException("Putanja " + trainingSetPath + " ne postoji.");
		if (!Files.isDirectory(trainingSetPath))
			throw new RuntimeException("Putanja " + trainingSetPath + " ne predstavlja direktorij.");
		
		UtilityMatrix utilityMatrix = new UtilityMatrix(rowLimit, colLimit);
		visitor = new TrainingSetFileVisitor(utilityMatrix, logger);
		try {
			Files.walkFileTree(trainingSetPath, visitor);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return utilityMatrix;
	}
	
	public TrainingSetFileVisitor getVisitor() {
		return visitor;
	}

}
