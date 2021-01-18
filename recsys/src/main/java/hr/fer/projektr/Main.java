package hr.fer.projektr;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import hr.fer.projektr.logger.Logger;
import hr.fer.projektr.logger.StandardOutputLogger;
import hr.fer.projektr.util.RecSysUtil;
import hr.fer.projektr.utilitymatrix.UtilityMatrix;
import hr.fer.projektr.utilitymatrix.UtilityMatrixLoader;

public class Main {
	
	private static final Logger logger = new StandardOutputLogger();
	private static final int WHOLE_DATASET_ITEM_COUNT = 17770;
	private static final int WHOLE_DATASET_USER_COUNT = 480189;
	
	public static void main(String[] args) {
		int argc = args.length;
		if (argc < 1) {
			logger.log("Glavni program očekuje barem jedan argument: putanju do direktorija sa podacima.");
			return;
		}
		if (argc > 3) {
			logger.log("Previše argumenata.");
			return;
		}
		
		String trainingSetPathStr = args[0];
		Path trainingSetPath = Paths.get(trainingSetPathStr).toAbsolutePath().normalize();
		int users = argc > 1 ? Integer.parseInt(args[1]) : WHOLE_DATASET_USER_COUNT;
		int items = argc > 2 ? Integer.parseInt(args[2]) : WHOLE_DATASET_ITEM_COUNT;
		logger.log("Argumenti glavnog programa uspješno učitani.\n"
				+ String.format("Putanja: %s; Korisnika: %d; Proizvoda: %d", trainingSetPath, 
						users, items));
		
		UtilityMatrixLoader loader = new UtilityMatrixLoader(users, items, logger);
		UtilityMatrix m = loader.loadUtilityMatrix(trainingSetPath);
		Map<Integer, Double> userAvgRatings = m.getAverageRatingsForUsers();
		int itemCount = m.itemCount();
		for (int i=0; i<itemCount; i++) {
			int itemId1 = m.getItemIDforColIndex(i);
			for (int j=i+1; j<itemCount; j++) {
				int itemId2 = m.getItemIDforColIndex(j);
				double sim = RecSysUtil.itemSimilarity(itemId1, itemId2, m, userAvgRatings);
				String msg = String.format("Sim(%d, %d): %f", itemId1, itemId2, sim);
				logger.log(msg);
			}
		}
	}
	
}
