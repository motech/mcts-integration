package mcts.care.data.migration;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class CareDataMigratorTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldValidateTheNumberOfArguments() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Invalid arguments. Expected only one argument: absolute filepath");

        CareDataMigrator.main(new String[]{});
    }
}
