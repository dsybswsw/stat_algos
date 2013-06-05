// Copyright (c) 2013. Shiwei Wu reserved.
package crf.features;

/**
 * User: dawei, dsybswsw@gmail.com
 * Date: 5/23/13
 */
public class TaggedSenteceTest {
	public static void main(String[] args) {
		TaggedSentence taggedSentence = new TaggedSentence("ACCDD");
		System.out.println(taggedSentence.size());
		System.out.println(taggedSentence.getSlot(1));
		System.out.println(taggedSentence.getSlot(5));
		System.out.println(taggedSentence.getSlot(6));
	}
}
