package com.hwx.backeend.result;

public class ErrorMessage {
    String errorCode = null;
    String errormessage = null;
    String cause = null;

    public ErrorMessage() {

    }

    public ErrorMessage(String errorCode, String errormessage, String cause) {
        this.errorCode = errorCode;
        this.errormessage = errormessage;
        this.cause = cause;
    }


    public String toString() {
        return "Error{" +
                "errorCode=" + errorCode +
                ", errormessage='" + errormessage + '\'' +
                ", cause='" + cause + '\'' +
                '}';
    }
}
