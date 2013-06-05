// Copyright (c) 2013. Shiwei Wu reserved.
package crf.features;

import crf.common.CRFModel;
import crf.utils.CrfIndexer;
import utils.common.Pair;

import java.util.*;

/**
 * User: dawei, dsybswsw@gmail.com
 * Date: 5/23/13
 */
public class CrfIndexerandModelTest {
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
		System.out.println(featTbl);
		System.out.println("feat length : " + indexer.getFeatLength());
		System.out.println("feat : " + indexer.getFeatContext(0));
		System.out.println("feat : " + indexer.getFeatContext(indexer.getFeatLength() - 1));
		System.out.println("feat : " + indexer.getFeatContext(indexer.getFeatLength() + 1));
		System.out.println("feat index : " + indexer.getFeatIndex(new Pair<String, String>("A", "B").toString()));
		System.out.println("feat index : " + indexer.getFeatIndex(new Pair<String, String>("C", "B").toString()));

		// Test Crf Model
		double[] weights = new double[indexer.getFeatLength()];
		for (int i = 0; i < indexer.getFeatLength(); ++i) {
			weights[i] = i * 1.0 / indexer.getFeatLength();
		}
		CRFModel crfModel = new CRFModel(weights, indexer);
		double value = crfModel.getExpValue(featTbl);
		System.out.println("function value is " + value);
	}
}
