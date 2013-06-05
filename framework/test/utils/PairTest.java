package utils;

import org.junit.Test;
import utils.common.Pair;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * User: dawei, dsybswsw@gmail.com
 * Date: 5/23/13
 */
public class PairTest {
	@Test
	public void testHashCode() {
		Pair<String, String> pair1 = new Pair<String, String>("a", "b");
		Pair<String, String> pair2 = new Pair<String, String>("c", "d");
		Map<Pair<String, String>, String> testMap = new HashMap<Pair<String, String>, String>();
		testMap.put(pair1, "pair1");
		testMap.put(pair2, "pair2");
		Pair<String, String> pair3 = new Pair<String, String>("a", "b");
		Pair<String, String> pair4 = new Pair<String, String>("c", "d");
		assertEquals("pair1", testMap.get(pair3));
		assertEquals("pair2", testMap.get(pair4));
	}
}
