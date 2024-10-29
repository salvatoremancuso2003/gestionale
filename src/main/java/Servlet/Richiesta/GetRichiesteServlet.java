package Servlet.Richiesta;

import Entity.Richiesta;
import static Utils.Utility.estraiEccezione;
import static Utils.Utility.logfile;
import java.text.SimpleDateFormat;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.persistence.TypedQuery;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class GetRichiesteServlet extends HttpServlet {

    /* Processes requests for both HTTP <code>GET</code
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
    private static final long serialVersionUID = 1L;

    private EntityManagerFactory emf;

    @Override
    public void init() throws ServletException {
        emf = Persistence.createEntityManagerFactory("gestionale");
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = emf.createEntityManager();
        try {
            String dataInizioStr = request.getParameter("dataInizio");
            String dataFineStr = request.getParameter("dataFine");
            String utenteIdStr = request.getParameter("utente");
            String tipoPermesso = request.getParameter("permesso");

            StringBuilder queryString = new StringBuilder("SELECT r FROM Richiesta r WHERE 1=1");

            if (dataInizioStr != null && !dataInizioStr.isEmpty()) {
                queryString.append(" AND r.data_inizio >= :dataInizio");
            }

            if (dataFineStr != null && !dataFineStr.isEmpty()) {
                queryString.append(" AND r.data_fine <= :dataFine");
            }

            if (utenteIdStr != null && !utenteIdStr.equals("Qualsiasi")) {
                queryString.append(" AND r.utente.id = :utenteId");
            }

            if (tipoPermesso != null && !tipoPermesso.equals("Qualsiasi")) {
                queryString.append(" AND r.tipo_permesso.codice = :tipoPermesso");
            }

            TypedQuery<Richiesta> query = em.createQuery(queryString.toString(), Richiesta.class);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            if (dataInizioStr != null && !dataInizioStr.isEmpty()) {
                Date dataInizio = sdf.parse(dataInizioStr);
                query.setParameter("dataInizio", dataInizio);
            }

            if (dataFineStr != null && !dataFineStr.isEmpty()) {
                Date dataFine = sdf.parse(dataFineStr);

                Calendar cal = Calendar.getInstance();
                cal.setTime(dataFine);
                cal.set(Calendar.HOUR_OF_DAY, 23);
                cal.set(Calendar.MINUTE, 59);
                cal.set(Calendar.SECOND, 59);
                cal.set(Calendar.MILLISECOND, 999);

                query.setParameter("dataFine", cal.getTime());
            }

            if (utenteIdStr != null && !utenteIdStr.equals("Qualsiasi")) {
                Long utenteId = Long.parseLong(utenteIdStr);
                query.setParameter("utenteId", utenteId);
            }

            if (tipoPermesso != null && !tipoPermesso.equals("Qualsiasi")) {
                query.setParameter("tipoPermesso", Long.valueOf(tipoPermesso));
            }

            List<Richiesta> richieste = query.getResultList();

            JsonArray dataArray = new JsonArray();
            SimpleDateFormat sdfOut = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

            for (Richiesta richiesta : richieste) {
                JsonObject rc = new JsonObject();
                rc.addProperty("id", richiesta.getId());
                rc.addProperty("tipoPermesso", richiesta.getTipo_permesso().getDescrizione());
                rc.addProperty("nomeUtente", richiesta.getUtente().getNome() + " " + richiesta.getUtente().getCognome());
                rc.addProperty("dataInizio", sdfOut.format(richiesta.getData_inizio()));
                rc.addProperty("dataFine", sdfOut.format(richiesta.getData_fine()));
                LocalDateTime dataInizio = richiesta.getData_inizio().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                LocalDateTime dataFine = richiesta.getData_fine().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                long oreRichieste = calcolaGiorniRichiestiEscludendoWeekend(richiesta.getData_inizio(), richiesta.getData_fine());
                boolean supera = false;

                SimpleDateFormat formatter = new SimpleDateFormat("EEEE dd MMMM yyyy ", Locale.ITALIAN);
                String dataInizioFormattata = formatter.format(richiesta.getData_inizio());
                String dataFineFormattata = formatter.format(richiesta.getData_fine());

                // Controllo disponibilità in base al tipo di permesso
                if (richiesta.getTipo_permesso().getDescrizione().equalsIgnoreCase("Ferie")) {
                    if (oreRichieste > richiesta.getUtente().getFerie_disponibili()) {
                        supera = true;
                    }
                } else if (richiesta.getTipo_permesso().getDescrizione().equalsIgnoreCase("Permesso_studio")) {
                    if (oreRichieste > richiesta.getUtente().getOre_disponibili()) {
                        supera = true;
                    }
                }
                String action = "";
                switch (richiesta.getStato()) {
                    case IN_ATTESA:
                        action = "<button class='btn btn-warning d-flex align-items-center' title='GESTISCI RICHIESTA' id='" + richiesta.getId() + "' onclick=\"mostraModalConferma("
                                + supera
                                + "," + richiesta.getId()
                                + ",'" + richiesta.getUtente().getNome() + "'"
                                + ",'" + richiesta.getTipo_permesso().getDescrizione() + "'"
                                + ",'" + dataInizioFormattata + "'"
                                + ",'" + dataFineFormattata + "'"
                                + ");\">"
                                + "<svg xmlns='http://www.w3.org/2000/svg' width='16' height='16' fill='white' class='bi bi-info-circle me-2' viewBox='0 0 16 16'>"
                                + "  <path d='M8 15A7 7 0 1 1 8 1a7 7 0 0 1 0 14m0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16'/>"
                                + "  <path d='m8.93 6.588-2.29.287-.082.38.45.083c.294.07.352.176.288.469l-.738 3.468c-.194.897.105 1.319.808 1.319.545 0 1.178-.252 1.465-.598l.088-.416c-.2.176-.492.246-.686.246-.275 0-.375-.193-.304-.533zM9 4.5a1 1 0 1 1-2 0 1 1 0 0 1 2 0'/>"
                                + "</svg><span style='color:white'>Gestisci richiesta</span></button>";
                        break;

                    case RIGETTATA:
                        action = "<button class='btn Smartoop-btn-error d-flex align-items-center' title='RIGETTATA' id='" + richiesta.getId() + "'>"
                                + "<svg xmlns='http://www.w3.org/2000/svg' width='16' height='16' fill='white' class='bi bi-x-circle me-2' viewBox='0 0 16 16'>"
                                + "  <path d='M8 15A7 7 0 1 1 8 1a7 7 0 0 1 0 14m0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16'/>"
                                + "  <path d='M4.646 4.646a.5.5 0 0 1 .708 0L8 7.293l2.646-2.647a.5.5 0 0 1 .708.708L8.707 8l2.647 2.646a.5.5 0 0 1-.708.708L8 8.707l-2.646 2.647a.5.5 0 0 1-.708-.708L7.293 8 4.646 5.354a.5.5 0 0 1 0-.708'/>"
                                + "</svg><span style='color:white'>Rifiutata</span></button>";
                        break;

                    case APPROVATA:
                        action = "<button class='btn btn-success d-flex align-items-center' title='ACCETTATA' id='" + richiesta.getId() + "' onclick=\"(" + richiesta.getId() + ");\">"
                                + "<svg xmlns='http://www.w3.org/2000/svg' width='16' height='16' fill='white' class='bi bi-check-circle me-2' viewBox='0 0 16 16'>"
                                + "  <path d='M8 15A7 7 0 1 0 8 1a7 7 0 0 0 0 14zM6.93 9.293 4.854 7.146a.5.5 0 1 1 .708-.708L7.293 8.293l3.147-3.146a.5.5 0 0 1 .708.708l-3.5 3.5a.5.5 0 0 1-.708 0z'/>"
                                + "</svg><span style='color:white'>Approvata</span></button>";
                        break;

                    default:
                        break;
                }

                rc.addProperty("Gestisci", action);
                dataArray.add(rc);
            }

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(dataArray.toString());
            out.flush();
        } catch (Exception e) {
            logfile.severe(estraiEccezione(e));
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while processing the request");
        } finally {
            em.close();
        }
    }

    // Calcola i giorni richiesti escludendo i weekend
    private long calcolaGiorniRichiestiEscludendoWeekend(Date dataInizio, Date dataFine) {
        Calendar start = Calendar.getInstance();
        start.setTime(dataInizio);
        Calendar end = Calendar.getInstance();
        end.setTime(dataFine);

        long giorniRichiesti = 0;

        while (!start.after(end)) {
            int dayOfWeek = start.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY) {
                giorniRichiesti++; // Conta solo se non è un weekend
            }
            start.add(Calendar.DATE, 1);
        }

        return giorniRichiesti;
    }

    public static final String ITOTALRECORDS = "iTotalRecords";
    public static final String ITOTALDISPLAY = "iTotalDisplayRecords";
    public static final String SECHO = "sEcho";
    public static final String SCOLUMS = "sColumns";
    public static final String APPJSON = "application/json";
    public static final String CONTENTTYPE = "Content-Type";
    public static final String AADATA = "aaData";

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
