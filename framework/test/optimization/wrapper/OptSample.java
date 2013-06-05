// Copyright by Shiwei Wu.
package optimization.wrapper;

import java.util.logging.Logger;

/**
 * @author Shiwei Wu
 * @Data May 6, 2013
 */
public class OptSample extends LBFGSWrapper {
	// double[] gradients = new double[2];

	private final static Logger logger = Logger.getLogger(OptSample.class.getName());

	public OptSample(int numPara, double[] initWeights) {
		super(numPara, initWeights);
	}

	@Override
	public double computeFuncValandGradients(double[] weights, double[] gradients) {
		double resFuncVal = (weights[0] - 3) * (weights[0] - 3) + (weights[1] - 4)
						* (weights[1] - 4) + 10;
		logger.info("resFucVal : " + resFuncVal);
		gradients[0] = 2 * (weights[0] - 3);
		gradients[1] = 2 * (weights[1] - 4);
		for (int i = 0; i < gradients.length; ++i) {
			logger.info("gradient " + i + " is " + gradients[i]);
		}
		return resFuncVal;
	}

	public static void main(String[] args) {
		double[] initWeights = new double[2];
		initWeights[0] = -100.0;
		initWeights[1] = -10.0;
		OptSample sample = new OptSample(2, initWeights);
		sample.run();
		double[] gradients = new double[2];
		sample.computeFuncValandGradients(initWeights, gradients);
		for (int i = 0; i < gradients.length; ++i) {
			logger.info("gradient " + i + " is " + gradients[i]);
		}
		double[] finalWeights = sample.getWeightVectors();
		System.out.println("Final weight X is " + finalWeights[0]);
		System.out.println("Final weight Y is " + finalWeights[1]);
		System.out.println("Function value is " + sample.getFinalFuncVal());
	}
}
