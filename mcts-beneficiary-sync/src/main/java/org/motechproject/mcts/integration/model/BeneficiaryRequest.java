/**
 * XML mapping object to be sent to Mcts
 */
package org.motechproject.mcts.integration.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "serviceupdates")
public class BeneficiaryRequest {

    @XmlElement(name = "update")
    private List<BeneficiaryDetails> allBeneficiaryDetails;

    public BeneficiaryRequest() {
        allBeneficiaryDetails = new ArrayList<>();
    }

    public void addBeneficiaryDetails(BeneficiaryDetails beneficiaryDetails) {
        allBeneficiaryDetails.add(beneficiaryDetails);
    }

    public List<BeneficiaryDetails> getAllBeneficiaryDetails() {
        return allBeneficiaryDetails;
    }
}
