package com.epam.esm.validation;

import com.epam.esm.exception.LogicException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * This class is used for additional validation
 * @author Stanislav Melnikov
 * @version 1.0
 */
public class Validation {

    public static void validateId(int id) {
        if (id <= 0) {
            throw new LogicException("messageCode10", "errorCode=3");
        }
    }

    public static void validatePage(int page) {
        if (page <= 0) {
            throw new LogicException("messageCode33", "errorCode=3");
        }
    }
    public static void validatePageSize(int pageSize) {
        if (pageSize <= 0) {
            throw new LogicException("messageCode34", "errorCode=3");
        }
    }
    public static void validateId(String id) {
        Pattern pattern = Pattern.compile("\\D+");
        Matcher matcher = pattern.matcher(id);
        if(matcher.find()){
            throw new LogicException("messageCode10", "errorCode=3");
        }
        validateId(Integer.parseInt(id));
    }
}
