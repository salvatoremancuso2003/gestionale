package Entity;

import java.io.Serializable;
import java.sql.Timestamp;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "presenza")
public class Presenza implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Utente utente;

    private Timestamp entrata;

    private Timestamp uscita;

    @ManyToOne
    private TipoPresenza tipo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public Timestamp getEntrata() {
        return entrata;
    }

    public void setEntrata(Timestamp entrata) {
        this.entrata = entrata;
    }

    public Timestamp getUscita() {
        return uscita;
    }

    public void setUscita(Timestamp uscita) {
        this.uscita = uscita;
    }

    public TipoPresenza getTipo() {
        return tipo;
    }

    public void setTipo(TipoPresenza tipo) {
        this.tipo = tipo;
    }

}
