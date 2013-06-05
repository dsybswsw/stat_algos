package max_ent;

import max_ent.common.MaxEntTrainer;

/**
 * User: dawei, dsybswsw@gmail.com
 * Date: 5/20/13
 */
public class MaxEntTrainerTest {
	public static void main(String[] args) {
		String trainingData = "data/test.tr";
		String modelFile = "data/test.model";
		new MaxEntTrainer().train(trainingData, modelFile);
	}
}
