/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Servlet.Allegato;

import Entity.Excel;
import Entity.FileEntity;
import Entity.InfoTrack;
import Entity.Richiesta;
import Entity.TipoDocumento;
import Entity.Utente;
import Enum.Tipo_documento_enum;
import Utils.Utility;
import static Utils.Utility.estraiEccezione;
import static Utils.Utility.findExcel;
import static Utils.Utility.logfile;
import static com.google.protobuf.JavaFeaturesProto.java;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author Aldo
 */
@MultipartConfig
public class AllegatoServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        boolean isDownload = Boolean.parseBoolean(request.getParameter("isDownload"));
        boolean isUpload = Boolean.parseBoolean(request.getParameter("isUpload"));

        if (isDownload) {
            downloadFile(request, response);
        } else if (isUpload) {
            uploadAllegato(request, response);
        }

    }

    //Metodo download allegato
    protected void downloadFile(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String filename = request.getParameter("filename");
        String id = request.getParameter("id");
        String idRichiesta = request.getParameter("idRichiesta");
        String userIdPresenze = request.getParameter("userId");
        String data = request.getParameter("data");
        SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
        String baseDirectory = Utility.config.getString("basePath");

        if (userIdPresenze != null && data != null) {
            try {
                Excel excel = findExcel(userIdPresenze, data);
                if (isValidFilename(excel.getFileName())) {
                    String filePath = baseDirectory + filename;

                    if (isPathInBaseDirectory(filePath, baseDirectory)) {
                        File downloadFile = new File(filePath);

                        if (downloadFile.exists() && downloadFile.isFile()) {
                            String mimeType = getMimeType(filePath);
                            if (mimeType == null) {
                                mimeType = "application/octet-stream";
                            }
                            response.setContentType(mimeType);

                            String headerKey = "Content-Disposition";
                            String sanitizedFilename = sanitizeFilename(filename);
                            String headerValue = String.format("attachment; filename=\"%s\"", sanitizedFilename);
                            response.setHeader(headerKey, headerValue);

                            try (OutputStream out = response.getOutputStream(); FileInputStream in = new FileInputStream(downloadFile)) {
                                byte[] buffer = new byte[4096];
                                int bytesRead;
                                while ((bytesRead = in.read(buffer)) != -1) {
                                    out.write(buffer, 0, bytesRead);
                                }
                                logfile.info("Il file è stato scaricato con successo!");

                            } catch (Exception e) {
                                logfile.severe(estraiEccezione(e));
                                response.setContentType("text/plain; charset=UTF-8");
                                response.getOutputStream()
                                        .write("Errore durante il download del file.".getBytes(StandardCharsets.UTF_8));
                            }
                        } else {
                            response.setContentType("text/plain");
                            response.getOutputStream()
                                    .write("Il file richiesto non esiste o non è accessibile."
                                            .getBytes(StandardCharsets.UTF_8));
                        }
                    } else {
                        response.setContentType("text/plain");
                        response.getOutputStream()
                                .write("Accesso non autorizzato al percorso del file.".getBytes(StandardCharsets.UTF_8));
                    }
                } else {
                    response.setContentType("text/plain");
                    response.getOutputStream().write("Nome del file non valido.".getBytes(StandardCharsets.UTF_8));
                }
            } catch (Exception e) {
                logfile.severe(estraiEccezione(e));
                response.sendRedirect("AD_gestioneUtente.jsp?esito=KO&codice=010");
            }

        } else {

            Boolean delete = Boolean.valueOf(request.getParameter("delete"));

            if (!delete) {

                if (isValidFilename(filename)) {
                    String filePath = baseDirectory + filename;

                    if (isPathInBaseDirectory(filePath, baseDirectory)) {
                        File downloadFile = new File(filePath);

                        if (downloadFile.exists() && downloadFile.isFile()) {
                            String mimeType = getMimeType(filePath);
                            if (mimeType == null) {
                                mimeType = "application/octet-stream";
                            }
                            response.setContentType(mimeType);

                            String headerKey = "Content-Disposition";
                            String sanitizedFilename = sanitizeFilename(filename);
                            String headerValue = String.format("attachment; filename=\"%s\"", sanitizedFilename);
                            response.setHeader(headerKey, headerValue);

                            try (OutputStream out = response.getOutputStream(); FileInputStream in = new FileInputStream(downloadFile)) {
                                byte[] buffer = new byte[4096];
                                int bytesRead;
                                while ((bytesRead = in.read(buffer)) != -1) {
                                    out.write(buffer, 0, bytesRead);
                                }
                                logfile.info("Il file è stato scaricato con successo!");

                            } catch (Exception e) {
                                logfile.severe(estraiEccezione(e));
                                response.setContentType("text/plain; charset=UTF-8");
                                response.getOutputStream()
                                        .write("Errore durante il download del file.".getBytes(StandardCharsets.UTF_8));
                            }
                        } else {
                            response.setContentType("text/plain");
                            response.getOutputStream()
                                    .write("Il file richiesto non esiste o non è accessibile."
                                            .getBytes(StandardCharsets.UTF_8));
                        }
                    } else {
                        response.setContentType("text/plain");
                        response.getOutputStream()
                                .write("Accesso non autorizzato al percorso del file.".getBytes(StandardCharsets.UTF_8));
                    }
                } else {
                    response.setContentType("text/plain");
                    response.getOutputStream().write("Nome del file non valido.".getBytes(StandardCharsets.UTF_8));
                }
            } else if (delete) {
                try {
                    if (idRichiesta != null) {
                        deleteFileEntity(id, response, idRichiesta);
                        updateRichiesta(idRichiesta);
                    } else {
                        deleteFileEntity2(id, response);
                    }

                    logfile.info("File cancellato con successo!");
                } catch (Exception e) {
                    logfile.severe(estraiEccezione(e));
                    response.sendRedirect("AD_attached.jsp?esito=KO&codice=004");
                }
            }

        }
    }

    private boolean isValidFilename(String filename) {
        return filename != null && !filename.isEmpty() && !filename.contains("..");
    }

    private String getMimeType(String filePath) {
        return "application/octet-stream";
    }

    private String sanitizeFilename(String filename) {
        return filename.replaceAll("[^a-zA-Z0-9._-]", "_");
    }

    private boolean isPathInBaseDirectory(String filePath, String baseDirectory) {
        File baseDir = new File(baseDirectory);
        File file = new File(filePath);
        try {
            return file.getCanonicalPath().startsWith(baseDir.getCanonicalPath());
        } catch (Exception e) {
            logfile.severe(estraiEccezione(e));
            return false;
        }
    }

    private void deleteFileEntity(String idParam, HttpServletResponse response, String idRichiesta) {
        EntityManager em = null;
        EntityManagerFactory emf = null;
        EntityTransaction et = null;

        try {
            emf = Persistence.createEntityManagerFactory("gestionale");
            em = emf.createEntityManager();
            et = em.getTransaction();
            et.begin();
            FileEntity fileEntity = em.find(FileEntity.class, Long.valueOf(idParam));
            em.remove(fileEntity);
            et.commit();

            logfile.info("File cancellato con successo!");
            response.sendRedirect("dettagliPermesso.jsp?richiestaId=" + idRichiesta + "&esito=OK&codice=004");
        } catch (Exception e) {
            if (et != null && et.isActive()) {
                et.rollback();
            }
            logfile.severe(estraiEccezione(e));
            try {
                response.sendRedirect("dettagliPermesso.jsp?richiestaId=" + idRichiesta + "&esito=KO&codice=004");
            } catch (IOException ioException) {
                logfile.severe(estraiEccezione(ioException));
            }
        } finally {
            if (em != null) {
                em.close();
            }
            if (emf != null) {
                emf.close();
            }
        }
    }

    private void deleteFileEntity2(String idParam, HttpServletResponse response) {
        EntityManager em = null;
        EntityManagerFactory emf = null;
        EntityTransaction et = null;

        try {
            emf = Persistence.createEntityManagerFactory("gestionale");
            em = emf.createEntityManager();
            et = em.getTransaction();
            et.begin();
            FileEntity fileEntity = em.find(FileEntity.class, Long.valueOf(idParam));
            em.remove(fileEntity);
            et.commit();

            logfile.info("File cancellato con successo!");
            response.sendRedirect("AD_attached.jsp?esito=OK&codice=004");
        } catch (Exception e) {
            if (et != null && et.isActive()) {
                et.rollback();
            }
            logfile.severe(estraiEccezione(e));
            try {
                response.sendRedirect("AD_attached.jsp&esito=KO&codice=004");
            } catch (IOException ioException) {
                logfile.severe(estraiEccezione(ioException));
            }
        } finally {
            if (em != null) {
                em.close();
            }
            if (emf != null) {
                emf.close();
            }
        }
    }

    private void updateRichiesta(String idRichiesta) {
        EntityManager em = null;
        EntityManagerFactory emf = null;
        EntityTransaction et = null;

        try {
            emf = Persistence.createEntityManagerFactory("gestionale");
            em = emf.createEntityManager();
            et = em.getTransaction();
            et.begin();
            Richiesta richiesta = em.find(Richiesta.class, Long.valueOf(idRichiesta));
            richiesta.setAllegato(null);
            em.merge(richiesta);
            et.commit();
        } catch (Exception e) {
            if (et != null && et.isActive()) {
                et.rollback();

            }
            logfile.severe(estraiEccezione(e));
        } finally {
            if (em != null) {
                em.close();
            }
            if (emf != null) {
                emf.close();
            }
        }
    }

    // private static final String UPLOAD_DIR = "uploads";
    // Metodo Carica Allegato
    protected void uploadAllegato(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Boolean edit = Boolean.valueOf(request.getParameter("edit"));
        String id = request.getParameter("id");

        if (!edit) {
            try {
                Part filePart = request.getPart("file");
                String originalFileName = filePart.getSubmittedFileName();
                String baseName = originalFileName.substring(0, originalFileName.lastIndexOf("."));
                String extension = originalFileName.substring(originalFileName.lastIndexOf("."));

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

                String userIdParam = request.getParameter("utenteId");
                Long userId = Long.parseLong(userIdParam);
                Utente utente = Utility.findUserById(userId);

                String note = request.getParameter("note");

                String tipo = request.getParameter("tipoDocumento");

                Tipo_documento_enum tipoEnum;
                try {
                    tipoEnum = Tipo_documento_enum.valueOf(tipo.toUpperCase());
                } catch (IllegalArgumentException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Tipo documento non valido.");
                    return;
                }

                TipoDocumento tipoDocumento = findTipoDocumento(tipoEnum);

                FileEntity fileEntity = new FileEntity();
                fileEntity.setFilename(fileName);
                String filePathCryptato = BCrypt.hashpw(filePath, BCrypt.gensalt());
                fileEntity.setFilepath(filePathCryptato);
                fileEntity.setFileSize(filePart.getSize());
                fileEntity.setUploadDate(new Timestamp(new Date().getTime()));
                fileEntity.setUser(utente);
                fileEntity.setDescription(note);
                fileEntity.setType(tipoDocumento);
                fileEntity.setStatus(1);

                if (fileName.endsWith(".pdf")) {
                    PDDocument document = PDDocument.load(file);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    document.save(baos);
                    document.close();
                } else if (fileName.endsWith(".docx")) {
                    try (XWPFDocument document = new XWPFDocument(new FileInputStream(file))) {
                        byte[] bytes = new byte[0];

                        for (XWPFPictureData picture : document.getAllPictures()) {
                            bytes = picture.getData();
                        }
                        document.close();

                        fileEntity.setFileContent(bytes);
                    }
                } else if (fileName.endsWith(".jpeg") || fileName.endsWith(".jpg") || fileName.endsWith(".png")) {
                    try (FileInputStream inputStream = new FileInputStream(file)) {
                        byte[] bytes = new byte[(int) file.length()];
                        inputStream.read(bytes);
                        inputStream.close();

                        fileEntity.setFileContent(bytes);
                    }
                } else if (fileName.endsWith(".tiff") || fileName.endsWith(".tif")) {
                    try (FileInputStream inputStream = new FileInputStream(file)) {
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }
                        byte[] bytes = outputStream.toByteArray();
                        inputStream.close();
                        outputStream.close();

                        fileEntity.setFileContent(bytes);
                    }
                }

                EntityManagerFactory emf = Persistence.createEntityManagerFactory("gestionale");
                EntityManager em = emf.createEntityManager();
                em.getTransaction().begin();
                em.persist(fileEntity);
                // InfoTrack.newFileTrack(request.getAttribute("username").toString(),
                // fileEntity.getEvent(), fileEntity);
                em.getTransaction().commit();
                em.close();
                emf.close();

                response.setContentType("text/plain;charset=UTF-8");
                PrintWriter out = response.getWriter();
                response.sendRedirect("AD_attached.jsp?esito=OK&codice=005");
                InfoTrack.fileTrackUpload(utente.getNome(), fileEntity, utente);

            } catch (Exception e) {
                logfile.severe(estraiEccezione(e));
                response.setContentType("text/plain;charset=UTF-8");
                PrintWriter out = response.getWriter();
                response.sendRedirect("AD_attached.jsp?esito=KO&codice=005");
                out.print("File non supportato o dimensioni del file da ridurre ");
            }
        } else {
            FileEntity fileEntity = Utility.findFileEntityById(Long.valueOf(id));
            Part filePart = request.getPart("file");
            String originalFileName = filePart.getSubmittedFileName();
            String baseName = originalFileName.substring(0, originalFileName.lastIndexOf("."));
            String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
            String fancybox = request.getParameter("fancybox");

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
            String filePathCryptato = BCrypt.hashpw(filePath, BCrypt.gensalt());
            fileEntity.setFilepath(filePathCryptato);
            fileEntity.setFilename(fileName);
            fileEntity.setFileSize(filePart.getSize());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDate = sdf.format(new Date());
            Date parsedDate = null;
            try {
                parsedDate = sdf.parse(formattedDate);
            } catch (ParseException ex) {
                Logger.getLogger(AllegatoServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            Timestamp timestamp = new Timestamp(parsedDate.getTime());

            fileEntity.setUploadDate(timestamp);

            if (fileName.endsWith(".pdf")) {
                PDDocument document = PDDocument.load(file);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                document.save(baos);
                document.close();
            } else if (fileName.endsWith(".docx")) {
                XWPFDocument document = new XWPFDocument(new FileInputStream(file));
                byte[] bytes = new byte[0];

                for (XWPFPictureData picture : document.getAllPictures()) {
                    bytes = picture.getData();
                }
                document.close();

                fileEntity.setFileContent(bytes);
            } else if (fileName.endsWith(".jpeg") || fileName.endsWith(".jpg") || fileName.endsWith(".png")) {
                FileInputStream inputStream = new FileInputStream(file);
                byte[] bytes = new byte[(int) file.length()];
                inputStream.read(bytes);
                inputStream.close();

                fileEntity.setFileContent(bytes);
            } else if (fileName.endsWith(".tiff") || fileName.endsWith(".tif")) {
                FileInputStream inputStream = new FileInputStream(file);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                byte[] bytes = outputStream.toByteArray();
                inputStream.close();
                outputStream.close();

                fileEntity.setFileContent(bytes);
            }

            EntityManagerFactory emf = Persistence.createEntityManagerFactory("gestionale");
            EntityManager em = emf.createEntityManager();
            em.getTransaction().begin();
            em.merge(fileEntity);
            // em.flush();
            em.getTransaction().commit();
            em.close();
            emf.close();

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"success\": true, \"fancybox\": \"" + fancybox + "\"}");

        }
    }

    private TipoDocumento findTipoDocumento(Tipo_documento_enum tipo) {
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;

        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("gestionale");
            entityManager = entityManagerFactory.createEntityManager();

            TipoDocumento tipoDocumento = entityManager
                    .createQuery("SELECT t FROM TipoDocumento t WHERE t.tipo = :tipo", TipoDocumento.class)
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
