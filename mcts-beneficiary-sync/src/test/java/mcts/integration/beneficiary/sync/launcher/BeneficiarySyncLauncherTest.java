package mcts.integration.beneficiary.sync.launcher;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class BeneficiarySyncLauncherTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldValidateTheNumberOfArguments() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Invalid arguments. Expected 3 arguments(in order): SyncType, StartTime and EndTime");

        BeneficiarySyncLauncher.main(new String[]{});
    }

    @Test
    public void shouldValidateSyncType() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Invalid sync type. Sync type should be getBeneficiaries or updateBeneficiaries");

        BeneficiarySyncLauncher.main(new String[]{"invalidSyncType", "26-11-2013", "27-12-2013"});
    }

    @Test
    public void shouldValidateTheFormatOfDateArguments() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Invalid date format. Date format should be: dd-MM-yyyy.");

        BeneficiarySyncLauncher.main(new String[]{"getBeneficiaries", "26-11-2013", "27-Dec-13"});
    }
}
