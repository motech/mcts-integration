package org.motechproject.mcts.integration.service;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationTestContext.xml"})
public class LocationDataPopulatorTest 
{
	/*@Autowired
	private SessionFactory sessionFactory;
	*/
	@Autowired
	private LocationDataPopulator locationDataPopulator;
	
	@Autowired
	private FLWDataPopulator flwDataPopulator;
	
	
	//private LocationDataPopulator locationDataPopulator = new LocationDataPopulator();
	
	/*public LocationDataPopulator getLocationDataPopulator() {
		return locationDataPopulator;
	}

	public void setLocationDataPopulator(LocationDataPopulator locationDataPopulator) {
		this.locationDataPopulator = locationDataPopulator;
	}*/

	@Test
	public void readWithCsvBeanReaderTest() throws Exception
	{	
		System.out.println("started");
	//	System.out.println(sessionFactory. );
		//locationDataPopulator = new LocationDataPopulator(sessionFactory);
		locationDataPopulator.readWithCsvBeanReader();
	}
	
	@Test
	public void saveLocationDataTest() throws Exception
	{
		locationDataPopulator.saveLocationData();
	}
	
	@Test
	public void flwDataPopulatorTest() throws Exception
	{
		flwDataPopulator.flwDataPopulator();
	}
	
	@Test
	public void populateFLWDataTest() throws Exception
	{
		flwDataPopulator.populateFLWData();
	}

}
