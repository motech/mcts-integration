package org.motechproject.mcts.utils;

import org.motechproject.mcts.integration.model.FLWDataCSV;

public class FlwValidator {

	public static boolean isValidateFlw(FLWDataCSV record) {
		if ((record.getId() == null) || (record.getDistrict_ID() == null) 
				|| (record.getTaluka_ID() == null)
				|| (record.getHealthBlock_ID() == null)
				|| (record.getPHC_ID() == null) || (record.getName() == null)
				|| (record.getType() == null)) {
			return false;
		}
		return true;
	}
	
	
}
