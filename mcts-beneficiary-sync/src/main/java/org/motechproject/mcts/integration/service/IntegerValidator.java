package org.motechproject.mcts.integration.service;

import org.motechproject.mcts.integration.exception.BeneficiaryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class IntegerValidator {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(IntegerValidator.class);

    private IntegerValidator() {

    }

    /**
     * Validate if the string is int or not && return by converting it to int
     *
     * @param field
     * @param id
     * @return
     * @throws BeneficiaryException
     */
    public static Integer validateAndReturnAsInt(String field, String id,
            boolean required) {

        if (id == null || id.isEmpty()) {

            LOGGER.error(String.format("Value received for [%s : %s] is null",
                    field, id));
            return null;
        } else {
            if (id != null && !id.isEmpty() && (id.matches("[0-9]+"))) {

                return Integer.parseInt(id);
            }
        }
        return null;
    }

    public static Integer validateAndReturnAsInt(String field, String id) {
        return validateAndReturnAsInt(field, id, true);
    }

}
