package Servlet;

import Entity.Permesso;
import Entity.Utente;
import Utils.Utility;
import static Utils.Utility.logfile;
import com.google.gson.Gson;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class CheckOreDisponibiliServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        try {
            EntityManager em = Persistence.createEntityManagerFactory("gestionale").createEntityManager();

            String tipoPermessoStr = request.getParameter("tipo_permesso");
            String dataInizioStr = request.getParameter("data_inizio");
            String dataFineStr = request.getParameter("data_fine");

            if (tipoPermessoStr == null || dataInizioStr == null || dataFineStr == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\":\"Parametri mancanti.\"}");
                return;
            }

            Utente utente = (Utente) request.getSession().getAttribute("user");
            if (utente == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"error\":\"Utente non autenticato.\"}");
                return;
            }

            Permesso permesso = em.createQuery("SELECT p FROM Permesso p WHERE p.codice = :codice", Permesso.class)
                    .setParameter("codice", Long.valueOf(tipoPermessoStr))
                    .getSingleResult();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

            Date dataInizio;
            Date dataFine;

            if (permesso.getCodice() == 3) {
                dataInizio = dateTimeFormat.parse(dataInizioStr);
                dataFine = dateTimeFormat.parse(dataFineStr);
            } else {
                dataInizio = dateFormat.parse(dataInizioStr);
                dataFine = dateFormat.parse(dataFineStr);
            }

            long oreRichieste = Utility.calcolaOreRichieste(dataInizio, dataFine);
            long giorniRichiesti = Utility.calcolaGiorniRichiesti(dataInizio, dataFine);

            long oreDisponibili = utente.getOre_disponibili();
            int ferieDisponibili = utente.getFerie_disponibili();

            boolean permessoApprovato = false;
            String messaggio = "";

            if (permesso.getCodice() == 1) {
                if (ferieDisponibili >= giorniRichiesti) {
                    permessoApprovato = true;
                } else {
                    messaggio = "Ferie insufficienti. Disponibili: " + ferieDisponibili + " giorni.";
                }
            } else if (permesso.getCodice() == 3) {
                if (oreDisponibili >= oreRichieste) {
                    permessoApprovato = true;
                } else {
                    messaggio = "Ore insufficienti. Disponibili: " + oreDisponibili + " ore.";
                }
            } else if (permesso.getCodice() == 2) {
                permessoApprovato = true;
            } else {
                messaggio = "Tipo di permesso non valido.";
            }

            String jsonResponse;
            if (permessoApprovato) {
                jsonResponse = new Gson().toJson(Map.of(
                        "success", true,
                        "message", "Permesso approvato"
                ));
            } else {
                jsonResponse = new Gson().toJson(Map.of(
                        "success", false,
                        "message", messaggio
                ));
            }
            response.getWriter().write(jsonResponse);

        } catch (Exception e) {
            logfile.severe(Utility.estraiEccezione(e));
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Errore durante il calcolo delle disponibilità.\"}");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
