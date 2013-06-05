// Copyright (c) $today.year. Shiwei Wu reserved.
package crf.utils;

public abstract class StateTransformer {
	public final static String START_STATE = "*";

	public abstract double getTranformProb(String lastState, String nowState, int pos);
}
