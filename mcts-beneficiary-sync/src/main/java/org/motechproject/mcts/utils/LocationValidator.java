package org.motechproject.mcts.utils;

import org.motechproject.mcts.integration.model.LocationDataCSV;

public class LocationValidator {

	public static boolean isValidateLocation(LocationDataCSV record) {

		if ((record.getStateIDasInteger() == null) || isStringNull(record.getState())
				|| (record.getDCodeasInteger() == null)
				|| isStringNull(record.getDistrict())
				|| (record.getVCodeasInteger() == null)
				|| isStringNull(record.getVillage())
				|| (record.getSIDasInteger() == null)
				|| isStringNull(record.getSUBCenter())
				|| (record.getBIDasInteger() == null)
				|| isStringNull(record.getBlock())
				|| isStringNull(record.getTaluka_Name())
				|| (record.getTCodeasInteger() == null) || (record.getPIDasInteger() == null)
				|| isStringNull(record.getPHC())) {
			return false;
		}
		return true;
	}

	private static boolean isStringNull(String sample) {
		if (sample == null) {
			return true;
		}
		return false;
	}

}
