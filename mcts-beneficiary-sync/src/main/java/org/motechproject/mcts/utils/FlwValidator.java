package org.motechproject.mcts.utils;

import org.motechproject.mcts.integration.model.FLWDataCSV;

public final class FlwValidator {

    private FlwValidator() {

    }

    public static boolean isValidateFlw(FLWDataCSV record) {
        if ((record.getIdasInteger() == null)
                || (record.getDistrict_IDasInteger() == null)
                || (record.getTaluka_IDasInteger() == null)) {
            return false;
        }
        if ((record.getHealthBlock_IDasInteger() == null)
                || (record.getPHC_IDasInteger() == null)
                || (record.getName() == null)) {
            return false;
        }
        if ((record.getSubCentre_IDasInteger() == null)
                || (record.getType() == null)) {
            return false;
        }
        return true;
    }
}
