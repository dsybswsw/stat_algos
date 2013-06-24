package utils.utils.crf;

import crf.common.CRFModel;
import crf.features.FeatExtractor;
import crf.features.TaggedSentence;
import crf.utils.CRFStateTransformer;
import crf.utils.CrfIndexer;
import forward_backward.FBStateCalculator;
import utils.common.CollectionUtil;

import java.util.*;

/**
 * User: dawei, dsybswsw@gmail.com
 * Date: 5/20/13
 */
public class FBStateCalculatorTest {
	public static void main(String[] args) {
		TaggedSentence taggedSentence = new TaggedSentence("aaabbc");
		List<String> tags = new ArrayList<String>(Arrays.asList("*", "A", "A", "A", "B", "B", "C"));
		Set<String> tagSet = new HashSet<String>(Arrays.asList("A", "A", "A", "B", "B", "C"));
		int length = tags.size();
		FeatExtractor featExtractor = new FeatExtractor();
		Map<String, Double> featTbl = new HashMap<String, Double>();
		for (int m = 1; m <= taggedSentence.size(); ++m) {
			featExtractor.extract(taggedSentence, tags.get(m - 1), tags.get(m), m, featTbl);
		}
		List<Map<String, Double>> featList = new CollectionUtil<Map<String, Double>>()
						.getEmptyList(taggedSentence.size() + 1);
		for (int m = 1; m <= taggedSentence.size(); ++m) {
			Map<String, Double> featMap = new HashMap<String, Double>();
			featExtractor.extractUniGram(taggedSentence, tags.get(m - 1), tags.get(m), m, featMap);
			featList.set(m, featMap);
		}

		CrfIndexer indexer = new CrfIndexer(featTbl.keySet(), tagSet);
		// Test Crf Model
		double[] weights = new double[indexer.getFeatLength()];
		for (int i = 0; i < indexer.getFeatLength(); ++i) {
			weights[i] = (i + 1) * 1.0 / indexer.getFeatLength();
		}
		CRFModel crfModel = new CRFModel(weights, indexer);
		CRFStateTransformer transformer = new CRFStateTransformer(taggedSentence, featExtractor, crfModel);
		FBStateCalculator fbStateCalculator = new FBStateCalculator(length, tagSet, transformer);
		System.out.println("Output result below:");
		double value = fbStateCalculator.getProb(3, "A");
		System.out.println("value is " + value);

		value = fbStateCalculator.getProb(3, "A", "A");
		System.out.println("value is " + value);

		System.out.println("norm value is " + fbStateCalculator.getNorm());
	}
}
