package Servlet;

import Entity.Presenza;
import static Utils.Utility.estraiEccezione;
import static Utils.Utility.logfile;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GetDettagliPresenzeServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userIdParam = request.getParameter("utenteId");
        Long userId = Long.parseLong(userIdParam);
        String data = request.getParameter("data");
        LocalDate giorno = LocalDate.parse(data);
        List<Presenza> presenze = getPresenzeByUserId(userId, giorno);

        JsonObject jsonResponse = new JsonObject();
        JsonArray dataArray = new JsonArray();

        for (Presenza presenza : presenze) {
            if (presenza != null) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("tipo", presenza.getTipo().getTipo().toString());
                jsonObject.addProperty("nomeCompleto", presenza.getUtente().getNome() + " " + presenza.getUtente().getCognome());
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                jsonObject.addProperty("ingresso", sdf.format(presenza.getEntrata()));
                jsonObject.addProperty("uscita", presenza.getUscita() != null ? sdf.format(presenza.getUscita()) : "Uscita non ancora registrata");
                dataArray.add(jsonObject);
            }
        }

        jsonResponse.addProperty("iTotalRecords", dataArray.size());
        jsonResponse.addProperty("iTotalDisplayRecords", dataArray.size());
        jsonResponse.add("aaData", dataArray);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse.toString());
        }
    }

    private List<Presenza> getPresenzeByUserId(Long userId, LocalDate giorno) {
        EntityManagerFactory emf = null;
        EntityManager em = null;
        List<Presenza> presenze = new ArrayList<>();

        try {
            emf = Persistence.createEntityManagerFactory("gestionale");
            em = emf.createEntityManager();

            presenze
                    = em.createQuery("SELECT p FROM Presenza p WHERE p.utente.id = :userId AND FUNCTION('DATE', p.entrata) = :giorno", Presenza.class
                    )
                            .setParameter("userId", userId)
                            .setParameter("giorno", giorno)
                            .getResultList();

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
        return presenze;
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
        return "Servlet per ottenere dettagli delle presenze";
    }
}
