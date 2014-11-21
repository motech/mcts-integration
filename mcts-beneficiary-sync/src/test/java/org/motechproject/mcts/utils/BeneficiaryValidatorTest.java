package org.motechproject.mcts.utils;

import org.junit.Test;
import org.motechproject.mcts.integration.model.Record;
import static junit.framework.Assert.assertEquals;

public class BeneficiaryValidatorTest {

    @Test
    public void isValidateBeneficiaryTest() {
        Record record = new Record();
        record.setBeneficiaryName("Ranju Devi");
        record.setBeneficiaryID("101216300411300080");
        record.setStateID("10");
        record.setDistrictID("11");
        record.setTehsilID("12");
        record.setFacilityID("13");
        record.setSubCentreID("14");
        record.setBlockID("15");
        boolean isValid = BeneficiaryValidator.isValidateBeneficiary(record);
        assertEquals(true, isValid);

    }

    @Test
    public void inValidBeneficiaryTest() {
        Record record1 = new Record();
        record1.setBeneficiaryName("Ranju Devi");
        record1.setBeneficiaryID("101216300411300080");
        record1.setStateID("10");
        record1.setDistrictID("11");
        record1.setTehsilID("12f");
        record1.setFacilityID("13");
        record1.setSubCentreID("14");
        record1.setBlockID("15");
        boolean valid = BeneficiaryValidator.isValidateBeneficiary(record1);
        assertEquals(false, valid);
    }

}
