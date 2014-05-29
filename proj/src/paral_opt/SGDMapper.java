package paral_opt;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * Created by wushiwei on 2014/5/27.
 */
public abstract class SGDMapper extends Mapper<LongWritable,Text,LongWritable,Text> {
    private int maxIterNum = 100;

    // stop criterion.
    private double relativeFuncThreshold = 1e-3;

    // number of histories used to approximate hessian.
    private int numCorrections = 21;

    private double epsilon = 1.0e-5;
    private double xtol = 1.0e-16; // machine precision

    private double bestFuncVal = 0;

    private double step = 0.13;

    protected int paramNumber = 0;
    protected String[] weightstr;

    public abstract double computeFuncValandGradients(double[] weights, String sample, double[] gradiants);

    protected void setup(Context context) throws IOException, InterruptedException {
        Configuration conf = context.getConfiguration();
        // get param number.
        paramNumber = conf.getInt(SGDUtils.PARAM_NUM, 0);
        weightstr = conf.getStrings(SGDUtils.PARAMS);
    }

    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String sample = value.toString();
        double[] weights = new double[paramNumber];
        for (int i = 0; i < paramNumber; ++i) {
            weights[i] = Double.parseDouble(weightstr[i]);
        }
        double[] deltas = new double[paramNumber];
        double[] gradients = new double[paramNumber];
        double val = computeFuncValandGradients(weights, sample, gradients);

        boolean success = runOneSGDIter(gradients, deltas);
        for (int i = 0; i < paramNumber; ++i) {
            String out = weights[i] + " " + deltas[i];
            context.write(new LongWritable(i), new Text(out));
        }
    }

    private boolean runOneSGDIter(double[] gradients, double[] deltas) {
        for (int i = 0; i < paramNumber; ++i) {
            deltas[i] = step * gradients[i];
        }
        return true;
    }
}
