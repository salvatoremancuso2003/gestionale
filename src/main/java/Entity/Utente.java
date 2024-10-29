/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

import jakarta.persistence.CascadeType;
import java.io.Serializable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Salvatore
 */
@Entity
@Table(name = "utente")
public class Utente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String nome;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    private String cognome;

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    @ManyToOne
    private Ruolo ruolo;

    public Ruolo getRuolo() {
        return ruolo;
    }

    public void setRuolo(Ruolo ruolo) {
        this.ruolo = ruolo;
    }

    @OneToMany(mappedBy = "utente")
    private List<Presenza> presenze;

    public List<Presenza> getPresenze() {
        return presenze;
    }

    public void setPresenze(List<Presenza> presenze) {
        this.presenze = presenze;
    }

    private int ferie_disponibili;

    public int getFerie_disponibili() {
        return ferie_disponibili;
    }

    public void setFerie_disponibili(int ferie_disponibili) {
        this.ferie_disponibili = ferie_disponibili;
    }

    @OneToMany(mappedBy = "utente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notifica> notifiche = new ArrayList<>();

    public List<Notifica> getNotifiche() {
        return notifiche;
    }

    public void setNotifiche(List<Notifica> notifiche) {
        this.notifiche = notifiche;
    }

    private int ore_disponibili;

    public int getOre_disponibili() {
        return ore_disponibili;
    }

    public void setOre_disponibili(int ore_disponibili) {
        this.ore_disponibili = ore_disponibili;
    }

    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getNumero_di_telefono() {
        return numero_di_telefono;
    }

    public void setNumero_di_telefono(String numero_di_telefono) {
        this.numero_di_telefono = numero_di_telefono;
    }

    private String numero_di_telefono;

    private int ore_contratto;

    public int getOre_contratto() {
        return ore_contratto;
    }

    public void setOre_contratto(int ore_contratto) {
        this.ore_contratto = ore_contratto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Utente)) {
            return false;
        }
        Utente other = (Utente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Utente[ id=" + id + " ]";
    }

}
