package org.motechproject.mcts.integration.service;

import java.io.FileReader;

import org.motechproject.mcts.integration.hibernate.model.Locationdata;
import org.motechproject.mcts.integration.hibernate.model.MctsDistrict;
import org.motechproject.mcts.integration.hibernate.model.MctsHealthblock;
import org.motechproject.mcts.integration.hibernate.model.MctsState;
import org.motechproject.mcts.integration.hibernate.model.MctsTaluk;
import org.motechproject.mcts.integration.model.LocationDataCSV;
import org.motechproject.mcts.integration.repository.CareDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

@Transactional
@Repository
@ContextConfiguration(locations = { "classpath:*Context.xml" })
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = true)
public class LocationDataPopulator {

	public CareDataRepository getCareDataRepository() {
		return careDataRepository;
	}

	public void setCareDataRepository(CareDataRepository careDataRepository) {
		this.careDataRepository = careDataRepository;
	}

	@Autowired
	private CareDataRepository careDataRepository;

	/*
	 * private SessionFactory sessionFactory;
	 * 
	 * public SessionFactory getSessionFactory() { return sessionFactory; }
	 * 
	 * 
	 * public void setSessionFactory(SessionFactory sessionFactory) {
	 * this.sessionFactory = sessionFactory; }
	 * 
	 * 
	 * @Autowired public LocationDataPopulator(SessionFactory sessionFactory) {
	 * this.sessionFactory = sessionFactory; }
	 */

	public LocationDataPopulator() {

	}

	public void readWithCsvBeanReader() throws Exception {
		ICsvBeanReader beanReader = null;

		try {
			beanReader = new CsvBeanReader(new FileReader(
					"/home/aman/Downloads/location.csv"),
					CsvPreference.STANDARD_PREFERENCE);
			final String[] header = beanReader.getHeader(true);
			// Session session = getCurrentSession();
			LocationDataCSV locationCSV = new LocationDataCSV();

			/*
			 * List<Locationdata> result = getCurrentSession().createQuery(
			 * "select location from Locationdata location where location.locationdataId=110"
			 * ).list(); System.out.println(result.get(0).getDistrict());
			 */
			while ((locationCSV = beanReader
					.read(LocationDataCSV.class, header)) != null) {

				try {
					// Locationdata location = new
					// Locationdata((int)careDataRepository.getNextKey(),locationCSV.getStateID(),
					// locationCSV.getState(), locationCSV.getDCode(),
					// locationCSV.getDistrict(), locationCSV.getTCode(),
					// locationCSV.getTaluka_Name(), locationCSV.getBID(),
					// locationCSV.getBlock(), locationCSV.getPID(),
					// locationCSV.getPHC(), locationCSV.getSID(),
					// locationCSV.getSUBCenter(), locationCSV.getVCode(),
					// locationCSV.getVillage());
					MctsState mctsState = new MctsState(
							locationCSV.getStateID(), locationCSV.getState());
					careDataRepository.saveOrUpdate(mctsState);
//					MctsDistrict mctsDistrict = new MctsDistrict(mctsState,
//							locationCSV.getDCode(), locationCSV.getDistrict());
//					careDataRepository.saveOrUpdate(mctsDistrict);
//					MctsTaluk mctsTaluk = new MctsTaluk(mctsDistrict,
//							locationCSV.getTCode(),
//							locationCSV.getTaluka_Name());
//					careDataRepository.saveOrUpdate(mctsTaluk);
					
					// careDataRepository.saveOrUpdate(location);

				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
				// getCurrentSession().flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (beanReader != null) {
				beanReader.close();
			}
		}
	}
	/*
	 * private Session getCurrentSession() { return
	 * sessionFactory.getCurrentSession(); }
	 */

}
