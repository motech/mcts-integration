package org.motechproject.mcts.integration.commcare.test;

import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.mcts.integration.commcare.CreateCaseXmlService;
import org.motechproject.mcts.integration.commcare.UpdateCaseXmlService;
import org.motechproject.mcts.integration.exception.BeneficiaryException;
import org.motechproject.mcts.integration.hibernate.model.MctsPregnantMother;
import org.motechproject.mcts.integration.repository.CareDataRepository;
import org.motechproject.mcts.integration.service.MCTSFormUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationBeneficiarySyncContextTest.xml"})
public class MCTSPersonaToXmlConvertor {
	
	 @Autowired
	 private CreateCaseXmlService s;
	 
	 @Autowired
	 private MCTSFormUpdateService mCTSFormUpdateService;
	 
	 @Autowired 
	 private UpdateCaseXmlService t;
	 
	 @Autowired CareDataRepository careDataRepository;
	
	
	@SuppressWarnings("deprecation")
	@Test
	public void cnverttoXml() throws Exception {
		
		
		/*MctsPregnantMother m = new MctsPregnantMother();
		m.setId(1);
		m.setName("mother");
		*/
				
		s.createCaseXml();;
		
	}
	
	/*@SuppressWarnings("deprecation")
	@Test
	public void formUpdateTest() throws BeneficiaryException {
		mCTSFormUpdateService.updateMctsPregnantMotherForm(21);
	}*/
	@Test
	public void convertUpdatetoXml() throws Exception {
		MctsPregnantMother m = careDataRepository.getMotherFromPrimaryId(21);
		
		t.updateXml(m);
		
	}
}
