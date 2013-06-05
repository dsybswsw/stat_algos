// Copyright (c) 2013. Shiwei Wu reserved.
package inference;

import optimization.wrapper.LBFGSWrapper;

/**
 * This implements GradientOptimizer, which relies on GradientComputer to compute gradient and
 * function value.
 *
 * @author Zhifei Li, <zhifei.work@gmail.com>
 */
public class GradientOptimizer extends LBFGSWrapper {
	GradientComputer gradientComputer;

	// Do not useModelDivergenceRegula.
	public GradientOptimizer(int numPara, double[] initWeights, GradientComputer gradientComputer) {
		super(numPara, initWeights);
		this.gradientComputer = gradientComputer;
	}

	@Override
	public double computeFuncValandGradients(double[] weights, double[] gradients) {
		gradientComputer.reComputeFuncValandGradients(weights);
		double[] tmpGradients = gradientComputer.getLatestGradient();
		for (int i = 0; i < tmpGradients.length; ++i) {
			gradients[i] = -tmpGradients[i];
		}
		return -gradientComputer.getLatestFunctionValue();
	}
}
