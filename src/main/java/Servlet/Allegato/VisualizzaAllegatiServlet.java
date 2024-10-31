package Servlet.Allegato;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import Entity.FileEntity;
import Entity.Utente;
import Utils.Utility;
import static Utils.Utility.estraiEccezione;
import static Utils.Utility.logfile;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.concurrent.atomic.AtomicInteger;

public class VisualizzaAllegatiServlet extends HttpServlet {

    private static AtomicInteger sEchoCounter = new AtomicInteger(0);

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        EntityManagerFactory emf = null;
        EntityManager em = null;
        String stato = request.getParameter("stato");

        boolean action = Boolean.valueOf(request.getParameter("filesWithDipendenti"));

        if (action) {
            visualizzaFilesConDipendenti(request, response);
        } else {
            visualizzaFilesUtente(request, response);
        }

    }

    private void visualizzaFilesUtente(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        EntityManagerFactory emf = null;
        EntityManager em = null;

        try {
            emf = Persistence.createEntityManagerFactory("gestionale");
            em = emf.createEntityManager();

            String dataInizioFiltro = request.getParameter("dataInizioFiltro");
            String dataFineFiltro = request.getParameter("dataFineFiltro");

            HttpSession session = request.getSession();
            String userIdParam = session.getAttribute("userId").toString();
            Long userId = Long.parseLong(userIdParam);
            Utente utente = Utility.findUserById(userId);
            if (utente == null) {
                response.sendRedirect("index.jsp");
                return;

            } else {

                StringBuilder queryBuilder = new StringBuilder("SELECT f FROM FileEntity f WHERE 1=1 and f.status= 1");

                if (dataInizioFiltro != null && !dataInizioFiltro.isEmpty()) {
                    queryBuilder.append(" AND f.uploadDate >= :dataInizio");
                }
                if (dataFineFiltro != null && !dataFineFiltro.isEmpty()) {
                    queryBuilder.append(" AND f.uploadDate <= :dataFine");
                }
                if (utente.getId() != null && !utente.getId().equals("Qualsiasi")) {
                    queryBuilder.append(" AND f.utente.id = :utenteId");
                }

                TypedQuery<FileEntity> query = em.createQuery(queryBuilder.toString(), FileEntity.class);

                if (dataInizioFiltro != null && !dataInizioFiltro.isEmpty()) {
                    query.setParameter("dataInizio", java.sql.Date.valueOf(dataInizioFiltro));
                }
                if (dataFineFiltro != null && !dataFineFiltro.isEmpty()) {
                    String dataFineConOrario = dataFineFiltro + " 23:59:59";
                    query.setParameter("dataFine", java.sql.Timestamp.valueOf(dataFineConOrario));
                }

                if (utente.getId() != null && !utente.getId().equals("Qualsiasi")) {
                    query.setParameter("utenteId", utente.getId());
                }

                List<FileEntity> files = query.getResultList();

                JsonObject jsonResponse = new JsonObject();
                jsonResponse.addProperty("iTotalRecords", files.size());
                jsonResponse.addProperty("iTotalDisplayRecords", files.size());
                int sEchoValue = sEchoCounter.incrementAndGet();
                jsonResponse.addProperty("sEcho", sEchoValue);

                JsonArray data = new JsonArray();
                for (FileEntity e : files) {
                    JsonObject filesData = new JsonObject();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    String uploadDate = (e.getUploadDate() != null) ? sdf.format(e.getUploadDate()) : "Non disponibile";
                    filesData.addProperty("id", e.getId());
                    filesData.addProperty("filename", e.getFilename());
                    filesData.addProperty("uploadDate", uploadDate);
                    filesData.addProperty("note", e.getDescription());

                    String scarica = "<div class='container-fluid'>"
                            + "<div class='row'>"
                            + "<div class='d-flex'>"
                            + "<div class='col-auto'>"
                            + "<form action='AllegatoServlet' method='POST' target='_blank'>"
                            + "<input type='hidden' name='filename' value='" + e.getFilename() + "' />"
                            + "<input type='hidden' name='isDownload' value='true' />"
                            + "<button class='btn Smartoop-btn-standard'>Scarica File</button>"
                            + "</form>"
                            + "</div>";

                    String editForm = "";

                    if (e.getUser().getId().equals(userId)) {
                        editForm = "<div class='col-auto'>"
                                + " <button class='btn Smartoop-btn-standard' style='min-width: 100px; margin-left: 5px;' data-fancybox data-src=\"edit_file.jsp?fancybox=false&id=" + e.getId() + "\" data-type=\"iframe\" data-options='{\"iframe\" : {\"css\": {\"width\": \"90%\", \"height\": \"90%\"}}}'>Modifica File</button>" + "</div>";
                        scarica += editForm + "</div></div>";
                        filesData.addProperty("gestisci", scarica);

                    } else {
                        filesData.addProperty("gestisci", scarica);
                    }

                    data.add(filesData);
                }

                jsonResponse.add("aaData", data);

                response.setContentType("application/json");
                try (PrintWriter out = response.getWriter()) {
                    out.print(jsonResponse.toString());
                }
            }
        } catch (Exception e) {
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

    private void visualizzaFilesConDipendenti(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManagerFactory emf = null;
        EntityManager em = null;

        try {
            emf = Persistence.createEntityManagerFactory("gestionale");
            em = emf.createEntityManager();
            HttpSession session = request.getSession();
            String userIdParam = session.getAttribute("userId").toString();
            Long userId = Long.parseLong(userIdParam);

            String dataInizio = request.getParameter("dataInizio");
            String dataFine = request.getParameter("dataFine");
            String utenteId = request.getParameter("utente");
            String tipoDocumento = request.getParameter("tipoDocumentoFiltro");
            StringBuilder queryBuilder = new StringBuilder("SELECT f FROM FileEntity f WHERE 1=1 and f.status= 1");

            if (dataInizio != null && !dataInizio.isEmpty()) {
                queryBuilder.append(" AND f.uploadDate >= :dataInizio");
            }
            if (dataFine != null && !dataFine.isEmpty()) {
                queryBuilder.append(" AND f.uploadDate <= :dataFine");
            }
            if (utenteId != null && !utenteId.equals("Qualsiasi")) {
                queryBuilder.append(" AND f.utente.id = :utenteId");
            }
            if (tipoDocumento != null && !tipoDocumento.equals("Qualsiasi")) {
                queryBuilder.append(" AND f.type.id = :tipoDocumentoFiltro");
            }

            TypedQuery<FileEntity> query = em.createQuery(queryBuilder.toString(), FileEntity.class);

            if (dataInizio != null && !dataInizio.isEmpty()) {
                query.setParameter("dataInizio", java.sql.Date.valueOf(dataInizio));
            }
            if (dataFine != null && !dataFine.isEmpty()) {
                String dataFineConOrario = dataFine + " 23:59:59";
                query.setParameter("dataFine", java.sql.Timestamp.valueOf(dataFineConOrario));
            }
            if (utenteId != null && !utenteId.equals("Qualsiasi")) {
                query.setParameter("utenteId", Long.valueOf(utenteId));
            }

            if (tipoDocumento != null && !tipoDocumento.equals("Qualsiasi")) {
                query.setParameter("tipoDocumentoFiltro", Long.valueOf(tipoDocumento));
            }

            List<FileEntity> files = query.getResultList();

            JsonObject jsonResponse = new JsonObject();
            jsonResponse.addProperty("iTotalRecords", files.size());
            jsonResponse.addProperty("iTotalDisplayRecords", files.size());
            int sEchoValue = sEchoCounter.incrementAndGet();
            jsonResponse.addProperty("sEcho", sEchoValue);

            JsonArray data = new JsonArray();
            for (FileEntity e : files) {
                JsonObject filesData = new JsonObject();
                filesData.addProperty("fileName", e.getFilename());
                filesData.addProperty("filePath", e.getFilepath());
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                String uploadDate = (e.getUploadDate() != null) ? sdf.format(e.getUploadDate()) : "Non disponibile";
                if (e.getType() != null) {
                    filesData.addProperty("tipoDocumento", e.getType().getTipo().toString());
                } else {
                    filesData.addProperty("tipoDocumento", "Non disponibile");
                }
                filesData.addProperty("uploadDate", uploadDate);
                filesData.addProperty("note", e.getDescription());

                if (e.getUser() != null) {
                    filesData.addProperty("dipendente", e.getUserFullName());
                } else {
                    filesData.addProperty("dipendente", "Non disponibile");
                }

                String scarica
                        = "<div class='container-fluid'>"
                        + "<div class='row'>"
                        + "<div class='d-flex'>"
                        + "<div class='col-auto'>"
                        + "<form action='AllegatoServlet' method='POST' target='_blank'>"
                        + "<input type = 'hidden' name='filename' value='" + e.getFilename() + "' />"
                        + "<input type='hidden' name='isDownload' value='true' />"
                        + "<button class='btn Smartoop-btn-standard' style='min-width: 100px;'>Scarica File</button>"
                        + "</form>"
                        + "</div>";

                //String editForm = "";
                String deleteForm = "";

//                editForm = "<form action='DownloadFile' method='POST'>"
//                        + "<input type = 'hidden' name='filename' value='" + e.getFilename() + "' />"
//                        + "<input type = 'hidden' name='id' value='" + e.getId() + "' />"
//                        + "<input type = 'hidden' name='edit' value='" + true + "' />"
//                        + "<hr>"
//                        + "<button class='btn btn-danger' style='padding: 10px' >Modifica File</button>"
//                        + "</form>";
                deleteForm
                        = "<div class='col-auto'>"
                        + "<form action='AllegatoServlet' method='POST'>"
                        + "<input type = 'hidden' name='filename' value='" + e.getFilename() + "' />"
                        + "<input type = 'hidden' name='id' value='" + e.getId() + "' />"
                        + "<input type = 'hidden' name='delete' value='" + true + "' />"
                        + "<input type = 'hidden' name='isDownload' value='" + true + "' />"
                        + "<button class='btn Smartoop-btn-standard' style='min-width: 100px; margin-left: 5px;' >Elimina File</button>"
                        + "</form>"
                        + "</div>";
                //filesData.addProperty("gestisci", scarica + " " + editForm + " " + deleteForm);
                filesData.addProperty("gestisci", scarica + " " + deleteForm);

                data.add(filesData);
            }

            jsonResponse.add("aaData", data);

            response.setContentType("application/json");
            try (PrintWriter out = response.getWriter()) {
                out.print(jsonResponse.toString());
            }
        } catch (Exception e) {
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

    public static final String ITOTALRECORDS = "iTotalRecords";
    public static final String ITOTALDISPLAY = "iTotalDisplayRecords";
    public static final String SECHO = "sEcho";
    public static final String SCOLUMS = "sColumns";
    public static final String APPJSON = "application/json";
    public static final String CONTENTTYPE = "Content-Type";
    public static final String AADATA = "aaData";

    /**
     * Recupera la lista degli allegati dell'utente.
     *
     * @param utente l'utente corrente.
     * @return lista di allegati.
     */
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
