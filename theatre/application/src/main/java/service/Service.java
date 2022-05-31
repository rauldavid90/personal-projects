package service;

import domain.Administrator;
import domain.Client;
import domain.Loc;
import domain.Spectacol;
import utils.IObserver;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Service {
    private final AdministratorService administratorService;
    private final ClientService clientService;
    private final LocService locService;
    private final SpectacolService spectacolService;

    private final Map<Long, IObserver> loggedAdministratori;
    private final Map<Long, IObserver> loggedClienti;

    public Service(AdministratorService administratorService, ClientService clientService, LocService locService, SpectacolService spectacolService) {
        this.administratorService = administratorService;
        this.clientService = clientService;
        this.locService = locService;
        this.spectacolService = spectacolService;
        loggedAdministratori = new ConcurrentHashMap<>();
        loggedClienti = new ConcurrentHashMap<>();
    }

    public void setObserverAdministrator(Long id, IObserver client) {
        loggedAdministratori.put(id, client);
    }
    public void setObserverClient(Long id, IObserver client) {
        loggedClienti.put(id, client);
    }
    public void notifyClients(Loc loc) {
        for (IObserver administrator : loggedAdministratori.values()) {
            administrator.update(loc);
        }
        for (IObserver client : loggedClienti.values()) {
            client.update(loc);
        }
    }

    public Administrator loginAdministrator(String username, String parola) throws ServiceException {
        Administrator res = administratorService.login(username, parola);
        if (res != null) {
            if (loggedAdministratori.get(res.getId()) != null) {
                throw new ServiceException("Administrator already logged in!");
            }
        } else {
            throw new ServiceException("Authentication failed!");
        }
        return res;
    }
    public void logoutAdministrator(Administrator administrator){
        IObserver localAdministrators = loggedAdministratori.remove(administrator.getId());
        if (localAdministrators == null) {
            throw new ServiceException("User " + administrator.getNume() + " is not logged in!");
        }
    }

    public Client loginClient(String username, String parola) throws ServiceException {
        Client res = clientService.login(username, parola);
        if (res != null) {
            if (loggedClienti.get(res.getId()) != null) {
                throw new ServiceException("Client already logged in!");
            }
        } else {
            throw new ServiceException("Authentication failed!");
        }
        return res;
    }
    public void logoutClient(Client client){
        IObserver localClients = loggedClienti.remove(client.getId());
        if (localClients == null) {
            throw new ServiceException("User " + client.getNume() + " is not logged in!");
        }
    }

    public Loc addLoc(Loc loc){
        Loc result = locService.save(loc);
        notifyClients(result);
        return result;
    }
    public Loc findLoc(Long id){
        return locService.findOne(id);
    }
    public Loc updateLoc(Loc loc){
        Loc result = locService.update(loc);
        notifyClients(result);
        return result;
    }
    public Loc deleteLoc(Loc loc){
        Loc result = locService.delete(loc);
        notifyClients(result);
        return result;
    }
    public List<Loc> findAllLocuri() {
        return locService.findAll();
    }

    public Spectacol findSpectacol(Long id){
        return spectacolService.findOne(id);
    }
    public List<Spectacol> findAllSpectacole() {
        return spectacolService.findAll();
    }

    public Client addClient(Client client){
        return clientService.save(client);
    }
    public Client updateClient(Client client){
        return clientService.update(client);
    }
    public Client deleteClient(Client client){
        return clientService.delete(client);
    }
    public List<Client> findAllClienti() {
        return clientService.findAll();
    }
}
