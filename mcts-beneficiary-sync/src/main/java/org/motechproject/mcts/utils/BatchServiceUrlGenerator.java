package org.motechproject.mcts.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
	  
	  public String getUploadXmlUrl() throws FileNotFoundException, IOException {
		  properties.load(new FileInputStream(new File("ClassName.properties")));
		  return String.format("%s%s", properties.getProperty("batch.base.url"), properties.getProperty("rest-batch.properties"));
	 }
	  
	  public String getScheduleBatchUrl() {
		  return String.format("%s%s", properties.getProperty("batch.base.url"), properties.getProperty("batch.schedule.job.url"));
	  }


}
