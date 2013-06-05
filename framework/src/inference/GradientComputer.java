// Copyright (c) 2013. Shiwei Wu reserved.
package inference;

import java.util.logging.Logger;

/**
 * This implements an abstract GradientComputer, which will be called by GradientOptimizer to
 * compute gradient and function value.
 */
public abstract class GradientComputer {
	protected double[] gradients;
	protected double functionValue;
	protected int numFeatures;

	private final static Logger logger = Logger.getLogger(GradientComputer.class.getName());

	public GradientComputer(int numFeatures) {
		this.numFeatures = numFeatures;
		this.gradients = new double[numFeatures];
	}

	public abstract void reComputeFuncValandGradients(double[] weights);

	public abstract void printLastestStatistics();

	public final double[] getLatestGradient() {
		return gradients;
	}

	public final double getLatestFunctionValue() {
		return functionValue;
	}

	public final void resetGradientAndFuncVal() {
		functionValue = 0;
		for (int i = 0; i < gradients.length; i++)
			gradients[i] = 0;
	}
}
