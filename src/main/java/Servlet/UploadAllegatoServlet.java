package Servlet;

import Entity.FileEntity;
import Entity.InfoTrack;
import Entity.TipoDocumento;
import Entity.Utente;
import Enum.Tipo_documento_enum;
import Utils.Utility;
import static Utils.Utility.estraiEccezione;
import static Utils.Utility.logfile;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Date;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import org.joda.time.DateTime;

@MultipartConfig
public class UploadAllegatoServlet extends HttpServlet {

    // private static final String UPLOAD_DIR = "uploads";
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
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
                fileEntity.setFilepath(filePath);
                fileEntity.setFileSize(filePart.getSize());
                fileEntity.setUploadDate(new Timestamp(new Date().getTime()));
                fileEntity.setUser(utente);
                fileEntity.setDescription(note);
                fileEntity.setType(tipoDocumento);

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
            fileEntity.setFilepath(filePath);
            fileEntity.setFilename(fileName);
            fileEntity.setFileSize(filePart.getSize());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDate = sdf.format(new Date());
            Date parsedDate = null;
            try {
                parsedDate = sdf.parse(formattedDate);
            } catch (ParseException ex) {
                Logger.getLogger(UploadAllegatoServlet.class.getName()).log(Level.SEVERE, null, ex);
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

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the
    // + sign on the left to edit the code.">
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
