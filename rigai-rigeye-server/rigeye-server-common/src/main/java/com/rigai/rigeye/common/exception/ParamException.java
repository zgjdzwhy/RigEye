package com.rigai.rigeye.common.exception;

/**
 * @author chenxing
 *         Created by ChenXing on 2018/8/20.
 */

public class ParamException extends RuntimeException{
    private static final long serialVersionUID = -8783211544345685918L;

    public ParamException(String message) {
        super(message);
    }
}
