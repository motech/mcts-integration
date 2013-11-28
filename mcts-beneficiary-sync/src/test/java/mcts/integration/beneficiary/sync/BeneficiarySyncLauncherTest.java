package mcts.integration.beneficiary.sync;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class BeneficiarySyncLauncherTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldValidateTheNumberOfArguments() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Invalid arguments. Only 2 arguments: StartTime and EndTime are allowed.");

        BeneficiarySyncLauncher.main(new String[]{});
    }

    @Test
    public void shouldValidateTheFormatOfDateArguments() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Invalid date format. Date format should be: dd-MM-yyyy.");

        BeneficiarySyncLauncher.main(new String[]{"26-11-2013", "27-Dec-13"});
    }
}
