/*Object to contain list of Beneficiary Updates Received from MCTS to be posted to Subcribers*/
package org.motechproject.mcts.integration.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Updates")
public class BeneficiarySubscriberUpdateUrlDTO {
	
	@XmlElement(name = "UpdateUrl")
	    private List<String> allUrls;

	    public BeneficiarySubscriberUpdateUrlDTO() {
	    	allUrls = new ArrayList<>();
	    }

	    public void addBeneficiaryDetails(String updateUrl) {
	    	allUrls.add(updateUrl);
	    }

	    public List<String> getAllBeneficiaryDetails() {
	        return allUrls;
	    }
	    
	    @Override
	    public String toString(){
	    	StringBuilder s = new StringBuilder();
	    	for (String updateUrl : allUrls)
	    	{
	    		s.append(updateUrl + "\n");
	    	}
	    	return s.toString();
	    }
}
