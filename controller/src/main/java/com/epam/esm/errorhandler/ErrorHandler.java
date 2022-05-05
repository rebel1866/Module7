package com.epam.esm.errorhandler;


import com.epam.esm.exception.LogicException;
import com.epam.esm.exception.RestControllerException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Constraint;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.*;

/**
 * This class handles all the exceptions that can be thrown while application is executed
 *
 * @author Stanislav Melnikov
 * @version 1.0
 */
@RestControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {
    private final static Map<String, HttpStatus> CODES_AND_STATUSES = new HashMap<>();
    private String defaultResponse = "Error has occurred";
    private static final String DEFAULT_LOCALE = "en";
    private static final String BUNDLE_NAME = "lang";

    static {
        CODES_AND_STATUSES.put("errorCode=1", HttpStatus.NOT_FOUND);
        CODES_AND_STATUSES.put("errorCode=2", HttpStatus.INTERNAL_SERVER_ERROR);
        CODES_AND_STATUSES.put("errorCode=3", HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(RestControllerException.class)
    public ResponseEntity<String> handleControllerException(RestControllerException resException, HttpServletRequest request) {
        String localizedMessage = getLocalizedMessage(resException.getMessage(), request);
        HttpStatus status = CODES_AND_STATUSES.get(resException.getErrorCode());
        Errors errors = resException.getErrors();
        StringBuilder causeMessage = generateCauseMessage(errors, resException, request);
        ErrorMessage errorMessage = new ErrorMessage(localizedMessage, resException.getErrorCode(),
                status, causeMessage.toString());
        try {
            defaultResponse = getDefResponse(errorMessage);
        } catch (IOException e) {
            return new ResponseEntity<>(defaultResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        HttpHeaders responseHeaders = getUTF8Headers();
        return new ResponseEntity<>(defaultResponse, responseHeaders, status);
    }

    @ExceptionHandler(LogicException.class)
    public ResponseEntity<String> handleLogicException
            (LogicException logicException, HttpServletRequest request) {
        return handle(logicException, logicException.getErrorCode(), request);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handleAuthException
            (AuthenticationException authenticationException, HttpServletRequest request) throws JsonProcessingException {
        ErrorMessage errorMessage = new ErrorMessage("Access token is expired or invalid", "errorCode=3",
                HttpStatus.BAD_REQUEST, "INCORRECT TOKEN");
        defaultResponse = getDefResponse(errorMessage);
        return new ResponseEntity<>(defaultResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException
            (AccessDeniedException accessDeniedException, HttpServletRequest request) throws JsonProcessingException {
        ErrorMessage errorMessage = new ErrorMessage("Access denied", "errorCode=3",
                HttpStatus.FORBIDDEN, "No access allowed");
        defaultResponse = getDefResponse(errorMessage);
        return new ResponseEntity<>(defaultResponse, HttpStatus.FORBIDDEN);
    }

    public ResponseEntity<String> handle(Exception exception, String errorCode, HttpServletRequest request) {
        HttpStatus status = CODES_AND_STATUSES.get(errorCode);
        String exceptionMessage = exception.getMessage();
        String localizedMessage;
        if (exceptionMessage.startsWith("W")) {
            String[] messageArray = exceptionMessage.split(":");
            String messageBody = messageArray[0];
            String messageMeta = messageArray[1];
            localizedMessage = getLocalizedMessage(messageBody, request);
            localizedMessage = localizedMessage + ": " + messageMeta;
        } else {
            localizedMessage = getLocalizedMessage(exception.getMessage(), request);
        }
        ErrorMessage errorMessage = new ErrorMessage(localizedMessage, errorCode,
                status, localizedMessage);
        try {
            defaultResponse = getDefResponse(errorMessage);
        } catch (IOException e) {
            return new ResponseEntity<>(defaultResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        HttpHeaders httpHeaders = getUTF8Headers();
        return new ResponseEntity<>(defaultResponse, httpHeaders, status);
    }


    private StringBuilder generateCauseMessage(Errors errors, RestControllerException resException, HttpServletRequest request) {
        StringBuilder causeMessage;
        if (errors != null) {
            List<ObjectError> errorList = errors.getAllErrors();
            causeMessage = new StringBuilder();
            for (int i = 0; i < errorList.size(); i++) {
                String defaultMessage = errorList.get(i).getDefaultMessage();
                String localizedMessage = getLocalizedMessage(defaultMessage, request);
                causeMessage.append(localizedMessage);
                if (i != errorList.size() - 1) causeMessage.append(", ");
            }
        } else {
            causeMessage = new StringBuilder(resException.getMessage());
        }
        return causeMessage;
    }

    private String getLocalizedMessage(String message, HttpServletRequest request) {
        String localeStr = request.getHeader("locale");
        if (localeStr == null) {
            localeStr = DEFAULT_LOCALE;
        }
        Locale locale = new Locale(localeStr);
        ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_NAME, locale);
        return bundle.getString(message);
    }

    private HttpHeaders getUTF8Headers() {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=utf-8");
        return responseHeaders;
    }

    private String getDefResponse(ErrorMessage errorMessage) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        defaultResponse = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(errorMessage);
        return defaultResponse;
    }


    public static class ErrorMessage {
        private String errorMessage;
        private String errorCode;
        private HttpStatus httpStatus;
        private String cause;

        public ErrorMessage(String errorMessage, String errorCode, HttpStatus httpStatus, String cause) {
            this.errorMessage = errorMessage;
            this.errorCode = errorCode;
            this.httpStatus = httpStatus;
            this.cause = cause;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public String getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(String errorCode) {
            this.errorCode = errorCode;
        }

        public HttpStatus getHttpStatus() {
            return httpStatus;
        }

        public void setHttpStatus(HttpStatus httpStatus) {
            this.httpStatus = httpStatus;
        }

        public String getCause() {
            return cause;
        }

        public void setCause(String cause) {
            this.cause = cause;
        }
    }
}