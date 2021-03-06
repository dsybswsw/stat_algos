package data.epinions;

import data.DataArc;
import data.DataNode;
import driver.epinions.EpinionsDataSet;

import java.io.IOException;

/**
 * User: dawei, dsybswsw@gmail.com
 * Date: 6/20/13
 */
public class DataTest {
	public static void main(String[] args) throws IOException {
		EpinionsDataSet epinionsDataSet = EpinionsDataSet.getInstance();
		EpinionBuilder graphBuilder = new EpinionBuilder();
		String userFilePath = epinionsDataSet.getUserRelationFile();
		// String authorArticlePath = epinionsDataSet.getAuthorArticleFile();
		// String ratingPath = epinionsDataSet.getRatingsFile();
		graphBuilder.addFriendshipRelations(userFilePath);
		// graphBuilder.addAuthorRelations(authorArticlePath);
		// graphBuilder.addRatingRelations(ratingPath);
		EpinionGraph epinionGraph = graphBuilder.getEpinionGraph();
		DataNode node = epinionGraph.getUserNode("209227652");
		System.out.println("node index is " + node.getIndex());
		System.out.println("output the neighbors: " );
		for (DataArc arc : node.getNeighbors(EpinionConstants.FRIENDSHIP_OUT)) {
			System.out.println("neighbor id : " + arc.getEndNode().getIndex());
		}
	}
}
