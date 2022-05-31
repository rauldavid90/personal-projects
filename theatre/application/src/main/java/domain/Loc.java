package domain;

import java.util.Objects;

public class Loc {
    Long id;
    Integer pret;
    String zona;
    String stare;
    // Zona zona;
    // Stare stare;

    public Loc() {
    }

    public Loc(Long id, Integer pret, String zona, String stare) {
        this.id = id;
        this.pret = pret;
        this.zona = zona;
        this.stare = stare;
    }

    public Loc(Integer pret, String zona, String stare) {
        this.id = getRandomId();
        this.pret = pret;
        this.zona = zona;
        this.stare = stare;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPret() {
        return pret;
    }

    public void setPret(Integer pret) {
        this.pret = pret;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public String getStare() {
        return stare;
    }

    public void setStare(String stare) {
        this.stare = stare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Loc loc = (Loc) o;
        return Objects.equals(getId(), loc.getId()) && Objects.equals(getPret(), loc.getPret()) && Objects.equals(getZona(), loc.getZona()) && Objects.equals(getStare(), loc.getStare());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getPret(), getZona(), getStare());
    }

    @Override
    public String toString() {
        return "Loc{" +
                "id=" + id +
                ", pret=" + pret +
                ", zona='" + zona + '\'' +
                ", stare='" + stare + '\'' +
                '}';
    }

    private Long getRandomId() {
        long leftLimit = 1L;
        long rightLimit = 100000L;
        return leftLimit + (long) (Math.random() * (rightLimit - leftLimit));
    }
}
