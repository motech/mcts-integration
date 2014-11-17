package org.motechproject.mcts.integration.commcare;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.motechproject.event.MotechEvent;
import org.motechproject.mcts.utils.MCTSEventConstants;

@RunWith(MockitoJUnitRunner.class)
public class BeneficiariesUpdatedListenerTest {
    @Mock
    UpdateCaseXmlService updateCaseXmlService;

    @InjectMocks
    BeneficiariesUpdatedListener beneficiariesUpdatedListener = new BeneficiariesUpdatedListener();

    private MotechEvent motechEvent;
    private Integer id;
    @Before
    public void setUp() {
        id = 1;
        motechEvent = new MotechEvent();
        motechEvent.getParameters().put(MCTSEventConstants.PARAM_BENEFICIARY_KEY, id);
    }

    @Test
    public void handleEvent_success() {

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                assertNotNull(args);
                assertEquals(1, args.length);
                int beneficiaryKey = (int) args[0];
                assertNotNull(id);
                assertEquals(1, beneficiaryKey);
                return null;
            }
        }).when(updateCaseXmlService).updateXml((Integer) any());
        beneficiariesUpdatedListener.handleEvent(motechEvent);
        verify(updateCaseXmlService).updateXml((Integer) any());
    }
}
