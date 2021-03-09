package com.company.Injector;

public class ContainerException extends RuntimeException {

    public ContainerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ContainerException(Throwable cause) {
        super(cause);
    }

    public ContainerException(String message) {
        super(message);
    }

}
