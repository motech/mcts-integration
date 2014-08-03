package org.motechproject.mcts.integration.model;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;

@SuppressWarnings("restriction")
@XmlRootElement
public class Meta {
    private int limit;
    private String next;
    private int offset;
    private String previous;
    private int totalCount;

    public int getLimit() {
        return limit;
    }

    @JsonProperty("limit")
    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public int getTotalCount() {
        return totalCount;
    }

    @JsonProperty("total_count")
    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

}
