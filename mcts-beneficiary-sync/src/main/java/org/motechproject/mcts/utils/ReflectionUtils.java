package org.motechproject.mcts.utils;

import java.lang.reflect.Field;

import org.motechproject.mcts.integration.exception.ApplicationErrors;
import org.motechproject.mcts.integration.exception.BeneficiaryException;

/** This is a class taken fro care-reporting module */
public final class ReflectionUtils {
    private ReflectionUtils() {

    }

    public static Object getValue(Object object, String fieldName) {
        try {
            Field field = getField(object, fieldName);
            field.setAccessible(true);
            return field.get(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new BeneficiaryException(
                    ApplicationErrors.RUN_TIME_EXCEPTION, e,
                    e.getLocalizedMessage());
        }
    }

    private static Field getField(Object object, String fieldName)
            throws NoSuchFieldException {
        Field field;
        try {
            field = object.getClass().getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            field = object.getClass().getSuperclass()
                    .getDeclaredField(fieldName);
        }
        return field;
    }
}
