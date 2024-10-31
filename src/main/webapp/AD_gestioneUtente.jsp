<%-- 
    Document   : AD_gestionale
    Created on : 3 set 2024, 12:02:42
    Author     : Salvatore
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="Utils.Utility"%>
<%@page import="Entity.Utente"%>
<%@page import="java.time.LocalDate"%>
<%@page import="java.time.ZoneId"%>
<%@page import="java.util.Date"%>

<%    
    String userId = Utility.checkAttribute(session, "userId");
    String ruolo = null;
    String pageName = null;

    Utente utente = (Utente) session.getAttribute("user");
    if (utente == null) {
        response.sendRedirect("index.jsp");
    } else {
        String uri = request.getRequestURI();
        pageName = uri.substring(uri.lastIndexOf("/") + 1);
        ruolo = String.valueOf(utente.getRuolo().getId());
        if (!Utility.isVisible(ruolo, pageName)) {
            response.sendRedirect(request.getContextPath() + "/error_page_403.jsp");
        } else {
            String src = Utility.checkAttribute(session, ("src"));
        }
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Admin Page</title>
        <!-- Datatable css -->
        <link rel="stylesheet" type="text/css" href="css/External/dataTables.bootstrap5.css">
        <!--Bootstrap css -->
        <link rel="stylesheet" href="css/custom/SmartOOP-bootstrap.min.css"/>
        <!-- Custom Global Css -->
        <link rel="stylesheet" href="css/custom/global.css"/>
        <!-- Bootstrap Icons -->
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css">
        <!-- Fancybox CSS -->
        <link rel="stylesheet" href="css/External/jquery.fancybox.css"/>

        <link rel="icon" href="assets/logo.png"/>
    </head>
    <body>

        <!-- Navbar -->
        <nav class="navbar navbar-expand-lg Smartoop-bg-standard navbar-SmartOOP-standard fixed-top w-100">
            <div class="container-fluid">
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarSupportedContent">
                    <ul class="navbar-nav me-auto">
                        <li class="nav-item">
                            <a class="nav-link  d-flex align-items-center" href="AD_gestionale.jsp">
                                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-house-door" viewBox="0 0 16 16">
                                <path d="M8.354 1.146a.5.5 0 0 0-.708 0l-6 6A.5.5 0 0 0 1.5 7.5v7a.5.5 0 0 0 .5.5h4.5a.5.5 0 0 0 .5-.5v-4h2v4a.5.5 0 0 0 .5.5H14a.5.5 0 0 0 .5-.5v-7a.5.5 0 0 0-.146-.354L13 5.793V2.5a.5.5 0 0 0-.5-.5h-1a.5.5 0 0 0-.5.5v1.293zM2.5 14V7.707l5.5-5.5 5.5 5.5V14H10v-4a.5.5 0 0 0-.5-.5h-3a.5.5 0 0 0-.5.5v4z"/>
                                </svg>
                                <span style="padding-left: 4px">HOME</span>
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link  d-flex align-items-center" href="AD_calendar.jsp">
                                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-calendar3" viewBox="0 0 16 16">
                                <path d="M14 0H2a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V2a2 2 0 0 0-2-2M1 3.857C1 3.384 1.448 3 2 3h12c.552 0 1 .384 1 .857v10.286c0 .473-.448.857-1 .857H2c-.552 0-1-.384-1-.857z"/>
                                <path d="M6.5 7a1 1 0 1 0 0-2 1 1 0 0 0 0 2m3 0a1 1 0 1 0 0-2 1 1 0 0 0 0 2m3 0a1 1 0 1 0 0-2 1 1 0 0 0 0 2m-9 3a1 1 0 1 0 0-2 1 1 0 0 0 0 2m3 0a1 1 0 1 0 0-2 1 1 0 0 0 0 2m3 0a1 1 0 1 0 0-2 1 1 0 0 0 0 2m3 0a1 1 0 1 0 0-2 1 1 0 0 0 0 2m-9 3a1 1 0 1 0 0-2 1 1 0 0 0 0 2m3 0a1 1 0 1 0 0-2 1 1 0 0 0 0 2m3 0a1 1 0 1 0 0-2 1 1 0 0 0 0 2"/>
                                </svg>
                                <span style="padding-left: 4px">CALENDARIO</span>
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link d-flex align-items-center" href="AD_presenze.jsp">
                                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-clock-history" viewBox="0 0 16 16">
                                <path d="M8.515 1.019A7 7 0 0 0 8 1V0a8 8 0 0 1 .589.022zm2.004.45a7 7 0 0 0-.985-.299l.219-.976q.576.129 1.126.342zm1.37.71a7 7 0 0 0-.439-.27l.493-.87a8 8 0 0 1 .979.654l-.615.789a7 7 0 0 0-.418-.302zm1.834 1.79a7 7 0 0 0-.653-.796l.724-.69q.406.429.747.91zm.744 1.352a7 7 0 0 0-.214-.468l.893-.45a8 8 0 0 1 .45 1.088l-.95.313a7 7 0 0 0-.179-.483m.53 2.507a7 7 0 0 0-.1-1.025l.985-.17q.1.58.116 1.17zm-.131 1.538q.05-.254.081-.51l.993.123a8 8 0 0 1-.23 1.155l-.964-.267q.069-.247.12-.501m-.952 2.379q.276-.436.486-.908l.914.405q-.24.54-.555 1.038zm-.964 1.205q.183-.183.35-.378l.758.653a8 8 0 0 1-.401.432z"/>
                                <path d="M8 1a7 7 0 1 0 4.95 11.95l.707.707A8.001 8.001 0 1 1 8 0z"/>
                                <path d="M7.5 3a.5.5 0 0 1 .5.5v5.21l3.248 1.856a.5.5 0 0 1-.496.868l-3.5-2A.5.5 0 0 1 7 9V3.5a.5.5 0 0 1 .5-.5"/>
                                </svg>
                                <span style="padding-left: 4px">TABELLA PRESENZE</span>
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link  d-flex align-items-center" href="AD_attached.jsp">
                                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-file-earmark-text" viewBox="0 0 16 16">
                                <path d="M5.5 7a.5.5 0 0 0 0 1h5a.5.5 0 0 0 0-1zM5 9.5a.5.5 0 0 1 .5-.5h5a.5.5 0 0 1 0 1h-5a.5.5 0 0 1-.5-.5m0 2a.5.5 0 0 1 .5-.5h2a.5.5 0 0 1 0 1h-2a.5.5 0 0 1-.5-.5"/>
                                <path d="M9.5 0H4a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h8a2 2 0 0 0 2-2V4.5zm0 1v2A1.5 1.5 0 0 0 11 4.5h2V14a1 1 0 0 1-1 1H4a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1z"/>
                                </svg>
                                <span style="padding-left: 4px">DOCUMENTAZIONE</span>
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link  d-flex align-items-center" href="AD_richiestePermessi.jsp">
                                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-alarm" viewBox="0 0 16 16">
                                <path d="M8.5 5.5a.5.5 0 0 0-1 0v3.362l-1.429 2.38a.5.5 0 1 0 .858.515l1.5-2.5A.5.5 0 0 0 8.5 9z"/>
                                <path d="M6.5 0a.5.5 0 0 0 0 1H7v1.07a7.001 7.001 0 0 0-3.273 12.474l-.602.602a.5.5 0 0 0 .707.708l.746-.746A6.97 6.97 0 0 0 8 16a6.97 6.97 0 0 0 3.422-.892l.746.746a.5.5 0 0 0 .707-.708l-.601-.602A7.001 7.001 0 0 0 9 2.07V1h.5a.5.5 0 0 0 0-1zm1.038 3.018a6 6 0 0 1 .924 0 6 6 0 1 1-.924 0M0 3.5c0 .753.333 1.429.86 1.887A8.04 8.04 0 0 1 4.387 1.86 2.5 2.5 0 0 0 0 3.5M13.5 1c-.753 0-1.429.333-1.887.86a8.04 8.04 0 0 1 3.527 3.527A2.5 2.5 0 0 0 13.5 1"/>
                                </svg>
                                <span style="padding-left: 4px">RICHIESTE PERMESSI</span>
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link active d-flex align-items-center" href="AD_gestioneUtente.jsp">
                                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-person-fill-gear" viewBox="0 0 16 16">
                                <path d="M11 5a3 3 0 1 1-6 0 3 3 0 0 1 6 0m-9 8c0 1 1 1 1 1h5.256A4.5 4.5 0 0 1 8 12.5a4.5 4.5 0 0 1 1.544-3.393Q8.844 9.002 8 9c-5 0-6 3-6 4m9.886-3.54c.18-.613 1.048-.613 1.229 0l.043.148a.64.64 0 0 0 .921.382l.136-.074c.561-.306 1.175.308.87.869l-.075.136a.64.64 0 0 0 .382.92l.149.045c.612.18.612 1.048 0 1.229l-.15.043a.64.64 0 0 0-.38.921l.074.136c.305.561-.309 1.175-.87.87l-.136-.075a.64.64 0 0 0-.92.382l-.045.149c-.18.612-1.048.612-1.229 0l-.043-.15a.64.64 0 0 0-.921-.38l-.136.074c-.561.305-1.175-.309-.87-.87l.075-.136a.64.64 0 0 0-.382-.92l-.148-.045c-.613-.18-.613-1.048 0-1.229l.148-.043a.64.64 0 0 0 .382-.921l-.074-.136c-.306-.561.308-1.175.869-.87l.136.075a.64.64 0 0 0 .92-.382zM14 12.5a1.5 1.5 0 1 0-3 0 1.5 1.5 0 0 0 3 0"/>
                                </svg>
                                <span style="padding-left: 4px">GESTIONE UTENTI</span>
                            </a>
                        </li>
                    </ul>
                    <div class="ms-auto d-flex align-items-center">
                        <span class="text-white me-3">
                            <svg xmlns="http://www.w3.org/2000/svg" width="25" height="20" fill="currentColor" class="bi bi-person-check" viewBox="0 0 16 16">
                            <path d="M12.5 16a3.5 3.5 0 1 0 0-7 3.5 3.5 0 0 0 0 7m1.679-4.493-1.335 2.226a.75.75 0 0 1-1.174.144l-.774-.773a.5.5 0 0 1 .708-.708l.547.548 1.17-1.951a.5.5 0 1 1 .858.514M11 5a3 3 0 1 1-6 0 3 3 0 0 1 6 0M8 7a2 2 0 1 0 0-4 2 2 0 0 0 0 4"/>
                            <path d="M8.256 14a4.5 4.5 0 0 1-.229-1.004H3c.001-.246.154-.986.832-1.664C4.484 10.68 5.711 10 8 10q.39 0 .74.025c.226-.341.496-.65.804-.918Q8.844 9.002 8 9c-5 0-6 3-6 4s1 1 1 1z"/>
                            </svg>
                            <%= utente.getNome().toUpperCase() + " " + utente.getCognome().toUpperCase() + " - " + Utility.tipoUtente(utente).toUpperCase() %>
                        </span>
                        <button type="button" class="btn Smartoop-btn-outline-light" id="logoutButton" onclick="logout()">
                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-box-arrow-right" viewBox="0 0 16 16">
                            <path fill-rule="evenodd" d="M10 12.5a.5.5 0 0 1-.5.5h-8a.5.5 0 0 1-.5-.5v-9a.5.5 0 0 1 .5-.5h8a.5.5 0 0 1 .5.5v2a.5.5 0 0 0 1 0v-2A1.5 1.5 0 0 0 9.5 2h-8A1.5 1.5 0 0 0 0 3.5v9A1.5 1.5 0 0 0 1.5 14h8a1.5 1.5 0 0 0 1.5-1.5v-2a.5.5 0 0 0-1 0z"/>
                            <path fill-rule="evenodd" d="M15.854 8.354a.5.5 0 0 0 0-.708l-3-3a.5.5 0 0 0-.708.708L14.293 7.5H5.5a.5.5 0 0 0 0 1h8.793l-2.147 2.146a.5.5 0 0 0 .708.708z"/>
                            </svg>
                            ESCI
                        </button>
                    </div>
                </div>
            </div>
        </nav>



        <!-- Contenuto principale -->
        <main class="container mt-5">
            <div class="row justify-content-center">
                <div class="col-md-8">
                    <!-- Card principale -->
                    <div class="card p-4 mb-4 bg-white rounded" style="box-shadow: 0 4px 15px rgba(220,53,69, 0.3);">
                        <div class="text-center mb-4">
                            <img src="assets/logo.png" class="img-fluid responsive-img" alt="Logo SmartOOP" style="max-width: 150px;">
                        </div>
                        <div class="col-md-12 text-center">
                            <h1 class="display-4 SmartOOP-text-standard">Gestione Utenti</h1>
                        </div>
                    </div>
                </div>
            </div>
        </main>
        <br>
        <div>
            <div class="container">
                <form id="creaUtenteForm" action="InserisciNuovoUtente" method="POST"
                      style="display: block">
                    <br>
                    <div class="mb-3">
                        <label for="nome" class="form-label SmartOOP-text-standard" style="font-weight: bold">Nome:</label>
                        <input type="text" class="form-control" id="nome" name="nome" required>
                    </div>

                    <div class="mb-3">
                        <label for="cognome" class="form-label SmartOOP-text-standard" style="font-weight: bold">Cognome:</label>
                        <input type="text" class="form-control" id="cognome" name="cognome" required>
                    </div>

                    <div class="mb-3">
                        <label for="email" class="form-label SmartOOP-text-standard" style="font-weight: bold">Email:</label>
                        <input type="email" class="form-control" id="email" name="email" required>
                    </div>
                    <div class="mb-3">
                        <label for="numero_di_telefono" class="form-label SmartOOP-text-standard" style="font-weight: bold">Numero di telefono</label>
                        <div class="input-group">
                            <span class="input-group-text">+39</span> 
                            <input type="text" maxlength="10" minlength="10" class="form-control" id="numero_di_telefono" name="numero_di_telefono" required>
                        </div>
                        <div id="error-message" class="SmartOOP-text-standard" style="display: none;">Il numero di telefono deve contenere esattamente 10 cifre.</div>
                    </div>

                    <script>
                        document.getElementById('numero_di_telefono').addEventListener('input', function (e) {
                            this.value = this.value.replace(/\D/g, '');
                        });

                        document.querySelector('form').addEventListener('submit', function (e) {
                            const numeroDiTelefono = document.getElementById('numero_di_telefono').value;
                            const errorMessage = document.getElementById('error-message');

                            if (numeroDiTelefono.length !== 10) {
                                e.preventDefault();
                                errorMessage.style.display = 'block';
                            } else {
                                errorMessage.style.display = 'none';
                            }
                        });
                    </script>

                    <div class="mb-3">
                        <label for="ore_lavorative" class="form-label SmartOOP-text-standard" style="font-weight: bold">Ore contratto</label>
                        <div class="input-group">
                            <input type="text" maxlength="1" minlength="1" class="form-control" id="ore_lavorative" name="ore_lavorative" required>
                        </div>
                        <div id="error-message2" class="SmartOOP-text-standard" style="display: none;">Le ore lavorative non possono essere 0.</div>
                    </div>

                    <script>
                        document.getElementById('ore_lavorative').addEventListener('input', function (e) {
                            this.value = this.value.replace(/\D/g, '');
                        });

                        document.querySelector('form').addEventListener('submit', function (e) {
                            const ore_lavorative = document.getElementById('ore_lavorative').value;
                            const errorMessage = document.getElementById('error-message2');

                            if (parseInt(ore_lavorative) === 0) {
                                e.preventDefault();
                                errorMessage.style.display = 'block';
                            } else {
                                errorMessage.style.display = 'none';
                            }
                        });
                    </script>




                    <div class="text-center">
                        <button type="submit" class="btn Smartoop-btn-standard" id="CreaUtente">Crea Utente</button>
                    </div>
                </form>

                <div class="table-responsive">
                    <table id="UtenteTable" class="table table-striped table-hover display-responsive table-bordered" style="width:100%; white-space: nowrap">
                        <thead>
                            <tr>
                                <th class="SmartOOP-text-standard">Id</th>
                                <th class="SmartOOP-text-standard">Nome </th>
                                <th class="SmartOOP-text-standard">Cognome </th>
                                <th class="SmartOOP-text-standard">Email </th>
                                <th class="SmartOOP-text-standard">Numero </th>
                                <th class="SmartOOP-text-standard">Ferie disponibili </th>
                                <th class="SmartOOP-text-standard">Ore disponibili </th>
                                <th class="SmartOOP-text-standard">Ore contratto </th>
                                <th class="SmartOOP-text-standard">Stato </th>
                                <th class="SmartOOP-text-standard">Gestisci</th>
                            </tr>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <br>
        <br>


        <div class="modal fade" id="esitoModal" tabindex="-1" aria-labelledby="esitoModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header" id="modal-header">
                        <h5 class="modal-title" id="esitoModalLabel">Esito Operazione</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body" id="esitoModalBody">

                    </div>
                    <div class="modal-footer">
                        <button type="button" id="esitoModalButton" class="btn" data-bs-dismiss="modal">Chiudi</button>
                    </div>
                </div>
            </div>
        </div>

        <div class="modal fade" id="confermaRiabilitazioneModal" tabindex="-1" aria-labelledby="confermaRiabilitazioneModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header bg-SmartOOP-standard text-white">
                        <h5 class="modal-title" id="confermaEliminazioneModalLabel">Conferma Riabilitazione</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        Sei sicuro di voler riabilitare l'utente <strong id="nomeUtenteModal"></strong>? 
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Annulla</button>
                        <button type="button" class="btn Smartoop-btn-outline-success" id="confermaRiabilitaButton">Riabilita</button>
                    </div>
                </div>
            </div>
        </div>

        <div class="modal fade" id="confermaEstrazioneModal" tabindex="-1" aria-labelledby="confermaEstrazioneModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header Smartoop-bg-standard">
                        <h5 class="modal-title text-white" id="confermaEstrazioneModalLabel">Estrazione</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        Quale mese vuoi estrarre per <strong id="nomeUtenteModal2"></strong>?
                        <input type="date" id="data" class="form-control" name="data">
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Annulla</button>
                        <button type="button" class="btn Smartoop-btn-outline-success" id="confermaEstrazioneButton">Conferma</button>
                    </div>
                </div>
            </div>
        </div>

        <br>
        <!-- Footer -->
        <%
            Date date = new Date();
            LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int year = localDate.getYear();
        %>
        <footer class="text-white text-lg-center Smartoop-bg-standard">
            <p>&copy; <%=year%> SmartOOP. Tutti i diritti riservati. | Contattaci: info@smartoop.it</p>
        </footer>

        <script src="js/bootstrap.bundle.min.js"></script>
        <script src="js/custom/global.js"></script>
        <script src="js/custom/globalModal.js"></script>
        <!-- jQuery e Bootstrap JS -->
        <script src="js/External/jquery-3.7.1.min.js"></script>
        <!-- Datatables -->
        <script src="js/External/dataTables.js"></script>
        <script src="js/External/dataTables.bootstrap5.js"></script>

        <!-- Fancybox JS -->
        <script src="js/External/jquery.fancybox.min.js"></script>


        <script>
                        $(document).ready(function () {
                            var table = $('#UtenteTable').DataTable({
                                "ajax": {
                                    "url": "GetUtenteServlet",
                                    "type": "POST",
                                    "data": function (d) {
                                        d.utente = $("#utente").val();
                                    },
                                    "charset": "utf-8",
                                    "dataSrc": "",
                                    "dataType": "json"
                                },
                                "columns": [
                                    {"data": "id"},
                                    {"data": "nome"},
                                    {"data": "cognome"},
                                    {"data": "email"},
                                    {"data": "numero"},
                                    {"data": "ferie"},
                                    {"data": "ore"},
                                    {"data": "ore_contratto"},
                                    {"data": "stato"},
                                    {"data": "Gestisci"}
                                ],
                                responsive: true,
                                language: {
                                    "infoFiltered": "(filtrati da _MAX_ elementi totali)",
                                    "infoThousands": ".",
                                    "loadingRecords": "Caricamento...",
                                    "processing": "Elaborazione...",
                                    "search": "Cerca:",
                                    "paginate": {
                                        "first": "Inizio",
                                        "previous": "Precedente",
                                        "next": "Successivo",
                                        "last": "Fine"
                                    },
                                    "aria": {
                                        "sortAscending": ": attiva per ordinare la colonna in ordine crescente",
                                        "sortDescending": ": attiva per ordinare la colonna in ordine decrescente"
                                    },
                                    "autoFill": {
                                        "cancel": "Annulla",
                                        "fill": "Riempi tutte le celle con <i>%d<\/i>",
                                        "fillHorizontal": "Riempi celle orizzontalmente",
                                        "fillVertical": "Riempi celle verticalmente"
                                    },
                                    "buttons": {
                                        "collection": "Collezione <span class=\"ui-button-icon-primary ui-icon ui-icon-triangle-1-s\"><\/span>",
                                        "colvis": "Visibilità Colonna",
                                        "colvisRestore": "Ripristina visibilità",
                                        "copy": "Copia",
                                        "copyKeys": "Premi ctrl o u2318 + C per copiare i dati della tabella nella tua clipboard di sistema.<br \/><br \/>Per annullare, clicca questo messaggio o premi ESC.",
                                        "copySuccess": {
                                            "1": "Copiata 1 riga nella clipboard",
                                            "_": "Copiate %d righe nella clipboard"
                                        },
                                        "copyTitle": "Copia nella Clipboard",
                                        "csv": "CSV",
                                        "excel": "Excel",
                                        "pageLength": {
                                            "-1": "Mostra tutte le righe",
                                            "_": "Mostra %d righe"
                                        },
                                        "pdf": "PDF",
                                        "print": "Stampa",
                                        "createState": "Crea stato",
                                        "removeAllStates": "Rimuovi tutti gli stati",
                                        "removeState": "Rimuovi",
                                        "renameState": "Rinomina",
                                        "savedStates": "Salva stato",
                                        "stateRestore": "Ripristina stato",
                                        "updateState": "Aggiorna"
                                    },
                                    "emptyTable": "Nessun dato disponibile nella tabella",
                                    "info": "Risultati da _START_ a _END_ di _TOTAL_ elementi",
                                    "infoEmpty": "Risultati da 0 a 0 di 0 elementi",
                                    "lengthMenu": "Mostra _MENU_ elementi",
                                    "searchBuilder": {
                                        "add": "Aggiungi Condizione",
                                        "button": {
                                            "0": "Generatore di Ricerca",
                                            "_": "Generatori di Ricerca (%d)"
                                        },
                                        "clearAll": "Pulisci Tutto",
                                        "condition": "Condizione",
                                        "conditions": {
                                            "date": {
                                                "after": "Dopo",
                                                "before": "Prima",
                                                "between": "Tra",
                                                "empty": "Vuoto",
                                                "equals": "Uguale A",
                                                "not": "Non",
                                                "notBetween": "Non Tra",
                                                "notEmpty": "Non Vuoto"
                                            },
                                            "number": {
                                                "between": "Tra",
                                                "empty": "Vuoto",
                                                "equals": "Uguale A",
                                                "gt": "Maggiore Di",
                                                "gte": "Maggiore O Uguale A",
                                                "lt": "Minore Di",
                                                "lte": "Minore O Uguale A",
                                                "not": "Non",
                                                "notBetween": "Non Tra",
                                                "notEmpty": "Non Vuoto"
                                            },
                                            "string": {
                                                "contains": "Contiene",
                                                "empty": "Vuoto",
                                                "endsWith": "Finisce Con",
                                                "equals": "Uguale A",
                                                "not": "Non",
                                                "notEmpty": "Non Vuoto",
                                                "startsWith": "Inizia Con",
                                                "notContains": "Non Contiene",
                                                "notStartsWith": "Non Inizia Con",
                                                "notEndsWith": "Non Finisce Con"
                                            },
                                            "array": {
                                                "equals": "Uguale A",
                                                "empty": "Vuoto",
                                                "contains": "Contiene",
                                                "not": "Non",
                                                "notEmpty": "Non Vuoto",
                                                "without": "Senza"
                                            }
                                        },
                                        "data": "Dati",
                                        "deleteTitle": "Elimina regola filtro",
                                        "leftTitle": "Criterio di Riduzione Rientro",
                                        "logicAnd": "E",
                                        "logicOr": "O",
                                        "rightTitle": "Criterio di Aumento Rientro",
                                        "title": {
                                            "0": "Generatore di Ricerca",
                                            "_": "Generatori di Ricerca (%d)"
                                        },
                                        "value": "Valore"
                                    },
                                    "searchPanes": {
                                        "clearMessage": "Pulisci Tutto",
                                        "collapse": {
                                            "0": "Pannello di Ricerca",
                                            "_": "Pannelli di Ricerca (%d)"
                                        },
                                        "count": "{total}",
                                        "countFiltered": "{shown} ({total})",
                                        "emptyPanes": "Nessun Pannello di Ricerca",
                                        "loadMessage": "Caricamento Pannello di Ricerca",
                                        "title": "Filtri Attivi - %d",
                                        "showMessage": "Mostra tutto",
                                        "collapseMessage": "Espandi tutto"
                                    },
                                    "select": {
                                        "cells": {
                                            "1": "1 cella selezionata",
                                            "_": "%d celle selezionate"
                                        },
                                        "columns": {
                                            "1": "1 colonna selezionata",
                                            "_": "%d colonne selezionate"
                                        },
                                        "rows": {
                                            "1": "1 riga selezionata",
                                            "_": "%d righe selezionate"
                                        }
                                    },
                                    "zeroRecords": "Nessun elemento corrispondente trovato",
                                    "datetime": {
                                        "amPm": [
                                            "am",
                                            "pm"
                                        ],
                                        "hours": "ore",
                                        "minutes": "minuti",
                                        "next": "successivo",
                                        "previous": "precedente",
                                        "seconds": "secondi",
                                        "unknown": "sconosciuto",
                                        "weekdays": [
                                            "Dom",
                                            "Lun",
                                            "Mar",
                                            "Mer",
                                            "Gio",
                                            "Ven",
                                            "Sab"
                                        ],
                                        "months": [
                                            "Gennaio",
                                            "Febbraio",
                                            "Marzo",
                                            "Aprile",
                                            "Maggio",
                                            "Giugno",
                                            "Luglio",
                                            "Agosto",
                                            "Settembre",
                                            "Ottobre",
                                            "Novembre",
                                            "Dicembre"
                                        ]
                                    },
                                    "editor": {
                                        "close": "Chiudi",
                                        "create": {
                                            "button": "Nuovo",
                                            "submit": "Aggiungi",
                                            "title": "Aggiungi nuovo elemento"
                                        },
                                        "edit": {
                                            "button": "Modifica",
                                            "submit": "Modifica",
                                            "title": "Modifica elemento"
                                        },
                                        "error": {
                                            "system": "Errore del sistema."
                                        },
                                        "multi": {
                                            "info": "Gli elementi selezionati contengono valori diversi. Per modificare e impostare tutti gli elementi per questa selezione allo stesso valore, premi o clicca qui, altrimenti ogni cella manterrà il suo valore attuale.",
                                            "noMulti": "Questa selezione può essere modificata individualmente, ma non se fa parte di un gruppo.",
                                            "restore": "Annulla le modifiche",
                                            "title": "Valori multipli"
                                        },
                                        "remove": {
                                            "button": "Rimuovi",
                                            "confirm": {
                                                "_": "Sei sicuro di voler cancellare %d righe?",
                                                "1": "Sei sicuro di voler cancellare 1 riga?"
                                            },
                                            "submit": "Rimuovi",
                                            "title": "Rimuovi"
                                        }
                                    },
                                    "thousands": ".",
                                    "decimal": ",",
                                    "stateRestore": {
                                        "creationModal": {
                                            "button": "Crea",
                                            "columns": {
                                                "search": "Colonna Cerca",
                                                "visible": "Colonna Visibilità"
                                            },
                                            "name": "Nome:",
                                            "order": "Ordinamento",
                                            "paging": "Paginazione",
                                            "scroller": "Scorri posizione",
                                            "search": "Ricerca",
                                            "searchBuilder": "Form di Ricerca",
                                            "select": "Seleziona",
                                            "title": "Crea nuovo Stato",
                                            "toggleLabel": "Includi:"
                                        },
                                        "duplicateError": "Nome stato già presente",
                                        "emptyError": "Il nome è obbligatorio",
                                        "emptyStates": "Non ci sono stati salvati",
                                        "removeConfirm": "Sei sicuro di eliminare lo Stato %s?",
                                        "removeError": "Errore durante l'eliminazione dello Stato",
                                        "removeJoiner": "e",
                                        "removeSubmit": "Elimina",
                                        "removeTitle": "Elimina Stato",
                                        "renameButton": "Rinomina",
                                        "renameLabel": "Nuovo nome per %s:",
                                        "renameTitle": "Rinomina Stato"
                                    }
                                },
                                order: [[0, 'asc']],
                                paginate: {
                                    first: "Inizio",
                                    last: "Fine",
                                    next: "Avanti",
                                    previous: "Indietro"
                                }
                            },
                                    );
                            $("#dataInizio").change(function () {
                                table.ajax.reload(null, false);
                            });
                            $("#dataFine").change(function () {
                                table.ajax.reload(null, false);
                            });

                            $("#permesso").change(function () {
                                table.ajax.reload(null, false);
                            });
                            $("#utente").change(function () {
                                table.ajax.reload(null, false);
                            });

                        });
        </script>
        
        <script>
            var userIdDaRiabilitare = null;

            function riabilitaUtente(userId, userName) {
                userIdDaRiabilitare = userId;

                document.getElementById('nomeUtenteModal').textContent = userName;

                var confermaRiabilitazioneModal = new bootstrap.Modal(document.getElementById('confermaRiabilitazioneModal'), {
                    keyboard: false
                });
                confermaRiabilitazioneModal.show();
            }

            document.getElementById('confermaRiabilitaButton').addEventListener('click', function () {
                if (userIdDaRiabilitare !== null) {
                    $.ajax({
                        type: 'POST',
                        url: 'InserisciNuovoUtente',
                        data: {riabilita: true, userId: userIdDaRiabilitare},
                        success: function (response) {
                            window.location.href = "AD_gestioneUtente.jsp?esito=OK3&codice=007";
                        },
                        error: function () {
                            window.location.href = "AD_gestioneUtente.jsp?esito=KO3&codice=007";
                        }
                    });
                }
            });

        </script>

        <script>
            var userIdPresenze = null;

            function estraiPresenze(userId, userName) {
                userIdPresenze = userId;

                document.getElementById('nomeUtenteModal2').textContent = userName;

                var confermaEstrazioneModal = new bootstrap.Modal(document.getElementById('confermaEstrazioneModal'), {
                    keyboard: false
                });
                confermaEstrazioneModal.show();
            }

            document.getElementById('confermaEstrazioneButton').addEventListener('click', function () {
                if (userIdPresenze !== null) {
                    var data = document.getElementById('data').value;
                    var url = 'EstraiExcel?userId=' + userIdPresenze + '&data=' + encodeURIComponent(data);
                    window.location.href = url;
                }
            });


        </script>
    </body>
</html>

