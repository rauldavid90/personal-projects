package domain;

import java.util.Objects;

public class Spectacol {
    Long id;
    String nume;
    String descriere;

    public Spectacol() {
    }

    public Spectacol(Long id, String nume, String descriere) {
        this.id = id;
        this.nume = nume;
        this.descriere = descriere;
    }

    public Spectacol(String nume, String descriere) {
        this.id = getRandomId();
        this.nume = nume;
        this.descriere = descriere;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Spectacol spectacol = (Spectacol) o;
        return Objects.equals(getId(), spectacol.getId()) && Objects.equals(getNume(), spectacol.getNume()) && Objects.equals(getDescriere(), spectacol.getDescriere());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getNume(), getDescriere());
    }

    @Override
    public String toString() {
        return "Spectacol{" +
                "id=" + id +
                ", nume='" + nume + '\'' +
                ", descriere='" + descriere + '\'' +
                '}';
    }

    private Long getRandomId() {
        long leftLimit = 1L;
        long rightLimit = 100000L;
        return leftLimit + (long) (Math.random() * (rightLimit - leftLimit));
    }
}
