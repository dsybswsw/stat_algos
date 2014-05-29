package main;

import libsvm.svm_predict;

/**
 * Created by wushiwei on 2014/5/6.
 */
public class SVMPredict {
    /*static static void predict(String model, String testFile, String resultFile) {
        String[] paramss = new String[3];
        paramss[0] = testFile;
        paramss[1] = model;
        paramss[2] = resultFile;
        svm_predict.predict(paramss);
    }*/
    public static void main(String[] argv) {
        argv = new String[3];
        argv[0] = "./data/a1a.txt";
        argv[1] = "./data/model";
        argv[2]  = "./data/result";
        svm_predict.predict(argv);
    }
}
