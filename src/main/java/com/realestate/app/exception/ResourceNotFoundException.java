package com.realestate.app.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String entityName, long id) {
        super(entityName + " with id " + id + " not found");
    }
}
