// Autor: Rodrigo Escobar Fecha: 27/5/2022
package com.multi_works_group.exceptions;

public class ServiceException extends RuntimeException {
    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}