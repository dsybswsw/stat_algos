package utils.utils.crf;

import crf.common.CRFModel;
import crf.utils.CrfIndexer;
import crf.features.FeatExtractor;
import crf.features.TaggedSentence;
import utils.common.CollectionUtil;
import crf.utils.CRFStateTransformer;

import java.util.*;

/**
 * User: dawei, dsybswsw@gmail.com
 * Date: 5/23/13
 */
public class CRFStateTransformerTest {
	public static void main(String[] args) {
		TaggedSentence taggedSentence = new TaggedSentence("aaabbc");
		List<String> tags = new ArrayList<String>(Arrays.asList("*", "A", "A", "A", "B", "B", "C"));
		Set<String> tagSet = new HashSet<String>(Arrays.asList("A", "A", "A", "B", "B", "C"));
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
		double value = crfModel.getExpValue(featTbl);
		System.out.println("function value is " + value);
		CRFStateTransformer transformer = new CRFStateTransformer(taggedSentence, featExtractor, crfModel);
		value = transformer.getTranformProb("A", "B", 4);
		System.out.println("value in test is : " + value);
		value = transformer.getTranformProb("A", "B", 3);
		System.out.println("value in test is : " + value);
		value = transformer.getTranformProb("*", "A", 1);
		System.out.println("value in test is : " + value);
		value = transformer.getTranformProb("*", "B", 1);
		System.out.println("value in test is : " + value);
		// transformer = new CRFStateTransformer(taggedSentence, featExtractor, crfModel);
	}
}
