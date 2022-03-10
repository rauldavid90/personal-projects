package com.example.domain.validators;


import com.example.domain.FriendshipRequest;

public class FriendshipRequestValidator implements Validator<FriendshipRequest> {
    @Override
    /**
     * validates an object of type Friendship
     */
    public void validate(FriendshipRequest entity) throws ValidatorException {
        if(entity.getFrom_id() <= 0)
            throw new ValidatorException("Id 'from' cannot be 0 or negative");
        if(entity.getTo_id() <= 0)
            throw new ValidatorException("Id 'to' cannot be 0 or negative");
        if(entity.getFrom_id() == entity.getTo_id())
            throw new ValidatorException("Ids cannot be the same");
    }
}
