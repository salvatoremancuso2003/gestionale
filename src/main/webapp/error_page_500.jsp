<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>SmartOOP</title>
        <!-- Bootstrap CSS -->
        <link href="css/custom/SmartOOP-bootstrap.min.css" rel="stylesheet">
        <!-- Font Awesome CSS -->
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" rel="stylesheet">
        <!-- Custom CSS -->
        <link rel="stylesheet" href="css/custom/errorpages.css"/>
        <link rel="icon" href="assets/logo.png"/>

    </head>
    <body>
        <div class="container-fluid">
            <div class="row justify-content-center">
                <div class="col-lg-6 col-md-8 col-sm-10">
                    <div class="text-center mt-5">
                        <h1 class="text-centser">500</h1>
                        <hr style="color: #dc3545; border: 1px solid #dc3545">
                        <img src="assets/logo.png" id="responsive-img" alt="SmartOOP_logo">
                        <h1 class="kt-error_subtitle">Errore del server</h1>
                        <p class="kt-error_description">Ci scusiamo per il disagio. Si è verificato un errore interno del server. </p>
                        <p class="kt-error_description">
                            <button id="back_btn" class="btn Smartoop-btn-standard"><i class="fa fa-backspace"></i> Torna indietro</button>
                            <button id="homepage" class="btn Smartoop-btn-standard"><i class="fa fa-home"></i> Homepage</button>
                        </p>
                        <hr style="color: #dc3545; border: 1px solid #dc3545">
                    </div>
                </div>
            </div>
        </div>

        <!-- Bootstrap JS -->
        <script src="js/bootstrap.bundle.min.js"></script>
        <!-- Font Awesome JS -->
        <script src="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/js/all.min.js"></script>
        <script src="js/External/jquery-3.7.1.min.js"></script>

        <script>
            $(document).ready(function () {
                $("#back_btn").click(function () {
                    window.history.back();
                });
            });
            $(document).ready(function () {
                $("#homepage").click(function () {
                    window.location.href = "index.jsp";
                });
            });
        </script>
    </body>
</html>
