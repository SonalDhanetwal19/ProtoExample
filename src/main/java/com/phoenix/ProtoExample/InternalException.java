package com.phoenix.ProtoExample;

import com.chargingtime.errorcodedetails.proto.ErrorCodeDetails.ErrorCode;
import lombok.Getter;
import lombok.Setter;

//make separate classes for different exception types
@Getter
@Setter
public class InternalException extends RuntimeException{

    private ErrorCode errorCode;

    public InternalException(String message, Throwable cause,  ErrorCode errorCode)
    {
        super(message,cause);
        this.errorCode = errorCode;
    }
    public InternalException(String message, Throwable cause)
    {
        super(message,cause);
    }
    public InternalException(String message, ErrorCode errorCode)
    {
        super(message);
        this.errorCode = errorCode;
        System.out.println("entered Internal exception constructor : "+errorCode.getNumber());
    }
    public InternalException(String message)
    {
        super(message);
    }

}
