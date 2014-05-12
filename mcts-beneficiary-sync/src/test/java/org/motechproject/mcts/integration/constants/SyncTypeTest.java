package org.motechproject.mcts.integration.constants;

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

        assertTrue(SyncType.isValid("GetMothersList"));

        assertTrue(SyncType.isValid("ServiceUpdation"));
    }

    @Test
    public void shouldGetSyncTypeFromDescription() {
        assertEquals(SyncType.GET, SyncType.from("GetMothersList"));

        assertEquals(SyncType.UPDATE, SyncType.from("ServiceUpdation"));

        assertNull(SyncType.from(null));

        assertNull(SyncType.from("invalidDescription"));
    }
}
