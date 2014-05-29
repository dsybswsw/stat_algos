package main;

import opennlp.maxent.io.GISModelReader;
import opennlp.maxent.io.PlainTextGISModelReader;
import opennlp.model.AbstractModel;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wushiwei on 2014/5/6.
 */
public class MaxEntPredict {
    public static void maxEntTest(String[] argv) throws IOException {
        String modelName = argv[0];
        String testFile = argv[1];
        String resultFile = argv[2];
        GISModelReader modelReader = new PlainTextGISModelReader(new File(modelName));
        AbstractModel model = modelReader.getModel();
        AdvancedClassifier classifier = new AdvancedClassifier(model);

        BufferedReader buff = new BufferedReader(new FileReader(new File(testFile)));
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(resultFile)));
        String line = "";
        while ((line = buff.readLine()) != null) {
            Map<String, Double> feat = getFeats(line);
            String lab = classifier.classify(feat);
            writer.write(lab + " " + line + "\n");
        }
        buff.close();
        writer.close();
    }

    public static void main(String[] argv) throws IOException {
        argv = new String[3];
        argv[0] = "./data/model";
        argv[1] = "./data/a1a.txt";
        argv[2] = "./data/result";
        maxEntTest(argv);
    }

    public static Map<String, Double> getFeats(String line) {
        String[] tokens = line.split(" ");
        Map<String, Double> featMap = new HashMap<String, Double>();
        for (String token : tokens) {
            String[] featVal = token.split(":");
            featMap.put(featVal[0], Double.valueOf(featVal[1]));
        }
        return featMap;
    }
}
