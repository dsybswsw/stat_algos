package main;

import libsvm.svm_train;

import java.io.IOException;

public class SVMTrainer {
	// private svm_train trainer = new svm_train();
	private svm_train trainer = new svm_train();

	public void train(String trainFile, String modelFile) throws IOException {
		String[] params = new String[6];
		params[0] = "-s"; // svm type
		params[1] = "0";
		params[2] = "-t"; // kernel function
		params[3] = "2";
		params[4] = trainFile;
		params[5] = modelFile;
		trainer.run(params);
	}

    // 3 parameters.
    public void train(String[] argv) throws IOException {
        String[] params = new String[6];
        params[0] = "-s"; // svm type
        params[1] = "0";
        params[2] = "-t"; // kernel function
        params[3] = argv[0];
        params[4] = argv[1];
        params[5] = argv[2];
        trainer.run(params);
    }

	public static void main(String[] args) {
		try {
			String trainFile = "./data/a1a.t";
            String model = "./data/model";
			new SVMTrainer().train(trainFile, model);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
