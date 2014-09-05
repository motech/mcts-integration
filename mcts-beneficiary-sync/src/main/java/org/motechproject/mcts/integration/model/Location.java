package org.motechproject.mcts.integration.model;

import org.motechproject.mcts.care.common.mds.model.MctsDistrict;
import org.motechproject.mcts.care.common.mds.model.MctsHealthblock;
import org.motechproject.mcts.care.common.mds.model.MctsPhc;
import org.motechproject.mcts.care.common.mds.model.MctsState;
import org.motechproject.mcts.care.common.mds.model.MctsSubcenter;
import org.motechproject.mcts.care.common.mds.model.MctsTaluk;
import org.motechproject.mcts.care.common.mds.model.MctsVillage;

public class Location {

    private MctsState mctsState;
    private MctsDistrict mctsDistrict;
    private MctsVillage mctsVillage;
    private MctsHealthblock mctsHealthblock;
    private MctsPhc mctsPhc;
    private MctsSubcenter mctsSubcenter;
    private MctsTaluk mctsTaluk;

    public MctsState getMctsState() {
        return mctsState;
    }

    public void setMctsState(MctsState mctsState) {
        this.mctsState = mctsState;
    }

    public MctsDistrict getMctsDistrict() {
        return mctsDistrict;
    }

    public void setMctsDistrict(MctsDistrict mctsDistrict) {
        this.mctsDistrict = mctsDistrict;
    }

    public MctsVillage getMctsVillage() {
        return mctsVillage;
    }

    public void setMctsVillage(MctsVillage mctsVillage) {
        this.mctsVillage = mctsVillage;
    }

    public MctsHealthblock getMctsHealthblock() {
        return mctsHealthblock;
    }

    public void setMctsHealthblock(MctsHealthblock mctsHealthblock) {
        this.mctsHealthblock = mctsHealthblock;
    }

    public MctsPhc getMctsPhc() {
        return mctsPhc;
    }

    public void setMctsPhc(MctsPhc mctsPhc) {
        this.mctsPhc = mctsPhc;
    }

    public MctsSubcenter getMctsSubcenter() {
        return mctsSubcenter;
    }

    public void setMctsSubcenter(MctsSubcenter mctsSubcenter) {
        this.mctsSubcenter = mctsSubcenter;
    }

    public MctsTaluk getMctsTaluk() {
        return mctsTaluk;
    }

    public void setMctsTaluk(MctsTaluk mctsTaluk) {
        this.mctsTaluk = mctsTaluk;
    }

}
