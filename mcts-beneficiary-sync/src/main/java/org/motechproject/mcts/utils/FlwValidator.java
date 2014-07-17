package org.motechproject.mcts.utils;

import org.motechproject.mcts.integration.model.FLWDataCSV;

public class FlwValidator {

	public static boolean isValidateFlw(FLWDataCSV record) {
		if ((record.getIdasInteger() == null) || (record.getDistrict_IDasInteger() == null) 
				|| (record.getTaluka_IDasInteger() == null)
				|| (record.getHealthBlock_IDasInteger() == null)
				|| (record.getPHC_IDasInteger() == null) || (record.getName() == null)
				|| (record.getType() == null) || (record.getSubCentre_IDasInteger()==null)) {
			return false;
		}
		return true;
	}
	
	
}
