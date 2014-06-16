package org.motechproject.mcts.utils;

import java.io.InputStream;
import java.util.Properties;

import org.springframework.stereotype.Component;

@Component
public class BatchServiceUrlGenerator {
	
	  private Properties properties = new Properties();
	  InputStream input = null;
	  
	    public BatchServiceUrlGenerator(Properties properties) {
	        this.properties = properties;
	    }
	    
	    public BatchServiceUrlGenerator() { 
	        
	    }
	  
	  public void setProperties(Properties properties) {
			this.properties = properties;
	  }
	  
	  public String getUploadXmlUrl() {
		  return String.format("%s%s", properties.getProperty("batch.base.url"), properties.getProperty("rest-batch.properties"));
	 }
	  
	  public String getScheduleBatchUrl() {
		  return String.format("%s%s", properties.getProperty("batch.base.url"), properties.getProperty("batch.schedule.job.url"));
	  }
	  
	  public String getTriggerJobUrl() {
		  return String.format("%s%s", properties.getProperty("batch.base.url"), properties.getProperty("batch.trigger.job.url"));
	  }
	  
	  public String getCaseUploadUrl() {
		  loadInputFile("rest-batch.properties");
		  return String.format("%s%s", properties.getProperty("commcareHQ.base.url"),properties.getProperty("commcare.upload.url"));
	  }
	  
	  private void loadInputFile(String propertiesPath) {
		  
		  try {
			input = BatchServiceUrlGenerator.class.getClassLoader().getResourceAsStream(propertiesPath);
			properties.load(input);
		} catch (Exception e) {
			e.printStackTrace();
		}
		  
		  
	  }
	  


}
