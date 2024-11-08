package com.learn.finance.exception;

import lombok.Data;

@Data
public class DownStreamException extends RuntimeException {
    public DownStreamException(int code, String body) {
        super("Exception calling Bank DownStream Service ");
        this.code = code;
        this.body = body;
    }
    int code;
    String body;


}
