// Copyright (c) 2013. Shiwei Wu reserved.
package max_ent.common;

import java.util.HashMap;
import java.util.Map;

/**
 * Max Entropy tester.
 *
 * @author Shiwei Wu
 * @Date May 13, 2013
 */
public class MaxEntTester {
	public static void main(String[] args) throws Exception {
		MaxEntModel model = new MaxEntModel("data/test.model");
		Map<String, Double> featTbl = new HashMap<String, Double>();
		featTbl.put("f1", 1.0);
		featTbl.put("f2", 1.0);
		String lable = model.getMaxLabel(featTbl);
		System.out.println(lable);
	}
}
