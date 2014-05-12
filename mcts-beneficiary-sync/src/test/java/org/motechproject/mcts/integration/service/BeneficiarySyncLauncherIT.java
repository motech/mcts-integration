package org.motechproject.mcts.integration.service;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:beneficiarySyncContext.xml"})
public class BeneficiarySyncLauncherIT {

	BeneficiarySyncLauncher beneficiarySyncLauncher;
    @Test
    @Ignore
    public void shouldGetBeneficiarySyncServiceFactoryBean() throws Exception {
        beneficiarySyncLauncher.syncLauncher(new String[]{"Service Update","26-11-2013", "27-11-2013"});
    }
}
