import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Divya Saxena
 *
 */
public class PosteriorProbability {
	private static final String MESSAGE_CHERRY = "\nProbability that the next candy we pick will be C, given Q: ";
	private static final String MESSAGE_LIME = "\nProbability that the next candy we pick will be L, given Q: ";
	private static final String RESULT_FILE_NAME = "result.txt";
	private static final String EXIT_MESSAGE = "The number of arguments should be 1.\n !!Exiting the program..\n";

	private static List<Double> PRIOR_PROB_OF_BAGS = Arrays.asList(0.1, 0.2, 0.4, 0.2, 0.1);
	private static List<Double> CHERRY_PROB_PER_BAG = Arrays.asList(1.0, 0.75, 0.50, 0.25, 0.0);
	private static List<Double> LIME_PROB_PER_BAG = Arrays.asList(0.0, 0.25, 0.50, 0.75, 1.0);
	private static String Q = "";
	private static Integer Q_LENGTH = 0;
	private static String OUTPUT_TO_FILE = "";

	public void calculatePosteriorProbability(String[] args) {
		List<Double> probOfBag = PRIOR_PROB_OF_BAGS;
		checkNoOfArguments(args);
		Q = args[0];
		Q_LENGTH = Q.length();
		OUTPUT_TO_FILE = "Observation sequence Q: " + Q;
		OUTPUT_TO_FILE += "\nLength of Q: " + Q_LENGTH;
		addOutputForEachObservation(probOfBag);
		writeToFile(OUTPUT_TO_FILE);
	}

	private void writeToFile(String outputData) {
		System.out.println(outputData);
		try {
			FileWriter fstream = new FileWriter(RESULT_FILE_NAME, false);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(outputData);
			out.close();
			fstream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void checkNoOfArguments(String[] args) {
		if (args.length != 1) {
			System.out.println(EXIT_MESSAGE);
			System.exit(0);
		}
	}

	private void addOutputForEachObservation(List<Double> probOfBag) {
		Map<String, Double> tempSum = tempSum(probOfBag);
		for (int i = 1; i <= Q_LENGTH; i++) {
			OUTPUT_TO_FILE += "\n\nAfter Observation " + i + " = " + Q.charAt(i - 1);
			if (Q.charAt(i - 1) == 'C') {
				probOfBag = outputProbToFile(probOfBag, CHERRY_PROB_PER_BAG, tempSum.get("cherry"));
			} else {
				probOfBag = outputProbToFile(probOfBag, LIME_PROB_PER_BAG, tempSum.get("lime"));
			}
			tempSum = tempSum(probOfBag);
			OUTPUT_TO_FILE += MESSAGE_CHERRY + String.format("%.10f", tempSum.get("cherry"));
			OUTPUT_TO_FILE += MESSAGE_LIME + String.format("%.10f", tempSum.get("lime"));
		}
	}

	private List<Double> outputProbToFile(List<Double> probOfBag, List<Double> probPerBag, Double tempSum) {
		for (int j = 0; j < 5; j++) {
			Double temp = (probOfBag.get(j) * probPerBag.get(j)) / tempSum;
			probOfBag.set(j, temp);
			OUTPUT_TO_FILE += "\nP(h" + (j + 1) + " | Q) = " + String.format("%.10f", temp);
		}
		return probOfBag;
	}

	private Map<String, Double> tempSum(List<Double> probOfBag) {
		Map<String, Double> tempSum = new HashMap<>();
		Double tempSumCherry = 0.0;
		Double tempSumLime = 0.0;
		for (int j = 0; j < 5; j++) {
			tempSumCherry += probOfBag.get(j) * CHERRY_PROB_PER_BAG.get(j);
			tempSumLime += probOfBag.get(j) * LIME_PROB_PER_BAG.get(j);
		}
		tempSum.put("cherry", tempSumCherry);
		tempSum.put("lime", tempSumLime);
		return tempSum;
	}
}
