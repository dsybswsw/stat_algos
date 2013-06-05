package graphical_models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Node {
	private int id;
	private String name;
	private List<Node> neighbors;

	private Map<Node, Message> sendMessage;

	private boolean isObserved;

	public Node(int id, String name) {
		this.id = id;
		this.name = name;
		neighbors = new ArrayList<Node>();
		sendMessage = new HashMap<Node, Message>();
	}

	public String getName() {
		return name;
	}

	public void addNeighbor(Node node) {
		neighbors.add(node);
	}

	public String toString() {
		return name;
	}

	public int hashCode() {
		return id;
	}

	public void setIsObserved(boolean isObserved) {
		this.isObserved = isObserved;
	}

	public boolean isObserved() {
		return isObserved;
	}

	public List<Node> getNeighbors() {
		return neighbors;
	}

	public void setSendMessage(Node node, Message message) {
		sendMessage.put(node, message);
	}

	public double getMessageVal(Node node, int stateIdx) {
		if (!sendMessage.containsKey(node))
			return 1.0;
		return sendMessage.get(node).getStateVal(stateIdx);
	}

	public Message getMessage(Node node) {
		return sendMessage.get(node);
	}

	public boolean equals(Object object) {
		if (!(object instanceof Node)) {
			return false;
		}
		Node node = (Node) object;
		if (id == node.id) {
			return true;
		}
		return false;
	}
}
