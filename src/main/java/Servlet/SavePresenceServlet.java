/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Servlet;

import Entity.InfoTrack;
import Entity.Presenza;
import Entity.TipoPresenza;
import Entity.Utente;
import Enum.Tipo_presenza_enum;
import Utils.Utility;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SavePresenceServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String tipo = request.getParameter("tipo");
        String modality = request.getParameter("modality");
        String tipoIngresso = "ingresso";
        if (tipo == null || tipo.isEmpty()) {
            response.sendRedirect("US_gestionale.jsp?esito=KO5&codice=002");
            return;
        }
        if (tipo.equals(tipoIngresso)) {
            if (modality == null || modality.isEmpty()) {
                response.sendRedirect("US_gestionale.jsp?esito=KO5&codice=002");
                return;
            }
        }

        HttpSession session = request.getSession();
        String user_id_param = session.getAttribute("userId").toString();
        Long user_id = Long.valueOf(user_id_param);
        Utente utente = Utility.findUserById(user_id);
        String usName = utente.getNome();

        TipoPresenza tipoPresenza = null;
        if (tipo.equals(tipoIngresso)) {
            tipoPresenza = findTipoPresenzaByEnum(Tipo_presenza_enum.valueOf(modality));

            if (tipoPresenza == null) {
                response.sendRedirect("US_gestionale.jsp?esito=KO4&codice=002");
                return;
            }
        }

        Presenza presenza = new Presenza();
        Timestamp currentTimestamp = Timestamp.valueOf(LocalDateTime.now());
        presenza.setUtente(utente);

        switch (tipo) {
            case "ingresso":
                Presenza lastPresenza = Utility.findLastPresenzaByUser(user_id);

                if (lastPresenza != null && lastPresenza.getUscita() == null) {
                    response.sendRedirect("US_gestionale.jsp?esito=KO2&codice=002");
                    return;
                }

                presenza.setEntrata(currentTimestamp);
                presenza.setTipo(tipoPresenza);
                break;

            case "uscita":
                lastPresenza = Utility.findLastPresenzaByUser(user_id);

                if (lastPresenza == null || lastPresenza.getEntrata() == null || lastPresenza.getUscita() != null) {
                    response.sendRedirect("US_gestionale.jsp?esito=KO3&codice=002");
                    return;
                }

                lastPresenza.setUscita(currentTimestamp);
                presenza = lastPresenza;
                break;

            default:
                response.sendRedirect("US_gestionale.jsp?esito=KO4&codice=002");
                return;
        }

        if (savePresence(presenza)) {
            response.sendRedirect("US_gestionale.jsp?esito=OK&codice=002&tipo=" + tipo);
            if (tipo.equals("ingresso")) {
                InfoTrack.presenzaTrackCreated(usName, presenza, utente);

            } else if (tipo.equals("uscita")) {
                InfoTrack.presenzaTrackCreatedSecond(usName, presenza, utente);
            }
        } else {
            response.sendRedirect("US_gestionale.jsp?esito=KO&codice=002");
        }
    }

    private TipoPresenza findTipoPresenzaByEnum(Tipo_presenza_enum tipoPresenzaEnum) {
        EntityManagerFactory emf = null;
        EntityManager em = null;
        try {
            emf = Persistence.createEntityManagerFactory("gestionale");
            em = emf.createEntityManager();
            return em.createQuery("SELECT t FROM TipoPresenza t WHERE t.tipo = :tipo", TipoPresenza.class)
                    .setParameter("tipo", tipoPresenzaEnum)
                    .getSingleResult();
        } catch (Exception e) {
            Logger.getLogger(SavePresenceServlet.class.getName()).log(Level.SEVERE, "Error fetching TipoPresenza", e);
            return null;
        } finally {
            if (em != null) {
                em.close();
            }
            if (emf != null) {
                emf.close();
            }
        }
    }

    private boolean savePresence(Presenza presenza) {
        EntityManagerFactory emf = null;
        EntityManager em = null;
        try {
            emf = Persistence.createEntityManagerFactory("gestionale");
            em = emf.createEntityManager();
            em.getTransaction().begin();
            if (presenza.getId() == null) {
                em.persist(presenza);
            } else {
                em.merge(presenza);
            }
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            Logger.getLogger(SavePresenceServlet.class.getName()).log(Level.SEVERE, "Error saving Presenza", e);
            return false;
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
