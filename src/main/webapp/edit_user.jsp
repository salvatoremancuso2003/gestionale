<%-- 
    Document   : edit_user
    Created on : 16 ott 2024, 09:36:52
    Author     : Salvatore
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="Utils.Utility"%>
<%@page import="Entity.FileEntity"%>
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

    String userIdEdit = request.getParameter("userId");
    Long idLong = null;
    Utente user = null;

    if (userIdEdit != null && !userIdEdit.isEmpty()) {
        try {
            idLong = Long.parseLong(userIdEdit);
            user = Utility.findUserById(idLong);
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
        <title>Modifica Utente</title>
        <!-- Bootstrap CSS -->
        <link rel="stylesheet" href="css/bootstrap.min.css"/>
        <!-- Custom Global CSS -->
        <link rel="icon" href="assets/logo.png"/>
        <link rel="stylesheet" href="css/custom/global.css"/>
        <!-- Fancybox CSS -->
        <link rel="stylesheet" href="css/External/jquery.fancybox.css"/>
        <style>
            .active>.page-link, .page-link.active {
                background-color:  #dc3545 !important;
                color: white !important;
                border: 1px solid white;
            }
            .form-control:focus{
                border-color: #dc3545 !important;
                box-shadow: 0 0 0 .25rem rgba(220,53,69, 0.35);
            }
            .form-select-sm:focus{
                border-color: #dc3545 !important;
                box-shadow: 0 0 0 .25rem rgba(220,53,69, 0.35);
            }

            .page-link{
                color: #dc3545 !important;
            }

            .page-link:focus{
                border-color: #dc3545 !important;
                box-shadow: 0 0 0 .25rem rgba(220,53,69, 0.35);
            }

        </style>
    </head>
    <body>

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
                            <h1 class="display-4 text-danger"> Modifica Utente </h1>
                            <p class="lead mt-4"><%= Utility.checkAttribute(session, "nome") %> ,Procedi all'interno della pagina per modificare con successo l'utente selezionato!</p>
                        </div>
                    </div>
                </div>
            </div>
        </main>


        <div class="container text-center">
            <form action="InserisciNuovoUtente" method="POST" class="needs-validation" novalidate>
                <input type="hidden" name="update" value="true">
                <input type="hidden" name="userId" value="<%= user.getId() %>">

                <div class="mb-3">
                    <label for="nome" class="form-label">Nome</label>
                    <input type="text" class="form-control" name="nuovoNome" id="nome" required value="<%= user != null ? user.getNome() : "" %>">
                    <div class="invalid-feedback">Campo obbligatorio</div>
                </div>

                <div class="mb-3">
                    <label for="cognome" class="form-label">Cognome</label>
                    <input type="text" class="form-control" name="nuovoCognome" id="cognome" required value="<%= user != null ? user.getCognome() : "" %>">
                    <div class="invalid-feedback">Campo obbligatorio</div>
                </div>

                <div class="mb-3">
                    <label for="email" class="form-label">Nuova Email</label>
                    <input type="email" class="form-control" name="nuovaEmail" id="email" required value="<%= user != null ? user.getEmail() : "" %>">
                    <div class="invalid-feedback">Campo obbligatorio</div>
                </div>

                <div class="mb-3">
                    <label for="ferie" class="form-label">Ferie disponibili</label>
                    <input type="text" class="form-control" name="ferie" id="ferie" required value="<%= user != null ? user.getFerie_disponibili() : "" %>">
                    <div class="invalid-feedback">Campo obbligatorio</div>
                </div>
                <div class="mb-3">
                    <label for="ore" class="form-label">Ore disponibili</label>
                    <input type="text" class="form-control" name="ore" id="ore" required value="<%= user != null ? user.getOre_disponibili() : "" %>">
                    <div class="invalid-feedback">Campo obbligatorio</div>
                </div>

                <button type="submit" class="btn btn-danger">Salva Modifiche</button>
                <button type="button" class="btn btn-outline-danger" id="deleteButton">Elimina Utente</button>
            </form>
        </div>

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


        <!-- jQuery e Bootstrap JS -->
        <script src="js/External/jquery-3.7.1.min.js"></script>
        <script src="js/bootstrap.bundle.min.js"></script>
        <script src="js/custom/global.js"></script>
        <script src="js/custom/globalModal.js"></script>
        <!-- Fancybox JS -->
        <script src="js/External/jquery.fancybox.min.js"></script>

        <script>
            document.getElementById('deleteButton').addEventListener('click', function () {
                if (confirm('Sei sicuro di voler eliminare questo utente?')) {
                    parent.jQuery.fancybox.close();
                    parent.window.location.href = 'InserisciNuovoUtente?remove=true&userId=<%= user.getId() %>';
                }
            });
        </script>

    </body>
</html>
