/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Servlet.Presenze;

import Entity.Presenza;
import Entity.Utente;
import Utils.EncryptionUtil;
import static Utils.Utility.estraiEccezione;
import static Utils.Utility.logfile;
import com.google.gson.Gson;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Salvatore
 */
public class GetDipendentiPresenzaServlet extends HttpServlet {

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
        try {
            String dataInizio = request.getParameter("dataInizio");
            String dataFine = request.getParameter("dataFine");
            String presenzaValue = request.getParameter("presenza");
            String utenteIdParam = request.getParameter("utente");

            Long utenteId = null;
            if (!"Qualsiasi".equals(utenteIdParam)) {
                utenteId = Long.parseLong(utenteIdParam);
            }

            Boolean sede = null;
            if (!"Qualsiasi".equals(presenzaValue)) {
                sede = "Sede".equals(presenzaValue);
            }

            List<Presenza> presenze = getPresenze();

            List<Map<String, Object>> presenzeList = filtraPresenze(presenze, dataInizio, dataFine, sede, utenteId);

            inviaRispostaJson(response, presenzeList);

        } catch (Exception e) {
            logfile.severe(estraiEccezione(e));
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore nel recupero dei dati.");
        }
    }

    private List<Map<String, Object>> filtraPresenze(List<Presenza> presenze, String dataInizio, String dataFine, Boolean sede, Long utenteId) {
        List<Map<String, Object>> presenzeList = new ArrayList<>();
        Date startDate = parseDate(dataInizio);
        Date endDate = parseDateWithEndOfDay(dataFine);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        for (Presenza p : presenze) {
            if (matchPresenza(p, startDate, endDate, sede, utenteId)) {
                Map<String, Object> list_presenze = new HashMap<>();
                list_presenze.put("name", EncryptionUtil.decrypt(p.getUtente().getNome()) + " " + EncryptionUtil.decrypt(p.getUtente().getCognome()));
                list_presenze.put("tipo", p.getTipo().getTipo());

                String entrataFormatted = (p.getEntrata() != null) ? sdf.format(p.getEntrata()) : "Non registrata";
                list_presenze.put("entrata", entrataFormatted);

                String uscitaFormatted = (p.getUscita() != null) ? sdf.format(p.getUscita()) : "Non ancora registrata";
                list_presenze.put("uscita", uscitaFormatted);

                presenzeList.add(list_presenze);
            }

        }
        return presenzeList;
    }

    private boolean matchPresenza(Presenza p, Date startDate, Date endDate, Boolean sede, Long utenteId) {
        boolean isSede = "SEDE".equalsIgnoreCase(p.getTipo().getTipo().toString());
        boolean dataValida = (startDate == null || !p.getEntrata().before(startDate))
                && (endDate == null || !p.getEntrata().after(endDate));
        boolean utenteValido = (utenteId == null || p.getUtente().getId().equals(utenteId));
        return (sede == null || isSede == sede) && dataValida && utenteValido;
    }

    private void inviaRispostaJson(HttpServletResponse response, List<Map<String, Object>> presenzeList) throws IOException {
        Gson gson = new Gson();
        try (PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "UTF-8"))) {
            out.print(gson.toJson(presenzeList));
        }
    }

    private Date parseDateWithEndOfDay(String dateStr) {
        if (dateStr != null && !dateStr.isEmpty()) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date parsedDate = dateFormat.parse(dateStr);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(parsedDate);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                calendar.set(Calendar.SECOND, 59);
                return new java.sql.Date(calendar.getTimeInMillis());
            } catch (Exception e) {
                logfile.severe(estraiEccezione(e));
            }
        }
        return null;
    }

    private Date parseDate(String dateStr) {
        if (dateStr != null && !dateStr.isEmpty()) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                return new java.sql.Date(dateFormat.parse(dateStr).getTime());
            } catch (Exception e) {
                logfile.severe(estraiEccezione(e));
            }
        }
        return null;
    }

    private List<Presenza> getPresenze() {
        EntityManagerFactory emf = null;
        EntityManager em = null;
        try {
            emf = Persistence.createEntityManagerFactory("gestionale");
            em = emf.createEntityManager();
            List<Utente> utenti = em.createQuery("SELECT u FROM Utente u WHERE u.ruolo.id = 2", Utente.class).getResultList();
            return em.createQuery("SELECT p FROM Presenza p WHERE p.utente IN :utenti", Presenza.class)
                    .setParameter("utenti", utenti)
                    .getResultList();
        } catch (Exception e) {
            logfile.severe(estraiEccezione(e));
            Logger.getLogger(GetDipendentiPresenzaServlet.class.getName()).log(Level.SEVERE, "Errore nel recupero delle presenze", e);
        } finally {
            if (em != null) {
                em.close();
            }
            if (emf != null) {
                emf.close();
            }
        }
        return Collections.emptyList();
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
