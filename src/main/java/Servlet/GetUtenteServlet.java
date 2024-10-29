package Servlet;

import Entity.Utente;
import static Utils.Utility.estraiEccezione;
import static Utils.Utility.logfile;
import java.text.SimpleDateFormat;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.persistence.TypedQuery;

public class GetUtenteServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private EntityManagerFactory emf;

    @Override
    public void init() throws ServletException {
        emf = Persistence.createEntityManagerFactory("gestionale");
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = emf.createEntityManager();
        try {
            String utenteIdStr = request.getParameter("utente");

            StringBuilder queryString = new StringBuilder("SELECT u FROM Utente u WHERE u.ruolo.id = 2");

            TypedQuery<Utente> query = em.createQuery(queryString.toString(), Utente.class);

            if (utenteIdStr != null && !utenteIdStr.equals("Qualsiasi")) {
                Long utenteId = Long.parseLong(utenteIdStr);
                query.setParameter("utenteId", utenteId);
            }

            List<Utente> utenti = query.getResultList();

            JsonArray dataArray = new JsonArray();

            for (Utente utente : utenti) {
                JsonObject rc = new JsonObject();
                rc.addProperty("id", utente.getId());
                if (utente.getNome() != null) {
                    rc.addProperty("nome", utente.getNome());
                } else {
                    rc.addProperty("nome", "Non disponibile");
                }
                if (utente.getCognome() != null) {
                    rc.addProperty("cognome", utente.getCognome());
                } else {
                    rc.addProperty("cognome", "Non disponibile");
                }
                if (utente.getEmail() != null) {
                    rc.addProperty("email", utente.getEmail());
                } else {
                    rc.addProperty("email", "Non disponibile");
                }
                if (utente.getNumero_di_telefono() != null) {
                    rc.addProperty("numero", utente.getNumero_di_telefono());
                } else {
                    rc.addProperty("numero", "Non disponibile");
                }
                rc.addProperty("ferie", utente.getFerie_disponibili());
                rc.addProperty("ore", utente.getOre_disponibili());
                rc.addProperty("ore_contratto", utente.getOre_contratto());

                String actionButton;
                String estraiPresenze = "<div class='container'>"
                        + "<div class='d-flex'>"
                        + "<button class='btn Smartoop-btn-standard' style='min-width: 100px; margin-left: 5px; color:white;' "
                        + " onclick='estraiPresenze(" + utente.getId() + ", \"" + utente.getNome() + " " + utente.getCognome() + "\")'>"
                        + "Estrai excel</button>"
                        + "</div>"
                        + "</div>";

                if (utente.getStatus() == 0) {
                    rc.addProperty("stato", "DISATTIVATO");

                    actionButton = "<div class='container'>"
                            + "<div class='d-flex'>"
                            + "<button class='btn Smartoop-btn-standard' style='min-width: 100px; margin-left: 5px; color:white;' "
                            + " onclick='riabilitaUtente(" + utente.getId() + ", \"" + utente.getNome() + " " + utente.getCognome() + "\")'>"
                            + "Riabilita utente</button>"
                            + "</div>"
                            + "</div>";
                } else {
                    if (utente.getStatus() == 1) {
                        rc.addProperty("stato", "ATTIVO");
                    } else if (utente.getStatus() == 2) {
                        rc.addProperty("stato", "IN ATTESA");
                    }

                    actionButton = "<div class='container'>"
                            + "<div class='d-flex'>"
                            + "<button class='btn Smartoop-btn-standard' style='min-width: 100px; margin-left: 5px; color:white;' "
                            + " data-fancybox "
                            + " href='edit_user.jsp?userId=" + utente.getId() + "'"
                            + " data-type='iframe' "
                            + " data-options='{\"iframe\" : {\"css\": {\"width\": \"100%\", \"height\": \"100%\"}}}'>"
                            + "Modifica utente</button>"
                            + "</div>"
                            + "</div>";
                }

                String combinedActions = "<div class='container'>"
                        + "<div class='d-flex justify-content-start'>"
                        + actionButton
                        + estraiPresenze
                        + "</div></div>";

                rc.addProperty("Gestisci", combinedActions);
                dataArray.add(rc);
            }
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(dataArray.toString());
            out.flush();
        } catch (Exception e) {
            logfile.severe(estraiEccezione(e));
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while processing the request");
        } finally {
            em.close();
        }
    }

    public static final String ITOTALRECORDS = "iTotalRecords";
    public static final String ITOTALDISPLAY = "iTotalDisplayRecords";
    public static final String SECHO = "sEcho";
    public static final String SCOLUMS = "sColumns";
    public static final String APPJSON = "application/json";
    public static final String CONTENTTYPE = "Content-Type";
    public static final String AADATA = "aaData";

// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /* Handles the HTTP<code> GET
    </code
    > method.
    *
     * @param
    request servlet request
    * @param
    response servlet response
    * @
    throws ServletException if a servlet
    -specific error occurs
    * @
    throws IOException if an I
    /O error occurs

     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /* Handles the HTTP<code> POST
    </code
    > method.
    *
     * @param
    request servlet request
    * @param
    response servlet response
    * @
    throws ServletException if a servlet
    -specific error occurs
    * @
    throws IOException if an I
    /O error occurs

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
