package org.motechproject.mcts.integration.handler;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.motechproject.mcts.integration.model.Beneficiary;
import org.motechproject.mcts.integration.repository.CareDataRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class BeneficiariesItemReader {

    private static final String CURRENT_INDEX = "current.index";
    private List<Beneficiary> beneficiariesList;
    @SuppressWarnings("unused")
    private int currentIndex;
    private DateTime startDate;
    private DateTime endDate;


    public BeneficiariesItemReader(CareDataRepository careDataRepository) {

        startDate = parseDate("03-01-2014");
        endDate = parseDate("03-02-2014");
        beneficiariesList = careDataRepository.getBeneficiariesToSync(
                startDate, endDate);
    }

    public List<Beneficiary> getBeneficiariesList() {
        return beneficiariesList;
    }

    public void setBeneficiariesList(List<Beneficiary> beneficiariesList) {
        this.beneficiariesList = beneficiariesList;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public DateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(DateTime startDate) {
        this.startDate = startDate;
    }

    public DateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(DateTime endDate) {
        this.endDate = endDate;
    }

    public static String getCurrentIndex() {
        return CURRENT_INDEX;
    }

    private static DateTime parseDate(String dateString) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat
                .forPattern("dd-MM-yyyy");
        return dateTimeFormatter.parseDateTime(dateString);
    }

}
