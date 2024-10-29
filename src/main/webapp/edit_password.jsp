<%-- 
    Document   : edit_password
    Created on : 15 ott 2024, 15:12:11
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

  
%>
<!DOCTYPE html>
<html lang="it">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Edit Password</title>
        <!-- Bootstrap CSS -->
        <link rel="stylesheet" href="css/custom/SmartOOP-bootstrap.min.css"/>
        <!-- Custom Global CSS -->
        <link rel="stylesheet" href="css/custom/global.css"/>
        <!-- Bootstrap Icons -->
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css">
        <link rel="icon" href="assets/logo.png"/>
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
                            <h1 class="display-4 SmartOOP-text-standard">Benvenuto <%= Utility.checkAttribute(session, "nome") %> ! </h1>
                            <p class="lead mt-4"><%= Utility.checkAttribute(session, "nome") %> , per poter accedere al gestionale, inserisci la tua nuova password.</p>
                        </div>
                    </div>
                </div>
            </div>
        </main>

        <div class="container">
            <form id="creaUtenteForm" action="InserisciNuovoUtente" method="POST"
                  style="display: block" onsubmit="return checkPasswords()">
                <br>

                <div class="mb-3">
                    <label for="password" class="form-label SmartOOP-text-standard" style="font-weight: bold">Nuova password</label>
                    <div class="input-group">
                        <input type="password" class="form-control" id="password" name="password" required>
                        <button type="button" class="btn Smartoop-btn-outline-standard" id="togglePassword">
                            <i class="bi bi-eye-slash"></i>
                        </button>
                    </div>
                </div>

                <input type="hidden" value="true" name="editPass"> 

                <div class="mb-3">
                    <label for="password2" class="form-label SmartOOP-text-standard" style="font-weight: bold">Conferma nuova password</label>
                    <div class="input-group">
                        <input type="password" class="form-control" id="password2" name="password2" required>
                        <button type="button" class="btn Smartoop-btn-outline-standard" id="togglePassword2">
                            <i class="bi bi-eye-slash"></i>
                        </button>
                    </div>
                </div>

                <div class="text-center">
                    <button type="submit" class="btn Smartoop-btn-standard" id="AggiornaPassword">Aggiorna password</button>
                </div>
            </form>
        </div>


        <br>

        <!-- Footer -->
        <%
            Date date = new Date();
            LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int year = localDate.getYear();
        %>
        <footer class="text-white text-lg-center">
            <p>&copy; <%=year%> SmartOOP. Tutti i diritti riservati. | Contattaci: info@smartoop.it</p>
        </footer>

        <script>
            function checkPasswords() {
                const password = document.getElementById('password').value;
                const password2 = document.getElementById('password2').value;
                if (password !== password2) {
                    alert('Le password non coincidono. Riprova.');
                    return false;
                }
                return true;
            }

            function togglePasswordVisibility(inputId, toggleButtonId) {
                const passwordInput = document.getElementById(inputId);
                const toggleButton = document.getElementById(toggleButtonId);
                const icon = toggleButton.querySelector('i');
                toggleButton.addEventListener('click', function () {
                    if (passwordInput.type === 'password') {
                        passwordInput.type = 'text';
                        icon.classList.remove('bi-eye-slash');
                        icon.classList.add('bi-eye');
                    } else {
                        passwordInput.type = 'password';
                        icon.classList.remove('bi-eye');
                        icon.classList.add('bi-eye-slash');
                    }
                });
            }

            togglePasswordVisibility('password', 'togglePassword');
            togglePasswordVisibility('password2', 'togglePassword2');
        </script>


        <script src="js/bootstrap.bundle.min.js" ></script>

    </body>
