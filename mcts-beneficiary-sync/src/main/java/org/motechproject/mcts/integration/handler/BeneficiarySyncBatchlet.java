package org.motechproject.mcts.integration.handler;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.batch.api.Batchlet;

import org.joda.time.DateTime;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.mcts.integration.model.Beneficiary;
import org.motechproject.mcts.integration.repository.CareDataRepository;
import org.motechproject.mcts.integration.service.MCTSHttpClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.motechproject.batch.exception.BatchException;
import org.motechproject.batch.service.JobTriggerService;

@Component
public class BeneficiarySyncBatchlet implements Batchlet {
	
//	@Autowired
//	JobOperator jsrJobOperator;
//	
	
	
	
	
	private final static Logger LOGGER = LoggerFactory.getLogger(BeneficiarySyncBatchlet.class);
	List<Beneficiary> beneficiariesList = new ArrayList<Beneficiary>();
	private DateTime startDate = new DateTime(2014,01,03,12,30,30);
	private DateTime endDate = new DateTime(2014,02,03,12,30,30);
	
	//@Autowired
	 //CareDataRepository careDataRepository;
	
	 @Override
	public String process() throws Exception {
		//beneficiariesList= careDataRepository.getBeneficiariesToSync(startDate, endDate);
		LOGGER.info("beneficiaries list size"+Integer.toString(beneficiariesList.size()));
		//System.out.println("beneficiaries list size"+Integer.toString(beneficiariesList.size()));
		return null;
	}

	@Override
	public void stop() throws Exception {
		// TODO Auto-generated method stub
		
	}
	


}
