package org.motechproject.mcts.integration.web;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
//import com.google.gson.Gson;

import org.motechproject.mcts.integration.hibernate.model.MctsPregnantMother;
import org.motechproject.mcts.integration.service.CareDataService;
import org.motechproject.mcts.integration.service.Publisher;
import org.motechproject.mcts.utils.PropertyReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping(value = "/publish")
public class PublishCallBack {

	@Autowired
	private CareDataService careDataService;
	
	static final long ONE_MINUTE_IN_MILLIS=60000;//millisecs
	
	private Publisher publisher = new Publisher();
	private final static Logger LOGGER = LoggerFactory
			.getLogger(BeneficiarySyncController.class);

	@Autowired
	private PropertyReader beneficiarySyncSettings;


	/**
	 * Method to validate connection
	 * @param query
	 * @return string
	 */
	@RequestMapping(value = "ping", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public String ping(@RequestParam("query") String query){
		return String.format("Ping Received Succefully with query param: %s", query);
	}
	
	@RequestMapping(value = "updatesreceived", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	HttpEntity sendUpdatesReceived(@RequestParam("time") String dateTime)
			throws Exception {
		LOGGER.info("Publishing Data to Hub.");
		Date date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SS", Locale.ENGLISH).parse(dateTime);
		long t=date.getTime();
		Date higherDate=new Date(t + (5 * ONE_MINUTE_IN_MILLIS));
		Date lowerDate=new Date(t - (5 * ONE_MINUTE_IN_MILLIS));
		LOGGER.debug("Params Passed are LowerDateTime: " + lowerDate + " & HigherDateTime: " + higherDate);
		List<MctsPregnantMother> mctsPregnantMothers = careDataService.findEntityByFieldWithConstarint(MctsPregnantMother.class, "creationTime",lowerDate, higherDate); 
		//String data = readFileData(time);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		//String mctsToJson = returnJson(mctsPregnantMothers);
		HttpEntity httpEntity = new HttpEntity(mctsPregnantMothers, httpHeaders);
		LOGGER.debug(mctsPregnantMothers.toString());
		return httpEntity;
	}
	
	/*@RequestMapping(value = "updatessent", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	HttpEntity sendUpdatesSent(@RequestParam("time") String time)
			throws Exception {
		LOGGER.info("Publishing Data to Hub.");
		String data = readFileData(time);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_XML);
		HttpEntity httpEntity = new HttpEntity(data, httpHeaders);
		return httpEntity;
	}
	*/
/*	public String returnJson(Object obj){
		Gson gson = new Gson();
		 
		// convert java object to JSON format,
		// and returned as JSON formatted string
		String json = gson.toJson(obj);
	 
		return json;
	}*/

	public String readFileData(String filePath) throws Exception {
		BufferedReader br = null;
		String data = new String();		
		try {
			String sCurrentLine;
			br = new BufferedReader(new FileReader(filePath));
			while ((sCurrentLine = br.readLine()) != null) {
				data += sCurrentLine;
			}
		} catch (IOException e) {
			throw new Exception(e);
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				throw new Exception(ex);
			}
			return data;
		}
	}

}
