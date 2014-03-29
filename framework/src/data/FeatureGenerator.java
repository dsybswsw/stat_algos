package data;

import driver.epinions.EpinionsDataSet;
import graph_feature.EpinionFeatBuilder;
import utils.common.LineReader;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;

/**
 * Created by wushiwei on 14-3-29.
 */
public class FeatureGenerator {
    public static void main(String[] args) throws IOException {
        EpinionsDataSet epinionsDataSet = EpinionsDataSet.getInstance();
        String userFilePath = epinionsDataSet.getUserRelationFile();

        EpinionFeatBuilder featBuilder = new EpinionFeatBuilder();
        featBuilder.initEpinionGraph(userFilePath);

        LineReader reader = new LineReader(userFilePath);

        String outFilename = "data/epinion-train.txt";
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outFilename));

        while (reader.hasNext()) {
            StringTokenizer tokenizer = new StringTokenizer(reader.next());
            String startIndex = tokenizer.nextToken();
            String endIndex = tokenizer.nextToken();
            String arcValue = tokenizer.nextToken();

            String featLine = featBuilder.getFeatLine(startIndex, endIndex);
            String line = startIndex + " " + endIndex + " " + arcValue + " " + featLine + "\n";
            bufferedWriter.write(line);
        }
        bufferedWriter.close();
        reader.close();
    }
}
