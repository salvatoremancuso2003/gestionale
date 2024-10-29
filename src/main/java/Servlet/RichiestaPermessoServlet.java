/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Servlet;

import Entity.FileEntity;
import Entity.InfoTrack;
import Entity.Permesso;
import Entity.Richiesta;
import Entity.TipoDocumento;
import Entity.Utente;
import Enum.Si_no_enum;
import Enum.Stato_enum;
import Enum.Tipo_documento_enum;
import Utils.Utility;
import static Utils.Utility.estraiEccezione;
import static Utils.Utility.logfile;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;

/**
 * Servlet per la gestione delle richieste di permesso.
 */
@MultipartConfig
public class RichiestaPermessoServlet extends HttpServlet {

    private EntityManagerFactory emf;

    @Override
    public void init() {
        emf = Persistence.createEntityManagerFactory("gestionale");
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            Richiesta richiesta = new Richiesta();
            Utente utente = (Utente) request.getSession().getAttribute("user");
            richiesta.setUtente(utente);

            String tipoPermessoStr = request.getParameter("tipo_permesso");
            Permesso permesso = em.createQuery("SELECT p FROM Permesso p WHERE p.codice = :codice", Permesso.class)
                    .setParameter("codice", Long.valueOf(tipoPermessoStr))
                    .getSingleResult();
            richiesta.setTipo_permesso(permesso);

            String dataInizioStr = request.getParameter("data_inizio");
            String dataFineStr = request.getParameter("data_fine");

            boolean isFerie = permesso.getDescrizione().equalsIgnoreCase("Ferie");
            boolean isMalattia = permesso.getDescrizione().equalsIgnoreCase("Malattia");

            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat dateFinalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Date dataInizio;
            Date finalDate;

            try {
                if (isFerie || isMalattia) {
                    dataInizio = dateFormat.parse(dataInizioStr);
                    finalDate = dateFinalFormat.parse(dataFineStr + " 23:59:59");
                } else {
                    dataInizio = dateTimeFormat.parse(dataInizioStr);
                    finalDate = dateTimeFormat.parse(dataFineStr);
                }

                if (richiesta.getTipo_permesso().getOre().equals(Si_no_enum.NO)) {
                    Long count = em.createQuery(
                            "SELECT COUNT(r) FROM Richiesta r WHERE r.utente = :utente AND "
                            + "(:dataInizio BETWEEN r.data_inizio AND r.data_fine OR :dataFine BETWEEN r.data_inizio AND r.data_fine "
                            + "OR r.data_inizio BETWEEN :dataInizio AND :dataFine OR r.data_fine BETWEEN :dataInizio AND :dataFine)", Long.class)
                            .setParameter("utente", utente)
                            .setParameter("dataInizio", new Timestamp(dataInizio.getTime()))
                            .setParameter("dataFine", new Timestamp(finalDate.getTime()))
                            .getSingleResult();

                    if (count > 0) {
                        response.sendRedirect("US_gestionale.jsp?esito=KO2&codice=001");
                        return;
                    }
                } else if (richiesta.getTipo_permesso().getOre().equals(Si_no_enum.SI)) {
                    Long count = em.createQuery(
                            "SELECT COUNT(r) FROM Richiesta r WHERE r.utente = :utente AND r.tipo_permesso.ore = :giornaliero AND "
                            + "(:dataInizio BETWEEN r.data_inizio AND r.data_fine OR :dataFine BETWEEN r.data_inizio AND r.data_fine "
                            + "OR r.data_inizio BETWEEN :dataInizio AND :dataFine OR r.data_fine BETWEEN :dataInizio AND :dataFine)", Long.class)
                            .setParameter("utente", utente)
                            .setParameter("giornaliero", Si_no_enum.NO) 
                            .setParameter("dataInizio", new Timestamp(dataInizio.getTime()))
                            .setParameter("dataFine", new Timestamp(finalDate.getTime()))
                            .getSingleResult();

                    if (count > 0) {
                        response.sendRedirect("US_gestionale.jsp?esito=KO2&codice=001");
                        return;
                    }
                }

                richiesta.setData_inizio(dataInizio);
                richiesta.setData_fine(finalDate);

                richiesta.setNote(request.getParameter("note"));

                Part filePart = request.getPart("allegato");
                if (filePart != null && filePart.getSize() > 0) {
                    String originalFileName = filePart.getSubmittedFileName();
                    int dotIndex = originalFileName.lastIndexOf(".");

                    if (dotIndex != -1) {
                        String baseName = originalFileName.substring(0, dotIndex);
                        String extension = originalFileName.substring(dotIndex);

                        String fileName = originalFileName;
                        String baseDirectory = Utility.config.getString("basePath");
                        String filePath = baseDirectory + fileName;
                        File file = new File(filePath);

                        int counter = 1;
                        while (file.exists()) {
                            fileName = baseName + "_" + counter + extension;
                            filePath = baseDirectory + fileName;
                            file = new File(filePath);
                            counter++;
                        }

                        file.getParentFile().mkdirs();
                        filePart.write(filePath);

                        FileEntity fileEntity = new FileEntity();
                        fileEntity.setFilename(fileName);
                        fileEntity.setFilepath(filePath);
                        fileEntity.setFileSize(filePart.getSize());
                        TipoDocumento tipoDocumento = findTipoDocumento(Tipo_documento_enum.RICHIESTA_PERMESSO);
                        fileEntity.setType(tipoDocumento);
                        fileEntity.setDescription(request.getParameter("note"));
                        fileEntity.setUploadDate(new Timestamp(new Date().getTime()));
                        fileEntity.setUser(utente);

                        if (fileName.endsWith(".pdf")) {
                            try (PDDocument document = PDDocument.load(file); ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                                document.save(baos);
                                fileEntity.setFileContent(baos.toByteArray());
                            }
                        } else if (fileName.endsWith(".docx")) {
                            try (XWPFDocument document = new XWPFDocument(new FileInputStream(file))) {
                                byte[] bytes = new byte[0];
                                for (XWPFPictureData picture : document.getAllPictures()) {
                                    bytes = picture.getData();
                                }
                                fileEntity.setFileContent(bytes);
                            }
                        } else if (fileName.endsWith(".jpeg") || fileName.endsWith(".jpg") || fileName.endsWith(".png")) {
                            try (FileInputStream inputStream = new FileInputStream(file)) {
                                byte[] bytes = new byte[(int) file.length()];
                                inputStream.read(bytes);
                                fileEntity.setFileContent(bytes);
                            }
                        } else if (fileName.endsWith(".tiff") || fileName.endsWith(".tif")) {
                            try (FileInputStream inputStream = new FileInputStream(file); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                                byte[] buffer = new byte[1024];
                                int bytesRead;
                                while ((bytesRead = inputStream.read(buffer)) != -1) {
                                    outputStream.write(buffer, 0, bytesRead);
                                }
                                fileEntity.setFileContent(outputStream.toByteArray());
                            }
                        }
                        em.persist(fileEntity);
                        richiesta.setAllegato(fileEntity);
                    } else {
                        richiesta.setAllegato(null);
                    }
                } else {
                    richiesta.setAllegato(null);
                }

                richiesta.setTimestamp(new Timestamp(new Date().getTime()));
                richiesta.setStato(Stato_enum.IN_ATTESA);

                em.persist(richiesta);
                transaction.commit();

                response.setContentType("text/plain;charset=UTF-8");
                response.sendRedirect("US_gestionale.jsp?esito=OK&codice=001");
                InfoTrack.richiestaTrackUpdate(utente.getNome(), richiesta, utente);
            } catch (ServletException | IOException | ParseException e) {
                logfile.severe(estraiEccezione(e));

            }
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            response.sendRedirect("US_gestionale.jsp?esito=KO&codice=001");
        } finally {
            em.close();
        }
    }

    private TipoDocumento findTipoDocumento(Tipo_documento_enum tipo) {
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;

        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("gestionale");
            entityManager = entityManagerFactory.createEntityManager();

            TipoDocumento tipoDocumento = entityManager.createQuery("SELECT t FROM TipoDocumento t WHERE t.tipo = :tipo", TipoDocumento.class)
                    .setParameter("tipo", tipo)
                    .getSingleResult();
            return tipoDocumento;
        } catch (Exception e) {
            logfile.severe(estraiEccezione(e));
            return null;
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
            if (entityManagerFactory != null) {
                entityManagerFactory.close();
            }
        }

    }

// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
