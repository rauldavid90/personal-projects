package com.example.domain.validators;


import com.example.domain.Friendship;

public class FriendshipValidator implements Validator<Friendship> {
    @Override
    /**
     * validates an object of type Friendship
     */
    public void validate(Friendship entity) throws ValidatorException {
        if(entity.getId_friend_1() <= 0)
            throw new ValidatorException("Id of friend 1 cannot be 0 or negative");
        if(entity.getId_friend_2() <= 0)
            throw new ValidatorException("Id of friend 2 cannot be 0 or negative");
        if(entity.getId_friend_1() == entity.getId_friend_2())
            throw new ValidatorException("Ids cannot be the same");
    }
}
