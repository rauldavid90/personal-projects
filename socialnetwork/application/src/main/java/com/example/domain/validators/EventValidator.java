package com.example.domain.validators;


import com.example.domain.Event;

public class EventValidator implements Validator<Event> {

    @Override
    public void validate(Event entity) throws ValidatorException {
        if(entity.getName().equals(""))
            throw new ValidatorException("Name cannot be empty");
        if(entity.getDescription().equals(""))
            throw new ValidatorException("Description cannot be empty");
    }
}
