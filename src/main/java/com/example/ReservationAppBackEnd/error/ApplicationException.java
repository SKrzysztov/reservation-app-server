package com.example.ReservationAppBackEnd.error;

import java.util.List;

public class ApplicationException extends RuntimeException {

    public ApplicationException(List<ApplicationError> errors) {
        this.errors = errors;
    }

    final List<ApplicationError> errors;
}