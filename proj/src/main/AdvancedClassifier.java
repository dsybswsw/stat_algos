// Copyright 2012 Mobvoi Inc. All Rights Reserved.
package main;

import opennlp.model.AbstractModel;

import java.util.Map;
import java.util.logging.Logger;

/**
 * Advanced classifier.
 * 
 * @author Shiwei Wu, <wushiwei@mobvoi.com>
 * @Date Feb 27, 2013
 */
public class AdvancedClassifier {  
  private AbstractModel advancedModel;
  
  private double threshold = 0.3;
  private String other = "other";
  
  private final static Logger logger = Logger.getLogger(AdvancedClassifier.class.getName());
  
  public AdvancedClassifier(AbstractModel model) {
    this.advancedModel = model;
  }
  
  public void setThresHold(double threshold) {
    this.threshold = threshold;
  }
  
  public void setOther(String other) {
    this.other = other;
  }
   
  private double[] inferenceProbs(Map<String, Double> featMap) {
    int size = featMap.size();
    String[] context = new String[size];
    float[] values = new float[size];
    int i = 0;
    for (String key : featMap.keySet()) {
      context[i] = key;
      values[i] = new Double(featMap.get(key)).floatValue();
      ++i;
    }
    return advancedModel.eval(context, values);
  }
  
  public String classify(Map<String, Double> featMap) {
    double[] probs = inferenceProbs(featMap);
    double maxProb = getHighestProb(probs);
    String result = advancedModel.getBestOutcome(probs);
    logger.info("the max probility is " + maxProb + " and classification result is " + result);
    if (maxProb < threshold)
      return other;
    return result;
  }
  
  private double getHighestProb(double[] probs) {
    double maxProb = probs[0];
    for (double prob : probs) {
      if (prob > maxProb) {
        maxProb = prob;
      }
    }
    return maxProb;
  }
}
