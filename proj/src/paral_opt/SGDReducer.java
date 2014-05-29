package paral_opt;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import java.util.logging.Logger;

import java.io.IOException;

/**
 * Created by wushiwei on 2014/5/27.
 */
public class SGDReducer extends Reducer<LongWritable, Text, LongWritable, FloatWritable> {
    private int paramNumber = 0;
    private final static Logger logger = Logger.getLogger(LogisticMapper.class.getName());

    protected void setup(Context context) throws IOException, InterruptedException {
        Configuration conf = context.getConfiguration();
        paramNumber = conf.getInt(SGDUtils.PARAM_NUM, 0);
    }

    protected void reduce(LongWritable key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        logger.info("key is " + key.toString());
        double delta = 0;
        double weight = 0;
        for (Text val : values) {
            String line = val.toString();
            String[] splits = line.split(" ");
            weight = Double.parseDouble(splits[0]);
            delta += Double.parseDouble(splits[1]);
        }
        logger.info("weight : " + weight);
        logger.info("delta : " + delta);
        weight -= delta;
        
        logger.info(key.toString() + ":" + weight);
        context.write(key,new FloatWritable((float)weight));
    }
}
