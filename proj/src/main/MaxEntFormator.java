package main;

import java.io.*;

/**
 * Created by wushiwei on 2014/5/6.
 */
public class MaxEntFormator {
    public static void formatTrainTrans(String inputName, String outputName) throws IOException {
        BufferedReader buff = new BufferedReader(new FileReader(new File(inputName)));
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(outputName)));

        String line = "";
        while ((line = buff.readLine()) != null) {
            String[] tokens = line.split(" ");
            if (tokens.length < 2) {
                continue;
            }
            int len = tokens.length;
            System.out.println(len);
            String newLine = tokens[0] + " ";
            for (int i = 1; i < len; ++i) {
                String[] feats = tokens[i].split(":");
                newLine += feats[0] + " ";
            }
            writer.write(newLine + "\n");
        }
        buff.close();
        writer.close();
    }

    public static void formatTestTrans(String inputName, String outputName) throws IOException {
        BufferedReader buff = new BufferedReader(new FileReader(new File(inputName)));
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(outputName)));

        String line = "";
        while ((line = buff.readLine()) != null) {
            String[] tokens = line.split(" ");
            if (tokens.length < 1) {
                continue;
            }
            int len = tokens.length;
            String newLine = "";
            for (int i = 0; i < len; ++i) {
                String[] feats = tokens[i].split("");
                newLine += feats[0] + " ";
            }
            writer.write(newLine + "\n");
        }
        buff.close();
        writer.close();
    }
}
