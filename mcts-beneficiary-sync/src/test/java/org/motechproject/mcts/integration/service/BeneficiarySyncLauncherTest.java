package org.motechproject.mcts.integration.service;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.motechproject.mcts.integration.constants.SyncType;

public class BeneficiarySyncLauncherTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    BeneficiarySyncLauncher beneficiarySyncLauncher;
    
    @Test
    public void shouldValidateTheNumberOfArguments() throws Exception {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Invalid arguments. Expected 3 arguments(in order): SyncType, StartTime and EndTime");

        beneficiarySyncLauncher.syncLauncher(new String[]{});
    }

    @Test
    public void shouldValidateSyncType() throws Exception {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(String.format("Invalid sync type. Sync type should be %s or %s", SyncType.GET.getDescription(), SyncType.UPDATE.getDescription()));

        beneficiarySyncLauncher.syncLauncher(new String[]{"invalidSyncType", "26-11-2013", "27-12-2013"});
    }

    @Test
    public void shouldValidateTheFormatOfDateArguments() throws Exception {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Invalid date format. Date format should be: dd-MM-yyyy.");

        beneficiarySyncLauncher.syncLauncher(new String[]{SyncType.GET.getDescription(), "26-11-2013", "27-Dec-13"});
    }
}
