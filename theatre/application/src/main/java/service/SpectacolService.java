package service;

import domain.Loc;
import domain.Spectacol;
import repository.SpectacolRepository;

import java.util.List;

public class SpectacolService {
    private final SpectacolRepository spectacolRepository;

    public SpectacolService(SpectacolRepository spectacolRepository) {
        this.spectacolRepository = spectacolRepository;
    }

    public Spectacol findOne(Long id) {
        return spectacolRepository.findOne(id);
    }

    public List<Spectacol> findAll(){
        return spectacolRepository.findAll();
    }
}
