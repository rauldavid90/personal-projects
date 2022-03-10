package com.example.domain.validators;

public interface Validator<T> {
    /**
     * validates an entity
     * @param entity
     * @throws ValidatorException
     */
    void validate(T entity) throws ValidatorException;
}