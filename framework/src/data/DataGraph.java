package data;

/**
 * User: dawei, dsybswsw@gmail.com
 * Date: 6/19/13
 */
public abstract class DataGraph implements Iterable<DataNode>{
	public abstract DataNodeIterator iterator();
}
