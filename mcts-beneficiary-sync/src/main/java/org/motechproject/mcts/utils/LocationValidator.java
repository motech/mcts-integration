package org.motechproject.mcts.utils;

import org.motechproject.mcts.integration.model.LocationDataCSV;

public class LocationValidator {

	public static boolean isValidateLocation(LocationDataCSV record) {

		if ((record.getStateID() == null) || isStringNotNull(record.getState())
				|| (record.getDCode() == null)
				|| isStringNotNull(record.getDistrict())
				|| (record.getVCode() == null)
				|| isStringNotNull(record.getVillage())
				|| (record.getSID() == null)
				|| isStringNotNull(record.getSUBCenter())
				|| (record.getBID() == null)
				|| isStringNotNull(record.getBlock())
				|| isStringNotNull(record.getTaluka_Name())
				|| (record.getTCode() == null) || (record.getPID() == null)
				|| isStringNotNull(record.getPHC())) {
			return false;
		}
		return true;
	}

	private static boolean isStringNotNull(String sample) {
		if (sample == null) {
			return false;
		}
		return true;
	}

}
