package org.motechproject.mcts.utils;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.mcts.integration.exception.BeneficiaryException;
import org.springframework.util.MultiValueMap;

@RunWith(MockitoJUnitRunner.class)
public class PropertyReaderTest {

    @Mock
    private Properties properties;
  
    @InjectMocks
    private PropertyReader propertyReader = Mockito.spy(new PropertyReader(properties));

    @Before
    public void setUp() throws Exception {
    	//beneficiarySyncSettings = new BeneficiarySyncSettings(properties);
    	//beneficiarySyncSettings.setProperties(properties);
    }

    @Test
    public void shouldGetSyncUrl() throws BeneficiaryException {
        when(properties.getProperty("mcts.base.url")).thenReturn("baseUrl");
        when(properties.getProperty("beneficiary.sync.update.request.url")).thenReturn("requestUrl");
        when(properties.getProperty("beneficiary.sync.update.request.operation.key")).thenReturn("operation");
        when(properties.getProperty("beneficiary.sync.update.request.operation")).thenReturn("update");
        when(properties.getProperty("beneficiary.sync.update.request.authentication.username.key")).thenReturn("username");
        when(properties.getProperty("mcts.authentication.username")).thenReturn("myUser");
        when(properties.getProperty("beneficiary.sync.update.request.authentication.password.key")).thenReturn("password");
        when(properties.getProperty("mcts.authentication.password")).thenReturn("myPassword");
        doReturn("myPassword").when(propertyReader).getPassword();
        String syncUrl = propertyReader.getUpdateRequestUrl();

        assertEquals("baseUrl/requestUrl?username=myUser&password=myPassword&operation=update", syncUrl);
    }

    @Test
    public void te() throws BeneficiaryException
    {
    	 when(propertyReader.getPassword()).thenReturn("myPassword");
    	 System.out.println(propertyReader.getPassword());
    }
    @Test
    public void shouldGetBeneficiaryStateId() {
        when(properties.getProperty("beneficiary.state.id")).thenReturn("31");

        assertEquals(31, (int) propertyReader.getStateId());
    }

    @Test
    public void shouldGetDefaultBeneficiaryListQueryParams() throws BeneficiaryException {
        when(properties.getProperty("beneficiary.state.id")).thenReturn("31");
        when(properties.getProperty("beneficiary.sync.get.request.authentication.username.key")).thenReturn("username");
        when(properties.getProperty("mcts.authentication.username")).thenReturn("myUser");
        when(properties.getProperty("beneficiary.sync.get.request.authentication.password.key")).thenReturn("password");
        when(properties.getProperty("mcts.authentication.password")).thenReturn("myPassword");
        doReturn("myPassword").when(propertyReader).getPassword();
        MultiValueMap<String,String> defaultQueryParams = propertyReader.getDefaultBeneficiaryListQueryParams();

        assertEquals(3, defaultQueryParams.size());
        assertEquals("myUser", defaultQueryParams.getFirst("username"));
        assertEquals("myPassword", defaultQueryParams.getFirst("password"));
        assertEquals("31", defaultQueryParams.getFirst("State_id"));
    }
}
