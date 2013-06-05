package utils;

import utils.common.CollectionUtil;

import java.util.List;

/**
 * User: dawei, dsybswsw@gmail.com
 * Date: 5/23/13
 */
public class CollectionUtilTest {
	public static void main(String[] args) {
		List<String> testList = new CollectionUtil<String>().getEmptyList(5);
		System.out.println(testList.size());
	}
}
