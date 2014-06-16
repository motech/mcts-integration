package mcts.integration.stub.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;

import mcts.integration.stub.exception.PubsubException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/subscriber")
public class SubscriberController {
	
	private final static Logger LOGGER = LoggerFactory
			.getLogger(SubscriberController.class);
		
	@ResponseStatus(value = HttpStatus.OK)
	@RequestMapping(value = "/ping", method = RequestMethod.POST)//, headers = "Content-Type=application/x-www-form-urlencoded")
	@ResponseBody
	String ping(@RequestBody String request) {
		LOGGER.info("HELLO Ping IS CALLED IN SUBSCRIBER CONTROLLER");
		LOGGER.info(request.toString());
		return request.toString();
	}
	
	@ResponseStatus(value = HttpStatus.OK)
	@RequestMapping(value = "/callback/{topic}", method = RequestMethod.GET, headers = "Content-Type=application/x-www-form-urlencoded", params = {"hub.challenge", "hub.mode", "hub.topic"} )
	@ResponseBody
	String verifyIntent(@PathVariable(value = "topic") String topic,
			@RequestParam(value = "hub.challenge") String challenge,
			@RequestParam(value = "hub.mode") String mode,
			@RequestParam(value = "hub.topic") String topicUrl,
			@RequestParam(value = "hub.lease_seconds", required = false) String leaseSeconds)
			throws PubsubException {
		String response = null;
		if ((mode.equals("subscribe") || mode.equals("unsubscribe")) && topicUrl.equals("http://localhost:9978/publish/beneficiaries")) {
			response = challenge;
		} 
		LOGGER.info(String.format("Topic: %s\nChallenge: %s\nMode: %s\nTopicUrl: %s\nLease Seconds:%s", topic, challenge, mode, topicUrl, leaseSeconds));
		return response;
	}
	
	
	@ResponseStatus(value = HttpStatus.OK)
	@RequestMapping(value = "/callback/beneficiary", method = RequestMethod.POST, headers = "Content-Type=text/xml")
	@ResponseBody
	String testMethod(@RequestBody String content/*,
			@RequestParam(value = "hub.mode") String mode,
			@RequestParam(value = "hub.topic") String topicUrl,
			@RequestParam(value = "hub.reason", required=false) String reason*/) throws IOException {
//		String body = null;
//	    StringBuilder stringBuilder = new StringBuilder();
//	    BufferedReader bufferedReader = null;
//
//	    try {
//	        InputStream inputStream = request.getInputStream();
//	        if (inputStream != null) {
//	            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//	            char[] charBuffer = new char[128];
//	            int bytesRead = -1;
//	            while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
//	                stringBuilder.append(charBuffer, 0, bytesRead);
//	            }
//	        } else {
//	            stringBuilder.append("");
//	        }
//	    } catch (IOException ex) {
//	        throw ex;
//	    } finally {
//	        if (bufferedReader != null) {
//	            try {
//	                bufferedReader.close();
//	            } catch (IOException ex) {
//	                throw ex;
//	            }
//	        }
//	    }
//
//	    body = stringBuilder.toString();
		LOGGER.info("Content received on call back is: \n" + content + "\n");
	    return content;
	}

}
