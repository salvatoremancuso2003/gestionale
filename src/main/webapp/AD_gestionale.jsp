<%-- 
    Document   : AD_gestionale
    Created on : 3 set 2024, 12:02:42
    Author     : Salvatore
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="Utils.Utility"%>
<%@page import="Utils.EncryptionUtil"%>
<%@page import="Entity.Utente"%>
<%@page import="Entity.Permesso"%>
<%@page import="Entity.Presenza"%>
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
        <!--Bootstrap css -->
        <link rel="stylesheet" href="css/custom/SmartOOP-bootstrap.min.css"/>

        <!-- Bootstrap Icons -->
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css">

        <link rel="icon" href="assets/logo.png"/>
        <!-- Custom Global Css -->
        <link rel="stylesheet" href="css/custom/global.css"/>

    </head>

    <body>

        <!-- Navbar -->
        <nav class="navbar Smartoop-bg-standard navbar-expand-lg fixed-top w-100">
            <div class="container-fluid">
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarSupportedContent">
                    <ul class="navbar-nav me-auto">
                        <li class="nav-item">
                            <a class="nav-link active d-flex align-items-center" href="AD_gestionale.jsp">
                                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-house-door" viewBox="0 0 16 16">
                                <path d="M8.354 1.146a.5.5 0 0 0-.708 0l-6 6A.5.5 0 0 0 1.5 7.5v7a.5.5 0 0 0 .5.5h4.5a.5.5 0 0 0 .5-.5v-4h2v4a.5.5 0 0 0 .5.5H14a.5.5 0 0 0 .5-.5v-7a.5.5 0 0 0-.146-.354L13 5.793V2.5a.5.5 0 0 0-.5-.5h-1a.5.5 0 0 0-.5.5v1.293zM2.5 14V7.707l5.5-5.5 5.5 5.5V14H10v-4a.5.5 0 0 0-.5-.5h-3a.5.5 0 0 0-.5.5v4z"/>
                                </svg>
                                <span style="padding-left: 4px">HOME</span>
                            </a>
                        </li>
                        <!-- Dropdown CALENDARIO -->
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle" href="#" id="navbarDarkDropdownMenuLink" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-calendar3" viewBox="0 0 16 16">
                                <path d="M14 0H2a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V2a2 2 0 0 0-2-2M1 3.857C1 3.384 1.448 3 2 3h12c.552 0 1 .384 1 .857v10.286c0 .473-.448.857-1 .857H2c-.552 0-1-.384-1-.857z"/>
                                <path d="M6.5 7a1 1 0 1 0 0-2 1 1 0 0 0 0 2m3 0a1 1 0 1 0 0-2 1 1 0 0 0 0 2m3 0a1 1 0 1 0 0-2 1 1 0 0 0 0 2m-9 3a1 1 0 1 0 0-2 1 1 0 0 0 0 2m3 0a1 1 0 1 0 0-2 1 1 0 0 0 0 2m3 0a1 1 0 1 0 0-2 1 1 0 0 0 0 2m3 0a1 1 0 1 0 0-2 1 1 0 0 0 0 2m-9 3a1 1 0 1 0 0-2 1 1 0 0 0 0 2m3 0a1 1 0 1 0 0-2 1 1 0 0 0 0 2m3 0a1 1 0 1 0 0-2 1 1 0 0 0 0 2"/>
                                </svg>
                                <span style="padding-left: 4px">CALENDARIO</span>
                            </a>
                            <ul class="dropdown-menu" aria-labelledby="navbarDarkDropdownMenuLink">
                                <li class="border-bottom">
                                    <a class="nav-link  d-flex align-items-center" href="AD_calendar.jsp">
                                        <svg xmlns="http://www.w3.org/2000/svg" style="padding-left: 10px; " width="25" height="25" fill="currentColor" class="bi bi-calendar3 SmartOOP-text-standard" viewBox="0 0 16 16">
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
                            <a class="nav-link  d-flex align-items-center" href="AD_gestioneUtente.jsp">
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
                            <h1 class="display-4 SmartOOP-text-standard">Home</h1>

                        </div>
                    </div>
                </div>
            </div>

            <!-- Sezione Card con icone -->
            <div id="sezioneCard" class="row text-center mt-5">
                <div class="row">
                    <div class="col-md-6">
                        <div class="card p-3 mb-4 bg-white rounded" style="box-shadow: 0 4px 15px rgba(220,53,69, 0.3);">
                            <div class="card-body">
                                <i class="bi bi-calendar3 SmartOOP-text-standard" style="font-size: 3rem;"></i>
                                <h5 class="card-title mt-3">Calendario</h5>
                                <p class="card-text">Consulta il calendario delle tue attività.</p>
                                <a href="AD_calendar.jsp" class="btn Smartoop-btn-outline-standard">Vai al calendario</a>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="card p-3 mb-4 bg-white rounded" style="box-shadow: 0 4px 15px rgba(220,53,69, 0.3);">
                            <div class="card-body">
                                <i class="bi bi-clock-history SmartOOP-text-standard" style="font-size: 3rem;"></i>
                                <h5 class="card-title mt-3">Presenze</h5>
                                <p class="card-text">Controlla le presenza giornaliere dei dipendenti.</p>
                                <a href="AD_presenze.jsp" class="btn Smartoop-btn-outline-standard">Vai alle presenze</a>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6">
                        <div class="card p-3 mb-4 bg-white rounded" style="box-shadow: 0 4px 15px rgba(220,53,69, 0.3);">
                            <div class="card-body">
                                <i class="bi bi-file-earmark-text SmartOOP-text-standard" style="font-size: 3rem;"></i>
                                <h5 class="card-title mt-3">Documentazione</h5>
                                <p class="card-text">Gestisci la documentazione.</p>
                                <a href="AD_attached.jsp" class="btn Smartoop-btn-outline-standard">Vai alla documentazione</a>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="card p-3 mb-4 bg-white rounded" style="box-shadow: 0 4px 15px rgba(220,53,69, 0.3);">
                            <div class="card-body">
                                <i class="bi bi-alarm SmartOOP-text-standard" style="font-size: 3rem;"></i>
                                <h5 class="card-title mt-3">Permessi</h5>
                                <p class="card-text">Gestisci i permessi dei dipendenti.</p>
                                <a href="AD_richiestePermessi.jsp" class="btn Smartoop-btn-outline-standard">Vai ai permessi</a>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <div class="card p-3 mb-4 bg-white rounded" style="box-shadow: 0 4px 15px rgba(220,53,69, 0.3);">
                            <div class="card-body">
                                <i class="bi bi-person-fill-gear SmartOOP-text-standard" style="font-size: 3rem;"></i>
                                <h5 class="card-title mt-3">Utenti</h5>
                                <p class="card-text">Gestisci i tuoi utenti.</p>
                                <a href="AD_gestioneUtente.jsp" class="btn Smartoop-btn-outline-standard">Vai alla gestione degli utenti</a>
                            </div>
                        </div>
                    </div>

                </div>
            </div>
        </main>

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
                            <input type="hidden" id="isForzato" name="forzaInvio" value="false">
                            <input type="hidden" id="isAdmin" name="isAdmin" value="false">
                            <input type="hidden" id="idPermesso" name="idPermesso" value="">

                            <% List<Permesso> AllTipiPermesso = Utility.getAllPermessi(); %>
                            <div class="mb-3">
                                <label for="tipoPermesso" class="form-label">Tipo di Permesso</label>
                                <select class="form-select" id="tipoPermesso" name="tipo_permesso" required>
                                    <option selected="" disabled>Seleziona permesso</option>
                                    <% for (Permesso p : AllTipiPermesso) { %>
                                    <option value="<%= p.getOre() %>" data-id="<%= p.getCodice() %>">
                                        <%= p.getDescrizione() %>
                                    </option>
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

        <!-- Modal Timbratura -->
        <form method="POST" action="SavePresenceServlet">
            <input type="hidden" id="isPresence" name="isPresence" value="true">
            <input type="hidden" id="isAdmin" name="isAdmin" value="true">
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

        <script>
            document.getElementById('richiediPermessoModal').addEventListener('hidden.bs.modal', function () {
                document.getElementById('richiediPermessoForm').reset();
            });
            document.getElementById("esitoModalButton2").addEventListener("click", function () {
                document.getElementById("richiediPermessoForm").submit();
            });
        </script>



        <script src="js/bootstrap.bundle.min.js"></script>
        <script src="js/custom/global.js"></script>
        <script src="js/custom/globalModal.js"></script>
        <script src="js/custom/ad_global.js"></script>
        <script src="js/External/jquery-3.7.1.min.js"></script>

    </body>
</html>

