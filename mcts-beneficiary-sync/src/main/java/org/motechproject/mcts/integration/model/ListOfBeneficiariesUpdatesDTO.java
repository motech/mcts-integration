/*Object to contain list of Beneficiary Updates Received from MCTS to be posted to Subcribers*/
package org.motechproject.mcts.integration.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Updates")
public class ListOfBeneficiariesUpdatesDTO {
	
	@XmlElement(name = "BeneficiaryUpdate")
	    private List<BeneficiaryUpdateDTO> allBeneficiaryUpdates;

	    public ListOfBeneficiariesUpdatesDTO() {
	        allBeneficiaryUpdates = new ArrayList<>();
	    }

	    public void addBeneficiaryDetails(BeneficiaryUpdateDTO beneficiaryUpdate) {
	        allBeneficiaryUpdates.add(beneficiaryUpdate);
	    }

	    public List<BeneficiaryUpdateDTO> getAllBeneficiaryDetails() {
	        return allBeneficiaryUpdates;
	    }
}
