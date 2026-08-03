package org.zero_consult.timesheet_backend.exceptions;

public class MonthAlreadyClosedException extends Exception {
    public MonthAlreadyClosedException(String message) {
        super(message);
    }
}
