package data.epinions;

import data.DataGraph;
import data.DataNode;
import data.DataNodeIterator;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * User: dawei, dsybswsw@gmail.com
 * Date: 6/18/13
 */
public class EpinionGraph extends DataGraph{
	private Map<String, DataNode> userNodes;
	private Map<String, DataNode> articles;

	public EpinionGraph() {
		userNodes = new HashMap<String, DataNode>();
		articles = new HashMap<String, DataNode>();
	}

	public DataNode getUserNode(String userIndex) {
		return userNodes.get(userIndex);
	}

	public DataNode getArticleNode(String articleIndex) {
		return articles.get(articleIndex);
	}

	public void addUserNode(String userIndex) {
		if (userNodes.containsKey(userIndex)) {
			return;
		}
		userNodes.put(userIndex, new DataNode(userIndex, EpinionConstants.USER_NODE));
	}

	public void addArticleNode(String articleIndex) {
		if (articles.containsKey(articleIndex)) {
			return;
		}
		userNodes.put(articleIndex, new DataNode(articleIndex, EpinionConstants.ARTICLE_NODE));
	}

	public DataNodeIterator iterator() {
		return new DataNodeIterator(this);
	}

	public Set<String> getUserIndexes() {
		return userNodes.keySet();
	}
}
