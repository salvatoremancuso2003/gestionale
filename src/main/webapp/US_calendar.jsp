<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="Utils.Utility"%>
<%@page import="Utils.EncryptionUtil"%>
<%@page import="Entity.Utente"%>
<%@page import="Entity.Presenza"%>
<%@page import="Entity.Permesso"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="java.time.LocalDate"%>
<%@page import="java.time.ZoneId"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.List"%>
<%@ page import="java.text.SimpleDateFormat" %>

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
            String src = Utility.checkAttribute(session, "src");
        }
    }
%>

<!DOCTYPE html>
<html lang="it">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Calendario Utente</title>
        <!-- Bootstrap CSS -->
        <link href="css/custom/SmartOOP-bootstrap.min.css" rel="stylesheet">
        <!-- Bootstrap Icons -->
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css">
        <!-- JQueryFancybox CSS -->
        <link href="css/External/jquery.fancybox.css" rel="stylesheet">
        <!-- Font Awesome CSS -->
        <link href="css/External/fontawesome.all.min.css" rel="stylesheet">
        <!-- FullCalendar CSS -->
        <link rel="stylesheet" href="css/External/fullcalendar.main.min.css">
        <!-- FancyBox -->
        <link href="css/External/jquery.fancybox.css" rel="stylesheet">
        <!-- Custom Global Css -->
        <link rel="stylesheet" href="css/custom/global.css"/>
        <link rel="icon" href="assets/logo.png"/>

        <!-- Bootstrap Icons -->
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css">
        <link rel="stylesheet" type="text/css" href="css/External/dataTables.bootstrap5.css">

        <style>
            .tooltip-inner {
                background-color: #f5f5f5;
                color: black;
                font-size: 14px;
                border-radius: 4px;
                padding: 10px;
                width: 100%;
                text-align: left;
                white-space: normal;
                word-wrap: break-word;
                display: inline-block;
            }


            .fc-event {
                background-color: #0d6efd;
                color: #ffffff;
                border-color: #0d6efd;
            }

            .fc-event-title {
                color: #ffffff;
            }

            .fc-event-SmartOOP-standard {
                background-color: #0d6efd;
                color: #ffffff;
            }



        </style>
    </head>
    <body>

        <!-- Navbar -->
        <nav class="navbar navbar-expand-lg navbar-SmartOOP-standard fixed-top w-100 Smartoop-bg-standard">
            <div class="container-fluid">
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarSupportedContent">
                    <ul class="navbar-nav me-auto">
                        <li class="nav-item">
                            <a class="nav-link d-flex align-items-center" href="US_gestionale.jsp">
                                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-house-door" viewBox="0 0 16 16">
                                <path d="M8.354 1.146a.5.5 0 0 0-.708 0l-6 6A.5.5 0 0 0 1.5 7.5v7a.5.5 0 0 0 .5.5h4.5a.5.5 0 0 0 .5-.5v-4h2v4a.5.5 0 0 0 .5.5H14a.5.5 0 0 0 .5-.5v-7a.5.5 0 0 0-.146-.354L13 5.793V2.5a.5.5 0 0 0-.5-.5h-1a.5.5 0 0 0-.5.5v1.293zM2.5 14V7.707l5.5-5.5 5.5 5.5V14H10v-4a.5.5 0 0 0-.5-.5h-3a.5.5 0 0 0-.5.5v4z"/>
                                </svg>
                                <span style="padding-left: 4px">HOME</span>
                            </a>
                        </li>

                        <!-- Dropdown CALENDARIO -->
                        <li class="nav-item dropdown">
                            <a class="nav-link active dropdown-toggle" href="#" id="navbarDarkDropdownMenuLink" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                                <svg xmlns="http://www.w3.org/2000/svg"  width="16" height="16" fill="currentColor" class="bi bi-calendar3" viewBox="0 0 16 16">
                                <path d="M14 0H2a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V2a2 2 0 0 0-2-2M1 3.857C1 3.384 1.448 3 2 3h12c.552 0 1 .384 1 .857v10.286c0 .473-.448.857-1 .857H2c-.552 0-1-.384-1-.857z"/>
                                <path d="M6.5 7a1 1 0 1 0 0-2 1 1 0 0 0 0 2m3 0a1 1 0 1 0 0-2 1 1 0 0 0 0 2m3 0a1 1 0 1 0 0-2 1 1 0 0 0 0 2m-9 3a1 1 0 1 0 0-2 1 1 0 0 0 0 2m3 0a1 1 0 1 0 0-2 1 1 0 0 0 0 2m3 0a1 1 0 1 0 0-2 1 1 0 0 0 0 2m3 0a1 1 0 1 0 0-2 1 1 0 0 0 0 2m-9 3a1 1 0 1 0 0-2 1 1 0 0 0 0 2m3 0a1 1 0 1 0 0-2 1 1 0 0 0 0 2m3 0a1 1 0 1 0 0-2 1 1 0 0 0 0 2"/>
                                </svg>
                                <span style="padding-left: 4px; white-space: nowrap">CALENDARIO</span> 
                            </a>
                            <ul class="dropdown-menu" aria-labelledby="navbarDarkDropdownMenuLink">
                                <li class="border-bottom">
                                    <a class="nav-link active d-flex align-items-center" href="US_calendar.jsp">
                                        <svg xmlns="http://www.w3.org/2000/svg" style="padding-left: 10px;" width="25" height="25" fill="currentColor" class="bi bi-calendar3 SmartOOP-text-standard" viewBox="0 0 16 16">
                                        <path d="M14 0H2a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V2a2 2 0 0 0-2-2M1 3.857C1 3.384 1.448 3 2 3h12c.552 0 1 .384 1 .857v10.286c0 .473-.448.857-1 .857H2c-.552 0-1-.384-1-.857z"/>
                                        <path d="M6.5 7a1 1 0 1 0 0-2 1 1 0 0 0 0 2m3 0a1 1 0 1 0 0-2 1 1 0 0 0 0 2m3 0a1 1 0 1 0 0-2 1 1 0 0 0 0 2m-9 3a1 1 0 1 0 0-2 1 1 0 0 0 0 2m3 0a1 1 0 1 0 0-2 1 1 0 0 0 0 2m3 0a1 1 0 1 0 0-2 1 1 0 0 0 0 2m3 0a1 1 0 1 0 0-2 1 1 0 0 0 0 2m-9 3a1 1 0 1 0 0-2 1 1 0 0 0 0 2m3 0a1 1 0 1 0 0-2 1 1 0 0 0 0 2m3 0a1 1 0 1 0 0-2 1 1 0 0 0 0 2"/>
                                        </svg>
                                        <span style="padding-left: 10px; white-space: nowrap" class="SmartOOP-text-standard">CALENDARIO</span>
                                    </a>
                                </li>
                                <li class="border-bottom">
                                    <a class="nav-link d-flex align-items-center" href="#" data-bs-toggle="modal" data-bs-target="#timbroModal">
                                        <svg xmlns="http://www.w3.org/2000/svg" style="padding-left: 10px; " width="25" height="25" fill="currentColor" class="bi bi-check-square SmartOOP-text-standard" viewBox="0 0 16 16">
                                        <path d="M14 1a1 1 0 0 1 1 1v12a1 1 0 0 1-1 1H2a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1zM2 0a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V2a2 2 0 0 0-2-2z"/>
                                        <path d="M10.97 4.97a.75.75 0 0 1 1.071 1.05l-3.992 4.99a.75.75 0 0 1-1.08.02L4.324 8.384a.75.75 0 1 1 1.06-1.06l2.094 2.093 3.473-4.425z"/>
                                        </svg>
                                        <span style="padding-left: 10px; white-space: nowrap" class="SmartOOP-text-standard">TIMBRO GIORNALIERO</span>
                                    </a>
                                </li>
                                <li>
                                    <a class="nav-link d-flex align-items-center" href="#" data-bs-toggle="modal" data-bs-target="#richiediPermessoModal">
                                        <svg xmlns="http://www.w3.org/2000/svg" style="padding-left: 10px;" width="25" height="25" fill="currentColor" class="bi bi-alarm SmartOOP-text-standard" viewBox="0 0 16 16">
                                        <path d="M8.5 5.5a.5.5 0 0 0-1 0v3.362l-1.429 2.38a.5.5 0 1 0 .858.515l1.5-2.5A.5.5 0 0 0 8.5 9z"/>
                                        <path d="M6.5 0a.5.5 0 0 0 0 1H7v1.07a7.001 7.001 0 0 0-3.273 12.474l-.602.602a.5.5 0 0 0 .707.708l.746-.746A6.97 6.97 0 0 0 8 16a6.97 6.97 0 0 0 3.422-.892l.746.746a.5.5 0 0 0 .707-.708l-.601-.602A7.001 7.001 0 0 0 9 2.07V1h.5a.5.5 0 0 0 0-1zm1.038 3.018a6 6 0 0 1 .924 0 6 6 0 1 1-.924 0M0 3.5c0 .753.333 1.429.86 1.887A8.04 8.04 0 0 1 4.387 1.86 2.5 2.5 0 0 0 0 3.5M13.5 1c-.753 0-1.429.333-1.887.86a8.04 8.04 0 0 1 3.527 3.527A2.5 2.5 0 0 0 13.5 1"/>
                                        </svg>
                                        <span style="padding-left: 10px; white-space: nowrap" class="SmartOOP-text-standard">RICHIEDI PERMESSO</span>
                                    </a>
                                </li>
                            </ul>
                        </li>

                        <li class="nav-item">
                            <a class="nav-link d-flex align-items-center" href="US_attached.jsp">
                                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-file-earmark-text" viewBox="0 0 16 16">
                                <path d="M5.5 7a.5.5 0 0 0 0 1h5a.5.5 0 0 0 0-1zM5 9.5a.5.5 0 0 1 .5-.5h5a.5.5 0 0 1 0 1h-5a.5.5 0 0 1-.5-.5m0 2a.5.5 0 0 1 .5-.5h2a.5.5 0 0 1 0 1h-2a.5.5 0 0 1-.5-.5"/>
                                <path d="M9.5 0H4a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h8a2 2 0 0 0 2-2V4.5zm0 1v2A1.5 1.5 0 0 0 11 4.5h2V14a1 1 0 0 1-1 1H4a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1z"/>
                                </svg>
                                <span style="padding-left: 4px">DOCUMENTAZIONE</span>
                            </a>
                        </li>
                    </ul>
                </div>
                
                <div class="ms-auto d-flex align-items-center">

                    <!-- Icona Notifiche -->
                    <div class="me-3 position-relative">
                        <button type="button" class="btn Smartoop-btn-outline-light" id="notificheButton" onclick="mostraNotifiche()">
                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-bell" viewBox="0 0 16 16">
                            <path d="M8 16a2 2 0 0 0 2-2H6a2 2 0 0 0 2 2M8 1.918l-.797.161A4 4 0 0 0 4 6c0 .628-.134 2.197-.459 3.742-.16.767-.376 1.566-.663 2.258h10.244c-.287-.692-.502-1.49-.663-2.258C12.134 8.197 12 6.628 12 6a4 4 0 0 0-3.203-3.92zM14.22 12c.223.447.481.801.78 1H1c.299-.199.557-.553.78-1C2.68 10.2 3 6.88 3 6c0-2.42 1.72-4.44 4.005-4.901a1 1 0 1 1 1.99 0A5 5 0 0 1 13 6c0 .88.32 4.2 1.22 6"/>
                            </svg>
                        </button>
                        <span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-white" id="notificaBadge" style="margin-top: 5px">
                            <span class="SmartOOP-text-standard">0</span>
                            <span class="visually-hidden">nuove notifiche</span>
                        </span>
                    </div>

                    <span class="text-white me-3">
                        <svg xmlns="http://www.w3.org/2000/svg" width="25" height="20" fill="currentColor" class="bi bi-person-check" viewBox="0 0 16 16">
                        <path d="M12.5 16a3.5 3.5 0 1 0 0-7 3.5 3.5 0 0 0 0 7m1.679-4.493-1.335 2.226a.75.75 0 0 1-1.174.144l-.774-.773a.5.5 0 0 1 .708-.708l.547.548 1.17-1.951a.5.5 0 1 1 .858.514M11 5a3 3 0 1 1-6 0 3 3 0 0 1 6 0M8 7a2 2 0 1 0 0-4 2 2 0 0 0 0 4"/>
                        <path d="M8.256 14a4.5 4.5 0 0 1-.229-1.004H3c.001-.246.154-.986.832-1.664C4.484 10.68 5.711 10 8 10q.39 0 .74.025c.226-.341.496-.65.804-.918Q8.844 9.002 8 9c-5 0-6 3-6 4s1 1 1 1z"/>
                        </svg>
                        <%= EncryptionUtil.decrypt(utente.getNome()).toUpperCase() + " " + EncryptionUtil.decrypt(utente.getCognome()).toUpperCase() + " - " + Utility.tipoUtente(utente).toUpperCase() %>
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
                        <h1 class="display-4 SmartOOP-text-standard">Calendario</h1>
                    </div>
                </div>
            </div>
        </div>

        <!-- Calendario FullCalendar -->
        <div id="calendar"></div>
    </main>

    <!-- Modal Timbratura -->
    <form method="POST" action="SavePresenceServlet">
        <input type="hidden" id="isPresence" name="isPresence" value="true">
        <div class="modal fade" id="timbroModal" tabindex="-1" aria-labelledby="timbroModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header Smartoop-bg-standard">
                        <h5 class="modal-title text-white" id="timbroModalLabel">Timbro Giornaliero</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>

                    <%
                        Long userIdLong = Long.parseLong(userId);
                        List<Presenza> presenze = Utility.findPresenzeByUserAndDate(userIdLong);
                    
                        boolean ultimoIngressoSenzaUscita = false;
                        if (!presenze.isEmpty()) {
                            Presenza lastPresenza = presenze.get(presenze.size() - 1);
                            ultimoIngressoSenzaUscita = lastPresenza.getEntrata() != null && lastPresenza.getUscita() == null;
                        }
                    %>

                    <div class="modal-body">
                        <% if (presenze.isEmpty() || !ultimoIngressoSenzaUscita) { %>
                        <!-- Se non ci sono ingressi o l'ultimo ingresso ha già un'uscita -->
                        <p>Seleziona tipo di ingresso</p>
                        <hr>

                        <div class="container d-flex justify-content-evenly">
                            <!-- Ingresso -->
                            <input class="form-check-input" type="radio" name="tipo" id="ingresso" value="ingresso" checked hidden>
                            <!-- Modalità di ingresso (in presenza o da remoto) -->
                            <div class="form-check">
                                <input class="form-check-input" type="radio" name="modality" id="ing_presenza" value="SEDE" required>
                                <label class="form-check-label" for="ing_presenza">In Presenza</label>
                            </div>
                            <div class="form-check">
                                <input class="form-check-input" type="radio" name="modality" id="ing_remoto" value="REMOTO" required>
                                <label class="form-check-label" for="ing_remoto">Da Remoto</label>
                            </div>
                        </div>
                        <hr>

                        <p>Premi il pulsante qui sotto per registrare l'orario di attivazione della giornata di lavoro:</p>
                        <button type="submit" id="btnRegistrareOra" class="btn Smartoop-btn-standard">Registra Ingresso</button>

                        <% } else if (ultimoIngressoSenzaUscita) { %>
                        <!-- Se l'ultimo ingresso non ha ancora un'uscita -->
                        <p>Hai già registrato un ingresso. Seleziona l'uscita:</p>

                        <!-- Uscita -->
                        <hr>
                        <div class="form-check">
                            <input class="form-check-input" type="radio" name="tipo" id="uscita" value="uscita" required>
                            <label class="form-check-label" for="uscita">Uscita</label>
                        </div>

                        <hr>
                        <p>Premi il pulsante qui sotto per registrare l'uscita:</p>
                        <button type="submit" id="btnRegistrareOra" class="btn Smartoop-btn-standard">Registra Uscita</button>

                        <% } %>

                        <hr>
                        <h5 class="text-center">Storico di oggi:</h5>
                        <hr>

                        <% 
                            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                        %>

                        <div class="container-fluid">
                            <div class="row justify-content-center">
                                <div class="col-12 text-center">
                                    <%  
                                    for (Presenza p : presenze) {
                                        if (p.getEntrata() != null) {
                                            String entrataFormatted = timeFormat.format(p.getEntrata()); 
                                    %>
                                    <p><strong class="SmartOOP-text-standard">Entrata:</strong> <%= entrataFormatted %></p> 
                                    <% 
                                        }
                                        if (p.getUscita() != null) {
                                            String uscitaFormatted = timeFormat.format(p.getUscita());
                                    %>
                                    <p><strong class="text-secondary">Uscita:</strong> <%= uscitaFormatted %></p>
                                    <% 
                                        }
                                    } 
                                    %>
                                </div>
                            </div>
                            <hr>
                        </div>

                    </div>
                </div>
            </div>
        </div>
    </form>


    <!-- Modal Richiesta Permesso-->
    <div class="modal fade" id="richiediPermessoModal" tabindex="-1" aria-labelledby="richiediPermessoLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header Smartoop-bg-standard">
                    <h5 class="modal-title text-white" id="richiediPermessoLabel">Richiesta Permesso</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form id="richiediPermessoForm" method="POST" action="RichiestaPermessoServlet" enctype="multipart/form-data">
                        <input type="hidden" id="isCreate" name="isCreate" value="false">
                        <input type="hidden" id="isCheck" name="isCheck" value="true">
                        <% List <Permesso> AllTipiPermesso = Utility.getAllPermessi();%>
                        <div class="mb-3">
                            <label for="tipoPermesso" class="form-label">Tipo di Permesso</label>
                            <select class="form-select" id="tipoPermesso" name="tipo_permesso" required>
                                <option selected="" disabled>Seleziona permesso</option>
                                <% for (Permesso p : AllTipiPermesso) { %>
                                <option value="<%= p.getCodice() %>"><%= p.getDescrizione() %></option>
                                <% } %>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label for="dataInizio" class="form-label">Data Inizio</label>
                            <input type="datetime-local" class="form-control" id="dataInizio" name="data_inizio" required>
                        </div>

                        <div class="mb-3">
                            <label for="dataFine" class="form-label">Data Fine</label>
                            <input type="datetime-local" class="form-control" id="dataFine" name="data_fine" required>
                        </div>
                        <div class="mb-3">
                            <label for="note" class="form-label">Note</label>
                            <textarea class="form-control" id="note" name="note" rows="3"></textarea>
                        </div>
                        <div class="mb-3">
                            <label for="allegato" class="form-label">Allegato (opzionale)</label>
                            <input class="form-control" type="file" id="allegato" name="allegato">
                        </div>
                        <button type="submit" class="btn Smartoop-btn-standard" id="esitoModalButton1" >Invia Richiesta</button>
                        <button type="button" class="btn btn-secondary" id="esitoModalButton2" style="display: none;" onclick="invioRichiestaSenzaOre()">Procedi senza modificare ore</button>

                    </form>
                </div>
            </div>
        </div>
    </div>



    <!-- Modal Notifiche -->
    <div class="modal fade" id="notificheModal" tabindex="-1" aria-labelledby="notificheModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-xl">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title SmartOOP-text-standard" style="font-weight: bold" id="notificheModalLabel">Notifiche</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <div id="noNotificheMessage" style="display:none;">
                        <p>Non ci sono notifiche disponibili</p>

                    </div>
                    <div class="table-responsive">
                        <table id="notificheTable" class="table table-bordered table-striped">
                            <thead>
                                <tr>
                                    <th class="SmartOOP-text-standard">ID</th>
                                    <th class="SmartOOP-text-standard">Messaggio</th>
                                    <th class="SmartOOP-text-standard">Data Creazione</th>
                                    <th class="SmartOOP-text-standard">Esito</th>
                                </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Chiudi</button>
                    <div>
                        <button type="button" class="btn Smartoop-btn-standard" id="segnaComeLettoButton" onclick="segnaComeLetto()">Segna come già lette</button>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- ESITO MODAL  -->
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

    <!-- ESITO MODAL INS  -->
    <div class="modal fade modal-xl" id="esitoModalIns" tabindex="-1" aria-labelledby="esitoModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header" id="modal-headerIns">
                    <h5 class="modal-title" id="esitoModalLabelIns">Esito Operazione</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body" id="esitoModalBodyIns">

                </div>
                <div class="modal-footer">
                    <button type="button" id="esitoModalButtonIns" class="btn btn-secondary" data-bs-dismiss="modal">Chiudi</button>
                    <button type="button" class="btn Smartoop-btn-outline-success" id="esitoModalButton2" onclick="invioRichiestaSenzaOre()">Procedi senza modificare ore</button>

                </div>
            </div>
        </div>
    </div>
    <div class="modal fade modal-xl" id="errorDateModal" tabindex="-1" aria-labelledby="errorDateModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header Smartoop-bg-standard" id="modal-header">
                    <h5 class="modal-title" id="errorDateModalLabel" style="color: white">Operazione non disponibile</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body" id="esitoModalBody">
                    Non puoi selezionare sabato o domenica. Riprova.
                </div>
                <div class="modal-footer">
                    <button type="button" id="errorDateModalButton" class="btn Smartoop-btn-standard" data-bs-dismiss="modal">Chiudi</button>
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


    <!-- jQuery e Bootstrap JS -->
    <script src="js/External/jquery-3.7.1.min.js"></script>
    <script src="js/bootstrap.bundle.min.js"></script>
    <!-- FullCalendar JS -->
    <script src="js/custom/globalModal.js"></script>
    <script src="js/External/fullcalendar.main.min.js"></script>
    <script src="js/External/jquery.fancybox.min.js"></script>
    <script src="js/External/fullcalendar_5.9.0_locales-all.js"></script>

    <script>
                        document.addEventListener('DOMContentLoaded', function () {
                            var calendarEl = document.getElementById('calendar');
                            var calendar = new FullCalendar.Calendar(calendarEl, {
                                initialView: 'dayGridMonth',
                                headerToolbar: {
                                    left: 'prev,next today',
                                    center: 'title',
                                    right: 'timeGridDay,timeGridWeek,dayGridMonth'
                                },
                                locale: 'it',
                                navLinks: true,
                                businessHours: true,
                                editable: true,
                                selectable: true,
                                eventSources: [
                                    {
                                        url: 'GetTurniDipendentiServlet?utente=' + true,
                                        method: 'POST',
                                        failure: function () {
                                            alert('Si è verificato un errore durante il recupero degli eventi.');
                                        },
                                        textColor: 'white'
                                    }
                                ],
                                eventDidMount: function (info) {
                                    var colore = info.event.extendedProps.colore;
                                    info.el.style.backgroundColor = colore;
                                    info.el.style.borderColor = colore;
                                },
                                eventClick: function (e) {
                                    var utenteId = e.event.extendedProps.utenteId;
                                    var richiestaId = e.event.extendedProps.richiestaId;
                                    var data = e.event.startStr;
                                    var permesso = e.event.extendedProps.permesso;
                                    var url = null;
                                    if (permesso) {
                                        url = "dettagliPermesso.jsp?richiestaId=" + richiestaId;
                                    } else {
                                        url = "dettagliPresenza.jsp?utenteId=" + utenteId + "&data=" + encodeURIComponent(data);
                                    }

                                    $.fancybox.open({
                                        src: url,
                                        type: 'iframe',
                                        iframe: {
                                            preload: false
                                        },
                                        openEffect: 'fadeIn',
                                        closeEffect: 'fadeOut',
                                        padding: 10,
                                        width: '80%',
                                        height: '80%',
                                        clickOutside: 'close'
                                    });
                                },
                                eventMouseEnter: function (info) {
                                    var tooltipContent = "<b>" + info.event.title + "</b>" + "<hr>" + "<p>" + info.event.extendedProps.description + "</p>";
                                    $(info.el).tooltip({
                                        title: tooltipContent,
                                        html: true,
                                        placement: 'top',
                                        container: 'body'
                                    });
                                    $(info.el).tooltip('show');
                                },
                                eventMouseLeave: function (info) {
                                    $(info.el).tooltip('hide');
                                }


                            });
                            calendar.render();
                        });
    </script>


    <script>
        document.getElementById('richiediPermessoModal').addEventListener('hidden.bs.modal', function () {
            document.getElementById('richiediPermessoForm').reset();
        });
        document.getElementById("esitoModalButton2").addEventListener("click", function () {
            document.getElementById("richiediPermessoForm").submit();
        });
    </script>

    <script src="js/custom/global.js"></script>
    <script src="js/custom/us_global.js"></script>
    <script src="js/External/dataTables.js"></script>
    <script src="js/External/dataTables.bootstrap5.js"></script>

</body>
</html>
