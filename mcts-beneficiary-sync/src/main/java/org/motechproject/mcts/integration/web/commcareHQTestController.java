package org.motechproject.mcts.integration.web;

import org.motechproject.mcts.integration.batch.PostXml;
import org.motechproject.mcts.integration.client.UpdateCommcareHQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class commcareHQTestController {
	
	@Autowired
	private UpdateCommcareHQ updateCommcareHQ;
	@Autowired
	private PostXml postXml;
	
	@ResponseStatus(value = HttpStatus.OK)
	@RequestMapping(value = "/update", method = RequestMethod.GET, produces="application/json")
	@ResponseBody public void updateCommcareHq()
			{
				updateCommcareHQ.sendUpdate();
		
		
	}

}
