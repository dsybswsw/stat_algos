package graphical_models;

import utils.common.Pair;

import java.util.*;
import java.util.logging.Logger;

/**
 * User: dawei, dsybswsw@gmail.com
 * Date: 5/28/13
 */
public class FactorGraphInference {
	private Map<Pair<Node, Node>, Boolean> messageTraverseMap;

	private FactorGraph factorGraph;

	public final static int MAX_INTER = 30;

	private final static Logger logger = Logger.getLogger(FactorGraphInference.class.getName());

	public FactorGraphInference(FactorGraph factorGraph) {
		this.factorGraph = factorGraph;
		messageTraverseMap = new HashMap<Pair<Node, Node>, Boolean>();
	}

	/**
	 * Use observed variable nodes as initial nodes and set the value of observed
	 * state to 1 and other states to 0.
	 */
	public void doBeliefPropogate() {
		Set<Node> observedNodes = factorGraph.getRoots();
		for (int i = 0; i < MAX_INTER; ++i) {
			messageTraverseMap.clear();
			logger.info("Traverse the graph in iteration " + i);
			traverseInWidthFirst(observedNodes);
			logger.info("Finish traverse the graph in iteration " + i);
		}
	}

	public void traverseInWidthFirst(Set<Node> startNodes) {
		Queue<Node> traverseNodeQueue = new LinkedList<Node>();
		traverseNodeQueue.addAll(startNodes);
		while (!traverseNodeQueue.isEmpty()) {
			Node nowNode = traverseNodeQueue.poll();
			for (Node neighbor : nowNode.getNeighbors()) {
				if (isMessageSend(nowNode, neighbor)) {
					sendMessage(nowNode, neighbor);
					traverseNodeQueue.add(neighbor);
					updateMessageTraverseMap(nowNode, neighbor);
				}
			}
		}
	}

	public void sendMessage(Node from, Node to) {
		logger.info("send message from " + from.getName() + " to " + to.getName());
		if (from instanceof VariableNode && to instanceof FactorNode) {
			sendToFactorNodeMessage(from, to, 0);
		} else if (from instanceof FactorNode && to instanceof VariableNode) {
			sendToVariableNodeMessage(from, to, 0);
		}
	}

	public void sendToFactorNodeMessage(Node variable, Node factorNode, int hipCount) {
		if (!(variable instanceof VariableNode && factorNode instanceof FactorNode)) {
			logger.info("Wrong node instance! Must Var -> factor in factor graph.");
			return;
		}
		Message message = new Message();
		if (variable.isObserved()) {
			for (int i = 0; i < ((VariableNode) variable).getStateLength(); ++i) {
				message.setStateVal(i, ((VariableNode) variable).getJointProb(i));
			}
		} else {
			for (int i = 0; i < ((VariableNode) variable).getStateLength(); ++i) {
				double val = 1;
				for (Node neighbor : variable.getNeighbors()) {
					if (neighbor.equals(factorNode))
						continue;
					val *= neighbor.getMessageVal(variable, i);
		 		}
				message.setStateVal(i, val);
			}
		}
		// Message oldMessage = factorNode.getMessage(variable);
		logger.info("message information is " + message.toString());
		variable.setSendMessage(factorNode, message);
	}

	public void sendToVariableNodeMessage(Node factorNode, Node variable, int hipCount) {
		if (!(variable instanceof VariableNode && factorNode instanceof FactorNode)) {
			logger.info("Wrong node instance! Must factor -> var in factor graph.");
			return;
		}

		Message message = new Message();
		if (variable.isObserved()) {
			for (int i = 0; i < ((VariableNode) variable).getStateLength(); ++i) {
				message.setStateVal(i, ((VariableNode) variable).getJointProb(i));
			}
		} else {
			for (int i = 0; i < ((VariableNode) variable).getStateLength(); ++i) {
				double val = 0;
				for (Node neighbor : factorNode.getNeighbors()) {
					if (neighbor.equals(variable))
						continue;
					for (int j = 0; j < ((VariableNode) neighbor).getStateLength(); ++j) {
						val += factorGraph.getFactorValue(neighbor, j, variable, i)
										* neighbor.getMessageVal(factorNode, j);
					}
				}
				message.setStateVal(i, val);
			}
		}
		// Message oldMessage = factorNode.getMessage(variable);
		logger.info("message information is " + message.toString());
		factorNode.setSendMessage(variable, message);

		/*if (hipCount > HOT_LIMIT || isConverged(oldMessage, message)) {
			return ;
		}

		for (Node nextFactor : variable.getNeighbors()) {
			if (nextFactor.equals(factorNode))
				continue;
			sendToFactorNodeMessage(variable, nextFactor, hipCount + 1);
		}*/
	}

	private boolean isMessageSend(Node from, Node to) {
		Pair<Node, Node> link = new Pair<Node, Node>(from, to);
		if (!messageTraverseMap.containsKey(link)) {
			return true;
		}
		return messageTraverseMap.get(link);
	}

	private void updateMessageTraverseMap(Node from, Node to) {
		this.messageTraverseMap.put(new Pair<Node, Node>(from, to), false);
	}
}
