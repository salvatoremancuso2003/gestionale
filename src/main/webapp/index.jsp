<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="it">
    <head>
        <title>Login</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <!-- Bootstrap CSS -->
        <link rel="stylesheet" href="css/custom/SmartOOP-bootstrap.min.css"/>
        <!-- Index CSS -->
        <link rel="stylesheet" href="css/custom/index.css"/>
        <link href="assets/logo.png" rel="icon">

    </head>
    <body>

        <div class="container-fluid">
            <div class="row d-flex justify-content-center align-items-center w-100">
                <div class="col-12 col-md-8 col-lg-6 col-xl-5">
                    <div class="card">
                        <div class="card-body">
                            <div class="text-center mb-4">
                                <img src="assets/logo.png" class="img-fluid responsive-img" alt="Logo SmartOOP">
                            </div>
                            <form method="POST" action="LoginServlet" id="login_form">
                                <input type="hidden" id="isLogin" name="isLogin" value="true">
                                <h2 class="SmartOOP-text-standard text-center" style="font-weight: bold">Effettua il login</h2>
                                <br>
                                <!-- Email input -->
                                <div class="form-outline mb-4">
                                    <label for="email" class="form-label SmartOOP-text-standard">Indirizzo Email</label>
                                    <input type="email" class="form-control custom-form" id="email" placeholder="Inserisci il tuo indirizzo email" name="username" required>
                                </div>
                                <!-- Password input -->
                                <div class="form-outline mb-3">
                                    <label for="password" class="form-label SmartOOP-text-standard">Password</label>
                                    <input type="password" class="form-control custom-form" id="password" placeholder="Inserisci la tua password" name="password" required>
                                </div>
                                <br>
                                <div class="text-center">
                                    <button type="submit" class="btn Smartoop-btn-standard btn-lg btn-block"
                                            style="padding-left: 2.5rem; padding-right: 2.5rem;">Login</button>
                                </div>
                                <br>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>

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


        <script src="js/bootstrap.bundle.min.js"></script>
        <script src="js/custom/globalModal.js"></script>

    </body>
</html>
