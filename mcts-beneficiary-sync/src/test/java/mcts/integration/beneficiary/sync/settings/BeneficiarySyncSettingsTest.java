package mcts.integration.beneficiary.sync.settings;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doReturn;

import java.util.Properties;

import mcts.integration.beneficiary.sync.util.Encryption;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.util.MultiValueMap;

@RunWith(MockitoJUnitRunner.class)
public class BeneficiarySyncSettingsTest {

    @Mock
    private Properties properties;
    @Mock
    private Encryption encryption;

    @InjectMocks
    private BeneficiarySyncSettings beneficiarySyncSettings = Mockito.spy(new BeneficiarySyncSettings(properties));

    @Before
    public void setUp() throws Exception {
    	//beneficiarySyncSettings = new BeneficiarySyncSettings(properties);
    	//beneficiarySyncSettings.setProperties(properties);
    }

    @Test
    public void shouldGetSyncUrl() {
        when(properties.getProperty("mcts.base.url")).thenReturn("baseUrl");
        when(properties.getProperty("beneficiary.sync.update.request.url")).thenReturn("requestUrl");
        when(properties.getProperty("beneficiary.sync.update.request.operation.key")).thenReturn("operation");
        when(properties.getProperty("beneficiary.sync.update.request.operation")).thenReturn("update");
        when(properties.getProperty("beneficiary.sync.update.request.authentication.username.key")).thenReturn("username");
        when(properties.getProperty("mcts.authentication.username")).thenReturn("myUser");
        when(properties.getProperty("beneficiary.sync.update.request.authentication.password.key")).thenReturn("password");
        when(properties.getProperty("mcts.authentication.password")).thenReturn("myPassword");
        doReturn("myPassword").when(beneficiarySyncSettings).getPassword();
        String syncUrl = beneficiarySyncSettings.getUpdateRequestUrl();

        assertEquals("baseUrl/requestUrl?username=myUser&password=myPassword&operation=update", syncUrl);
    }

    @Test
    public void te()
    {
    	 when(beneficiarySyncSettings.getPassword()).thenReturn("myPassword");
    	 System.out.println(beneficiarySyncSettings.getPassword());
    }
    @Test
    public void shouldGetBeneficiaryStateId() {
        when(properties.getProperty("beneficiary.state.id")).thenReturn("31");

        assertEquals(31, (int) beneficiarySyncSettings.getStateId());
    }

    @Test
    public void shouldGetDefaultBeneficiaryListQueryParams() {
        when(properties.getProperty("beneficiary.state.id")).thenReturn("31");
        when(properties.getProperty("beneficiary.sync.get.request.authentication.username.key")).thenReturn("username");
        when(properties.getProperty("mcts.authentication.username")).thenReturn("myUser");
        when(properties.getProperty("beneficiary.sync.get.request.authentication.password.key")).thenReturn("password");
        when(properties.getProperty("mcts.authentication.password")).thenReturn("myPassword");
        doReturn("myPassword").when(beneficiarySyncSettings).getPassword();
        MultiValueMap<String,String> defaultQueryParams = beneficiarySyncSettings.getDefaultBeneficiaryListQueryParams();

        assertEquals(3, defaultQueryParams.size());
        assertEquals("myUser", defaultQueryParams.getFirst("username"));
        assertEquals("myPassword", defaultQueryParams.getFirst("password"));
        assertEquals("31", defaultQueryParams.getFirst("State_id"));
    }
}
