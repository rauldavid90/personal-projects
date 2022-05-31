package repository;

import domain.Loc;

import java.util.List;

public interface ILocRepository {
    public List<Loc> findAll();
}
