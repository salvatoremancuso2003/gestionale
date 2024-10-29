/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

import Entity.Email;
import Entity.FileEntity;
import Entity.Pagina;
import Entity.Permesso;
import Entity.Presenza;
import Entity.Richiesta;
import Entity.TipoDocumento;
import Entity.Utente;
import Enum.Tipo_documento_enum;
import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.resource.Emailv31;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import jakarta.servlet.http.HttpSession;
import java.io.File;
import static java.io.File.separator;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.logging.Logger.getLogger;
import java.util.logging.SimpleFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;
import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author Salvatore
 */
public class Utility {

    public static String checkAttribute(HttpSession session, String attribute) {
        try {
            if (session.getAttribute(attribute) != null) {
                return String.valueOf(session.getAttribute(attribute));
            }
        } catch (Exception e) {
            logfile.severe(estraiEccezione(e));
            Logger.getLogger(Utility.class.getName()).log(Level.SEVERE, "ERRORE GENERICO", e);
        }
        return "";
    }

    public static int tryParse(String param) {
        try {
            return Integer.parseInt(param);
        } catch (Exception e) {
            logfile.severe(estraiEccezione(e));
            Logger.getLogger(Utility.class.getName()).log(Level.SEVERE, "ERRORE GENERICO", e);
        }
        return 0;
    }

    public static String sanitize(String input) {
        if (input == null) {
            return null;
        }
        input = input.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
        input = input.replaceAll("[\r\n]", "");
        return StringEscapeUtils.escapeHtml4(input);
    }

    public static String sanitizeFilename(String filename) {
        return filename.replaceAll("[^a-zA-Z0-9._-]", "_");
    }

    public static String sanitizePath(String input) {
        input = input.replaceAll("\\.\\./", "")
                .replaceAll("~", "")
                .replaceAll("\\\\", "/");

        input = input.replaceAll("[^a-zA-Z0-9_./-]", "");
        Path sanitizedPath = Paths.get(input).normalize();

        return sanitizedPath.toString();
    }

    public static Boolean isVisible(String ruolo, String page) {
        if (ruolo == null || ruolo.isEmpty() || page == null || page.isEmpty()) {
            return false;
        }

        EntityManagerFactory emf = null;
        EntityManager em = null;

        try {
            emf = Persistence.createEntityManagerFactory("gestionale");
            em = emf.createEntityManager();
            String jpql = "SELECT p FROM Pagina p WHERE p.nome = :page";
            TypedQuery<Pagina> query = em.createQuery(jpql, Pagina.class
            );
            query.setParameter("page", page);

            Pagina pagina = query.getSingleResult();

            String[] permessi = pagina.getPermessi().split("-");

            for (String permesso : permessi) {
                if (permesso.equals(ruolo)) {
                    return true;
                }
            }

        } catch (Exception e) {
            logfile.severe(estraiEccezione(e));
            Logger.getLogger(Utility.class.getName()).log(Level.SEVERE, "ERRORE GENERICO", e);
            return false;
        } finally {
            if (em != null) {
                em.close();
            }
            if (emf != null) {
                emf.close();
            }
        }
        return false;
    }

    public static final ResourceBundle config = ResourceBundle.getBundle("conf.config");

    public static final String PATHLOG = config.getString("logPath");

    private static final String APPNAME = "Gestionale";
    private static final String PAT_4 = "yyyyMMdd";
    private static final String PAT_9 = "yyMMddHHmmssSSS";

    private static Logger createLog() {
        Logger logger = getLogger(APPNAME);
        try {
            String dataOdierna = new org.joda.time.DateTime().toString(PAT_4);

            File logdir = new File(PATHLOG);
            if (!logdir.exists()) {
                logdir.mkdir();
            }
            String ora = new org.joda.time.DateTime().toString(PAT_9);
            String pathLog = PATHLOG + dataOdierna;
            File dirLog = new File(pathLog);
            if (!dirLog.exists()) {
                dirLog.mkdirs();
            }
            FileHandler fh = new FileHandler(pathLog + separator + APPNAME + "_" + ora + ".log", true);
            logger.addHandler(fh);
            fh.setFormatter(new SimpleFormatter());
            fh.setLevel(Level.ALL);
        } catch (IOException | SecurityException ex) {
            logger.severe(ex.getMessage());
        }
        return logger;
    }

