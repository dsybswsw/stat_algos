/**
 * @file MaxEntMapper.java
 * @author wushiwei
 * @date 2014/05/30 12:24:25
 * @brief 
 *  
 **/

package paral_opt;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class MaxEntMapper extends SGDMapper {
    private final static Logger logger = Logger.getLogger(MaxEntMapper.class.getName());

    private int classNum = 2;
    protected void setup(Context context) throws IOException, InterruptedException {
        Configuration conf = context.getConfiguration();
        // get param number.
        weightstr = conf.getStrings(SGDUtils.PARAMS);
        classNum = conf.getInt(SGDUtils.CLASS_NUM, 0);
        paramNumber = conf.getInt(SGDUtils.PARAM_NUM, 0);
    }

    @Override
    public double computeFuncValandGradients(double[] weights, String sample, double[] gradiants) {
        List<Pair<Integer, Double>> pairs = new ArrayList<Pair<Integer, Double>>();
        int y = SGDUtils.handleLine(sample, pairs);
        double funcVal = get_val(weights, pairs, y); 

        for (int i = 0; i < paramNumber; ++i) {
            gradiants[i] = 0;
        }

        for (Pair<Integer, Double> pair : pairs) {
            double gd = 0;
            for (int i = 0; i < classNum; ++i) {
                double val = 0.0;
                if (i == y) {val = pair.getSecond();}
                gd += get_val(weights, pairs, i) * val;
            }
            int id = pair.getFirst() * classNum + y; 
            gradiants[id] = - pair.getSecond() + gd;
            logger.info("feat val : " + pair.getFirst() + "," + pair.getSecond() + "," + get_val(weights, pairs, 0));
            logger.info("gradient " + id + " : " + gradiants[id]);
        }

        // do normalization.
        logger.info("function value is " + funcVal);
        return funcVal;
    }
    
    public double get_val(double[] weights, List<Pair<Integer, Double>> pairs, int y) {
        double liComb = 0;
        for (Pair<Integer, Double> pair : pairs) {
            int id = pair.getFirst();
            if (id >= paramNumber) {continue;}
            liComb += weights[id * classNum + y] * pair.getSecond();
        }

        double norm = 0;
        for (int j = 0; j < classNum; ++j) {
            double val_y = 0.0;
            for (Pair<Integer, Double> pair : pairs) {
                int id = pair.getFirst();
                if (id * classNum >= paramNumber) {continue;}
                val_y += weights[id * classNum + j] * pair.getSecond();
            }
            norm += Math.exp(val_y);
        }

        double funcVal = Math.exp(liComb) / norm;
        return funcVal;
    }
}

/* vim: set expandtab ts=4 sw=4 sts=4 tw=100: */
