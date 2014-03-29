package driver.epinions;

import data.epinions.CoAuthorBuilder;
import data.epinions.CoAuthorGraph;

import java.io.IOException;

/**
 * User: dawei, dsybswsw@gmail.com
 * Date: 6/20/13
 */
public class EpinionsExperimentDriver {
	public static void main(String[] args) throws IOException {
		EpinionsDataSet epinionsDataSet = EpinionsDataSet.getInstance();
		CoAuthorBuilder graphBuilder = new CoAuthorBuilder();
		String userFilePath = epinionsDataSet.getUserRelationFile();
		String authorArticlePath = epinionsDataSet.getAuthorArticleFile();
		String ratingPath = epinionsDataSet.getRatingsFile();
		graphBuilder.addFriendshipRelations(userFilePath);
		graphBuilder.addAuthorRelations(authorArticlePath);
		graphBuilder.addRatingRelations(ratingPath);
		CoAuthorGraph epinionGraph = graphBuilder.getEpinionGraph();

		// DataFactorGraphTransformer dataTransformer = new DataFactorGraphTransformer(epinionGraph);
		// FactorGraph factorGraph = dataTransformer.getFactorGraph();
		// FactorGraphInference inference = new FactorGraphInference(factorGraph);
		// inference.doBeliefPropogate();
	}
}
