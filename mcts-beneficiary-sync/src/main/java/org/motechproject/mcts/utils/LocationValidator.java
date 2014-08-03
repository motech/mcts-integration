package org.motechproject.mcts.utils;

import org.motechproject.mcts.integration.model.LocationDataCSV;

public final class LocationValidator {

    private LocationValidator() {

    }

    public static boolean isValidateLocation(LocationDataCSV record) {

        if (!isStateValid(record)) {
            return false;
        }
        if (!isDistrictValid(record)) {
            return false;
        }
        if (!isVillageValid(record)) {
            return false;
        }
        if (!isPHCValid(record)) {
            return false;
        }
        if (!isBlockValid(record)) {
            return false;
        }
        if (!isTalukValid(record)) {
            return false;
        }
        if (!isSubCentreValid(record)) {
            return false;
        }

        return true;
    }

    private static boolean isStateValid(LocationDataCSV record) {
        if ((record.getStateIDasInteger() == null)
                || isStringNull(record.getState())) {
            return false;
        }
        return true;
    }

    private static boolean isDistrictValid(LocationDataCSV record) {
        if ((record.getDCodeasInteger() == null)
                || isStringNull(record.getDistrict())) {
            return false;
        }
        return true;
    }

    private static boolean isVillageValid(LocationDataCSV record) {
        if ((record.getVCodeasInteger() == null)
                || isStringNull(record.getVillage())) {
            return false;
        }
        return true;
    }

    private static boolean isPHCValid(LocationDataCSV record) {
        if ((record.getPIDasInteger() == null) || isStringNull(record.getPHC())) {
            return false;
        }
        return true;
    }

    private static boolean isBlockValid(LocationDataCSV record) {
        if ((record.getBIDasInteger() == null)
                || isStringNull(record.getBlock())) {
            return false;
        }
        return true;
    }

    private static boolean isTalukValid(LocationDataCSV record) {
        if (isStringNull(record.getTaluka_Name())
                || (record.getTCodeasInteger() == null)) {
            return false;
        }
        return true;
    }

    private static boolean isSubCentreValid(LocationDataCSV record) {
        if ((record.getSIDasInteger() == null)
                || isStringNull(record.getSUBCenter())) {
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
