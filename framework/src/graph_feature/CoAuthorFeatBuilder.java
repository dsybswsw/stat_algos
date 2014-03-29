package graph_feature;

import data.DataArc;
import data.DataNode;
import data.coauthor.CoAuthorConstants;
import data.epinions.EpinionBuilder;
import data.epinions.EpinionGraph;
import data.epinions.EpinionConstants;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by wushiwei on 14-3-27.
 */
public class CoAuthorFeatBuilder {
    EpinionGraph coauthorGraph;

    public CoAuthorFeatBuilder() {
        coauthorGraph = new EpinionGraph();
    }

    public void initCoAuthorGraph(String adviserFile, String college, String paperFile) {
        EpinionBuilder builder = new EpinionBuilder();
        try {
            builder.addAuthorRelations(adviserFile);
            builder.addAuthorRelations(college);
            builder.addRatingRelations(paperFile);
            this.coauthorGraph = builder.getEpinionGraph();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double getFeatOutDegree(String nodeName) {
        DataNode node = coauthorGraph.getUserNode(nodeName);
        int indegree1 = node.getNeighbors(EpinionConstants.FRIENDSHIP_OUT).size();
        return indegree1;
    }

    public double getFeatInDgree(String nodeName) {
        return getFeatInDgree(nodeName);
    }

    public double getCommonNeighbors(String nodeName1, String nodeName2) {
        List<DataArc> neighborSet1 = coauthorGraph.getUserNode(nodeName1).getNeighbors(EpinionConstants.FRIENDSHIP_OUT);
        List<DataArc> neighborSet2 = coauthorGraph.getUserNode(nodeName2).getNeighbors(EpinionConstants.FRIENDSHIP_OUT);
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

    public double getPaperCount(String author) {
        List<DataArc> neighbors = coauthorGraph.getUserNode(author).getNeighbors(CoAuthorConstants.AUTHOR_ARTICAL);
        return neighbors.size();
    }

    public double getPaperRatio(String vi, String vj) {
        List<DataArc> neighborSet1 = coauthorGraph.getUserNode(vi).getNeighbors(EpinionConstants.ARTICLE_NODE);
        List<DataArc> neighborSet2 = coauthorGraph.getUserNode(vj).getNeighbors(EpinionConstants.ARTICLE_NODE);
        Set<String> set1 = new HashSet<String>();
        Set<String> set2 = new HashSet<String>();
        for (DataArc arc : neighborSet1) {
            set1.add(arc.getEndNode().getIndex());
        }

        for (DataArc arc : neighborSet2) {
            set2.add(arc.getEndNode().getIndex());
        }

        set1.removeAll(set2);
        return set1.size();
    }

    public double getCoAuthorRatio(String vi, String vj) {
        List<DataArc> neighborSet1 = coauthorGraph.getUserNode(vi).getNeighbors(EpinionConstants.ARTICLE_NODE);
        List<DataArc> neighborSet2 = coauthorGraph.getUserNode(vj).getNeighbors(EpinionConstants.ARTICLE_NODE);
        Set<String> set1 = new HashSet<String>();
        Set<String> set2 = new HashSet<String>();
        for (DataArc arc : neighborSet1) {
            set1.add(arc.getEndNode().getIndex());
        }

        for (DataArc arc : neighborSet2) {
            set2.add(arc.getEndNode().getIndex());
        }

        int size1 = set1.size();
        set1.retainAll(set1);
        int size2 = set1.size();
        return ((double)size2) / ((double)size1) ;
    }
}
