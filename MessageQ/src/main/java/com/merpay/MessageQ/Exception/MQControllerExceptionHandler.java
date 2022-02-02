package com.merpay.MessageQ.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global Exception Handler for Controller
 * Handles all exceptions in Controller
 */
@ControllerAdvice
public class MQControllerExceptionHandler {

    @ExceptionHandler(TopicUnsubscribedException.class)
    public ResponseEntity<Object> handleTopicUnsubscribedException(TopicUnsubscribedException exception) {

        ErrorDetails errorDetails = new ErrorDetails(exception.getMessage());

        return new ResponseEntity<Object>(errorDetails, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UnregisteredTopicException.class)
    public ResponseEntity<Object> handleUnregisteredTopicException(UnregisteredTopicException exception) {

        ErrorDetails errorDetails = new ErrorDetails(exception.getMessage());
        return new ResponseEntity<Object>(errorDetails,
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UnauthorisedToPublishException.class)
    public ResponseEntity<Object> handleUnauthorisedToPublishException(UnauthorisedToPublishException exception) {

        ErrorDetails errorDetails = new ErrorDetails(exception.getMessage());

        return new ResponseEntity<Object>(
                errorDetails,
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NoSubscribersToTopicException.class)
    public ResponseEntity<Object> handleNoSubscribersToTopicException(NoSubscribersToTopicException exception) {

        ErrorDetails errorDetails = new ErrorDetails(exception.getMessage());

        return new ResponseEntity<Object>(
                errorDetails,
                HttpStatus.UNAUTHORIZED);
    }

}
