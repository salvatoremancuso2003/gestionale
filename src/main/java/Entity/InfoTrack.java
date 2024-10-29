/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

/**
 *
 * @author Aldo
 */
import java.io.Serializable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Date;
import jakarta.persistence.Column;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Persistence;
import jakarta.persistence.Temporal;

@Entity
@NamedQuery(name = "InfoTrack.findAll", query = "SELECT t FROM InfoTrack t")
public class InfoTrack implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String infoName;

    @Lob()
    @Column(length = 256)
    private String descrizione;

    @Column(name = "dataOraTracciamento")
    @Temporal(jakarta.persistence.TemporalType.TIMESTAMP)
    private Date dataOraTracciamento;

    @ManyToOne
    private Richiesta richiesta;

    @ManyToOne
    private FileEntity fileEntity;

    @ManyToOne
    private Presenza presenza;

    @ManyToOne
    private Notifica notifica;
    
    @ManyToOne
    private Utente utente;
    
    
    
       public InfoTrack() {
    }

    public static void loginTrack(String username,Utente utente) {

        try {

            EntityManagerFactory emf = Persistence.createEntityManagerFactory("gestionale");
            EntityManager em = emf.createEntityManager();

            InfoTrack t = new InfoTrack(username, "LOGIN UTENTE", new Date(), utente);

            em.getTransaction().begin();
            em.persist(t);
            em.getTransaction().commit();
            em.close();

        } catch (Exception e) {
            System.out.println(e);

        }

    }

    public static void richiestaTrackUpdate(String infoName,Richiesta richiesta, Utente utente) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("gestionale");
        EntityManager em = emf.createEntityManager();

        InfoTrack p = new InfoTrack(infoName, "HA CREATO UNA RICHIESTA", new Date(), richiesta,utente);

        em.getTransaction().begin();
        em.persist(p);
        em.getTransaction().commit();
        em.close();
    }
    
    public static void richiestaTrackUpdateSecond(String infoName,Richiesta richiesta, Utente utente) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("gestionale");
        EntityManager em = emf.createEntityManager();

        InfoTrack p = new InfoTrack(infoName, "HA CREATO UNA RICHIESTA SENZA ORE/FERIE DISPONIBILI", new Date(), richiesta,utente);

        em.getTransaction().begin();
        em.persist(p);
        em.getTransaction().commit();
        em.close();
    }

   

    public static void notificaTrack(String infoName, Notifica notifica, Utente utente) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("gestionale");
        EntityManager em = emf.createEntityManager();

       InfoTrack n = new InfoTrack(infoName, "HA CREATO UNA NUOVA NOTIFICA",new Date(),notifica, utente);

        em.getTransaction().begin();
        em.persist(n);
        em.getTransaction().commit();
        em.close();
    }

   

    public static void presenzaTrackCreated(String infoName, Presenza presenza, Utente utente) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("gestionale");
        EntityManager em = emf.createEntityManager();

        InfoTrack p = new InfoTrack(infoName, "HA TIMBRATO UN INGRESSO", new Date(), presenza, utente);

        em.getTransaction().begin();
        em.persist(p);
        em.getTransaction().commit();
        em.close();
    }
    
    public static void insertNewUser(String infoName,Utente utente) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("gestionale");
        EntityManager em = emf.createEntityManager();

        InfoTrack p = new InfoTrack(infoName, "HA CREATO UN NUOVO UTENTE", new Date(),utente);

        em.getTransaction().begin();
        em.persist(p);
        em.getTransaction().commit();
        em.close();
    }
    
    public static void presenzaTrackCreatedSecond(String infoName, Presenza presenza, Utente utente) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("gestionale");
        EntityManager em = emf.createEntityManager();

        InfoTrack p = new InfoTrack(infoName, "HA TIMBRATO UN USCITA", new Date(), presenza, utente);

        em.getTransaction().begin();
        em.persist(p);
        em.getTransaction().commit();
        em.close();
    }

   
    public static void fileTrackUpload(String infoName, FileEntity fileUploaded, Utente utente) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("gestionale");
        EntityManager em = emf.createEntityManager();

        InfoTrack f = new InfoTrack(infoName, "HA GENERATO UN NUOVO PDF", new Date(), fileUploaded, utente);

        em.getTransaction().begin();
        em.persist(f);
        em.getTransaction().commit();
        em.close();
    }

    public static void downloadTrackUpload(String infoName, Utente utente) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("gestionale");
        EntityManager em = emf.createEntityManager();

        InfoTrack f = new InfoTrack(infoName, "DOWNLOAD FILE", new Date(), utente);

        em.getTransaction().begin();
        em.persist(f);
        em.getTransaction().commit();
        em.close();
    }

    public static void logoutTrack(String infoUserName,Utente utente) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("gestionale");
        EntityManager em = emf.createEntityManager();

        InfoTrack l = new InfoTrack(infoUserName, "LOGOUT UTENTE", new Date(), utente);

        em.getTransaction().begin();
        em.persist(l);
        em.getTransaction().commit();
        em.close();
    }
 

    public InfoTrack(String infoName, String descrizione, Date dataOraTracciamento, Utente utente) {
        this.infoName = infoName;
        this.descrizione = descrizione;
        this.dataOraTracciamento = dataOraTracciamento;
        this.utente = utente;
    }

    public InfoTrack(String infoName, String descrizione, Date dataOraTracciamento, Presenza presenza, Utente utente) {
        this.infoName = infoName;
        this.descrizione = descrizione;
        this.dataOraTracciamento = dataOraTracciamento;
        this.presenza = presenza;
        this.utente = utente;
    }

    public InfoTrack(String infoName, String descrizione, Date dataOraTracciamento, FileEntity fileEntity,Utente utente) {
        this.infoName = infoName;
        this.descrizione = descrizione;
        this.dataOraTracciamento = dataOraTracciamento;
        this.fileEntity = fileEntity;
        this.utente = utente;
    }

    public InfoTrack(String infoName, String descrizione, String info2, Date dataOraTracciamento, Presenza presenza,Utente utente) {
        this.infoName = infoName;
        this.descrizione = descrizione;
        this.dataOraTracciamento = dataOraTracciamento;
        this.presenza = presenza;
        this.utente = utente;
    }

    public InfoTrack(String infoName, String descrizione, Date dataOraTracciamento, Richiesta richiesta,Utente utente) {
        this.infoName = infoName;
        this.descrizione = descrizione;
        this.dataOraTracciamento = dataOraTracciamento;
        this.richiesta = richiesta;
        this.utente = utente;
    }

    public InfoTrack(String infoName, String descrizione, Date dataOraTracciamento, Notifica notifica,Utente utente) {
        this.infoName = infoName;
        this.descrizione = descrizione;
        this.dataOraTracciamento = dataOraTracciamento;
        this.notifica = notifica;
        this.utente = utente;
    }

    // Getter e Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInfo() {
        return infoName;
    }

    public void setInfo(String infoName) {
        this.infoName = infoName;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Date getDataOraTracciamento() {
        return dataOraTracciamento;
    }

    public void setDataOraTracciamento(Date dataOraTracciamento) {
        this.dataOraTracciamento = dataOraTracciamento;
    }

    public Presenza getPractice() {
        return presenza;
    }

    public void setPractice(Presenza presenza) {
        this.presenza = presenza;
    }

    public FileEntity getFileEntity() {
        return fileEntity;
    }

    public void setFileEntity(FileEntity fileEntity) {
        this.fileEntity = fileEntity;
    }

    public Richiesta getAttachments() {
        return richiesta;
    }

    public void setAttachments(Richiesta richiesta) {
        this.richiesta = richiesta;
    }

    public Notifica getNotifica() {
        return notifica;
    }

    public void setNotifica(Notifica notifica) {
        this.notifica = notifica;
    }
    
    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

}
