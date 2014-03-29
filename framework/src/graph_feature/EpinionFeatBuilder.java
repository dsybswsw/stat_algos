package graph_feature;

import data.DataArc;
import data.DataNode;
import data.epinions.EpinionBuilder;
import data.epinions.EpinionGraph;
import data.epinions.EpinionConstants;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Created by wushiwei on 14-3-27.
 */
public class EpinionFeatBuilder {
    private EpinionGraph epinionGraph;

    private final static Logger logger = Logger.getLogger(EpinionFeatBuilder.class.getName());

    public EpinionFeatBuilder() {
        epinionGraph = new EpinionGraph();
    }

    public void initEpinionGraph(String graphFile) {
        EpinionBuilder builder = new EpinionBuilder();
        try {
            builder.addFriendshipRelations(graphFile);
            this.epinionGraph = builder.getEpinionGraph();
        } catch (IOException e) {
            logger.info("initialize failed.");
            e.printStackTrace();
        }
    }

    public double getFeatOutDegree(String nodeName) {
        DataNode node = epinionGraph.getUserNode(nodeName);
        if (node == null) {
            logger.info(nodeName + " empty");
            return -1;
        }
        List<DataArc> arcs = node.getNeighbors(EpinionConstants.FRIENDSHIP_OUT);
        if (arcs == null) {
            return 0;
        }
        int indegree1 = arcs.size();
        return indegree1;
    }

    public double getFeatInDegree(String nodeName) {
        DataNode node = epinionGraph.getUserNode(nodeName);
        if (node == null) {
            logger.info(nodeName + " empty");
            return -1;
        }
        List<DataArc> arcs = node.getNeighbors(EpinionConstants.FRIENDSHIP_IN);
        if (arcs == null) {
            return 0;
        }
        int indegree1 = arcs.size();
        return indegree1;
    }

    public double getCommonNeighbors(String nodeName1, String nodeName2) {
        List<DataArc> neighborSet1 = epinionGraph.getUserNode(nodeName1).getNeighbors(EpinionConstants.FRIENDSHIP_OUT);
        List<DataArc> neighborSet2 = epinionGraph.getUserNode(nodeName2).getNeighbors(EpinionConstants.FRIENDSHIP_OUT);

        Set<String> set1 = new HashSet<String>();
        Set<String> set2 = new HashSet<String>();
        for (DataArc arc : neighborSet1) {
            set1.add(arc.getEndNode().getIndex());
        }

        for (DataArc arc : neighborSet2) {
            set2.add(arc.getEndNode().getIndex());
        }

        set1.retainAll(set2);
        return set1.size();
    }

    public String getFeatLine(String nodeName1, String nodeName2) {
        double inDegree1 = getFeatInDegree(nodeName1);
        double inDegree2 = getFeatInDegree(nodeName2);
        double outDegree1 = getFeatOutDegree(nodeName1);
        double outDegree2 = getFeatOutDegree(nodeName2);
        double commonNeighbors = getCommonNeighbors(nodeName1, nodeName2);
        String res = "";
        res = inDegree1 + " " + inDegree2 + " " + outDegree1 + " " + outDegree2 + " " + commonNeighbors;
        return res;
    }
}
