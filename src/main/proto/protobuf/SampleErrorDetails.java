package protobuf;
import java.util.*;
import java.lang.*;
import io.protostuff.Tag;
public class SampleErrorDetails {

public enum ErrorCode  {
    UNDEFINED(0),
    VALIDATION_ERROR(1),
    DB_UNAVAILABLE(2),
    DATA_NOT_PRESENT(3),
    INTERNAL_SERVER_ERROR(4),
    UNAUTHORIZED(5),
    ;
    private int value;

    private ErrorCode(int value) {
      this.value = value;
    }

    public int getValue() {
      return value;
    }
}

public static class ErrorDetails {
    @Tag(1)
    ErrorCode errorCode;
    @Tag(2)
    String errorMessage;
    public ErrorCode getErrorCode() {
        return errorCode;
    }
    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
    public String getErrorMessage() {
        return errorMessage;
    }
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}

}
