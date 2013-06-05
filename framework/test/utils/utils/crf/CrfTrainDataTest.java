package utils.utils.crf;

import crf.utils.CrfIndexer;
import crf.features.FeatExtractor;
import crf.features.TaggedSentence;
import utils.common.Pair;
import crf.utils.CrfTrainData;
import crf.utils.TrainDataCalculator;

import java.util.*;

/**
 * User: dawei, dsybswsw@gmail.com
 * Date: 5/20/13
 */
public class CrfTrainDataTest {
	public static void main(String[] args) {
		TaggedSentence taggedSentence = new TaggedSentence("aaabbc");
		List<String> tags = new ArrayList<String>(Arrays.asList("*", "A", "A", "A", "B", "B", "C"));
		Set<String> tagSet = new HashSet<String>(Arrays.asList("A", "A", "A", "B", "B", "C"));
		FeatExtractor featExtractor = new FeatExtractor();

		Map<String, Double> featTbl = new HashMap<String, Double>();
		for (int m = 1; m <= taggedSentence.size(); ++m) {
			featExtractor.extract(taggedSentence, tags.get(m - 1), tags.get(m), m, featTbl);
		}
		CrfIndexer indexer = new CrfIndexer(featTbl.keySet(), tagSet);

		List<TaggedSentence> trainData = new ArrayList<TaggedSentence>();
		trainData.add(taggedSentence);

		List<List<String>> tagList = new ArrayList<List<String>>();
		tagList.add(tags);

		CrfTrainData crfTrainData = new CrfTrainData(trainData, tagList, featExtractor);
		System.out.println("size is " + crfTrainData.size());
		System.out.println("feat value " + crfTrainData.getFeatValue(0, 2,
						indexer.getFeatIndex(new Pair<String, String>("a", "A").toString())));

		double[] weights = new double[indexer.getFeatLength()];
		double[] gradients = new double[indexer.getFeatLength()];
		for (int i = 0; i < indexer.getFeatLength(); ++i) {
			weights[i] = (i + 1) * 1.0 / indexer.getFeatLength();
		}
		TrainDataCalculator calculator = new TrainDataCalculator(crfTrainData, featExtractor);
		double funcVal = calculator.computeFuncValandGradients(weights, gradients);
		System.out.println("function value is " + funcVal);
		for (int i = 0; i < indexer.getFeatLength(); ++i) {
			System.out.println("gradient i " + i + " is " + gradients[i]);
		}
	}
}
