package data.epinions;

import data.DataArc;
import data.DataArcContent;
import data.DataNode;
import utils.common.LineReader;

import java.io.IOException;
import java.util.StringTokenizer;
import java.util.logging.Logger;

/**
 * User: dawei, dsybswsw@gmail.com
 * Date: 6/19/13
 */
public class EpinionBuilder {
	private EpinionGraph epinionGraph;

	private final static int LOG_MARGGIN = 10000;

	private final static Logger logger = Logger.getLogger(EpinionBuilder.class.getName());

	public EpinionBuilder() {
		epinionGraph = new EpinionGraph();
	}

	public void addFriendshipRelations(String friendshipDataFile) throws IOException {
		LineReader lineReader = new LineReader(friendshipDataFile);
		int index = 0;
		while (lineReader.hasNext()) {
			StringTokenizer tokenizer = new StringTokenizer(lineReader.next());
			String startIndex = tokenizer.nextToken();
			String endIndex = tokenizer.nextToken();
			double arcValue = Double.parseDouble(tokenizer.nextToken());

			epinionGraph.addUserNode(startIndex);
			epinionGraph.addUserNode(endIndex);

			DataNode startNode = epinionGraph.getUserNode(startIndex);
			DataNode endNode = epinionGraph.getUserNode(endIndex);;

			DataArc arc = new DataArc(startNode, endNode, new DataArcContent(EpinionConstants.FRIENDSHIP_OUT, arcValue));
			startNode.addNeighbour(arc);
            DataArc antiArc = new DataArc(endNode, startNode, new DataArcContent(EpinionConstants.FRIENDSHIP_IN, arcValue));
            endNode.addNeighbour(antiArc);

			++index;
			if (index % LOG_MARGGIN == 0) {
				logger.info("load friendship network in line " + index);
			}
		}
		logger.info("Finish load file " + friendshipDataFile);
	}

	public void addAuthorRelations(String authorArticleFile) throws IOException {
		LineReader lineReader = new LineReader(authorArticleFile);
		int index = 0;
		while (lineReader.hasNext()) {
			StringTokenizer tokenizer = new StringTokenizer(lineReader.next(), "\\|");
			String endIndex = tokenizer.nextToken();
			String startIndex = tokenizer.nextToken();
			double arcValue = 1.0;

			epinionGraph.addUserNode(startIndex);
			epinionGraph.addArticleNode(endIndex);

			DataNode startNode = epinionGraph.getUserNode(startIndex);
			DataNode endNode = epinionGraph.getArticleNode(endIndex);;

			DataArc arc = new DataArc(startNode, endNode, new DataArcContent(EpinionConstants.AUTHOR_ARTICAL, arcValue));
			startNode.addNeighbour(arc);
			++index;
			if (index % LOG_MARGGIN == 0) {
				logger.info("load author article network in line " + index);
			}
		}
		logger.info("Finish load file " + authorArticleFile);
	}

	public void addRatingRelations(String ratingFile) throws IOException {
		LineReader lineReader = new LineReader(ratingFile);
		int index = 0;
		while (lineReader.hasNext()) {
			StringTokenizer tokenizer = new StringTokenizer(lineReader.next());
			String endIndex = tokenizer.nextToken();
			String startIndex = tokenizer.nextToken();
			//double arcValue = Double.parseDouble(tokenizer.nextToken());

			epinionGraph.addUserNode(startIndex);
			epinionGraph.addArticleNode(endIndex);

			DataNode startNode = epinionGraph.getUserNode(startIndex);
			DataNode endNode = epinionGraph.getArticleNode(endIndex);;

			DataArc arc = new DataArc(startNode, endNode, new DataArcContent(EpinionConstants.REVIEM, 1));
			startNode.addNeighbour(arc);
			++index;
			if (index % LOG_MARGGIN == 0) {
				logger.info("load review network in line " + index);
			}
		}
		logger.info("Finish load file " + ratingFile);
	}

	public EpinionGraph getEpinionGraph() {
		return epinionGraph;
	}

	public static void main(String[] args) throws IOException {

	}
}
