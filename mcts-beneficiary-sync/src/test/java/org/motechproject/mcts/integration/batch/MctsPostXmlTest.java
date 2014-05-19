package org.motechproject.mcts.integration.batch;
import java.io.File;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.mcts.utils.BatchServiceUrlGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationBeneficiarySyncContextTest.xml"})
public class MctsPostXmlTest {
	
	@Autowired
	private RestTemplate mctsRestTemplate;
	@Autowired
	private BatchServiceUrlGenerator batchServiceUrlGenerator;
	
	@Test
	public void postXmlTest() {
		PostXml postXml = new PostXml(mctsRestTemplate, batchServiceUrlGenerator);
		File file = new File("C:\\Users\\Rakesh\\Desktop\\log4j_sandeep.xml");
		postXml.sendXml(file);
	}
	

}
