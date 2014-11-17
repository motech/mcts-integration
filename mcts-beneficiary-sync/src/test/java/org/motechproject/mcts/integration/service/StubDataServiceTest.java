package org.motechproject.mcts.integration.service;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.motechproject.mcts.utils.PropertyReader;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@PrepareForTest(IOUtils.class)
@RunWith(PowerMockRunner.class)
public class StubDataServiceTest {

    @InjectMocks
    private StubDataService stubDataService = new StubDataService();

    @Mock
    private PropertyReader propertyReader;

    @Mock
    private HttpClient commonsHttpClient;

    @Before
    public void setUp() {
        when(propertyReader.getFixtureLoginUrl(anyInt(), anyInt()))
                .thenReturn(
                        "https://www.commcarehq.org/a/bihar/api/v0.4/fixture/?fixture_type=asha");
        when(propertyReader.getFixtureUserName()).thenReturn(
                "haritha@beehyv.com");
        when(propertyReader.getFixturePassword()).thenReturn("\\][poi00");
        HttpClientParams params = new HttpClientParams();
        when(commonsHttpClient.getParams()).thenReturn(params);
        HttpState state = new HttpState();
        when(commonsHttpClient.getState()).thenReturn(state);

    }

    @Test
    public void shouldGetFixtureData() throws IOException {
        PowerMockito.mockStatic(IOUtils.class);
        String resp = "{\"meta\": {\"limit\": 1000, \"next\": null, \"offset\": 0, \"previous\": null, \"total_count\": 2}, \"objects\": [{\"fields\": {\"group_id\": {\"field_list\": [{\"field_value\": \"636162a69e05723c145247411a309507\", \"properties\": {}}]}, \"id\": {\"field_list\": [{\"field_value\": \"2345\", \"properties\": {}}]}, \"name\": {\"field_list\": [{\"field_value\": \"1. mcts.test ASHA\", \"properties\": {\"lang\": \"en\"}}, {\"field_value\": \"1. परीक्षण आशा MCTS\", \"properties\": {\"lang\": \"hin\"}}]}}, \"fixture_type\": \"asha\", \"id\": \"dad9669188de51982fd6287a65f4c310\", \"resource_uri\": \"\"}, {\"fields\": {\"group_id\": {\"field_list\": [{\"field_value\": \"ea313b8eed45c14d4579e6c3cffd2ebd\", \"properties\": {}}]}, \"id\": {\"field_list\": [{\"field_value\": \"1234\", \"properties\": {}}]}, \"name\": {\"field_list\": [{\"field_value\": \"2. mcts.rishad ASHA\", \"properties\": {\"lang\": \"en\"}}, {\"field_value\": \"2. MCTS Rishad आशा\", \"properties\": {\"lang\": \"hin\"}}]}}, \"fixture_type\": \"asha\", \"id\": \"dad9669188de51982fd6287a65f4b09b\", \"resource_uri\": \"\"}]}";
        Mockito.when(IOUtils.toString((InputStream) any())).thenReturn(resp);
        stubDataService.getFixtureData();
    }

}
