/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Servlet.Presenze;

import Entity.Presenza;
import Entity.Richiesta;
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
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Calendar;
import java.util.stream.Collectors;

public class GetTurniDipendentiServlet extends HttpServlet {

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
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String userIdParam = request.getParameter("userId");
        Boolean utenteServlet = Boolean.valueOf(request.getParameter("utente"));
        Long richiestaId = null;
        HttpSession session = request.getSession();
        final Long userIdSession = Long.valueOf(session.getAttribute("userId").toString());

        if (utenteServlet) {
            try {
                SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");
                List<Map<String, Object>> eventsList = new ArrayList<>();
                Map<String, Map<String, StringBuilder>> presenzeAggregate = new HashMap<>();
                Map<String, List<Map<String, Object>>> permessiPerData = new HashMap<>();
                Map<String, String> userIdPerGiorno = new HashMap<>();

                List<Presenza> presenze = getPresenze().stream()
                        .filter(p -> p.getUtente().getId().equals(userIdSession))
                        .collect(Collectors.toList());

                for (Presenza p : presenze) {
                    String giorno = new SimpleDateFormat("yyyy-MM-dd").format(p.getEntrata());
                    String utenteKey = EncryptionUtil.decrypt(p.getUtente().getNome());

                    presenzeAggregate.putIfAbsent(utenteKey, new HashMap<>());
                    Map<String, StringBuilder> presenzePerData = presenzeAggregate.get(utenteKey);

                    presenzePerData.putIfAbsent(giorno, new StringBuilder());
                    StringBuilder presenzeConcat = presenzePerData.get(giorno);

                    presenzeConcat.append("Tipo: ").append(p.getTipo().getTipo().toString()).append("<br>");
                    presenzeConcat.append("Entrata: ").append(sdfDateTime.format(p.getEntrata())).append("<br>");
                    if (p.getUscita() != null) {
                        presenzeConcat.append("Uscita: ").append(sdfDateTime.format(p.getUscita())).append("<hr>");
                    }

                    userIdPerGiorno.put(giorno, userIdSession.toString());
                }

                List<Richiesta> richieste = getRichieste().stream()
                        .filter(r -> r.getUtente().getId().equals(userIdSession))
                        .collect(Collectors.toList());

                for (Richiesta richiesta : richieste) {
                    richiestaId = richiesta.getId();

                    Date inizio = richiesta.getData_inizio();
                    Date fine = richiesta.getData_fine();

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(inizio);

                    while (!cal.getTime().after(fine)) {
                        String giornoPermesso = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());

                        Map<String, Object> permessoEvent = new HashMap<>();
                        permessoEvent.put("title", "Permesso - " + richiesta.getTipo_permesso().getDescrizione() + " - " + EncryptionUtil.decrypt(richiesta.getUtente().getNome()));
                        permessoEvent.put("start", giornoPermesso);
                        permessoEvent.put("end", giornoPermesso);
                        permessoEvent.put("description", "Permesso di tipo " + richiesta.getTipo_permesso().getDescrizione() + " di " + EncryptionUtil.decrypt(richiesta.getUtente().getNome()));
                        permessoEvent.put("color", "#28a745");
                        permessoEvent.put("richiestaId", richiesta.getId());
                        permessoEvent.put("permesso", true);

                        eventsList.add(permessoEvent);

                        cal.add(Calendar.DATE, 1);
                    }
                }

                for (Map.Entry<String, Map<String, StringBuilder>> entryUtente : presenzeAggregate.entrySet()) {
                    String utente = entryUtente.getKey();
                    for (Map.Entry<String, StringBuilder> entryData : entryUtente.getValue().entrySet()) {
                        Map<String, Object> event = new HashMap<>();
                        String dataKey = entryData.getKey();
                        String description = entryData.getValue().toString();
                        String userIdPresenza2 = userIdPerGiorno.get(dataKey);

                        event.put("title", utente);
                        event.put("start", dataKey);
                        event.put("description", description);
                        event.put("utenteId", Long.valueOf(userIdPresenza2));
                        event.put("permesso", false);
                        eventsList.add(event);
                    }
                }

                for (Map.Entry<String, List<Map<String, Object>>> entry : permessiPerData.entrySet()) {
                    String dataKey = entry.getKey();
                    List<Map<String, Object>> permessi = entry.getValue();

                    if (!permessi.isEmpty()) {
                        Map<String, Object> groupedPermessoEvent = permessi.get(0);
                        groupedPermessoEvent.put("title", "Permessi");
                        groupedPermessoEvent.put("start", dataKey);
                        groupedPermessoEvent.put("description", "Richieste di permesso per il giorno");
                        groupedPermessoEvent.put("color", "#28a745");
                        groupedPermessoEvent.put("richiestaId", richiestaId);
                        groupedPermessoEvent.put("permesso", true);
                        eventsList.add(groupedPermessoEvent);
                    }
                }

                String json = new Gson().toJson(eventsList);
                response.getWriter().write(json);
            } catch (Exception e) {
                logfile.severe(estraiEccezione(e));
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore durante il recupero dei dati.");
            }
        } else {
            try {
                SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");
                List<Map<String, Object>> eventsList = new ArrayList<>();
                if (userIdParam != null) {
                    Long userId = Long.parseLong(userIdParam);
                    List<Presenza> presenze = getPresenzeByUserId(userId);

                    Map<String, StringBuilder> presenzePerData = new HashMap<>();
                    Map<String, StringBuilder> permessiPerData = new HashMap<>();

                    for (Presenza p : presenze) {
                        String giorno = new SimpleDateFormat("yyyy-MM-dd").format(p.getEntrata());
                        String dataKey = giorno;

                        presenzePerData.putIfAbsent(dataKey, new StringBuilder());
                        StringBuilder presenzeConcat = presenzePerData.get(dataKey);
                        presenzeConcat.append("Tipo: ").append(p.getTipo().getTipo().toString()).append("<br>");
                        presenzeConcat.append("Entrata: ").append(sdfDateTime.format(p.getEntrata())).append("<br>");
                        if (p.getUscita() != null) {
                            presenzeConcat.append("Uscita: ").append(sdfDateTime.format(p.getUscita())).append("<hr>");
                        }
                    }

                    for (Map.Entry<String, StringBuilder> entry : presenzePerData.entrySet()) {
                        Map<String, Object> event = new HashMap<>();
                        String dataKey = entry.getKey();
                        event.put("title", "Presenze del giorno");
                        event.put("start", dataKey);
                        event.put("description", entry.getValue().toString());
                        event.put("utenteId", userId);
                        eventsList.add(event);
                    }

                    List<Richiesta> richieste = getRichiesteByUserId(userId);
                    for (Richiesta richiesta : richieste) {
                        String giornoPermesso = new SimpleDateFormat("yyyy-MM-dd").format(richiesta.getData_inizio());
                        permessiPerData.putIfAbsent(giornoPermesso, new StringBuilder());
                        StringBuilder permessoConcat = permessiPerData.get(giornoPermesso);
                        permessoConcat.append("Richiesta di permesso per il giorno ").append(giornoPermesso).append("<br>");
                    }

                    for (Map.Entry<String, StringBuilder> entry : permessiPerData.entrySet()) {
                        Map<String, Object> permessoEvent = new HashMap<>();
                        String dataKey = entry.getKey();
                        permessoEvent.put("title", "Permessi del giorno");
                        permessoEvent.put("start", dataKey);
                        permessoEvent.put("description", entry.getValue().toString());
                        permessoEvent.put("color", "#28a745");
                        permessoEvent.put("permesso", true);
                        permessoEvent.put("richiestaId", richiestaId);
                        eventsList.add(permessoEvent);
                    }
                } else {
                    Map<String, Map<String, StringBuilder>> presenzeAggregate = new HashMap<>();
                    Map<String, List<Map<String, Object>>> permessiPerData = new HashMap<>();
                    Map<String, String> userIdPerGiorno = new HashMap<>();

                    List<Presenza> presenze = getPresenze();
                    for (Presenza p : presenze) {
                        String userIdPresenza2 = p.getUtente().getId().toString();
                        String giorno = new SimpleDateFormat("yyyy-MM-dd").format(p.getEntrata());
                        String utenteKey = EncryptionUtil.decrypt(p.getUtente().getNome());

                        presenzeAggregate.putIfAbsent(utenteKey, new HashMap<>());
                        Map<String, StringBuilder> presenzePerData = presenzeAggregate.get(utenteKey);

                        presenzePerData.putIfAbsent(giorno, new StringBuilder());
                        StringBuilder presenzeConcat = presenzePerData.get(giorno);

                        presenzeConcat.append("Tipo: ").append(p.getTipo().getTipo().toString()).append("<br>");
                        presenzeConcat.append("Entrata: ").append(sdfDateTime.format(p.getEntrata())).append("<br>");
                        if (p.getUscita() != null) {
                            presenzeConcat.append("Uscita: ").append(sdfDateTime.format(p.getUscita())).append("<hr>");
                        }

                        userIdPerGiorno.put(giorno, userIdPresenza2);
                    }

                    List<Richiesta> richieste = getRichieste();
                    for (Richiesta richiesta : richieste) {
                        richiestaId = richiesta.getId();

                        Date inizio = richiesta.getData_inizio();
                        Date fine = richiesta.getData_fine();

                        Calendar cal = Calendar.getInstance();
                        cal.setTime(inizio);

                        while (!cal.getTime().after(fine)) {
                            String giornoPermesso = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());

                            Map<String, Object> permessoEvent = new HashMap<>();
                            permessoEvent.put("title", "Permesso - " + richiesta.getTipo_permesso().getDescrizione() + " - " + EncryptionUtil.decrypt(richiesta.getUtente().getNome()));
                            permessoEvent.put("start", giornoPermesso);
                            permessoEvent.put("end", giornoPermesso);
                            permessoEvent.put("description", "Permesso di tipo " + richiesta.getTipo_permesso().getDescrizione() + " di " + EncryptionUtil.decrypt(richiesta.getUtente().getNome()));
                            permessoEvent.put("color", "#28a745");
                            permessoEvent.put("richiestaId", richiesta.getId());
                            permessoEvent.put("permesso", true);

                            eventsList.add(permessoEvent);

                            cal.add(Calendar.DATE, 1);
                        }
                    }

                    for (Map.Entry<String, Map<String, StringBuilder>> entryUtente : presenzeAggregate.entrySet()) {
                        String utente = entryUtente.getKey();
                        for (Map.Entry<String, StringBuilder> entryData : entryUtente.getValue().entrySet()) {
                            Map<String, Object> event = new HashMap<>();
                            String dataKey = entryData.getKey();
                            String description = entryData.getValue().toString();
                            String userIdPresenza2 = userIdPerGiorno.get(dataKey);

                            event.put("title", utente);
                            event.put("start", dataKey);
                            event.put("description", description);
                            event.put("utenteId", userIdPresenza2);
                            event.put("permesso", false);
                            eventsList.add(event);
                        }
                    }

                    for (Map.Entry<String, List<Map<String, Object>>> entry : permessiPerData.entrySet()) {
                        String dataKey = entry.getKey();
                        List<Map<String, Object>> permessi = entry.getValue();

                        if (!permessi.isEmpty()) {
                            Map<String, Object> groupedPermessoEvent = permessi.get(0);
                            groupedPermessoEvent.put("title", "Permessi");
                            groupedPermessoEvent.put("start", dataKey);
                            groupedPermessoEvent.put("description", "Richieste di permesso per il giorno");
                            groupedPermessoEvent.put("color", "#28a745");
                            groupedPermessoEvent.put("richiestaId", richiestaId);
                            groupedPermessoEvent.put("permesso", true);
                            eventsList.add(groupedPermessoEvent);
                        }
                    }

                    String json = new Gson().toJson(eventsList);
                    response.getWriter().write(json);

                }
            } catch (Exception e) {
                logfile.severe(estraiEccezione(e));
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore durante il recupero dei dati.");
            }
        }
    }

    private List<Presenza> getPresenze() {
        EntityManagerFactory emf = null;
        EntityManager em = null;
        List<Presenza> presenze = new ArrayList<>();

        try {
            emf = Persistence.createEntityManagerFactory("gestionale");
            em = emf.createEntityManager();

            List<Utente> utenti = em.createQuery("SELECT u FROM Utente u", Utente.class
            ).getResultList();

            presenze
                    = em.createQuery("SELECT p FROM Presenza p WHERE p.utente IN :utenti", Presenza.class
                    )
                            .setParameter("utenti", utenti)
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

    private List<Richiesta> getRichieste() {
        EntityManagerFactory emf = null;
        EntityManager em = null;
        List<Richiesta> richieste = new ArrayList<>();

        try {
            emf = Persistence.createEntityManagerFactory("gestionale");
            em = emf.createEntityManager();

            richieste
                    = em.createQuery("SELECT r FROM Richiesta r", Richiesta.class
                    ).getResultList();

            return richieste;

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

        return richieste;
    }

    private List<Presenza> getPresenzeByUserId(Long userId) {
        EntityManagerFactory emf = null;
        EntityManager em = null;
        List<Presenza> presenze = new ArrayList<>();

        try {
            emf = Persistence.createEntityManagerFactory("gestionale");
            em = emf.createEntityManager();

            presenze
                    = em.createQuery("SELECT p FROM Presenza p WHERE p.utente.id = :userId", Presenza.class
                    )
                            .setParameter("userId", userId)
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

    private List<Richiesta> getRichiesteByUserId(Long userId) {
        EntityManagerFactory emf = null;
        EntityManager em = null;
        List<Richiesta> richieste = new ArrayList<>();

        try {
            emf = Persistence.createEntityManagerFactory("gestionale");
            em = emf.createEntityManager();

            richieste
                    = em.createQuery("SELECT r FROM Richiesta r WHERE r.utente.id = :userId", Richiesta.class
                    )
                            .setParameter("userId", userId)
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
        return richieste;
    }

    private Date convertToDateViaInstant(LocalDate dateToConvert) {
        return Date.from(dateToConvert.atStartOfDay(ZoneId.systemDefault()).toInstant());
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
