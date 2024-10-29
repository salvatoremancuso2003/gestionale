/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Servlet.Notifica;

import Entity.InfoTrack;
import Entity.Notifica;
import Entity.Richiesta;
import Entity.Utente;
import Enum.Si_no_enum;
import Enum.Stato_enum;
import Utils.Utility;
import static Utils.Utility.estraiEccezione;
import static Utils.Utility.logfile;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class NotificaServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        boolean visual = Boolean.parseBoolean(request.getParameter("visual"));
        boolean read = Boolean.parseBoolean(request.getParameter("read"));

        try {
            if (visual == true) {
                creaNotifica(request, response);
            } else if (read == true) {
                readNotification(request, response);
            }
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }

    //Creazione notifica modal admin
    private void creaNotifica(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Utente loggato (admin)
        Utente admin = (Utente) request.getSession().getAttribute("user");

        // Estrazione dei parametri dal form
        String richiestaIdParam = request.getParameter("richiestaIdConferma");

        // Converti richiestaId in Long
        Long richiestaId = Long.parseLong(richiestaIdParam);

        // Crea EntityManager
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("gestionale");
        EntityManager em = emf.createEntityManager();

        try {
            // Trova la richiesta nel database
            Richiesta richiesta = em.find(Richiesta.class, richiestaId);

            if (richiesta != null) {
                Utente utente = richiesta.getUtente();

                if (utente != null) {
                    // Ottieni l'esito dal form
                    String esito = request.getParameter("esito");

                    if ("OK".equals(esito) || "KO".equals(esito)) {
                        // Crea il messaggio della notifica
                        String messaggio = "La richiesta con ID " + richiestaId + " è stata gestita con esito: " + esito;

                        // Inizia la transazione
                        em.getTransaction().begin();

                        // Crea una nuova notifica
                        Notifica notifica = new Notifica();
                        notifica.setEsito(esito);

                        try {
                            // Se esito è RIGETTATA, imposta lo stato a RIGETTATA
                            if ("KO".equals(esito)) {
                                richiesta.setStato(Stato_enum.RIGETTATA);
                            } // Se esito è APPROVATA, aggiorna lo stato e verifica ferie/ore disponibili
                            else if ("OK".equals(esito)) {
                                richiesta.setStato(Stato_enum.APPROVATA);

                                // Gestisci ferie se è un permesso ferie
                                if (richiesta.getTipo_permesso().getFerie().equals(Si_no_enum.SI) && richiesta.getTipo_permesso().getOre().equals(Si_no_enum.NO)) {
                                    int ferieDisponibili = utente.getFerie_disponibili();
                                    LocalDate dataInizio = richiesta.getData_inizio().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                                    LocalDate dataFine = richiesta.getData_fine().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                                    long giorniRichiesti = ChronoUnit.DAYS.between(dataInizio, dataFine);

                                    // Verifica e aggiorna le ferie disponibili
                                    if (!(giorniRichiesti > ferieDisponibili)) {
                                        int giorniRimossi = ferieDisponibili - (int) giorniRichiesti;
                                        utente.setFerie_disponibili(giorniRimossi);
                                        em.merge(utente);
                                    } else {
                                        utente.setFerie_disponibili(0);
                                    }
                                } // Gestisci ore se è un permesso orario
                                else if (richiesta.getTipo_permesso().getOre().equals(Si_no_enum.SI) && richiesta.getTipo_permesso().getFerie().equals(Si_no_enum.NO)) {
                                    int oreDisponibili = utente.getOre_disponibili();
                                    LocalDateTime dataInizio = richiesta.getData_inizio().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                                    LocalDateTime dataFine = richiesta.getData_fine().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                                    long oreRichieste = ChronoUnit.HOURS.between(dataInizio, dataFine);

                                    // Verifica e aggiorna le ore disponibili
                                    if (!(oreRichieste > oreDisponibili)) {
                                        int oreRimosse = oreDisponibili - (int) oreRichieste;
                                        utente.setOre_disponibili(oreRimosse);
                                        em.merge(utente);
                                    } else {
                                        utente.setOre_disponibili(0);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            // Gestione delle eccezioni e log
                            logfile.severe(estraiEccezione(e));
                            e.printStackTrace();
                        }

                        // Completa la notifica
                        notifica.setMessaggio(messaggio);
                        notifica.setUtente(utente);
                        notifica.setDataCreazione(LocalDateTime.now());
                        notifica.setLette(false);
                        em.persist(notifica);

                        // Commit della transazione
                        em.getTransaction().commit();

                        // Risposta e reindirizzamento
                        response.setStatus(HttpServletResponse.SC_OK);

                        // Traccia la notifica
                        InfoTrack.notificaTrack(admin.getNome(), notifica, utente);

                    } else {
                        // Esito non valido
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        response.getWriter().write("Esito non valido");
                    }
                } else {
                    // Utente non trovato
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("Utente non trovato per la richiesta");
                }
            } else {
                // Richiesta non trovata
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Richiesta non trovata");
            }
            response.sendRedirect("AD_richiestePermessi.jsp?esito=OK&codice=008");
        } catch (IOException e) {
            // Rollback della transazione in caso di errore
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Errore nella creazione della notifica");
            response.sendRedirect("AD_richiestePermessi.jsp?esito=KO&codice=008");
        } finally {
            // Chiudi EntityManager
            em.close();
        }
    }

    //Segna come gia letto utente
    protected void readNotification(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"message\": \"Utente non autenticato.\"}");
            return;
        }

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("gestionale");
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            em.createQuery("UPDATE Notifica n SET n.lette = TRUE WHERE n.utente.id = :userId")
                    .setParameter("userId", userId)
                    .executeUpdate();

            em.getTransaction().commit();

            PrintWriter out = response.getWriter();
            out.write("{\"message\": \"Notifiche segnate come lette.\"}");
        } catch (Exception e) {
            logfile.severe(estraiEccezione(e));
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"message\": \"Errore nel segnare le notifiche come lette.\"}");
        } finally {
            em.close();
        }
    }

    //Data table notifiche utente
    protected void readAllUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession();
        String user_id_param = session.getAttribute("userId").toString();
        Long user_id = Long.valueOf(user_id_param);
        Utente utente = Utility.findUserById(user_id);

        if (utente != null) {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("gestionale");
            EntityManager em = emf.createEntityManager();

            try {
                List<Notifica> notifiche = em.createQuery("SELECT n FROM Notifica n WHERE n.utente = :utente AND n.lette = false", Notifica.class)
                        .setParameter("utente", utente)
                        .getResultList();
                JsonObject jsonResponse = new JsonObject();

                if (notifiche.isEmpty()) {
                    jsonResponse.addProperty("noData", true);
                } else {
                    jsonResponse.addProperty("noData", false);
                }

                JsonArray dataArray = new JsonArray();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

                for (Notifica notifica : notifiche) {
                    JsonObject rc = new JsonObject();
                    rc.addProperty("id", notifica.getId());
                    rc.addProperty("messaggio", notifica.getMessaggio());
                    rc.addProperty("dataCreazione", formatter.format(notifica.getDataCreazione()));
                    rc.addProperty("esito", notifica.getEsito());

                    dataArray.add(rc);
                }

                int totalNotifiche = notifiche.size();

                jsonResponse.addProperty("totalNotifiche", totalNotifiche);
                jsonResponse.addProperty("noData", false);
                jsonResponse.add("aaData", dataArray);

                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print(jsonResponse.toString());
                out.flush();
            } catch (Exception e) {
                logfile.severe(estraiEccezione(e));
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while processing the request");
            } finally {
                em.close();
            }
        }
    }

// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
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
