/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servlet.Login;

import Entity.InfoTrack;
import Entity.Utente;
import Utils.EncryptionUtil;
import Utils.Utility;
import static Utils.Utility.logfile;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 *
 * @author Aldo
 */
public class LoginServlet extends HttpServlet {

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

        String isLogin = request.getParameter("isLogin");

        try {
            if (isLogin.equals("true")) {
                Login(request, response);
            } else {
                Logout(request, response);
            }

        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }

    }

    //LOGIN
    protected void Login(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {

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
                        session.setAttribute("nome", Utility.sanitize(EncryptionUtil.decrypt(user.getNome())));
                        session.setAttribute("user", user);
                        if (user.getRuolo().getId() == 2) {
                            if (user.getStatus() == 0) {
                                response.sendRedirect("index.jsp?esito=KO2&codice=000");
                            } else if (user.getStatus() == 1) {
                                redirectToPageByRole(response, request, userId, roleId);
                                InfoTrack.loginTrack(EncryptionUtil.decrypt(user.getNome()), user);
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //RENDER PAGINE
    private void redirectToPageByRole(HttpServletResponse response, HttpServletRequest request, int userId, int roleId) throws IOException {
        String targetPage;

        String isLogin = request.getParameter("isLogin");

        try {

            if (isLogin.equals("true")) {
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
                    response.sendRedirect("index.jsp?esito=KO6&codice=002");
                }
            } else {
                response.sendRedirect("index.jsp?esito=KO");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //LOGOUT
    protected void Logout(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            HttpSession session = request.getSession();
            Utente userSession = (Utente) session.getAttribute("user");
            InfoTrack.logoutTrack(EncryptionUtil.decrypt(userSession.getNome()), userSession);
            request.getSession().invalidate();

            response.sendRedirect("index.jsp?esito=OK&codice=000");

        } catch (Exception e) {
            logfile.severe(Utility.estraiEccezione(e));

        }
    }

// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
