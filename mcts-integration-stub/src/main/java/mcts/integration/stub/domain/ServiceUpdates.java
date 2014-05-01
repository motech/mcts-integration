package mcts.integration.stub.domain;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "serviceupdates")
public class ServiceUpdates {

    @XmlElement(name = "update")
    private List<Update> allUpdates;

    public ServiceUpdates() {
        allUpdates = new ArrayList<>();
    }

    public List<Update> getAllUpdates() {
        return allUpdates;
    }
    
}