/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Servlet;

import Entity.InfoTrack;
import Entity.Notifica;
import Entity.Richiesta;
import Entity.Utente;
import Enum.Si_no_enum;
import Enum.Stato_enum;
import static Utils.Utility.estraiEccezione;
import static Utils.Utility.logfile;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public class CreaNotifica extends HttpServlet {

    /*Processes requests for both HTTP <code>GET</code
    > and<code> POST
    </code
    >
     * methods.
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
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Utente admin = (Utente) request.getSession().getAttribute("user");

        String richiestaIdParam = request.getParameter("richiestaIdConferma");
        Long richiestaId = Long.parseLong(richiestaIdParam);

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("gestionale");
        EntityManager em = emf.createEntityManager();

        try {
            Richiesta richiesta = em.find(Richiesta.class, richiestaId);

            if (richiesta != null) {
                Utente utente = richiesta.getUtente();

                if (utente != null) {
                    String esito = request.getParameter("esito");

                    if ("OK".equals(esito) || "KO".equals(esito)) {
                        String messaggio = "La richiesta con ID " + richiestaId + " è stata gestita con esito: " + esito;

                        em.getTransaction().begin();
                        Notifica notifica = new Notifica();
                        notifica.setEsito(esito);

                        try {
                            if ("KO".equals(esito)) {
                                richiesta.setStato(Stato_enum.KO);
                            } else if ("OK".equals(esito)) {
                                richiesta.setStato(Stato_enum.OK);
                                if (richiesta.getTipo_permesso().getFerie().equals(Si_no_enum.SI) && richiesta.getTipo_permesso().getOre().equals(Si_no_enum.NO)) {
                                    int ferieDisponibili = utente.getFerie_disponibili();

                                    LocalDate dataInizio = richiesta.getData_inizio().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                                    LocalDate dataFine = richiesta.getData_fine().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                                    long giorniRichiesti = ChronoUnit.DAYS.between(dataInizio, dataFine);

                                    if (!(giorniRichiesti > ferieDisponibili)) {

                                        int giorniRimossi = ferieDisponibili - (int) giorniRichiesti;
                                        utente.setFerie_disponibili(giorniRimossi);
                                        em.merge(utente);
                                    } else {
                                        utente.setFerie_disponibili(0);
                                    }

                                } else if (richiesta.getTipo_permesso().getOre().equals(Si_no_enum.SI) && richiesta.getTipo_permesso().getFerie().equals(Si_no_enum.NO)) {
                                    int oreDisponibili = utente.getOre_disponibili();
                                    LocalDateTime dataInizio = richiesta.getData_inizio().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                                    LocalDateTime dataFine = richiesta.getData_fine().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

                                    long oreRichieste = ChronoUnit.HOURS.between(dataInizio, dataFine);

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
                            logfile.severe(estraiEccezione(e));
                        }

                        notifica.setMessaggio(messaggio);
                        notifica.setUtente(utente);
                        notifica.setDataCreazione(LocalDateTime.now());
                        notifica.setLette(false);

                        em.persist(notifica);

                        em.getTransaction().commit();

                        response.setStatus(HttpServletResponse.SC_OK);
                        //response.getWriter().write("Notifica creata con successo");
                        response.sendRedirect("AD_richiestePermessi.jsp");
                        InfoTrack.notificaTrack(admin.getNome(), notifica, utente);

                    } else {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        response.getWriter().write("Esito non valido");
                    }
                } else {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("Utente non trovato per la richiesta");
                }
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Richiesta non trovata");
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Errore nella creazione della notifica");
        } finally {
            em.close();
        }
    }

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
