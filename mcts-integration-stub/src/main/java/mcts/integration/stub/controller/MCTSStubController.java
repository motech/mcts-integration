package mcts.integration.stub.controller;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
public class MCTSStubController {

    @RequestMapping(value = "/ping", method = RequestMethod.GET)
    public String ping() {
        return "MCTS Stub Ping Page";
    }

    @RequestMapping(value = "/GetMotherDetails", method = RequestMethod.POST)
    @ResponseBody
    public String getBeneficiaryDetails(@RequestParam("State_id") String stateId,
                                        @RequestParam("id") String id,
                                        @RequestParam("pwd") String password,
                                        @RequestParam("FromDate") String fromDate,
                                        @RequestParam("ToDate") String toDate) throws IOException {


        return IOUtils.toString(new ClassPathResource("response/mother_details_response.xml").getInputStream());
    }

    @RequestMapping(value = "/beneficiary/update", method = RequestMethod.POST)
    @ResponseBody
    public String updateBeneficiaryDetails(@RequestParam("url") String username,
                                           @RequestParam("Sec_Code") String password,
                                           @RequestParam("Op") String operation,
                                           @RequestBody String beneficiaryDetails) {

        return "Updated Beneficiary Details";
    }

}
