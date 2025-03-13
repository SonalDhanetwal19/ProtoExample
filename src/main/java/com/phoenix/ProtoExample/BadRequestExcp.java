package com.phoenix.ProtoExample;

import com.chargingtime.errorcodedetails.proto.ErrorCodeDetails.ErrorCode;
import lombok.Getter;
import lombok.Setter;

//make separate classes for different exception types
@Getter
@Setter
public class BadRequestExcp extends RuntimeException{

    private ErrorCode errorCode;

    public BadRequestExcp(String message, Throwable cause, ErrorCode errorCode)
    {
        super(message,cause);
        this.errorCode = errorCode;
    }
    public BadRequestExcp(String message, Throwable cause)
    {
        super(message,cause);
    }
    public BadRequestExcp(String message, ErrorCode errorCode)
    {
        super(message);
        this.errorCode = errorCode;
        System.out.println("entered BadRequestException constructor : "+errorCode.getNumber());
    }
    public BadRequestExcp(String message)
    {
        super(message);
    }

}
