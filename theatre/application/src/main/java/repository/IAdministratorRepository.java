package repository;

import domain.Administrator;

public interface IAdministratorRepository {
    public Administrator login(String username, String parola);
}
