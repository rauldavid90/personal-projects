package repository;

import domain.Client;

public interface IClientRepository {
    public Client login(String username, String parola);
}
