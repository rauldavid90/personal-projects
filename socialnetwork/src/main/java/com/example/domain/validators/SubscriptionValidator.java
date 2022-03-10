package com.example.domain.validators;


import com.example.domain.Subscription;

public class SubscriptionValidator implements Validator<Subscription> {

    @Override
    public void validate(Subscription entity) throws ValidatorException {
        if(entity.getUser_id() == 0L)
            throw new ValidatorException("User id cannot be 0");
        if(entity.getEvent_id() == 0L)
            throw new ValidatorException("Event id cannot be 0");
    }
}
