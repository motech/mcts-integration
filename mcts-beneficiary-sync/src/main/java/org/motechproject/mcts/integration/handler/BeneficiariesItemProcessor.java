package org.motechproject.mcts.integration.handler;

import java.util.List;

import javax.batch.api.chunk.ItemProcessor;

import org.motechproject.mcts.integration.model.Beneficiary;
import org.motechproject.mcts.integration.model.BeneficiaryDetails;
import org.motechproject.mcts.integration.model.BeneficiaryRequest;

public class BeneficiariesItemProcessor implements ItemProcessor{

	@Override
	public Object processItem(Object item) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/*private BeneficiarySyncSettings beneficiarySyncSettings;
	
	@Override
	public BeneficiaryRequest process(List<Beneficiary> beneficiariesList) throws Exception {
        BeneficiaryRequest beneficiaryRequest = new BeneficiaryRequest();
        Integer stateId = beneficiarySyncSettings.getStateId();
        for (Beneficiary beneficiary : beneficiariesList) {
            beneficiaryRequest.addBeneficiaryDetails(new BeneficiaryDetails(stateId, 
            	beneficiary.getMctsId(),
            	beneficiary.getServiceType(),
            	beneficiary.getServiceDeliveryDate(),
            	beneficiary.getMobileNumber(),
            	beneficiary.getHbLevelStr()));
        }
        return beneficiaryRequest;
	}*/

}
