package org.motechproject.mcts.integration.processor;

import org.motechproject.mcts.integration.model.Beneficiary;
import org.motechproject.mcts.integration.model.BeneficiaryDetails;
import org.motechproject.mcts.utils.PropertyReader;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("beneficiariesSyncToProcessor")
public class BeneficiariesSyncToProcessor implements
        ItemProcessor<Beneficiary, BeneficiaryDetails> {

    @Autowired
    private PropertyReader propertyReader;

    @Override
    public BeneficiaryDetails process(Beneficiary beneficiary) throws Exception {
        return mapToBeneficiaryDetails(beneficiary);
    }

    /**
     * Maps the List of Beneficiaries received from Database to
     * <code>BeneficiaryDetails</code> to be sent to Mcts
     *
     * @param beneficiary
     * @return
     */
    private BeneficiaryDetails mapToBeneficiaryDetails(Beneficiary beneficiary) {
        Integer stateId = propertyReader.getStateId();
        BeneficiaryDetails beneficiaryDetails = new BeneficiaryDetails(stateId,
                beneficiary.getMctsId(), beneficiary.getServiceType(),
                beneficiary.getServiceDeliveryDate(), beneficiary
                        .getMobileNumber(), beneficiary.getHbLevelStr());
        return beneficiaryDetails;
    }

}
