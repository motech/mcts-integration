package org.motechproject.mcts.integration.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.motechproject.http.agent.service.HttpAgent;
import org.motechproject.http.agent.service.Method;
import org.motechproject.mcts.utils.BatchServiceUrlGenerator;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@RunWith(MockitoJUnitRunner.class)
public class UpdateCommcareHQTest {

    @Mock
    private RestTemplate restTemplate;
    @Mock
    private HttpAgent httpAgentServiceOsgi;
    @Mock
    private BatchServiceUrlGenerator batchServiceUrlGenerator;
    private String caseUploadUrl;
    private List<HttpMessageConverter<?>> messageConverters;

    @InjectMocks
    UpdateCommcareHQ updateCommcareHQ = new UpdateCommcareHQ(restTemplate,
            batchServiceUrlGenerator);

    @Before
    public void setUp() {
        caseUploadUrl = "case_upload_url";
        messageConverters = new ArrayList<>();
        when(restTemplate.getMessageConverters()).thenReturn(messageConverters);
        when(batchServiceUrlGenerator.getCaseUploadUrl()).thenReturn(
                caseUploadUrl);
    }

    @Test
    public void sendUpdate_success() {

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                assertNotNull(args);
                assertEquals(3, args.length);
                String uploadUrl = (String) args[0];
                HttpEntity<MultiValueMap<String, Object>> requestEntity = (HttpEntity<MultiValueMap<String, Object>>) args[1];
                Method method = (Method) args[2];
                assertNotNull(uploadUrl);
                assertNotNull(requestEntity);
                assertNotNull(method);
                assertEquals(caseUploadUrl, uploadUrl);
                assertNotNull(requestEntity.getHeaders());
                assertEquals(MediaType.MULTIPART_FORM_DATA, requestEntity
                        .getHeaders().getContentType());
                assertEquals(Method.POST, method);
                return null;
            }
        }).when(httpAgentServiceOsgi).executeSync((String) any(), anyObject(),
                (Method) any());
        updateCommcareHQ.sendUpdate();
        verify(restTemplate, times(2)).getMessageConverters();
        verify(httpAgentServiceOsgi).executeSync((String) any(), anyObject(),
                (Method) any());
    }

}
