package mcts.integration.stub.controller;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import mcts.integration.stub.interceptor.RequestLoggingInterceptor;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class MCTSStubController {

	private final static Logger LOGGER = LoggerFactory
			.getLogger(MCTSStubController.class);
	
	@ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/ping", method = RequestMethod.GET)
    @ResponseBody
    public String ping() {
        return "MCTS Stub Ping Page";
    }

    @RequestMapping(value = "/GetMotherDetails", method = RequestMethod.POST)
    @ResponseBody
    public String getBeneficiaryDetails(@ModelAttribute("State_id") String stateId,
                                        @ModelAttribute("id") String id,
                                        @ModelAttribute("pwd") String password,
                                        @ModelAttribute("FromDate") String fromDate,
                                        @ModelAttribute("ToDate") String toDate) throws IOException {


        return IOUtils.toString(new ClassPathResource("response/mother_details_response.xml").getInputStream());
    }

    @RequestMapping(value = "/beneficiary/update", method = RequestMethod.POST, consumes = "text/xml")
    @ResponseBody
    public String updateBeneficiaryDetails(@RequestParam("url") String username,
                                           @RequestParam("Sec_Code") String password,
                                           @RequestParam("Op") String operation,
                                           @RequestBody String beneficiaryDetails) throws JAXBException {

    	return "Updated Beneficiary Details";
    }
    
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/hub", method = RequestMethod.POST, consumes = "application/x-www-form-urlencoded")
    @ResponseBody
    public String publish(@RequestParam("hub.mode") String mode, 
    					@RequestParam("hub.url")String url) throws Exception{
    	if (!mode.equals("publish") || url.isEmpty()){
    		LOGGER.error(String.format("INVALID REQUEST. Mode is %s and url is %s", mode, url));
    	    		throw new Exception(String.format("INVALID REQUEST. Mode is %s and url is %s", mode, url));
    	}
		LOGGER.info(String.format("REQUEST RECEIVED. Mode is %s and url is %s", mode, url));
    	return "Hub Notified Successfully";
    }
}
