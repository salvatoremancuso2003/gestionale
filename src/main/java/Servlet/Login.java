/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Servlet;

import Entity.InfoTrack;
import Entity.Utente;
import Utils.Utility;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 *
 * @author Salvatore
 */
public class Login extends HttpServlet {

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
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (LoginAuthenticationService.isPasswordValid(username, password)) {
            int roleId = LoginAuthenticationService.authenticate(username, password);
            if (roleId != -1) {
                Utente user = LoginAuthenticationService.getUserByUsername(username);
                if (request.getContextPath().contains("gestionale")) {
                    request.getSession().setAttribute("src", "../..");
                }

                if (user != null) {
                    HttpSession session = request.getSession();
                    session.setAttribute("userId", user.getId());
                    String userIdParam = session.getAttribute("userId").toString();
                    int userId = Utility.tryParse(userIdParam);
                    session.setAttribute("username", Utility.sanitize(user.getUsername()));
                    session.setAttribute("nome", Utility.sanitize(user.getNome()));
                    session.setAttribute("user", user);
                    if (user.getRuolo().getId() == 2) {
                        if (user.getStatus() == 0) {
                            response.sendRedirect("index.jsp?esito=KO2&codice=000");
                        } else if (user.getStatus() == 1) {
                            redirectToPageByRole(response, request, userId, roleId);
                            InfoTrack.loginTrack(username, user);
                        } else if (user.getStatus() == 2) {
                            response.sendRedirect("edit_password.jsp");
                        }
                    } else if (user.getRuolo().getId() == 1) {
                        redirectToPageByRole(response, request, userId, roleId);
                    }

                } else {
                    response.sendRedirect("index.jsp?esito=KO&codice=000");
                }
            } else {
                response.sendRedirect("index.jsp?esito=KO&codice=000");
            }
        } else {
            response.sendRedirect("index.jsp?esito=KO&codice=000");
        }
    }

    private void redirectToPageByRole(HttpServletResponse response, HttpServletRequest request, int userId, int roleId) throws IOException {
        String targetPage;

        switch (roleId) {
            case 1:
                targetPage = "AD_gestionale.jsp";
                break;

            case 2:
                targetPage = "US_gestionale.jsp";
                break;

            default:
                targetPage = "";
                break;
        }

        if (!targetPage.isEmpty()) {
            response.sendRedirect(response.encodeRedirectURL(targetPage));
        } else {
            response.sendRedirect("index.jsp?esito=KO");
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
