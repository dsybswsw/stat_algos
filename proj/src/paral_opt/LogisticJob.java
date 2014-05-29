package paral_opt;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * Created by wushiwei on 2014/5/27.
 */
public class LogisticJob extends Configured implements Tool {
    public static int param_num = 4;
    public static int max_iter = 20;

    public static void main(String[] args) {
        try {
            ToolRunner.run(new LogisticJob(), args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int runOneIter(int iter, String in, String out) throws Exception {
        Job job = new Job(getConf(), "lr");
        job.setJarByClass(LogisticJob.class);
        Configuration conf = job.getConfiguration();
        conf.setInt(SGDUtils.PARAM_NUM, param_num);
        if (iter == 0) {
            conf.setStrings(SGDUtils.PARAMS, SGDUtils.buildInitParams(param_num));
        } else {
            String last_out = out + "_" + (iter - 1);
            String[] weights = SGDUtils.loadWeights(last_out, param_num);
            conf.setStrings(SGDUtils.PARAMS, weights);
        }
        setDefualtConf(conf);

        Path input = new Path(in);
        Path output = new Path(out + "_" + iter);
        job.setOutputKeyClass(LongWritable.class);
        job.setOutputValueClass(FloatWritable.class);
        job.setMapOutputKeyClass(LongWritable.class);
        job.setMapOutputValueClass(Text.class);

        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        FileInputFormat.setInputPaths(job, input);
        FileOutputFormat.setOutputPath(job, output);

        job.setMapperClass(LogisticMapper.class);
        job.setReducerClass(SGDReducer.class);
        job.setNumReduceTasks(1);
        return job.waitForCompletion(true) ? 0 : 1;
    }

    @Override
    public int run(String[] args) throws Exception {
        if (args.length != 3) {
            System.out.println("./exe feat_num in out");
            return -1;
        }
        LogisticJob.param_num = Integer.parseInt(args[0]);
        String in = args[1];
        String out = args[2];
        for (int i = 0; i < max_iter; ++i) {
            int ret = runOneIter(i, in, out);
            if (ret == 1) {return 1;}
        }
        SGDUtils.dump_model(out, param_num, max_iter - 1);
        return 0;
    }

    private void setDefualtConf(Configuration conf) {
        //conf.setInt("mapred.max.split.size", 64 * 1024 * 1024);
        conf.setInt("io.sort.mb",64);
        conf.set("mapred.job.priority", "VERY_HIGH");
        conf.setBoolean("mapred.compress.map.output", true);
        conf.set("mapred.map.output.compression.codec", "org.apache.hadoop.io.compress.DefaultCodec");
        conf.setInt("mapred.reduce.parallel.copies", 10);
        conf.set("mapred.map.child.java.opts", "-Xmx512M");
        conf.set("mapred.reduce.child.java.opts","-Xmx512M");
        conf.set("mapred.child.java.opts", "-Xmx512m");
        conf.setFloat("mapred.job.shuffle.input.buffer.percent", Float.valueOf("0.8"));
        conf.set("io.seqfile.compression.type","CompressionType.BLOCK");
    }
}
