/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Servlet.Presenze;

import Entity.InfoTrack;
import Entity.Presenza;
import Entity.TipoPresenza;
import Entity.Utente;
import Enum.Tipo_presenza_enum;
import Utils.EncryptionUtil;
import Utils.Utility;
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
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SavePresenceServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        boolean isPresence = Boolean.parseBoolean(request.getParameter("isPresence"));
        boolean isDetails = Boolean.parseBoolean(request.getParameter("isDetails"));
        boolean isAdmin = Boolean.parseBoolean(request.getParameter("isAdmin"));

        try {
            if (isPresence) {
                savePresence(request, response, isAdmin);
            } else if (isDetails) {
                getDettagliPresenza(request, response);
            }

        } catch (ServletException e) {
            logfile.severe(estraiEccezione(e));

        }
    }

    //Metodo salvataggio presenza - TimbroE,TimbroU - modalTimbro Utente's pages
    protected void savePresence(HttpServletRequest request, HttpServletResponse response, boolean isAdmin) throws ServletException, IOException {

        String tipo = request.getParameter("tipo");
        String modality = request.getParameter("modality");
        String tipoIngresso = "ingresso";
        if (isAdmin) {
            if (tipo == null || tipo.isEmpty()) {
                response.sendRedirect("AD_gestionale.jsp?esito=KO5&codice=002");
                return;
            }

            if (tipo.equals(tipoIngresso)) {
                if (modality == null || modality.isEmpty()) {
                    response.sendRedirect("AD_gestionale.jsp?esito=KO5&codice=002");
                    return;
                }
            }
        } else {

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
        }

        HttpSession session = request.getSession();
        String user_id_param = session.getAttribute("userId").toString();
        Long user_id = Long.valueOf(user_id_param);
        Utente utente = Utility.findUserById(user_id);
        String usName = EncryptionUtil.decrypt(utente.getNome());

        TipoPresenza tipoPresenza = null;

        if (tipo.equals(tipoIngresso)) {
            tipoPresenza = findTipoPresenzaByEnum(Tipo_presenza_enum.valueOf(modality));

            if (isAdmin) {
                if (tipoPresenza == null) {
                    response.sendRedirect("AD_gestionale.jsp?esito=KO4&codice=002");
                    return;
                }
            } else {

                if (tipoPresenza == null) {
                    response.sendRedirect("US_gestionale.jsp?esito=KO4&codice=002");
                    return;
                }
            }
        }

        LocalDate currentDate = LocalDate.now();
        Timestamp currentTimestamp = Timestamp.valueOf(LocalDateTime.now());
        Presenza presenza = new Presenza();
        Presenza lastPresenza = Utility.findLastPresenzaByUserOnDate(user_id, currentDate);

        presenza.setUtente(utente);

        switch (tipo) {
            case "ingresso":
                if (isAdmin) {
                    if (lastPresenza != null && lastPresenza.getUscita() == null) {
                        response.sendRedirect("AD_gestionale.jsp?esito=KO2&codice=002");
                        return;
                    }
                } else {
                    if (lastPresenza != null && lastPresenza.getUscita() == null) {
                        response.sendRedirect("US_gestionale.jsp?esito=KO2&codice=002");
                        return;
                    }
                }

                presenza.setEntrata(currentTimestamp);
                presenza.setTipo(tipoPresenza);
                break;

            case "uscita":
                if (isAdmin) {
                    if (lastPresenza == null || lastPresenza.getEntrata() == null || lastPresenza.getUscita() != null) {
                        response.sendRedirect("AD_gestionale.jsp?esito=KO3&codice=002");
                        return;
                    }
                } else {
                    if (lastPresenza == null || lastPresenza.getEntrata() == null || lastPresenza.getUscita() != null) {
                        response.sendRedirect("US_gestionale.jsp?esito=KO3&codice=002");
                        return;
                    }
                }

                lastPresenza.setUscita(currentTimestamp);
                presenza = lastPresenza;
                break;

            default:
                response.sendRedirect("US_gestionale.jsp?esito=KO4&codice=002");
                return;
        }

        if (savePresence(presenza)) {
            if (isAdmin) {
                response.sendRedirect("AD_gestionale.jsp?esito=OK&codice=002&tipo=" + tipo);
            } else {
                response.sendRedirect("US_gestionale.jsp?esito=OK&codice=002&tipo=" + tipo);
            }
            if (tipo.equals("ingresso")) {
                InfoTrack.presenzaTrackCreated(usName, presenza, utente);

            } else if (tipo.equals("uscita")) {
                InfoTrack.presenzaTrackCreatedSecond(usName, presenza, utente);
            }
        } else {
            if (isAdmin) {
                response.sendRedirect("AD_gestionale.jsp?esito=KO&codice=002");
            } else {
                response.sendRedirect("US_gestionale.jsp?esito=KO&codice=002");
            }
        }
    }

    private TipoPresenza findTipoPresenzaByEnum(Tipo_presenza_enum tipoPresenzaEnum) {
        EntityManagerFactory emf = null;
        EntityManager em = null;
        try {
            emf = Persistence.createEntityManagerFactory("gestionale");
            em = emf.createEntityManager();

            return em.createQuery("SELECT t FROM TipoPresenza t WHERE t.tipo = :tipo", TipoPresenza.class
            )
                    .setParameter("tipo", tipoPresenzaEnum)
                    .getSingleResult();

        } catch (Exception e) {
            Logger.getLogger(SavePresenceServlet.class
                    .getName()).log(Level.SEVERE, "Error fetching TipoPresenza", e);
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
            Logger.getLogger(SavePresenceServlet.class
                    .getName()).log(Level.SEVERE, "Error saving Presenza", e);
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

    //DataTable dettagli timbro : dettagliPresenza.jsp
    protected void getDettagliPresenza(HttpServletRequest request, HttpServletResponse response)
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
                jsonObject.addProperty("nomeCompleto", EncryptionUtil.decrypt(presenza.getUtente().getNome()) + " " + EncryptionUtil.decrypt(presenza.getUtente().getCognome()));
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
