package driver.epinions;

import data.DataFactorGraphTransformer;
import data.DataGraph;
import data.DataNode;
import data.epinions.EpinionBuilder;
import data.epinions.EpinionGraph;
import graphical_models.FactorGraph;
import graphical_models.FactorGraphInference;

import java.io.IOException;

/**
 * User: dawei, dsybswsw@gmail.com
 * Date: 6/20/13
 */
public class EpinionsExperimentDriver {
	public static void main(String[] args) throws IOException {
		EpinionsDataSet epinionsDataSet = EpinionsDataSet.getInstance();
		EpinionBuilder graphBuilder = new EpinionBuilder();
		String userFilePath = epinionsDataSet.getUserRelationFile();
		String authorArticlePath = epinionsDataSet.getAuthorArticleFile();
		String ratingPath = epinionsDataSet.getRatingsFile();
		graphBuilder.addFriendshipRelations(userFilePath);
		graphBuilder.addAuthorRelations(authorArticlePath);
		graphBuilder.addRatingRelations(ratingPath);
		EpinionGraph epinionGraph = graphBuilder.getEpinionGraph();

		// DataFactorGraphTransformer dataTransformer = new DataFactorGraphTransformer(epinionGraph);
		// FactorGraph factorGraph = dataTransformer.getFactorGraph();
		// FactorGraphInference inference = new FactorGraphInference(factorGraph);
		// inference.doBeliefPropogate();
	}
}
