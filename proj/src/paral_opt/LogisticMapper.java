package paral_opt;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by wushiwei on 2014/5/27.
 */
public class LogisticMapper extends SGDMapper {
    private final static Logger logger = Logger.getLogger(LogisticMapper.class.getName());
    @Override
    public double computeFuncValandGradients(double[] weights, String sample, double[] gradiants) {
        List<Pair<Integer, Double>> pairs = new ArrayList<Pair<Integer, Double>>();
        int y = SGDUtils.handleLine(sample, pairs);
        double liComb = 0;
        for (Pair<Integer, Double> pair : pairs) {
            int id = pair.getFirst();
            if (id >= paramNumber) {continue;}
            liComb += weights[id] * pair.getSecond();
        }
        double h = sigmoid(liComb);
        double funcVal = y * Math.log(h) + (1 - y) * Math.log(1 - h);

        for (int i = 0; i < paramNumber; ++i) {
            gradiants[i] = 0;
        }

        for (Pair<Integer, Double> pair : pairs) {
            gradiants[pair.getFirst()] = (h - y) * pair.getSecond();
        }

        // do normalization.
        logger.info("function value is " + funcVal);
        return funcVal;
    }

    public double sigmoid(double x) {
        return 1.0 / (1 + Math.exp(x));
    }
}
