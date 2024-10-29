/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

import Enum.Si_no_enum;
import java.io.Serializable;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 *
 * @author Salvatore
 */
@Entity
public class Permesso implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codice;

    private String descrizione;

    @Enumerated(EnumType.STRING)
    private Si_no_enum ore;

    @Enumerated(EnumType.STRING)
    private Si_no_enum ferie;

    @Enumerated(EnumType.STRING)
    private Si_no_enum rol;

    public Long getCodice() {
        return codice;
    }

    public void setCodice(Long codice) {
        this.codice = codice;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Si_no_enum getOre() {
        return ore;
    }

    public void setOre(Si_no_enum ore) {
        this.ore = ore;
    }

    public Si_no_enum getFerie() {
        return ferie;
    }

    public void setFerie(Si_no_enum ferie) {
        this.ferie = ferie;
    }

    public Si_no_enum getRol() {
        return rol;
    }

    public void setRol(Si_no_enum rol) {
        this.rol = rol;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codice != null ? codice.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Permesso)) {
            return false;
        }
        Permesso other = (Permesso) object;
        if ((this.codice == null && other.codice != null) || (this.codice != null && !this.codice.equals(other.codice))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Permesso[ id=" + codice + " ]";
    }

}
