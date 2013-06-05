package com.datascience.bayes;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.ToolRunner;
import org.apache.mahout.classifier.naivebayes.training.TrainNaiveBayesJob;

import java.util.logging.Logger;

/**
 * User: dawei, dsybswsw@gmail.com
 * Date: 6/3/13
 */
public class BayesTrainingWrapper {
	private final static Logger logger = Logger.getLogger(BayesTrainingWrapper.class.getName());

	public static void main(String[] args) throws Exception {
		logger.info("train a bayes model");
		for (int i = 0;i < args.length; ++i) {
			logger.info("args " + i + ":" + args[i]);
		}
		ToolRunner.run(new Configuration(), new TrainNaiveBayesJob(), args);
		logger.info("end train a bayes model");
	}
}
