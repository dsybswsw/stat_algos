package com.datascience.bayes;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.ToolRunner;
import org.apache.mahout.classifier.naivebayes.test.TestNaiveBayesDriver;

import java.util.logging.Logger;

/**
 * User: dawei, dsybswsw@gmail.com
 * Date: 6/3/13
 */
public class BayesTestingWrapper {
	private final static Logger logger = Logger.getLogger(BayesTestingWrapper.class.getName());

	public static void main(String[] args) throws Exception {
		logger.info("testing the bayes model.");
		ToolRunner.run(new Configuration(), new TestNaiveBayesDriver(), args);
		logger.info("Finish testing the bayes model.");
	}
}

