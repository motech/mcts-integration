package org.motechproject.mcts.utils;

import java.util.Properties;
import org.springframework.stereotype.Component;

@Component
public class BatchServiceUrlGenerator {
	
	  private Properties properties = new Properties();
	  
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


}
