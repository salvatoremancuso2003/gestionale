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
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Modifica Utente</title>
        <!-- Bootstrap CSS -->
        <link rel="stylesheet" href="css/custom/SmartOOP-bootstrap.min.css"/>
        <!-- Custom Global CSS -->
        <link rel="icon" href="assets/logo.png"/>
        <link rel="stylesheet" href="css/custom/global.css"/>
        <!-- Fancybox CSS -->
        <link rel="stylesheet" href="css/External/jquery.fancybox.css"/>
    </head>
    <body>

        <div class="container text-center">
            <h1 class="SmartOOP-text-standard" style="font-weight: bold">Modifica utente</h1>
            <form id="editUserForm" action="InserisciNuovoUtente" method="POST" class="needs-validation">
                <input type="hidden" name="update" value="true">
                <input type="hidden" name="userId" value="<%= user.getId() %>">
                <div class="row">
                    <div class="col-6">
                        <label for="nome" class="form-label SmartOOP-text-standard" style="font-weight: bold">Nome</label>
                        <input type="text" class="form-control" name="nuovoNome" id="nome" required value="<%= user != null ? user.getNome() : "" %>">
                        <div class="invalid-feedback">Campo obbligatorio</div>
                    </div>

                    <div class="col-6">
                        <label for="cognome" class="form-label SmartOOP-text-standard" style="font-weight: bold">Cognome</label>
                        <input type="text" class="form-control" name="nuovoCognome" id="cognome" required value="<%= user != null ? user.getCognome() : "" %>">
                        <div class="invalid-feedback">Campo obbligatorio</div>
                    </div>

                </div>
                <div class="row">
                    <div class="col-4">
                        <label for="email" class="form-label SmartOOP-text-standard" style="font-weight: bold">Email</label>
                        <input type="email" class="form-control" name="nuovaEmail" id="email" required value="<%= user != null ? user.getEmail() : "" %>">
                        <div class="invalid-feedback">Campo obbligatorio</div>
                    </div>

                    <div class="col-4">
                        <label for="numero_di_telefono" class="form-label SmartOOP-text-standard" style="font-weight: bold">Numero di telefono</label>
                        <div class="input-group">
                            <span class="input-group-text">+39</span> 
                            <input type="text" maxlength="10" minlength="10" class="form-control" id="numero_di_telefono" name="nuovoNumero" required pattern="\d{10}" value="<%= user != null ? user.getNumero_di_telefono().replace("+39", "") : "" %>" onkeypress="return event.charCode >= 48 && event.charCode <= 57">
                        </div>
                        <div class="invalid-feedback">Campo obbligatorio</div>
                    </div>
                    <div class="col-4">
                        <label for="ore_lavorative" class="form-label SmartOOP-text-standard" style="font-weight: bold">Ore contratto</label>
                        <div class="input-group">
                            <input type="text" maxlength="1" minlength="1" class="form-control" id="ore_lavorative" name="nuove_ore_lavorative" required pattern="\d{1}" value="<%= user != null ? user.getOre_contratto() : "" %>" onkeypress="return event.charCode >= 48 && event.charCode <= 57">
                        </div>
                        <div class="invalid-feedback">Campo obbligatorio</div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-6">
                        <label for="ferie" class="form-label SmartOOP-text-standard" style="font-weight: bold">Ferie disponibili (GIORNI)</label>
                        <input type="text" maxlength="2" class="form-control" name="ferie" id="ferie" required pattern="\d{1,2}" value="<%= user != null ? user.getFerie_disponibili() : "" %>" onkeypress="return event.charCode >= 48 && event.charCode <= 57">
                        <div class="invalid-feedback">Campo obbligatorio</div>
                    </div>

                    <div class="col-6">
                        <label for="ore" class="form-label SmartOOP-text-standard" style="font-weight: bold">Ore R.O.L. disponibili</label>
                        <input type="text" maxlength="2" class="form-control" name="ore" id="ore" required pattern="\d{1,2}" value="<%= user != null ? user.getOre_disponibili() : "" %>" onkeypress="return event.charCode >= 48 && event.charCode <= 57">
                        <div class="invalid-feedback">Campo obbligatorio</div>
                    </div>
                </div>

                <br>


                <button type="submit" id="updateButton" class="btn btn-success">Salva Modifiche</button>
                <button type="button" class="btn Smartoop-btn-outline-error " id="deleteButton">Elimina Utente</button>
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

        <div class="modal fade" id="confermaEliminazioneModal" tabindex="-1" aria-labelledby="confermaEliminazioneModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header Smartoop-bg-standard text-white">
                        <h5 class="modal-title" id="confermaEliminazioneModalLabel">Conferma Eliminazione</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        Sei sicuro di voler eliminare l'utente <strong><%= user != null ? user.getNome() + " " + user.getCognome() : "" %></strong>? 
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Annulla</button>
                        <button type="button" class="btn Smartoop-btn-outline-success" id="confermaEliminaButton">Elimina</button>
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
                            document.getElementById('updateButton').addEventListener('click', function (event) {
                                event.preventDefault();
                                var form = document.getElementById('editUserForm');

                                if (form.checkValidity()) {
                                    $.ajax({
                                        type: form.method,
                                        url: form.action,
                                        data: $(form).serialize(),
                                        success: function (response) {
                                            parent.jQuery.fancybox.close();
                                            parent.window.location = 'AD_gestioneUtente.jsp?esito=OK&codice=007';
                                        },
                                        error: function () {
                                            parent.window.location = 'AD_gestioneUtente.jsp?esito=KO&codice=007';
                                        }
                                    });
                                } else {
                                    form.classList.add('was-validated');
                                }
                            });
        </script>


        <script>
            document.getElementById('deleteButton').addEventListener('click', function () {
                var eliminaModal = new bootstrap.Modal(document.getElementById('confermaEliminazioneModal'), {
                    keyboard: false
                });
                eliminaModal.show();
            });

            document.getElementById('confermaEliminaButton').addEventListener('click', function () {
                $.ajax({
                    type: 'POST',
                    url: 'InserisciNuovoUtente',
                    data: {remove: true, userId: '<%= user.getId() %>'},
                    success: function (response) {
                        parent.jQuery.fancybox.close();
                        parent.window.location.href = 'AD_gestioneUtente.jsp?esito=OK2&codice=007';
                    },
                    error: function () {
                        response.sendRedirect("AD_gestioneUtente.jsp?esito=KO2&codice=007");
                    }
                });
            });

        </script>

    </body>
</html>
