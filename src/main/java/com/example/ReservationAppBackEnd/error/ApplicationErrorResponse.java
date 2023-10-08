package com.example.ReservationAppBackEnd.error;

import java.util.List;

public record ApplicationErrorResponse(List<ApplicationError> errors) {
}