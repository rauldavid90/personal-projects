package domain;

import java.util.Objects;

public class Client {
    Long id;
    String username;
    String parola;
    String nume;
    String cnp;
    String nrTelefon;

    public Client() {
    }

    public Client(Long id, String username, String parola, String nume, String cnp, String nrTelefon) {
        this.id = id;
        this.username = username;
        this.parola = parola;
        this.nume = nume;
        this.cnp = cnp;
        this.nrTelefon = nrTelefon;
    }

    public Client(String username, String parola, String nume, String cnp, String nrTelefon) {
        this.id = getRandomId();
        this.username = username;
        this.parola = parola;
        this.nume = nume;
        this.cnp = cnp;
        this.nrTelefon = nrTelefon;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getParola() {
        return parola;
    }

    public void setParola(String parola) {
        this.parola = parola;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getCnp() {
        return cnp;
    }

    public void setCnp(String cnp) {
        this.cnp = cnp;
    }

    public String getNrTelefon() {
        return nrTelefon;
    }

    public void setNrTelefon(String nrTelefon) {
        this.nrTelefon = nrTelefon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(getId(), client.getId()) && Objects.equals(getUsername(), client.getUsername()) && Objects.equals(getParola(), client.getParola()) && Objects.equals(getNume(), client.getNume()) && Objects.equals(getCnp(), client.getCnp()) && Objects.equals(getNrTelefon(), client.getNrTelefon());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUsername(), getParola(), getNume(), getCnp(), getNrTelefon());
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", parola='" + parola + '\'' +
                ", nume='" + nume + '\'' +
                ", cnp='" + cnp + '\'' +
                ", nrTelefon='" + nrTelefon + '\'' +
                '}';
    }

    private Long getRandomId() {
        long leftLimit = 1L;
        long rightLimit = 100000L;
        return leftLimit + (long) (Math.random() * (rightLimit - leftLimit));
    }
}
