package service;

import domain.Client;
import domain.Loc;
import repository.ClientRepository;

import java.util.List;

public class ClientService {
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client login(String username, String parola) {
        Client client = clientRepository.login(username, parola);
        return client;
    }

    public Client save(Client client){
        return clientRepository.save(client);
    }

    public Client update(Client client){
        return clientRepository.update(client);
    }

    public Client delete(Client client){
        return clientRepository.delete(client);
    }

    public List<Client> findAll(){
        return clientRepository.findAll();
    }
}
