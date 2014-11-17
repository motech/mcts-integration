package org.motechproject.mcts.integration.listener;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doAnswer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.motechproject.commcare.domain.CommcareForm;
import org.motechproject.event.MotechEvent;
import org.motechproject.mcts.integration.processor.MotherFormProcessor;

@RunWith(MockitoJUnitRunner.class)
public class FormCommcareListenerTest {

    @InjectMocks
    private FormCommcareListener formCommcareListener = new FormCommcareListener();

    @Mock
    private MotherFormProcessor motherFormProcessor;

    @Test
    public void shouldHandle() {
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                assertNotNull(args);
                assertEquals(1, args.length);
                return null;
            }
        }).when(motherFormProcessor).process((CommcareForm) anyObject());
        formCommcareListener.handleEvent(new MotechEvent());
    }

}
