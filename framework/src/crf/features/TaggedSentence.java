// Copyright (c) $today.year. Shiwei Wu reserved.
package crf.features;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TaggedSentence {
	private List<String> rawSlots;

	private int size;

	public TaggedSentence(String rawSentence) {
		this.size = rawSentence.length();
		this.rawSlots = new ArrayList<String>(Arrays.asList(new String[size + 1]));
		for (int i = 0; i < rawSentence.length(); ++i) {
			char slot = rawSentence.charAt(i);
			rawSlots.set(i + 1, String.valueOf(slot));
		}
	}

	public int size() {
		return size;
	}

	public String getSlot(int i) {
		if (i < 1 || i > size) {
			return null;
		}
		return rawSlots.get(i);
	}
}