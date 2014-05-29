package paral_opt;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.Tool;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by wushiwei on 2014/5/27.
 */
public abstract class SGDTemplate extends Configured implements Tool {
    // Number of parameters.
    private int paramNumber;

    private double[] weightsVector;
    // Run at most 100 iterations. Default is 100.
    private int maxIterNum = 100;

    // stop criterion.
    private double relativeFuncThreshold = 1e-3;

    // number of histories used to approximate hessian.
    private int numCorrections = 21;

    private int checkConvergeTimes = 0;

    private int[] iprint;
    private int[] iflag;

    private double[] diag;

    private double epsilon = 1.0e-5;
    private double xtol = 1.0e-16; // machine precision

    private double bestFuncVal = 0;

    // Check converge 3 times.
    private final static int MAX_PASS_CONVERGE = 3;

    public static final String INPUT_DIR = "input";
    public static final String OUTPUT_DIR = "output";
    public static final String REDUCE_NUM = "reducenum";
    public static final String JOB_NAME = "jobname";
    public static final String FUNC_VAL = "func_val";

    private final static Logger logger = Logger.getLogger(SGDTemplate.class.getName());

    /**
     * Compute the gradients of the function.
     *
     * @return gradient values.
     */
    public abstract double computeFuncValandGradients(double[] weights, double[] gradiants);

    public SGDTemplate(int paramNumber, double[] initWeights) {
        // Make sure parameter number is equal to the weights number.
        assert paramNumber == initWeights.length;

        this.paramNumber = paramNumber;

        this.weightsVector = new double[paramNumber];
        for (int i = 0; i < paramNumber; i++) {
            if (initWeights != null)
                weightsVector[i] = initWeights[i];
            else
                weightsVector[i] = 1.0 / paramNumber;
        }

        this.iprint = new int[2];
        this.iprint[0] = -1; // specifies the frequency of the output: output at each iterations
        this.iprint[1] = 0;// specifies the type of output generated:

        // ### set the status flag
        this.iflag = new int[1];
        this.iflag[0] = 0;// this will make sure the LBFGS clear all the state information

        // Print the first n weight at each iteration.
        diag = new double[paramNumber];
    }

    public boolean run() {
        int iterNum = 0;
        double[] gradients = new double[paramNumber];
        double nowFuncVal = 0;
        double lastFuncVal = 0;

        while (isConverged() == false && iterNum <= maxIterNum) {
            // First compute the gradients and function value of the objective function.
            logger.info("In iteration " + iterNum);
            // gradients = computeGradients(weightsVector);
			/*for (int i = 0; i < gradients.length; ++i) {
		    logger.info("old gradient " + i + " is " + gradients[i]);
      }*/
            nowFuncVal = computeFuncValandGradients(weightsVector, gradients);
            for (int i = 0; i < gradients.length; ++i) {
                logger.info("gradient " + i + " is " + gradients[i]);
            }
            bestFuncVal = nowFuncVal;
            // check converge.
            if (iterNum != 0 && Math.abs((nowFuncVal - lastFuncVal)) < epsilon) {
                ++checkConvergeTimes;
                // If the functional is smaller
                if (checkConvergeTimes >= MAX_PASS_CONVERGE) {
                    logger.info("The function value converged and optimization stoped.");
                }
                break;
            }

            // update rule.
            boolean success = true;
            //boolean success = runOneIterLBFGS(nowFuncVal, gradients);
            if (!success) {
                logger.info("Failed in line search in iteration " + iterNum);
                return false;
            }

            lastFuncVal = nowFuncVal;
            ++iterNum;
        }
        return true;
    }

    public double[] getWeightVectors() {
        return weightsVector;
    }

    public double getFinalFuncVal() {
        return bestFuncVal;
    }

    private boolean isConverged() {
        return false;
    }

    /*
     * the default LBFGS minimizes the function; so we need to negate the function and graident_vector
     * if we want to maximize the funciton
     */
    /*private boolean runOneIterLBFGS(double functionValue, double[] gradients) {
        assert gradients.length == paramNumber;
        logger.info("Run one iteration of L-BFGS");
        try {
            SGDTemplate.lbfgs(paramNumber, numCorrections, weightsVector, functionValue, gradients, false,
                    diag, iprint, epsilon, xtol, iflag);
        } catch (SGDTemplate.ExceptionWithIflag e) {
            logger.info(e.toString());
            return false;
        }
        return true;
    }*/

    private String buildParamString() {
        String line = "";
        for (int i = 0; i < paramNumber; ++i) {
            line += weightsVector[i] + " ";
        }
        return line;
    }

    private void parseParamString(String paramString) {
        String[] splits = paramString.split(" ");
        for (int i = 0; i < paramNumber; ++i) {
            weightsVector[i] = Double.parseDouble(splits[i]);
        }
        return;
    }

    private boolean runOneIterSGD(double funcVal, double[] gradients) throws IOException {
        Job job = new Job(getConf(), "sgd");
        job.setJarByClass(SGDTemplate.class);
        Configuration conf = job.getConfiguration();

        //conf.setFloat(FUNC_VAL, bestFuncVal);

        String weightstr = buildParamString();
        conf.setStrings("WEIGHT", weightstr);
        conf.setInt("mapred.max.split.size", 64 * 1024 * 1024);
        conf.setInt("io.sort.mb",1024);
        conf.set("mapred.job.priority", "VERY_HIGH");
        conf.setBoolean("mapred.compress.map.output", true);
        conf.set("mapred.map.output.compression.codec", "org.apache.hadoop.io.compress.DefaultCodec");
        conf.setInt("mapred.reduce.parallel.copies", 10);
        conf.set("mapred.map.child.java.opts", "-Xmx4096M");
        conf.set("mapred.reduce.child.java.opts","-Xmx4096M");
        conf.set("mapred.child.java.opts", "-Xmx2048m");
        conf.setFloat("mapred.job.shuffle.input.buffer.percent", Float.valueOf("0.8"));
        conf.set("io.seqfile.compression.type","CompressionType.BLOCK");

        return true;
    }
}
