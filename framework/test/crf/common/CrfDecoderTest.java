// Copyright (c) 2013. Shiwei Wu reserved.
package crf.common;

import crf.features.FeatExtractor;
import crf.features.TaggedSentence;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User: dawei, dsybswsw@gmail.com
 * Date: 5/22/13
 */
public class CrfDecoderTest {
	public static void main(String[] args) throws Exception {
		CRFModel crfModel = new CRFModel("data/crf.model");
		TaggedSentence taggedSentence = new TaggedSentence("aabbnc");
		Set<String> tags = new HashSet<String>(Arrays.asList("A", "A", "A", "B", "B", "C"));
		FeatExtractor featExtractor = new FeatExtractor();
		CRFDecoder crfDecoder = new CRFDecoder(taggedSentence, crfModel, featExtractor, tags);
		crfDecoder.viterbiDecode();
		List<String> tagSeqs = crfDecoder.getTags();
		System.out.println(tagSeqs);
	}
}
