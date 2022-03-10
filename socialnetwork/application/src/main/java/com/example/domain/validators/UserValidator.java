package com.example.domain.validators;

import com.example.domain.User;

public class UserValidator implements Validator<User> {
    @Override
    /**
     * validates an object of type User
     */
    public void validate(User entity) throws ValidatorException {
        if(entity.getFirstName().isEmpty())
            throw new ValidatorException("First name cannot be empty");
        if(entity.getLastName().isEmpty())
            throw new ValidatorException("Last name cannot be empty");
        if(entity.getUsername().isEmpty())
            throw new ValidatorException("Username cannot be empty");
        if(entity.getPassword().isEmpty())
            throw new ValidatorException("Password cannot be empty");
    }
}