    public static final Logger logfile = createLog();

    public static String estraiEccezione(Exception ec1) {
        try {
            return ec1.getStackTrace()[0].getMethodName() + " - " + getStackTrace(ec1);
        } catch (Exception e) {
            logfile.severe(estraiEccezione(e));
            Logger.getLogger(Utility.class.getName()).log(Level.SEVERE, "ERRORE GENERICO", e);
        }
        return ec1.getMessage();
    }

    public static Utente findUserById(Long id) {
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;

        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("gestionale");
            entityManager = entityManagerFactory.createEntityManager();

            TypedQuery<Utente> query = entityManager.createQuery(
                    "SELECT u FROM Utente u WHERE u.id = :id", Utente.class
            )
                    .setParameter("id", id);

            Utente utente = query.getSingleResult();

            if (utente != null) {
                return utente;
            }

        } catch (Exception e) {
            logfile.severe(estraiEccezione(e));
            return null;
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
            if (entityManagerFactory != null) {
                entityManagerFactory.close();
            }
        }

        return null;
    }
    
    public static Richiesta findRichiestaById(Long id) {
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;

        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("gestionale");
            entityManager = entityManagerFactory.createEntityManager();

            TypedQuery<Richiesta> query = entityManager.createQuery(
                    "SELECT r FROM Richiesta r WHERE r.id = :id", Richiesta.class
            )
                    .setParameter("id", id);

            Richiesta richiesta = query.getSingleResult();

            if (richiesta != null) {
                return richiesta;
            }

        } catch (Exception e) {
            logfile.severe(estraiEccezione(e));
            return null;
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
            if (entityManagerFactory != null) {
                entityManagerFactory.close();
            }
        }

        return null;
    }

    public static Presenza findPresenzaByUserAndDate(Long userId) {
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;

        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("gestionale");
            entityManager = entityManagerFactory.createEntityManager();

            LocalDate today = LocalDate.now();
            Date startOfDay = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date endOfDay = Date.from(today.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());

            TypedQuery<Presenza> query = entityManager.createQuery(
                    "SELECT p FROM Presenza p WHERE p.utente.id = :userId "
                    + "AND p.entrata >= :startOfDay AND p.entrata < :endOfDay", Presenza.class)
                    .setParameter("userId", userId)
                    .setParameter("startOfDay", startOfDay)
                    .setParameter("endOfDay", endOfDay);

            List<Presenza> presenze = query.getResultList();

            if (!presenze.isEmpty()) {
                return presenze.get(0);
            }

        } catch (Exception e) {
            logfile.severe(estraiEccezione(e));
            return null;
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
            if (entityManagerFactory != null) {
                entityManagerFactory.close();
            }
        }

        return null;
    }

    public static List<Utente> getAllUtenti() {
        EntityManagerFactory emf = null;
        EntityManager em = null;

        try {
            emf = Persistence.createEntityManagerFactory("gestionale");
            em = emf.createEntityManager();
            List<Utente> utenti = em.createQuery(
                    "SELECT u FROM Utente u WHERE u.ruolo.id = 2", Utente.class
            ).getResultList();

            if (!utenti.isEmpty()) {
                return utenti;
            }

        } catch (Exception e) {
            logfile.severe(estraiEccezione(e));
            return Collections.emptyList();
        } finally {
            if (em != null) {
                em.close();
            }
            if (emf != null) {
                emf.close();
            }
        }

        return Collections.emptyList();
    }

    public static List<TipoDocumento> getAllTipi() {
        EntityManagerFactory emf = null;
        EntityManager em = null;

        try {
            emf = Persistence.createEntityManagerFactory("gestionale");
            em = emf.createEntityManager();
            List<TipoDocumento> tipi = em.createQuery(
                    "SELECT t FROM TipoDocumento t ", TipoDocumento.class
            ).getResultList();

            if (!tipi.isEmpty()) {
                return tipi;
            }

        } catch (Exception e) {
            logfile.severe(estraiEccezione(e));
            return Collections.emptyList();
        } finally {
            if (em != null) {
                em.close();
            }
            if (emf != null) {
                emf.close();
            }
        }

        return Collections.emptyList();
    }

    public static List<TipoDocumento> getAllTipiSenzaRichiesta() {
        EntityManagerFactory emf = null;
        EntityManager em = null;

        try {
            emf = Persistence.createEntityManagerFactory("gestionale");
            em = emf.createEntityManager();

            List<TipoDocumento> tipi = em.createQuery(
                    "SELECT t FROM TipoDocumento t WHERE t.tipo != :richiestaPermesso", TipoDocumento.class
            ).setParameter("richiestaPermesso", Tipo_documento_enum.RICHIESTA_PERMESSO)
                    .getResultList();
            if (!tipi.isEmpty()) {
                return tipi;
            }

        } catch (Exception e) {
            logfile.severe(estraiEccezione(e));
            return Collections.emptyList();
        } finally {
            if (em != null) {
                em.close();
            }
            if (emf != null) {
                emf.close();
            }
        }

        return Collections.emptyList();
    }

    public static String tipoUtente(Utente utente) {
        try {
            return utente.getRuolo().getNome();
        } catch (Exception e) {

        }
        return "";
    }

    public static List<Permesso> getAllPermessi() {
        EntityManagerFactory emf = null;
        EntityManager em = null;

        try {
            emf = Persistence.createEntityManagerFactory("gestionale");
            em = emf.createEntityManager();
            List<Permesso> permessi = em.createQuery(
                    "SELECT p FROM Permesso p", Permesso.class
            ).getResultList();

            if (!permessi.isEmpty()) {
                return permessi;
            }

        } catch (Exception e) {
            logfile.severe(estraiEccezione(e));
            return Collections.emptyList();
        } finally {
            if (em != null) {
                em.close();
            }
            if (emf != null) {
                emf.close();
            }
        }

        return Collections.emptyList();
    }

    public static List<Presenza> findPresenzeByUserAndDate(Long userId) {
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;

        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("gestionale");
            entityManager = entityManagerFactory.createEntityManager();

            LocalDate today = LocalDate.now();
            Date startOfDay = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date endOfDay = Date.from(today.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());

            TypedQuery<Presenza> query = entityManager.createQuery(
                    "SELECT p FROM Presenza p WHERE p.utente.id = :userId "
                    + "AND p.entrata >= :startOfDay AND p.entrata < :endOfDay", Presenza.class)
                    .setParameter("userId", userId)
                    .setParameter("startOfDay", startOfDay)
                    .setParameter("endOfDay", endOfDay);

            return query.getResultList();

        } catch (Exception e) {
            logfile.severe(estraiEccezione(e));
            return new ArrayList<>();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
            if (entityManagerFactory != null) {
                entityManagerFactory.close();
            }
        }
    }

    public static Presenza findLastPresenzaByUser(Long userId) {
        EntityManager em = Persistence.createEntityManagerFactory("gestionale").createEntityManager();
        try {
            return em.createQuery(
                    "SELECT p FROM Presenza p WHERE p.utente.id = :userId ORDER BY p.entrata DESC", Presenza.class)
                    .setParameter("userId", userId)
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    public static Presenza findPresenzaById(Long presenzaId) {
        EntityManager em = null;
        EntityManagerFactory emf = null;

        try {
            emf = Persistence.createEntityManagerFactory("gestionale");
            em = emf.createEntityManager();

            TypedQuery<Presenza> query = em.createQuery(
                    "SELECT p FROM Presenza p WHERE p.id = :id", Presenza.class
            )
                    .setParameter("id", presenzaId);

            Presenza presenza = query.getSingleResult();

            if (presenza != null) {
                return presenza;
            }

        } catch (Exception e) {
            logfile.severe(estraiEccezione(e));
            return null;
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

    public static FileEntity findFileEntityById(Long id) {
        EntityManager em = null;
        EntityManagerFactory emf = null;

        try {
            emf = Persistence.createEntityManagerFactory("gestionale");
            em = emf.createEntityManager();

            TypedQuery<FileEntity> query = em.createQuery(
                    "SELECT f FROM FileEntity f WHERE f.id = :id", FileEntity.class
            )
                    .setParameter("id", id);

            FileEntity fileEntity = query.getSingleResult();

            if (fileEntity != null) {
                return fileEntity;
            }

        } catch (Exception e) {
            logfile.severe(estraiEccezione(e));
            return null;
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

    public static Richiesta findRichiestaPermessoByPresenza(Presenza presenza) {
        EntityManager em = null;
        EntityManagerFactory emf = null;

        try {
            emf = Persistence.createEntityManagerFactory("gestionale");
            em = emf.createEntityManager();

            TypedQuery<Richiesta> query = em.createQuery(
                    "SELECT r FROM Richiesta r WHERE r.utente = :utente AND r.data_inizio <= :dataInizio AND r.data_fine >= :dataFine",
                    Richiesta.class
            );
            query.setParameter("utente", presenza.getUtente());
            query.setParameter("dataInizio", presenza.getEntrata());
            query.setParameter("dataFine", presenza.getUscita() != null ? presenza.getUscita() : new java.util.Date());

            List<Richiesta> richieste = query.getResultList();
            return richieste.isEmpty() ? null : richieste.get(0);

        } catch (Exception e) {
            logfile.severe(estraiEccezione(e));
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

    public static long calcolaOreRichieste(Date dataInizio, Date dataFine) {
        long diff = dataFine.getTime() - dataInizio.getTime();
        return TimeUnit.MILLISECONDS.toHours(diff);
    }

    public static long calcolaGiorniRichiesti(Date dataInizio, Date dataFine) {
        LocalDate startDate = dataInizio.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endDate = dataFine.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        return ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }

    public static boolean sendEmail(String email, String name, Long id, String usernameUtente, String password) {
        try {
            sendEmailJVM(email, name, id, usernameUtente, password);
            //sendEmailMJ(email, name, id, password);
            logfile.info("Email inviata con successo");
            return true;

        } catch (Exception e) {
            logfile.severe(estraiEccezione(e));
        }
        return false;
    }

    public static void sendEmailMJ(String email, String name, Long id, String password) {

        if (email == null) {
            logfile.warning("Email non inviata perché l'indirizzo email è null");
            return;
        }
        MailjetClient client;
        MailjetRequest request1;
        @SuppressWarnings("unused")
        MailjetResponse response1;

        String apiKey = config.getString("apikey");
        String secretKey = config.getString("secretKey");

        ClientOptions co = ClientOptions.builder()
                .apiKey(apiKey)
                .apiSecretKey(secretKey)
                .build();

        client = new MailjetClient(co);

        try {
            Email email2 = findEmailContent(1L); //email creazione nuova utenza
            String link = config.getString("link");
            final String username = config.getString("username");

            String contentTemplate = email2.getHtmlContent();
            String content = contentTemplate
                    .replace("[name]", name)
                    .replace("[email]", email)
                    .replace("[password]", password)
                    .replace("[link]", link);

            request1 = new MailjetRequest(Emailv31.resource)
                    .property(Emailv31.MESSAGES, new JSONArray()
                            .put(new JSONObject()
                                    .put(Emailv31.Message.FROM, new JSONObject()
                                            .put("Email", username)
                                            .put("Name", "Gestionale SmartOOP")
                                    )
                                    .put(Emailv31.Message.TO, new JSONArray()
                                            .put(new JSONObject()
                                                    .put("Email", email)
                                                    .put("Name", name))
                                    )
                                    .put(Emailv31.Message.SUBJECT, "Creazione nuova utenza")
                                    .put(Emailv31.Message.HTMLPART, content)
                            )
                    );

            response1 = client.post(request1);

        } catch (Exception ex) {
            logfile.severe(estraiEccezione(ex));
        }
    }

    public static void sendEmailJVM(String email, String name, Long id, String usernameUtente, String passwordUtente) {

        if (email == null) {
            logfile.warning("Email non inviata perché l'indirizzo email è null");
            return;
        }
        Properties properties = new Properties();
        final String username = config.getString("username");
        final String password = config.getString("password");
        properties.put("mail.smtp.host", config.getString("host"));
        properties.put("mail.smtp.port", config.getString("port"));
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new jakarta.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username, "Notifica da Gestionale SmartOOP"));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
            message.setSubject("Creazione nuovo utente");

            MimeBodyPart htmlPart = new MimeBodyPart();
            Email email2 = findEmailContent(1L); //email creazione nuova utenza
            String link = config.getString("link");

            String contentTemplate = email2.getHtmlContent();
            String content = contentTemplate
                    .replace("[name]", name)
                    .replace("[email]", usernameUtente)
                    .replace("[password]", passwordUtente)
                    .replace("[link]", link);

            htmlPart.setContent(content, "text/html; charset=utf-8");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(htmlPart);

            message.setContent(multipart);

            Transport.send(message);

        } catch (Exception ex) {
            logfile.severe(estraiEccezione(ex));
        }
    }

    public static Email findEmailContent(Long emailId) {
        EntityManagerFactory emf = null;
        EntityManager em = null;

        try {
            emf = Persistence.createEntityManagerFactory("gestionale");
            em = emf.createEntityManager();

            return em.find(Email.class,
                    emailId);
        } finally {
            if (em != null) {
                em.close();
            }
            if (emf != null) {
                emf.close();
            }
        }
    }

    public static void rimuoviUtenteCompletamente(Utente utente) {
        EntityManagerFactory emf = null;
        EntityManager em = null;
        EntityTransaction et = null;

        try {
            emf = Persistence.createEntityManagerFactory("gestionale");
            em = emf.createEntityManager();
            et = em.getTransaction();
            et.begin();

            utente.setStatus(0);
            em.merge(utente);
            et.commit();
        } catch (Exception e) {
            if (et != null && et.isActive()) {
                et.rollback();

            }
            logfile.severe(estraiEccezione(e));
        } finally {
            if (em != null) {
                em.close();
            }
            if (emf != null) {
                emf.close();
            }
        }
    }

    public static void aggiornaPassword(Utente utente, String password) {
        EntityManagerFactory emf = null;
        EntityManager em = null;
        EntityTransaction et = null;

        try {
            emf = Persistence.createEntityManagerFactory("gestionale");
            em = emf.createEntityManager();
            et = em.getTransaction();
            et.begin();

            utente.setStatus(1);
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            utente.setPassword(hashedPassword);
            em.merge(utente);
            et.commit();
        } catch (Exception e) {
            if (et != null && et.isActive()) {
                et.rollback();

            }
            logfile.severe(estraiEccezione(e));
        } finally {
            if (em != null) {
                em.close();
            }
            if (emf != null) {
                emf.close();
            }
        }
    }

    public static String createNewRandomPassword(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+";
        StringBuilder password = new StringBuilder();

        SecureRandom random = new SecureRandom();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            password.append(characters.charAt(randomIndex));
        }

        return password.toString();
    }

    public static String createNewRandomNumbers(int length) {
        String characters = "1234567890";
        StringBuilder password = new StringBuilder();

        SecureRandom random = new SecureRandom();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            password.append(characters.charAt(randomIndex));
        }

        return password.toString();
    }

    private static final String EMAIL_PATTERN
            = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    public static boolean validateEmail(String email) {
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
