package paral_opt;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.util.LineReader;
import org.apache.hadoop.fs.FileStatus;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import java.io.*;

/**
 * Created by wushiwei on 2014/5/27.
 */
public class SGDUtils {
    public final static String PARAM_NUM = "PARAM_NUM";
    public final static String PARAMS = "PARAMS";
    public final static String CLASS_NUM = "CLASS_NUM";

    
    private final static Logger logger = Logger.getLogger(SGDUtils.class.getName());

    public static String buildParamString(double[] weightsVector, int paramNumber) {
        String line = "";
        for (int i = 0; i < paramNumber; ++i) {
            line += weightsVector[i] + " ";
        }
        return line;
    }

    public static String[] buildInitParams(int paramNum) {
        String[] params = new String[paramNum];
        for (int i = 0; i < paramNum; ++i) {
            params[i] = new Double(0.5).toString();
        }
        return params;
    }

    private static double[] parseParamString(String paramString, int paramNumber) {
        double[] weightsVector = new double[paramNumber];
        String[] splits = paramString.split(" ");
        for (int i = 0; i < paramNumber; ++i) {
            weightsVector[i] = Double.parseDouble(splits[i]);
        }
        return weightsVector;
    }
    
    public static void dump_model(String out,int paramNum,  int iter) throws IOException {
        String[] weights = loadWeights(out + "_" + iter, paramNum);
        BufferedWriter buff = new BufferedWriter(new FileWriter(out + ".mdl"));
        for (int i = 0; i < weights.length; ++i) {
            buff.write(weights[i] + "\n");
        }
        buff.close();
    }

    public static String[] loadWeights(String hdPath, int paramNum) throws IOException {
        String[] weight = new String[paramNum];
        Configuration conf = new Configuration();
        FileSystem hdfs = FileSystem.get(conf);
        Path inPath = new Path(hdPath);
        // read all key value from dir hdPath.
        FileStatus[] inputFiles = hdfs.listStatus(inPath);
        if (inputFiles == null) {
            logger.info("path not correct.");
            return null;
        }

        for (FileStatus fs : inputFiles) {
            Path subPath = fs.getPath();
            FSDataInputStream dis = hdfs.open(subPath);
            LineReader in = new LineReader(dis,conf);
            Text line = new Text();

            while(in.readLine(line) > 0){
                String strLine = line.toString();
                logger.info("params : " + strLine);
                String[] tokens = strLine.split("\t");
                int i = Integer.parseInt(tokens[0]);
                weight[i] = tokens[1];
            }
            in.close();
            dis.close();
        }
        return weight;
    }

    public static int handleLine(String line, List<Pair<Integer, Double>> id_feats) {
        String[] tokens = line.split(" ");
        int label = Integer.parseInt(tokens[0]);
        for (int i = 1; i < tokens.length; ++i) {
            String[] kv = tokens[i].split(":");
            int key = Integer.parseInt(kv[0]);
            double val = Double.parseDouble(kv[1]);
            Pair<Integer, Double> kv_pair = new Pair<Integer, Double>(key, val);
            id_feats.add(kv_pair);
        }
        return label;
    }
}
