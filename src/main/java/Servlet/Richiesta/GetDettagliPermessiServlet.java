/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Servlet.Richiesta;

import Entity.Richiesta;
import Entity.Utente;
import Enum.Si_no_enum;
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
import java.text.SimpleDateFormat;

/**
 *
 * @author Salvatore
 */
public class GetDettagliPermessiServlet extends HttpServlet {

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
        String richiestaIdParam = request.getParameter("richiestaId");
        Long richiestaId = Long.parseLong(richiestaIdParam);
        HttpSession session = request.getSession();
        String userIdParam = session.getAttribute("userId").toString();
        int userId = Utility.tryParse(userIdParam);
        Utente user = Utility.findUserById(Long.valueOf(userIdParam));
        Richiesta richiesta = getRichiestaByRichiestaId(richiestaId);

        JsonObject jsonResponse = new JsonObject();
        JsonArray dataArray = new JsonArray();

        if (richiesta != null) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("tipo", richiesta.getTipo_permesso().getDescrizione());
            jsonObject.addProperty("nomeCompleto", richiesta.getUtente().getNome() + " " + richiesta.getUtente().getCognome());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            SimpleDateFormat sdfFormatData = new SimpleDateFormat("dd/MM/yyyy");
            if (richiesta.getTipo_permesso().getOre().equals(Si_no_enum.NO)) {
                jsonObject.addProperty("data_inizio", sdfFormatData.format(richiesta.getData_inizio()));
                jsonObject.addProperty("data_fine", sdfFormatData.format(richiesta.getData_fine()));
            } else {
                jsonObject.addProperty("data_inizio", sdf.format(richiesta.getData_inizio()));
                jsonObject.addProperty("data_fine", sdf.format(richiesta.getData_fine()));
            }
            jsonObject.addProperty("note", richiesta.getNote());
            jsonObject.addProperty("stato", richiesta.getStato().toString());
            String uploadDate = "";
            if (richiesta.getAllegato() != null) {
                jsonObject.addProperty("allegato", richiesta.getAllegato().getFilename());
                uploadDate = (richiesta.getAllegato().getUploadDate() != null) ? sdf.format(richiesta.getAllegato().getUploadDate()) : "Non disponibile";
                jsonObject.addProperty("uploadDate", uploadDate);
            } else {
                jsonObject.addProperty("allegato", "");
                jsonObject.addProperty("uploadDate", uploadDate);
            }

            if (user.getRuolo().getId() == 1) {
                if (richiesta.getAllegato() != null) {
                    String scarica
                            = "<div class='container-fluid'>"
                            + "<div class='row'>"
                            + "<div class='d-flex'>"
                            + "<div class='col-auto'>"
                            + "<form action='AllegatoServlet' method='POST' target='_blank'>"
                            + "<input type='hidden' name='isDownload' value='true' />"
                            + "<input type = 'hidden' name='filename' value='" + richiesta.getAllegato().getFilename() + "' />"
                            + "<button class='btn Smartoop-btn-standard' style='min-width: 100px;'>Scarica File</button>"
                            + "</form>"
                            + "</div>";

//                    String editForm = "<hr> <button class='btn btn-danger' style='padding: 10px' "
//                            + "onclick=\"$.fancybox.close(); window.location.href='edit_file.jsp?id=" + richiesta.getAllegato().getId() + "';\">"
//                            + "Modifica File</button>";
                    String deleteForm
                            = "<div class='col-auto'>"
                            + "<form id=\"deleteForm\" action='AllegatoServlet' method='POST' onsubmit=\"closeFancyboxAndSubmit(event)\">\n"
                            + "<input type='hidden' name='isDownload' value='true' />"
                            + "    <input type=\"hidden\" name=\"filename\" value=\"" + richiesta.getAllegato().getFilename() + "\" />\n"
                            + "    <input type=\"hidden\" name=\"id\" value=\"" + richiesta.getAllegato().getId() + "\" />\n"
                            + "    <input type=\"hidden\" name=\"idRichiesta\" value=\"" + richiesta.getId() + "\" />\n"
                            + "    <input type=\"hidden\" name=\"delete\" value=\"true\" />\n"
                            + "    <button class='btn Smartoop-btn-standard' style='min-width: 100px; margin-left: 5px;'>Elimina File</button>\n"
                            + "</form>"
                            + "</div>";

                    //jsonObject.addProperty("gestisci", scarica + " " + editForm + " " + deleteForm);
                    jsonObject.addProperty("gestisci", scarica + " " + deleteForm);
                } else {
                    jsonObject.addProperty("gestisci", "");
                }

                dataArray.add(jsonObject);
            } else {

                if (richiesta.getAllegato() != null) {
                    String scarica = "<div class='container-fluid'>"
                            + "<div class='row'>"
                            + "<div class='d-flex'>"
                            + "<div class='col-auto'>"
                            + "<form action='AllegatoServlet' method='POST' target='_blank'>"
                            + "<input type='hidden' name='isDownload' value='true' />"
                            + "<input type = 'hidden' name='filename' value='" + richiesta.getAllegato().getFilename() + "' />"
                            + "<button class='btn Smartoop-btn-standard' style='min-width: 100px;'>Scarica File</button>"
                            + "</form>"
                            + "</div>";

                    String editForm = "<div class='col-auto'>"
                            + "<button class='btn Smartoop-btn-standard' style='min-width: 100px; margin-left: 5px;'"
                            + "onclick=\"$.fancybox.close(); window.location.href='edit_file.jsp?id=" + richiesta.getAllegato().getId() + "&richiestaId=" + richiesta.getId() + "&fancybox=true';\">"
                            + "Modifica File</button>"
                            + "</div>";

                    scarica += editForm + "</div></div>";

                    jsonObject.addProperty("gestisci", scarica);
                } else {
                    jsonObject.addProperty("gestisci", "");
                }

                dataArray.add(jsonObject);
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
    }

    private Richiesta getRichiestaByRichiestaId(Long richiestaId) {
        EntityManagerFactory emf = null;
        EntityManager em = null;

        try {
            emf = Persistence.createEntityManagerFactory("gestionale");
            em = emf.createEntityManager();
            Richiesta richiesta = em.find(Richiesta.class, richiestaId);
            return richiesta;
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
        return null;
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
