package org.motechproject.mcts.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DateValidator {

    private DateValidator() {

    }
    private static final String VALID_DATE_PATTERN = "^\\d{1-2}-\\d{1-2}-\\d{4}$";
    
    private static final Logger LOGGER = LoggerFactory
            .getLogger(DateValidator.class);


    public static Date checkDateInFormat(String dateStr, String format) {

        SimpleDateFormat formatter = new SimpleDateFormat(format);
        formatter.setLenient(false);
        Date date = null;
        try {
            date = formatter.parse(dateStr);
            //TODO check whether this is needed
//            if (!dateStr.equals(formatter.format(date))) {
//                date = null;
//            }
        } catch (ParseException e) {
            LOGGER.error(e.getMessage());

        }
        return date;

    }
}
