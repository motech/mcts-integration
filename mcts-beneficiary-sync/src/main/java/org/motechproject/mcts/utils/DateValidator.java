package org.motechproject.mcts.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateValidator {

	private final static Logger LOGGER = LoggerFactory
			.getLogger(DateValidator.class);

	public static Date checkDateInFormat(String dateStr, String format) {
		
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		formatter.setLenient(false);
		Date date = null;
		try {
			date = formatter.parse(dateStr);
			if (!dateStr.equals(formatter.format(date))) {
				date = null;
			}
		} catch (ParseException e) {
			LOGGER.error(e.getMessage());
			
		}
		return date;

	}
}
