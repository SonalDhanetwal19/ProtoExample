package com.phoenix.ProtoExample;

import com.chargingtime.errorcodedetails.proto.ErrorCodeDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionalHandler {

    @ExceptionHandler(InternalException.class)
    public ResponseEntity<byte[]> handleException(InternalException exception, WebRequest request) {
        System.out.println("inside handleException");
        var errorDetails = buildErrorResponseProto(exception, HttpStatus.INTERNAL_SERVER_ERROR, request);
        return ResponseEntity.internalServerError()
                .header(HttpHeaders.CONTENT_TYPE,"application/x-protobuf")
                .body(errorDetails.toByteArray());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<byte[]> handleBadRequestException(BadRequestException exception, WebRequest request) {
        System.out.println("inside handleBadRequestException");
        var errorDetails = buildErrorResponseProto(exception, HttpStatus.BAD_REQUEST, request);
        return ResponseEntity.internalServerError()
                .header(HttpHeaders.CONTENT_TYPE,"application/x-protobuf")
                .body(errorDetails.toByteArray());
    }

//    @ExceptionHandler(InternalException.class)
//    public ResponseEntity<ErrorResponse> handleExceptionInJsonFormat( InternalException exception, WebRequest request) {
//        return buildErrorResponse(exception, HttpStatus.NOT_FOUND, request);
//    }

    private ErrorCodeDetails.ErrorDetails buildErrorResponseProto(Exception exception, HttpStatus httpStatus, WebRequest request)
    {
        System.out.println("entered buildErrorResponseProto");
        var errorDetails = ErrorCodeDetails.ErrorDetails.newBuilder().setErrorCodeValue(httpStatus.value()).setErrorMessage(exception.getMessage()).build();
        return errorDetails;
    }
    private ResponseEntity<ErrorResponse> buildErrorResponse(  InternalException exception, HttpStatus httpStatus, WebRequest request) {

        String path = extractPath(request);

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(httpStatus.value());
        errorResponse.setPath(path);
        errorResponse.setTimestamp(LocalDateTime.now());

        if (exception.getMessage().contains("500")) {
            String message = exception.getMessage();
            String jsonMessage = message.substring(message.indexOf("["));
            System.out.println("Extracting exception from message : {}"+ jsonMessage);
            ObjectMapper mapper = new ObjectMapper();

            try {
                JsonNode jsonNode = mapper.readTree(jsonMessage);

                if (jsonNode.isArray() && jsonNode.get(0).has("title") && jsonNode.get(0).has("detail")) {
                    String title = jsonNode.get(0).get("title").asText();
                    String detail = jsonNode.get(0).get("detail").asText();
                    System.out.println("Exception due to "+ title +"  "+ detail);
                    String errorMessage = String.join(" - ", title, detail);
                    errorResponse.setMessage(errorMessage);
                }
            } catch (JsonProcessingException ex) {
                //Handling json parsing error
                System.out.println("!!! Exception occurred while parsing the json - {}"+ ex.getMessage());
                throw new InternalException("Error occurred while parsing the json");
            }
        }
        return null;
    }

        private String extractPath(WebRequest request) {
            System.out.println(" Extracting web request path");
            String description = request.getDescription(false);
            return description.substring(4);
        }
}