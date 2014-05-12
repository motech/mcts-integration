package org.motechproject.mcts.integration.beneficiary.sync.factory;

import org.motechproject.mcts.integration.constants.SyncType;
import org.motechproject.mcts.integration.service.BeneficiarySyncService;
import org.motechproject.mcts.integration.service.MCTSBeneficiarySyncService;
import org.motechproject.mcts.integration.service.MotechBeneficiarySyncService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class BeneficiarySyncServiceFactoryTest {

    @Mock
    private MCTSBeneficiarySyncService mctsBeneficiarySyncService;
    @Mock
    private MotechBeneficiarySyncService motechBeneficiarySyncService;

    private BeneficiarySyncServiceFactory beneficiarySyncServiceFactory;

    @Before
    public void setUp() throws Exception {
        beneficiarySyncServiceFactory = new BeneficiarySyncServiceFactory(motechBeneficiarySyncService, mctsBeneficiarySyncService);
    }

    @Test
    public void shouldGetMCTSBeneficiarySyncServiceForSyncTypeGet() {
        BeneficiarySyncService beneficiarySyncService = beneficiarySyncServiceFactory.getBeneficiarySyncService(SyncType.GET);

        assertEquals(mctsBeneficiarySyncService, beneficiarySyncService);
    }

    @Test
    public void shouldGetMotechBeneficiarySyncServiceForSyncTypeUpdate() {
        BeneficiarySyncService beneficiarySyncService = beneficiarySyncServiceFactory.getBeneficiarySyncService(SyncType.UPDATE);

        assertEquals(motechBeneficiarySyncService, beneficiarySyncService);
    }
}
