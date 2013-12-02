package motech.care.data.service;

import motech.care.data.domain.Beneficiary;
import motech.care.data.repository.CareDataRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CareDataService {

    private CareDataRepository careDataRepository;

    @Autowired
    public CareDataService(CareDataRepository careDataRepository) {
        this.careDataRepository = careDataRepository;
    }

    public List<Beneficiary> getBeneficiariesToSync(DateTime startDate, DateTime endDate) {
        return careDataRepository.getBeneficiariesToSync(startDate, endDate);
    }
}
