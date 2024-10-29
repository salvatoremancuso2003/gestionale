/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

import Enum.Stato_enum;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 *
 * @author Salvatore
 */
@Entity
@NamedQuery(name = "findAll", query = "SELECT r FROM Richiesta r")
public class Richiesta implements Serializable {

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

    @ManyToOne
    @JoinColumn(name = "tipo_permesso_codice")
    private Permesso tipo_permesso;

    @ManyToOne
    @JoinColumn(name = "utente_id")
    private Utente utente;

    @Temporal(TemporalType.TIMESTAMP)
    private Date data_inizio;

    @Temporal(TemporalType.TIMESTAMP)
    private Date data_fine;

    @ManyToOne
    @JoinColumn(name = "allegato_id")
    private FileEntity allegato;

    private String note;
    private Timestamp timestamp;

    @Enumerated(EnumType.STRING)
    private Stato_enum stato;

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public Permesso getTipo_permesso() {
        return tipo_permesso;
    }

    public void setTipo_permesso(Permesso tipo_permesso) {
        this.tipo_permesso = tipo_permesso;
    }

    public Date getData_inizio() {
        return data_inizio;
    }

    public void setData_inizio(Date data_inizio) {
        this.data_inizio = data_inizio;
    }

    public Date getData_fine() {
        return data_fine;
    }

    public void setData_fine(Date data_fine) {
        this.data_fine = data_fine;
    }

    public FileEntity getAllegato() {
        return allegato;
    }

    public void setAllegato(FileEntity allegato) {
        this.allegato = allegato;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Stato_enum getStato() {
        return stato;
    }

    public void setStato(Stato_enum stato) {
        this.stato = stato;
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
        if (!(object instanceof Richiesta)) {
            return false;
        }
        Richiesta other = (Richiesta) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Richiesta[ id=" + id + " ]";
    }

}
