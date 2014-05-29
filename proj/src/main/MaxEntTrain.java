package main;

import opennlp.maxent.GIS;
import opennlp.maxent.GISModel;
import opennlp.maxent.io.GISModelWriter;
import opennlp.maxent.io.PlainTextGISModelWriter;
import opennlp.model.OnePassRealValueDataIndexer;
import opennlp.model.RealValueFileEventStream;

import java.io.File;
import java.io.IOException;

/**
 * Created by wushiwei on 2014/5/6.
 */
public class MaxEntTrain {
    public static void train(String trainFilePath, String textModelPath) throws IOException {
        MaxEntFormator.formatTrainTrans(trainFilePath, "./data/tmp.tr");
        RealValueFileEventStream rvfes1 = new RealValueFileEventStream("./data/tmp.tr");
        GISModel realModel = GIS.trainModel(1000, new OnePassRealValueDataIndexer(rvfes1, 1));
        GISModelWriter modelWriter = new PlainTextGISModelWriter(realModel, new File(textModelPath));
        modelWriter.persist();
    }

    public static void main(String[] args) {
        /*if (args == null || args.length != 2) {
            System.out.println("2 arguments: input the training data and return the model");
            return;
        }*/
        //String input = args[0];
        //String outpout = args[1];
        String input = "./data/a1a.t";
        String outpout = "./data/model";
        try {
            MaxEntTrain.train(input, outpout);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
