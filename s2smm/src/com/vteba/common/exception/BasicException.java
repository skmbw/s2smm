package com.vteba.common.exception;

/**
 * 一般异常，非事务异常。
 * @author yinlei
 * date 2012-08-31
 */
public class BasicException extends RuntimeException {

	private static final long serialVersionUID = -4971709041482821303L;

	public BasicException() {
		super();
	}

	public BasicException(String message, Throwable cause) {
		super(message, cause);
	}

	public BasicException(String message) {
		super(message);
	}

	public BasicException(Throwable cause) {
		super(cause);
	}
	
}
