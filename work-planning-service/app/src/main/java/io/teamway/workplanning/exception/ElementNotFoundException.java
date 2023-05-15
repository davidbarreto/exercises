package io.teamway.workplanning.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Objects;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ElementNotFoundException extends RuntimeException {
    public <T> ElementNotFoundException(String resourceName, T id) {
        super(String.format("Element [%s] with id [%s] no found", resourceName, Objects.toString(id)));
    }
}
