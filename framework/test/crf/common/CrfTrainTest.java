// Copyright (c) 2013. Shiwei Wu reserved.
package crf.common;

import crf.features.TaggedSentence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: dawei, dsybswsw@gmail.com
 * Date: 5/20/13
 */
public class CrfTrainTest {
	public static void main(String[] args) {
		TaggedSentence taggedSentence = new TaggedSentence("aaabbc");
		List<String> tags = new ArrayList<String>(Arrays.asList("*", "A", "A", "A", "B", "B", "C"));

		List<TaggedSentence> trainData = new ArrayList<TaggedSentence>();
		trainData.add(taggedSentence);

		List<List<String>> tagList = new ArrayList<List<String>>();
		tagList.add(tags);

		CrfTrainer.train(trainData, tagList, "data/crf.model");
	}
}
