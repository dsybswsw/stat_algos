package data.coauthor;

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
public class CoAuthorGraph extends DataGraph{
	private Map<String, DataNode> userNodes;
	private Map<String, DataNode> articles;

	public CoAuthorGraph() {
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
		userNodes.put(userIndex, new DataNode(userIndex, CoAuthorConstants.USER_NODE));
	}

	public void addArticleNode(String articleIndex) {
		if (articles.containsKey(articleIndex)) {
			return;
		}
		userNodes.put(articleIndex, new DataNode(articleIndex, CoAuthorConstants.ARTICLE_NODE));
	}

	public DataNodeIterator iterator() {
		return new DataNodeIterator(this);
	}

    @Override
    public Set<String> getUserNodes() {
        return userNodes.keySet();
    }

    public Set<String> getUserIndexes() {
		return userNodes.keySet();
	}
}
