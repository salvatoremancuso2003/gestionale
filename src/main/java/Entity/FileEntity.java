/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

import java.io.Serializable;
import java.sql.Timestamp;
import jakarta.persistence.*;

@Entity
@Table(name = "files")
@NamedQueries({
    @NamedQuery(
            name = "FileEntity.findByFilename",
            query = "SELECT f FROM FileEntity f WHERE f.filename = :filename"
    ),
    @NamedQuery(
            name = "FileEntity.findAll",
            query = "SELECT f FROM FileEntity f"
    ),
    @NamedQuery(
            name = "FileEntity.findByUser",
            query = "SELECT f FROM FileEntity f WHERE f.utente.id= :userId"
    )

})
public class FileEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "filename")
    private String filename;

    @Column(name = "filepath")
    private String filepath;

    @Column(name = "outputfilepath")
    private String outputFilepath;

    @Column(name = "description")
    private String description;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "uploadDate", nullable = false, updatable = true, insertable = true, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp uploadDate;

    @Lob
    @Column(name = "file_content", columnDefinition = "LONGBLOB")
    private byte[] fileContent;

    //private int status;
    @ManyToOne
    private Utente utente;

    @Column(name = "expiration_date")
    private Timestamp expiration_date;

    @Column(name = "json", columnDefinition = "json")
    private String json;

    @ManyToOne
    private TipoDocumento type;

    public TipoDocumento getType() {
        return type;
    }

    public void setType(TipoDocumento type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Timestamp getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Timestamp uploadDate) {
        this.uploadDate = uploadDate;
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

//    public int getStatus() {
//        return status;
//    }
//
//    public void setStatus(int status) {
//        this.status = status;
//    }
    public Utente getUser() {
        return utente;
    }

    public String getUserFullName() {
        return utente.getNome() + " " + utente.getCognome();
    }

    public void setUser(Utente user) {
        this.utente = user;
    }

    public Timestamp getExpiration_date() {
        return expiration_date;
    }

    public void setExpiration_date(Timestamp expiration_date) {
        this.expiration_date = expiration_date;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getOutputFilepath() {
        return outputFilepath;
    }

    public void setOutputFilepath(String outputFilepath) {
        this.outputFilepath = outputFilepath;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

}
