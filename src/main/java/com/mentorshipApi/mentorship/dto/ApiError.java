package com.mentorshipApi.mentorship.dto;

import java.util.List;

public class ApiError {
    private int status;
    private String message;
    private List<FieldValidationError> errors;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<FieldValidationError> getErrors() {
        return errors;
    }

    public void setErrors(List<FieldValidationError> errors) {
        this.errors = errors;
    }

    public static class FieldValidationError {
        private String field;
        private Object rejectedValue;
        private String message;

        public FieldValidationError(String field, Object rejectedValue, String defaultMessage) {
            this.field = field;
            this.rejectedValue = rejectedValue;
            this.message = defaultMessage;
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public Object getRejectedValue() {
            return rejectedValue;
        }

        public void setRejectedValue(Object rejectedValue) {
            this.rejectedValue = rejectedValue;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

}
