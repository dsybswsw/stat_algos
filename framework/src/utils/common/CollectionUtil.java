package utils.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dawei
 * Date: 5/20/13
 * Time: 4:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class CollectionUtil<T> {
	public List<T> getEmptyList(int size) {
		List<T> result = new ArrayList<T>();
		for (int i = 0; i < size; ++i) {
			result.add(null);
		}
		return result;
	}
}
