/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Servlet;

import Entity.Notifica;
import Entity.Utente;
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
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 *
 * @author Aldo
 */
public class GetNotificheServlet extends HttpServlet {

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
                
                if(notifiche.isEmpty()){
                    jsonResponse.addProperty("noData", true);
                }else{
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
    public static final String ITOTALRECORDS = "iTotalRecords";
    public static final String ITOTALDISPLAY = "iTotalDisplayRecords";
    public static final String SECHO = "sEcho";
    public static final String SCOLUMS = "sColumns";
    public static final String APPJSON = "application/json";
    public static final String CONTENTTYPE = "Content-Type";
    public static final String AADATA = "aaData";

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
