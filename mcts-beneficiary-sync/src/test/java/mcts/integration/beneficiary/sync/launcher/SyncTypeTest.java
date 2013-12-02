package mcts.integration.beneficiary.sync.launcher;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

public class SyncTypeTest {
    @Test
    public void shouldValidateGivenSyncTypeDescription() {
        assertFalse(SyncType.isValid("invalidDescription"));

        assertFalse(SyncType.isValid(null));

        assertTrue(SyncType.isValid("getBeneficiaries"));

        assertTrue(SyncType.isValid("updateBeneficiaries"));
    }

    @Test
    public void shouldGetSyncTypeFromDescription() {
        assertEquals(SyncType.GET, SyncType.from("getBeneficiaries"));

        assertEquals(SyncType.UPDATE, SyncType.from("updateBeneficiaries"));

        assertNull(SyncType.from(null));

        assertNull(SyncType.from("invalidDescription"));
    }
}
