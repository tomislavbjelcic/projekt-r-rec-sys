package hr.fer.projektr;

import java.nio.file.Path;
import java.nio.file.Paths;

import hr.fer.projektr.logger.Logger;
import hr.fer.projektr.logger.StandardOutputLogger;
import hr.fer.projektr.utilitymatrix.UtilityMatrix;
import hr.fer.projektr.utilitymatrix.UtilityMatrixLoader;

public class Main {
	
	private static final Logger logger = new StandardOutputLogger();
	
	public static void main(String[] args) {
		logger.log("o da");
		Path trainingSetPath = Paths.get("../download/training_set").normalize();
		UtilityMatrixLoader loader = new UtilityMatrixLoader(480189, 5, logger);
		UtilityMatrix m = loader.loadUtilityMatrix(trainingSetPath);
		System.out.println(m.getRating(218543,33));
	}
	
}
