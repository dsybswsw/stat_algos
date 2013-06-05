// Copyright (c) 2013. Shiwei Wu reserved.
package max_ent.common;

import utils.common.LineReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class MaxEntFileHandler {
	private final static Logger logger = Logger.getLogger(MaxEntFileHandler.class.getName());

	public static List<MaxEntUnit> transFileToDataStruct(String filePath) {
		try {
			LineReader lineReader = new LineReader(filePath);
			List<MaxEntUnit> unitList = new ArrayList<MaxEntUnit>();
			while (lineReader.hasNext()) {
				String line = lineReader.next();
				MaxEntUnit maxEntUnit = parseTrainLine(line);
				if (maxEntUnit == null) {
					logger.info("Illegal format and skip it");
					continue;
				}
				unitList.add(maxEntUnit);
			}
			lineReader.close();
			return unitList;
		} catch (IOException e) {
			logger.info(e.toString());
			return null;
		}
	}

	private static MaxEntUnit parseTrainLine(String line) {
		String[] tokens = line.split("\\s+");
		if (tokens.length < 2) {
			logger.info("Line " + line + " is not in max entropy training format");
			return null;
		}
		String label = tokens[0];
		Map<String, Double> featTbl = new HashMap<String, Double>();
		for (int i = 1; i < tokens.length; ++i) {
			String feat = tokens[i];
			featTbl.put(feat, 1.0);
			logger.info("feature:" + feat);
		}
		logger.info("label:" + label);
		return new MaxEntUnit(label, featTbl);
	}
}
