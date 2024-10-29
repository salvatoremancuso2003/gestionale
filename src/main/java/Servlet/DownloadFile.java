/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Servlet;

import Entity.FileEntity;
import Entity.Richiesta;
import Utils.Utility;
import static Utils.Utility.estraiEccezione;
import static Utils.Utility.logfile;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author Salvatore
 */
public class DownloadFile extends HttpServlet {

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
        String filename = request.getParameter("filename");
        String id = request.getParameter("id");
        String idRichiesta = request.getParameter("idRichiesta");

        String baseDirectory = Utility.config.getString("basePath");
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
