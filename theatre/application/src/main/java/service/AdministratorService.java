package service;

import domain.Administrator;
import domain.Client;
import repository.AdministratorRepository;

public class AdministratorService {
    private final AdministratorRepository administratorRepository;

    public AdministratorService(AdministratorRepository administratorRepository) {
        this.administratorRepository = administratorRepository;
    }

    public Administrator login(String username, String parola) {
        Administrator administrator = administratorRepository.login(username, parola);
        return administrator;
    }


}
