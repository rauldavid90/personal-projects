package service;

import domain.Loc;
import repository.LocRepository;

import java.util.List;

public class LocService {
    private final LocRepository locRepository;

    public LocService(LocRepository locRepository) {
        this.locRepository = locRepository;
    }

    public Loc save(Loc loc){
        return locRepository.save(loc);
    }

    public Loc update(Loc loc){
        return locRepository.update(loc);
    }

    public Loc delete(Loc loc){
        return locRepository.delete(loc);
    }

    public Loc findOne(Long id) {
        return locRepository.findOne(id);
    }

    public List<Loc> findAll(){
        return locRepository.findAll();
    }
}
