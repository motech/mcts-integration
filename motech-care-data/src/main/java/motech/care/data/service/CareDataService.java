package motech.care.data.service;

import motech.care.data.domain.Beneficiary;
import motech.care.data.repository.CareDataRepository;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CareDataService {

    private final static Logger LOGGER = LoggerFactory.getLogger(CareDataService.class);

    private CareDataRepository careDataRepository;

    @Autowired
    public CareDataService(CareDataRepository careDataRepository) {
        this.careDataRepository = careDataRepository;
    }

    public List<Beneficiary> getBeneficiariesToSync(DateTime startDate, DateTime endDate) {
        return careDataRepository.getBeneficiariesToSync(startDate, endDate);
    }

    public void updateCase(String caseId, String mctsId) {
        LOGGER.info(String.format("Updating case %s with MCTS Id %s", caseId, mctsId));
        careDataRepository.updateCase(caseId, mctsId);
    }
}
