package data;

import java.util.Set;

/**
 * User: dawei, dsybswsw@gmail.com
 * Date: 6/19/13
 */
public abstract class DataGraph implements Iterable<DataNode>{
	public abstract DataNodeIterator iterator();

    public abstract Set<String> getUserNodes();

    public abstract DataNode getUserNode(String index);
}
