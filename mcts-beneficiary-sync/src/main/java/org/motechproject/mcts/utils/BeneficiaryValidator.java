package org.motechproject.mcts.utils;

import org.motechproject.mcts.integration.model.Record;

public class BeneficiaryValidator {

	public static boolean isValidateBeneficiary(Record record) {
		if (isStringValid(record.getBeneficiaryName())
				|| isStringValid(record.getBeneficiaryID())
				|| (record.getBeneficiaryID().length() != 18)
				|| isValidInteger(record.getStateID())
				|| isValidInteger(record.getDistrictID())
				|| isValidInteger(record.getTehsilID())
				|| isValidInteger(record.getFacilityID())
				|| isValidInteger(record.getSubCentreID())
				|| isValidInteger(record.getBlockID())) {
			return false;
		}
		return true;
	}

	private static boolean isStringValid(String sample) {
		if ((sample == null) || sample.isEmpty()) {
			return true;
		}
		return false;
	}

	private static boolean isValidInteger(String sample) {
		if ((sample == null) || sample.isEmpty() || !(sample.matches("[0-9]+"))) {
			return true;
		}
		return false;
	}
}
