package com.vteba.common.exception;

/**
 * Service layer throws Exception for Transaction Rollback。
 * @author yinlei
 * date 2013-9-18 下午8:02:56
 */
public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = -3298300980854698201L;

	public ServiceException() {
		super();
	}

	public ServiceException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
		
	}

	public ServiceException(String message) {
		super(message);
		
	}

	public ServiceException(Throwable cause) {
		super(cause);
		
	}


}
