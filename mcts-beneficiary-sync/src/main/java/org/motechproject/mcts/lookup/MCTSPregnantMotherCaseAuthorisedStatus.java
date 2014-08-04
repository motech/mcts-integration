package org.motechproject.mcts.lookup;

public enum MCTSPregnantMotherCaseAuthorisedStatus {
    BLANK(0), PENDING(1), APPROVED(2), DENIED(3);

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    MCTSPregnantMotherCaseAuthorisedStatus(int id) {
        this.id = id;
    }
}
