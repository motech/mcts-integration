package mcts.integration.beneficiary.sync.settings;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BeneficiarySyncSettingsTest {

    @Mock
    private Properties properties;

    private BeneficiarySyncSettings beneficiarySyncSettings;

    @Before
    public void setUp() throws Exception {
        beneficiarySyncSettings = new BeneficiarySyncSettings(properties);
    }

    @Test
    public void shouldGetSyncUrl() {
        when(properties.getProperty("mcts.base.url")).thenReturn("baseUrl");
        when(properties.getProperty("beneficiary.sync.request.url")).thenReturn("requestUrl");

        when(properties.getProperty("beneficiary.sync.request.operation.key")).thenReturn("operation");
        when(properties.getProperty("beneficiary.sync.request.operation")).thenReturn("update");
        when(properties.getProperty("mcts.authentication.username.key")).thenReturn("username");
        when(properties.getProperty("mcts.authentication.username")).thenReturn("myUser");
        when(properties.getProperty("mcts.authentication.password.key")).thenReturn("password");
        when(properties.getProperty("mcts.authentication.password")).thenReturn("myPassword");

        String syncUrl = beneficiarySyncSettings.getSyncUrl();

        assertEquals("baseUrl/requestUrl?username=myUser&password=myPassword&operation=update", syncUrl);
    }

    @Test
    public void shouldGetBeneficiaryStateId() {
        when(properties.getProperty("beneficiary.state.id")).thenReturn("31");

        assertEquals(31, (int) beneficiarySyncSettings.getStateId());
    }
}
