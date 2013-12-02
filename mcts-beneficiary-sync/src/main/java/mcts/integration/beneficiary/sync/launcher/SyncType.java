package mcts.integration.beneficiary.sync.launcher;

import static org.apache.commons.lang3.StringUtils.equalsIgnoreCase;

public enum SyncType {

    GET("getBeneficiaries"),
    UPDATE("updateBeneficiaries");

    private String description;

    SyncType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static boolean isValid(String description) {
        return from(description) != null;
    }

    public static SyncType from(String description) {
        for (SyncType syncType : SyncType.values()) {
            if (equalsIgnoreCase(syncType.getDescription(), description))
                return syncType;
        }
        return null;
    }
}
