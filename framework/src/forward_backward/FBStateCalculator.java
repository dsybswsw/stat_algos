// Copyright (c) 2013. Shiwei Wu reserved.
package forward_backward;

import utils.common.CollectionUtil;
import crf.utils.BiStateSlot;
import crf.utils.StateTransformer;
import crf.utils.UniStateSlot;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Calculate the forward and backward state probability.
 *
 * @author Shiwei Wu
 * @Date May 16, 2013
 */
public class FBStateCalculator {
	private int m;
	private Set<String> candStates;
	private StateTransformer stateTransformer;

	// Forward parameters.
	private List<UniStateSlot> alpha;

	// Backward parameters.
	private List<UniStateSlot> beta;

	private List<UniStateSlot> uniStateProbs;
	private List<BiStateSlot> biStateProbs;

	private double normValue;

	private final static Logger logger = Logger.getLogger(FBStateCalculator.class.getName());

	public FBStateCalculator(int m, Set<String> candStates, StateTransformer transformer) {
		this.m = m;
		this.candStates = candStates;
		this.stateTransformer = transformer;

		alpha = new CollectionUtil<UniStateSlot>().getEmptyList(m + 1);
		beta = new CollectionUtil<UniStateSlot>().getEmptyList(m + 1);

		uniStateProbs = new CollectionUtil<UniStateSlot>().getEmptyList(m + 1);
		biStateProbs = new CollectionUtil<BiStateSlot>().getEmptyList(m);

		normValue = 0;
		runForwardBackward();
	}

	public void runForwardBackward() {
		logger.info("Run forward backward algorithm");
		runForward();
		logger.info("Finish forward algorithm and goto backward algorithm");
		runBackward();
		logger.info("Finish backward algorithm and begin to compute the norm value");

		// Compute normalize value.
		normValue = 0;
		for (String state : candStates) {
			normValue += alpha.get(m).getValue(state);
		}

		for (int i = 1; i <= m; ++i) {
			UniStateSlot uniUniStateSlot = new UniStateSlot();
			for (String state : candStates) {
				double ita = alpha.get(i).getValue(state) * beta.get(i).getValue(state);
				uniUniStateSlot.setValue(state, ita);
			}
			uniStateProbs.set(i, uniUniStateSlot);
		}

		for (int i = 1; i <= m - 1; ++i) {
			BiStateSlot biUniStateSlot = new BiStateSlot();
			for (String preState : candStates) {
				for (String nextState : candStates) {
					double ita = alpha.get(i).getValue(preState)
									* stateTransformer.getTranformProb(preState, nextState, i + 1)
									* beta.get(i + 1).getValue(nextState);
					biUniStateSlot.setValue(preState, nextState, ita);
				}
				biStateProbs.set(i, biUniStateSlot);
			}
		}
	}

	private void runForward() {
		// Initialization first forward alpha.
		UniStateSlot firstForwardUniStateSlot = new UniStateSlot();
		for (String state : candStates) {
			firstForwardUniStateSlot.setValue(state,
							stateTransformer.getTranformProb(StateTransformer.START_STATE, state, 1));
		}
		alpha.set(1, firstForwardUniStateSlot);
		for (int j = 2; j <= m; ++j) {
			UniStateSlot forwardUniStateSlot = new UniStateSlot();
			for (String state : candStates) {
				double tmpAlpha = 0;
				for (String lastState : candStates) {
					tmpAlpha += alpha.get(j - 1).getValue(lastState)
									* stateTransformer.getTranformProb(lastState, state, j);
				}
				forwardUniStateSlot.setValue(state, tmpAlpha);
			}
			alpha.set(j, forwardUniStateSlot);
		}
	}

	private void runBackward() {
		// Initialize the last backward probability.
		UniStateSlot lastBackwardUniStateSlot = new UniStateSlot();
		for (String state : candStates) {
			lastBackwardUniStateSlot.setValue(state, 1);
		}
		beta.set(m, lastBackwardUniStateSlot);

		for (int i = m - 1; i >= 1; --i) {
			UniStateSlot backwardUniStateSlot = new UniStateSlot();
			for (String state : candStates) {
				double tmpBeta = 0;
				for (String nextState : candStates) {
					tmpBeta += beta.get(i + 1).getValue(nextState)
									* stateTransformer.getTranformProb(state, nextState, i + 1);
				}
				backwardUniStateSlot.setValue(state, tmpBeta);
			}
			beta.set(i, backwardUniStateSlot);
		}
	}

	public double getProb(int pos, String state) {
		return uniStateProbs.get(pos).getValue(state) / normValue;
	}

	public double getNorm() {
		return normValue;
	}

	public double getProb(int pos, String prevState, String nextState) {
		if (pos == 1)
			return 0;
		return biStateProbs.get(pos - 1).getValue(prevState, nextState) / normValue;
	}
}
