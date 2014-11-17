package org.motechproject.mcts.integration.commcare;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

@RunWith(MockitoJUnitRunner.class)
public class BeneficiariesAddedListenerTest {
    @Mock
    CreateCaseXmlService createCaseXmlService;

    @InjectMocks
    BeneficiaryAddedListener beneficiaryAddedListener = new BeneficiaryAddedListener();

    @Test
    public void handleEvent_success() {

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                assertNotNull(args);
                assertEquals(0, args.length);
                return null;
            }
        }).when(createCaseXmlService).createCaseXml();
        beneficiaryAddedListener.handleEvent(null);
        verify(createCaseXmlService).createCaseXml();
    }
}
