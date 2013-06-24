package data;

import data.epinions.EpinionConstants;
import graphical_models.*;

import java.util.*;

/**
 * User: dawei, dsybswsw@gmail.com
 * Date: 6/19/13
 */
public class DataFactorGraphTransformer {
	private DataGraph dataGraph;
	private FactorGraph factorGraph;
	private Map<DataArc, Integer> dataArcIndexer;
	private Map<Integer, Node> variableNodeMap;
	private Map<Integer, Boolean> visitMap;

	public final static int STATE_LENGTH = 2;

	public DataFactorGraphTransformer(DataGraph dataGraph) {
		this.dataGraph = dataGraph;
		this.dataArcIndexer = new HashMap<DataArc, Integer>();
		variableNodeMap = new HashMap<Integer, Node>();
		visitMap = new HashMap<Integer, Boolean>();
		buildFactorGraph();
	}

	private void buildFactorGraph() {
		// build network.
		DataNodeIterator dataNodeIterator = dataGraph.iterator();
		int index = 0;
		while (dataNodeIterator.hasNext()) {
			DataNode node = dataNodeIterator.next();
			List<DataArc> arcs = node.getNeighbors(EpinionConstants.USER_NODE);
			for (DataArc dataArc : arcs) {
				if (!dataArcIndexer.containsKey(dataArc)) {
					++index;
					addNewArcToGraph(dataArc, index);
				}
				Node centerNode = getCorrespondNode(dataArc);
				DataNode start = dataArc.getStartNode();
				DataNode end = dataArc.getEndNode();
				for (DataArc arc : start.getNeighbors(EpinionConstants.USER_NODE)) {
					if (isVisited(arc))
						continue;
					if (!dataArcIndexer.containsKey(dataArc)) {
						++index;
						addNewArcToGraph(dataArc, index);
					}
					Node sideNode = getCorrespondNode(arc);
					++index;
					String factorName = centerNode.getName() + "--" + sideNode.getName();
					Node factorNode = new FactorNode(index, factorName);
					factorNode.addNeighbor(centerNode);
					factorNode.addNeighbor(sideNode);
					centerNode.addNeighbor(factorNode);
					sideNode.addNeighbor(factorNode);
				}

				for (DataArc arc : end.getNeighbors(EpinionConstants.USER_NODE)) {
					if (isVisited(arc))
						continue;
					if (!dataArcIndexer.containsKey(dataArc)) {
						++index;
						addNewArcToGraph(dataArc, index);
					}
					Node sideNode = getCorrespondNode(arc);
					++index;
					String factorName = centerNode.getName() + "--" + sideNode.getName();
					Node factorNode = new FactorNode(index, factorName);
					factorNode.addNeighbor(centerNode);
					factorNode.addNeighbor(sideNode);
					centerNode.addNeighbor(factorNode);
					sideNode.addNeighbor(factorNode);
				}
				visitMap.put(dataArcIndexer.get(dataArc), true);
			}
		}

		// Set roots, the observed variables and connect them to other variables.
		Set<Node> observedNodes = new HashSet<Node>();
		// add feature and increase index.

		for (int key : variableNodeMap.keySet()) {
			Node varNode = variableNodeMap.get(key);
			for (Node observedNode : observedNodes) {
				++index;
				Node factorNode = new FactorNode(index, varNode.getName() + "_" + observedNode.getName());
				factorNode.addNeighbor(varNode);
				factorNode.addNeighbor(observedNode);
				varNode.addNeighbor(factorNode);
				observedNode.addNeighbor(factorNode);
			}
		}
		factorGraph = new FeatureFactorGraph(null, observedNodes);
	}

	private void addNewArcToGraph(DataArc dataArc, int index) {
		dataArcIndexer.put(dataArc, index);
		StringBuilder nameBuilder = new StringBuilder();
		nameBuilder.append(dataArc.getStartNode().getIndex());
		nameBuilder.append("_");
		nameBuilder.append(dataArc.getEndNode().getIndex());
		String name = nameBuilder.toString();
		Node arcNode = new VariableNode(index, name, STATE_LENGTH);
		variableNodeMap.put(index, arcNode);
	}

	public Node getCorrespondNode(DataArc arc) {
		int index = dataArcIndexer.get(arc);
		return variableNodeMap.get(index);
	}

	public FactorGraph getFactorGraph() {
		return factorGraph;
	}

	public boolean isVisited(DataArc arc) {
		if (!dataArcIndexer.containsKey(arc))
			return false;
		int index = dataArcIndexer.get(arc);
		if (!visitMap.containsKey(index))
			return true;
		return visitMap.get(index);
	}
}
