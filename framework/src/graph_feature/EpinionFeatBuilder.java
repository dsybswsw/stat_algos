package graph_feature;

import data.DataArc;
import data.DataNode;
import data.epinions.CoAuthorBuilder;
import data.epinions.CoAuthorGraph;
import data.epinions.EpinionConstants;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by wushiwei on 14-3-27.
 */
public class EpinionFeatBuilder {
    private CoAuthorGraph epinionGraph;

    public EpinionFeatBuilder() {
        epinionGraph = new CoAuthorGraph();
    }

    public void initEpinionGraph(String graphFile) {
        CoAuthorBuilder builder = new CoAuthorBuilder();
        try {
            builder.addAuthorRelations(graphFile);
            this.epinionGraph = builder.getEpinionGraph();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double getFeatOutDegree(String nodeName) {
        DataNode node = epinionGraph.getUserNode(nodeName);
        int indegree1 = node.getNeighbors(EpinionConstants.FRIENDSHIP).size();
        return indegree1;
    }

    public double getFeatInDgree(String nodeName) {
        return getFeatInDgree(nodeName);
    }

    public double getCommonNeighbors(String nodeName1, String nodeName2) {
        List<DataArc> neighborSet1 = epinionGraph.getUserNode(nodeName1).getNeighbors(EpinionConstants.FRIENDSHIP);
        List<DataArc> neighborSet2 = epinionGraph.getUserNode(nodeName2).getNeighbors(EpinionConstants.FRIENDSHIP);
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
}
