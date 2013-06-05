// Copyright (c) 2013. Shiwei Wu reserved.
package crf.features;

import java.util.HashMap;
import java.util.Map;

/**
 * User: dawei, dsybswsw@gmail.com
 * Date: 5/23/13
 */
public class FeatExtractorTest {
	public static void main(String[] args) {
		TaggedSentence taggedSentece = new TaggedSentence("aaabbc");
		FeatExtractor featExtractor = new FeatExtractor();
		Map<String, Double> featTbl = new HashMap<String, Double>();
		featExtractor.extract(taggedSentece, "*", "A", 1, featTbl);
		featExtractor.extract(taggedSentece, "B", "C", 6, featTbl);
		System.out.println(featTbl);
	}
}
