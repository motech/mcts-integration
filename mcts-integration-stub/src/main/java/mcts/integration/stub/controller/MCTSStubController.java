package mcts.integration.stub.controller;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MCTSStubController {

    @RequestMapping(value = "/ping", method = RequestMethod.GET)
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
}
