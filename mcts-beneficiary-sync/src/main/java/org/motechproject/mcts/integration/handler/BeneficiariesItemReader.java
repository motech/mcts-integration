package org.motechproject.mcts.integration.handler;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.motechproject.mcts.integration.model.Beneficiary;
import org.motechproject.mcts.integration.repository.CareDataRepository;
//import org.springframework.batch.item.ItemReader;
//import org.springframework.batch.item.ParseException;
//import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class BeneficiariesItemReader{// implements ItemReader<Beneficiary>{

		private static final String CURRENT_INDEX = "current.index";
	 	List<Beneficiary> beneficiariesList;
	    int currentIndex = 0;
	    private DateTime startDate;
	    private DateTime endDate;
	    
	    @Autowired
	    CareDataRepository careDataRepository;
	   
	    public BeneficiariesItemReader(CareDataRepository careDataRepository){
	    	
	    	startDate = parseDate("03-01-2014");
	    	endDate = parseDate("03-02-2014");
	    	
			//beneficiariesList =careDataService.getBeneficiariesToSync(startDate, endDate); 
	    	beneficiariesList= careDataRepository.getBeneficiariesToSync(startDate, endDate);
	    	//beneficiariesList = new ArrayList<Beneficiary>();
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

	
//	    @Override
//	    public Beneficiary read() throws Exception, UnexpectedInputException, ParseException {
//	        
//	    	if (currentIndex < beneficiariesList.size()) { 
//	            return (Beneficiary)beneficiariesList.get(currentIndex++);
//	    	}
//	        return null;
//	    }
//	    
	    private static DateTime parseDate(String dateString) {
			DateTimeFormatter dateTimeFormatter = DateTimeFormat
					.forPattern("dd-MM-yyyy");
			return dateTimeFormatter.parseDateTime(dateString);
		}

//	    @Override
//	    public void open(ExecutionContext executionContext) throws ItemStreamException {
//	            beneficiariesList = careDataRepository.getBeneficiariesToSync(startDate, endDate);
//	    }
//
//	    @Override
//	    public void update(ExecutionContext executionContext) throws ItemStreamException {
//	        executionContext.putLong(CURRENT_INDEX, new Long(currentIndex).longValue());
//	    }
//
//	    @Override
//		public void close() throws ItemStreamException {
//	    	
//	    }
}
