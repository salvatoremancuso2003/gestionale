package Servlet.Utente;

import Entity.InfoTrack;
import Entity.Ruolo;
import Entity.Utente;
import Utils.EncryptionUtil;
import Utils.Utility;
import static Utils.Utility.estraiEccezione;
import static Utils.Utility.logfile;
import static Utils.Utility.tryParse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author Salvatore
 */
public class InserisciNuovoUtente extends HttpServlet {

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
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("gestionale");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();

        try {
            if (!et.isActive()) {
                et.begin();
            }

            HttpSession session = request.getSession();
            Utente AdminUser = (Utente) session.getAttribute("user");
            Utente us = (Utente) request.getSession().getAttribute("user");
            String nome = request.getParameter("nome");
            String cognome = request.getParameter("cognome");
            String email = request.getParameter("email");
            String numero = request.getParameter("numero_di_telefono");
            String us_password = request.getParameter("password");
            String ore_lavorativeString = request.getParameter("ore_lavorative");
            int ore_lavorative = tryParse(ore_lavorativeString);

            boolean isEditPass = Boolean.parseBoolean(request.getParameter("editPass"));
            boolean isUpdate = Boolean.parseBoolean(request.getParameter("update"));
            boolean isRemove = Boolean.parseBoolean(request.getParameter("remove"));
            boolean isRiabilita = Boolean.parseBoolean(request.getParameter("riabilita"));

            Utente utente;
            if (isEditPass) {
                try {
                    if (us != null) {
                        utente = em.find(Utente.class, us.getId());
                        Utility.aggiornaPassword(utente, us_password);
                        logfile.info("Password aggiornata con successo!");
                        response.getWriter().write("Password aggiornata con successo!");
                        response.sendRedirect("index.jsp?esito=OK2&codice=000");
                    } else {
                        response.getWriter().write("Utente non trovato");
                        response.sendRedirect("index.jsp?esito=KO3&codice=000");
                    }
                } catch (Exception e) {
                    if (et.isActive()) {
                        et.rollback();
                    }
                    logfile.severe(estraiEccezione(e));
                    response.getWriter().write("Errore durante l'aggiornamento della password.");
                    response.sendRedirect("index.jsp?esito=KO4&codice=000");
                }
            }

            if (isRemove) {
                String userIdParam = request.getParameter("userId");
                Long userId = null;

                if (userIdParam != null && !userIdParam.isEmpty()) {
                    userId = Long.valueOf(userIdParam);
                }

                try {
                    if (userId != null) {
                        utente = em.find(Utente.class, userId);
                        Utility.rimuoviUtenteCompletamente(utente, false);
                        logfile.info("Utente rimosso con successo!");
                    } else {
                        response.getWriter().write("Errore: userId non valido.");
                    }
                } catch (Exception e) {
                    if (et.isActive()) {
                        et.rollback();
                    }
                    logfile.severe(estraiEccezione(e));
                }
            } else if (isRiabilita) {
                String userIdParam = request.getParameter("userId");
                Long userId = null;

                if (userIdParam != null && !userIdParam.isEmpty()) {
                    userId = Long.valueOf(userIdParam);
                }

                try {
                    if (userId != null) {
                        utente = em.find(Utente.class, userId);
                        Utility.rimuoviUtenteCompletamente(utente, true);
                        logfile.info("Utente riattivato con successo!");
                    } else {
                        response.getWriter().write("Errore: userId non valido.");
                    }
                } catch (Exception e) {
                    if (et.isActive()) {
                        et.rollback();
                    }
                    logfile.severe(estraiEccezione(e));
                }
            }
            if (isUpdate && !isRemove) {
                String nuovoNome = request.getParameter("nuovoNome");
                String nuovaEmail = request.getParameter("nuovaEmail");
                String nuovoCognome = request.getParameter("nuovoCognome");
                String nuovoNumero = request.getParameter("nuovoNumero");
                String userIdParam = request.getParameter("userId");
                String ferieParam = request.getParameter("ferie");
                String oreParam = request.getParameter("ore");
                String ore_lavorative_param = request.getParameter("nuove_ore_lavorative");
                int nuove_ore_lavorative = tryParse(ore_lavorative_param);
                int ferie = tryParse(ferieParam);
                int ore = tryParse(oreParam);

                utente = em.find(Utente.class, Long.valueOf(userIdParam));
                utente.setNome(EncryptionUtil.encrypt(nuovoNome));
                utente.setCognome(EncryptionUtil.encrypt(nuovoCognome));
                utente.setEmail(nuovaEmail);
                utente.setNumero_di_telefono("+39" + nuovoNumero);
                utente.setOre_contratto(nuove_ore_lavorative);
                utente.setFerie_disponibili(ferie);
                utente.setOre_disponibili(ore);
                em.merge(utente);
                logfile.info("Utente aggiornato con successo!");
                response.sendRedirect("edit_user.jsp?esito=OK&codice=007&userId=" + userIdParam);
            } else if (!isRemove && !isUpdate) {
                utente = new Utente();
                utente.setNome(EncryptionUtil.encrypt(nome));
                utente.setCognome(EncryptionUtil.encrypt(cognome));
                utente.setEmail(email);
                String username = "US_" + Utility.createNewRandomNumbers(2) + "." + email;
                utente.setUsername(username);
                utente.setNumero_di_telefono("+39" + numero);
                utente.setOre_contratto(ore_lavorative);
                String password = Utility.createNewRandomPassword(8);
                String nomeCompleto = EncryptionUtil.decrypt(utente.getNome()) + " " + EncryptionUtil.decrypt(utente.getCognome());

                if (Utility.validateEmail(email)) {
                    Utility.sendEmail(utente.getEmail(), nomeCompleto, utente.getId(), username, password);
                }
                String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
                utente.setPassword(hashedPassword);
                utente.setStatus(2);
                Ruolo ruolo = new Ruolo();
                ruolo.setId(2);
                utente.setRuolo(ruolo);
                em.persist(utente);
                logfile.info("Utente creato con successo!");
                response.sendRedirect("AD_gestioneUtente.jsp?esito=OK&codice=006");
                InfoTrack.insertNewUser(EncryptionUtil.decrypt(AdminUser.getNome()), AdminUser);
            }

            et.commit();

        } catch (Exception e) {
            if (et.isActive()) {
                et.rollback();
            }
            logfile.severe(estraiEccezione(e));
        } finally {
            em.close();
            emf.close();
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
