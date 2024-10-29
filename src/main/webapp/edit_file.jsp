<%-- 
    Document   : edit_file
    Created on : 1 ott 2024, 12:09:18
    Author     : Salvatore
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="Utils.Utility"%>
<%@page import="Entity.FileEntity"%>
<%@page import="Entity.Utente"%>
<%
    String userId = Utility.checkAttribute(session, "userId");
    String ruolo = null;
    String pageName = null;

    Utente utente = (Utente) session.getAttribute("user");
    if (utente == null) {
        response.sendRedirect("index.jsp");
        return;
    } else {
        String uri = request.getRequestURI();
        pageName = uri.substring(uri.lastIndexOf("/") + 1);
        ruolo = String.valueOf(utente.getRuolo().getId());
        if (!Utility.isVisible(ruolo, pageName)) {
            response.sendRedirect(request.getContextPath() + "/error_page_403.jsp");
            return;
        }
    }

    String id = request.getParameter("id");
    String richiestaId = Utility.sanitize(request.getParameter("richiestaId"));
    Long idLong = null;
    FileEntity fileEntity = null;

    if (id != null && !id.isEmpty()) {
        try {
            idLong = Long.parseLong(id);
            fileEntity = Utility.findFileEntityById(idLong);
        } catch (NumberFormatException e) {
            out.println("<script>alert('ID non valido.');</script>");
        }
    } else {
        out.println("<script>alert('ID mancante.');</script>");
    }
%>
<!DOCTYPE html>
<html lang="it">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Modifica File</title>
        <!-- Bootstrap CSS -->
        <link rel="stylesheet" href="css/custom/SmartOOP-bootstrap.min.css"/>
        <!-- Custom Global CSS -->
        <link rel="icon" href="assets/logo.png"/>
        <!-- Fancybox CSS -->
        <link rel="stylesheet" href="css/External/jquery.fancybox.css"/>
        <style>
            * {
                margin: 0;
                padding: 0;
                box-sizing: border-box;
                font-family: "Cambria", serif;
                font-size: 1.0rem;
            }

        </style>
    </head>
    <body>
        <% String fancybox = Utility.sanitize(request.getParameter("fancybox"));
        %>
        <div class="container mt-5">
            <h2 class="mb-4 text-center SmartOOP-text-standard" style="font-weight: bold">Modifica File</h2>
            <form action="AllegatoServlet" method="POST" enctype="multipart/form-data" class="needs-validation" novalidate>
                <input type="hidden" value="true" name="edit">
                <input type="hidden" value="true" name="isUpload">
                <input type="hidden" value="<%= fileEntity != null ? fileEntity.getId() : "" %>" name="id">
                <input type="hidden" value="<%= fancybox.equals("true") ? "true" : "false" %>" name="fancybox">
                <div class="mb-3">
                    <label for="file" class="form-label SmartOOP-text-standard" style="font-weight: bold">Seleziona file</label>
                    <input type="file" class="form-control" name="file" id="file" required>
                    <div class="invalid-feedback">Questo campo è obbligatorio.</div>
                </div>
                <button type="submit" class="btn Smartoop-btn-standard">Carica Allegato</button>
            </form>
        </div>

        <!-- jQuery e Bootstrap JS -->
        <script src="js/External/jquery-3.7.1.min.js"></script>
        <script src="js/bootstrap.bundle.min.js"></script>
        <!-- Fancybox JS -->
        <script src="js/External/jquery.fancybox.min.js"></script>
        <script>
            (function () {
                'use strict';
                window.addEventListener('load', function () {
                    var forms = document.getElementsByClassName('needs-validation');
                    Array.prototype.filter.call(forms, function (form) {
                        form.addEventListener('submit', function (event) {
                            if (form.checkValidity() === false) {
                                event.preventDefault();
                                event.stopPropagation();
                            } else {
                                event.preventDefault();
                                var formData = new FormData(form);

                                fetch(form.action, {
                                    method: 'POST',
                                    body: formData
                                }).then(response => response.json()).then(data => {
                                    var richiestaId = "<%= richiestaId %>";
                                    if (data.fancybox === 'true') {
                                        if (data.success) {
                                            window.location.href = 'dettagliPermesso.jsp?richiestaId=' + richiestaId + '&esito=OK&codice=003';
                                        } else {
                                            window.location.href = 'dettagliPermesso.jsp?richiestaId=' + richiestaId + '&esito=KO&codice=003';
                                        }
                                    } else {
                                        if (data.success) {
                                            parent.jQuery.fancybox.close();
                                            parent.window.location.href = 'US_attached.jsp?esito=OK&codice=003';
                                        } else {
                                            parent.jQuery.fancybox.close();
                                            parent.window.location.href = 'US_attached.jsp?esito=KO&codice=003';
                                        }
                                    }
                                }).catch(error => {
                                    console.error('Errore:', error);
                                });
                            }
                            form.classList.add('was-validated');
                        }, false);
                    });
                }, false);
            })();

        </script>
    </body>
</html>
