package org.motechproject.mcts.utils;

import org.motechproject.mcts.integration.model.Record;

public final class BeneficiaryValidator {

    private BeneficiaryValidator() {

    }

    public static boolean isValidateBeneficiary(Record record) {
        if (isStringValid(record.getBeneficiaryName())
                || isStringValid(record.getBeneficiaryID())
                || (record.getBeneficiaryID().length() != MctsConstants.BENEFICIARY_LENGTH)) {
            return false;
        }
        if (isValidInteger(record.getStateID())
                || isValidInteger(record.getDistrictID())
                || isValidInteger(record.getTehsilID())) {
            return false;
        }
        if (isValidInteger(record.getFacilityID())
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
