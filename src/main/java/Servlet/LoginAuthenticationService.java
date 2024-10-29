package Servlet;

import Entity.Utente;
import static Utils.Utility.estraiEccezione;
import static Utils.Utility.logfile;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

public class LoginAuthenticationService {

    public static int authenticate(String username, String enteredPassword) {
        EntityManagerFactory emf = null;
        EntityManager em = null;
        try {
            emf = Persistence.createEntityManagerFactory("gestionale");
            em = emf.createEntityManager();

            jakarta.persistence.TypedQuery<Utente> query = em.createQuery(
                    "SELECT u FROM Utente u WHERE u.username = :username", Utente.class).setParameter("username", username);

            query.setParameter("username", username);
            query.setMaxResults(1);
            List<Utente> resultList = query.getResultList();

            if (!resultList.isEmpty()) {
                Utente user = resultList.get(0);
                String hashedPasswordFromDatabase = user.getPassword();
                boolean isPasswordValid = BCrypt.checkpw(enteredPassword, hashedPasswordFromDatabase);
                if (isPasswordValid) {
                    return user.getRuolo().getId();

                }
            }
        } catch (Exception e) {
             logfile.severe(estraiEccezione(e));
        } finally {
            em.close();
            emf.close();
        }
        return -1;
    }

    public static boolean isPasswordValid(String username, String enteredPassword) {
        EntityManagerFactory emf = null;
        EntityManager em = null;
        try {
            emf = Persistence.createEntityManagerFactory("gestionale");
            em = emf.createEntityManager();

            jakarta.persistence.TypedQuery<Utente> query = em.createQuery(
                    "SELECT u FROM Utente u WHERE u.username = :username", Utente.class).setParameter("username", username);

            query.setParameter("username", username);
            query.setMaxResults(1);
            List<Utente> resultList = query.getResultList();

            if (!resultList.isEmpty()) {
                Utente user = resultList.get(0);
                String hashedPasswordFromDatabase = user.getPassword();

                return BCrypt.checkpw(enteredPassword, hashedPasswordFromDatabase);
            }
        } catch (Exception e) {
            logfile.severe(estraiEccezione(e));
        } finally {
            em.close();
            emf.close();
        }

        return false;
    }

    public static Utente getUserByUsername(String username) {
        EntityManagerFactory emf = null;
        EntityManager em = null;
        try {
            emf = Persistence.createEntityManagerFactory("gestionale");
            em = emf.createEntityManager();
            jakarta.persistence.TypedQuery<Utente> query = em.createQuery(
                    "SELECT u FROM Utente u WHERE u.username = :username", Utente.class).setParameter("username", username);

            query.setParameter("username", username);
            query.setMaxResults(1);
            List<Utente> resultList = query.getResultList();

            if (!resultList.isEmpty()) {
                return resultList.get(0);

            }
        } catch (Exception e) {
             logfile.severe(estraiEccezione(e));
        } finally {
            em.close();
            emf.close();
        }

        return null;
    }
}
