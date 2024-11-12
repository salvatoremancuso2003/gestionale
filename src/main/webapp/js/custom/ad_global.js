/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */


function invioRichiestaSenzaOre() {
    const formData = new URLSearchParams(new FormData(document.getElementById("richiediPermessoForm")));
    formData.append("forzaInvio", "true");
    formData.append("isCreate", "true");

    fetch("RichiestaPermessoServlet", {
        method: "POST",
        body: formData
    })
            .then(response => {
                if (response.ok) {
                    window.location.href = "AD_gestionale.jsp?esito=OK&codice=009";
                } else {
                    throw new Error("Errore durante l'invio della richiesta.");
                }
            })
            .catch(error => {
                showErrorModal("Errore durante l'invio della richiesta.");
            });
}

function showErrorModal(message) {
    const esitoModalBody = document.getElementById("esitoModalBody");
    const esitoModalHeader = document.getElementById("modal-header");
    const esitoModalButton = document.getElementById("esitoModalButton");
    const esitoModalButton2 = document.getElementById("esitoModalButton2");

    esitoModalBody.textContent = message;
    esitoModalBody.classList.add('SmartOOP-text-standard');

    esitoModalHeader.style.background = '#dc3545';
    esitoModalHeader.style.color = 'white';
    esitoModalHeader.querySelector('h5').textContent = "Operazione non andata a buon fine!";
    esitoModalButton.classList.add('Smartoop-btn-danger');

    const esitoModal = new bootstrap.Modal(document.getElementById('esitoModal'));
    esitoModal.show();
}


document.getElementById('tipoPermesso').addEventListener('change', function () {
    var tipoPermesso = this.value;
    var dataInizioWrapper = document.getElementById('dataInizio').parentNode;
    var dataFineWrapper = document.getElementById('dataFine').parentNode;
    var dataInizio = document.getElementById('dataInizio');
    var dataFine = document.getElementById('dataFine');
    dataInizioWrapper.removeChild(dataInizio);
    dataFineWrapper.removeChild(dataFine);
    var now = new Date();
    var today = now.toISOString().split('T')[0];
    var year = now.getFullYear();
    var month = (now.getMonth() + 1).toString().padStart(2, '0');
    var day = now.getDate().toString().padStart(2, '0');
    var hours = now.getHours().toString().padStart(2, '0');
    var minutes = now.getMinutes().toString().padStart(2, '0');
    var minDateTime = year + '-' + month + '-' + day + 'T' + hours + ':' + minutes;


    if (tipoPermesso === '1' || tipoPermesso === '2') {
        var newDataInizio = document.createElement('input');
        newDataInizio.type = 'date';
        newDataInizio.className = 'form-control';
        newDataInizio.id = 'dataInizio';
        newDataInizio.name = 'data_inizio';
        newDataInizio.required = true;
        newDataInizio.setAttribute("min", today);
        var newDataFine = document.createElement('input');
        newDataFine.type = 'date';
        newDataFine.className = 'form-control';
        newDataFine.id = 'dataFine';
        newDataFine.name = 'data_fine';
        newDataFine.required = true;
        newDataFine.setAttribute("min", today);
    } else {
        var newDataInizio = document.createElement('input');
        newDataInizio.type = 'datetime-local';
        newDataInizio.className = 'form-control';
        newDataInizio.id = 'dataInizio';
        newDataInizio.name = 'data_inizio';
        newDataInizio.required = true;
        newDataInizio.setAttribute("min", minDateTime);
        var newDataFine = document.createElement('input');
        newDataFine.type = 'datetime-local';
        newDataFine.className = 'form-control';
        newDataFine.id = 'dataFine';
        newDataFine.name = 'data_fine';
        newDataFine.required = true;
        newDataFine.setAttribute("min", minDateTime);
    }

    dataInizioWrapper.appendChild(newDataInizio);
    dataFineWrapper.appendChild(newDataFine);

    jQuery(document).ready(function ($) {

        $('#dataInizio').change(function () {
            var date = new Date($(this).val());
            const day = date.getDay();
            if (day == 0) {
                const errorDateModal = new bootstrap.Modal(document.getElementById('errorDateModal'));
                errorDateModal.show();
                $(this).val("");
            } else if (day == 6) {
                const errorDateModal = new bootstrap.Modal(document.getElementById('errorDateModal'));
                errorDateModal.show();
                $(this).val("");
            }
        });

        $('#dataFine').change(function () {
            var date = new Date($(this).val());
            const day = date.getDay();
            if (day == 0) {
                const errorDateModal = new bootstrap.Modal(document.getElementById('errorDateModal'));
                errorDateModal.show();
                $(this).val("");
            } else if (day == 6) {
                const errorDateModal = new bootstrap.Modal(document.getElementById('errorDateModal'));
                errorDateModal.show();
                $(this).val("");
            }
        });
    });
});

document.getElementById("richiediPermessoForm").addEventListener("submit", function (event) {
    event.preventDefault();

    const tipoPermesso = document.getElementById("tipoPermesso").value;
    const dataInizio = document.getElementById("dataInizio").value;
    const dataFine = document.getElementById("dataFine").value;


    document.getElementById("isCheck").value = "true";
    document.getElementById("isCreate").value = "false";


    const formData = new URLSearchParams();
    formData.append("tipo_permesso", tipoPermesso);
    formData.append("data_inizio", dataInizio);
    formData.append("data_fine", dataFine);
    formData.append("isCheck", "true");

    fetch("RichiestaPermessoServlet", {
        method: "POST",
        body: formData
    })
            .then(response => {
                if (!response.ok) {
                    throw new Error("Errore generico");
                }
                return response.json();
            })
            .then(data => {
                if (data.success) {
                    // Cambia isCreate a true per la creazione della richiesta
                    document.getElementById("isCheck").value = "false";
                    document.getElementById("isCreate").value = "true";
                    document.getElementById("richiediPermessoForm").submit();
                } else {
                    showErrorIns(data.message);
                }
            })
            .catch(error => {
                showErrorModal("Errore durante la verifica delle ore disponibili.");
            });
});

function showErrorModal(message) {
    const esitoModalBody = document.getElementById("esitoModalBody");
    const esitoModalHeader = document.getElementById("modal-header");
    const esitoModalButton = document.getElementById("esitoModalButton");

    esitoModalBody.textContent = message;
    esitoModalBody.classList.add('SmartOOP-text-error');

    esitoModalHeader.style.background = '#dc3545';
    esitoModalHeader.style.color = 'white';
    esitoModalHeader.querySelector('h5').textContent = "Operazione non andata a buon fine!";
    esitoModalButton.classList.add('Smartoop-btn-error');

    const esitoModal = new bootstrap.Modal(document.getElementById('esitoModal'));
    esitoModal.show();
}

function showErrorIns(message) {
    const esitoModalBody = document.getElementById("esitoModalBodyIns");
    const esitoModalHeader = document.getElementById("modal-headerIns");
    const esitoModalButton = document.getElementById("esitoModalButton");
    const esitoModalButton2 = document.getElementById("esitoModalButton2");

    esitoModalBody.textContent = message;
    //esitoModalBody.classList.add('text-warning');

    esitoModalHeader.style.background = '#ffc107';
    esitoModalHeader.style.color = 'white';
    esitoModalHeader.querySelector('h5').textContent = "Attenzione!";
    esitoModalButton.classList.add('Smartoop-btn-error');

    const esitoModal = new bootstrap.Modal(document.getElementById('esitoModalIns'));
    esitoModal.show();
}


document.getElementById('tipoPermesso').addEventListener('change', function () {
    var tipoPermesso = this.value;
    var dataInizio = document.getElementById('dataInizio');
    var dataFine = document.getElementById('dataFine');
    var now = new Date();
    var todayWithTime = now.toISOString().slice(0, 16);
    dataInizio.setAttribute("min", todayWithTime);
    dataFine.setAttribute("min", todayWithTime);
    if (tipoPermesso === '1') {
        dataInizio.type = 'date';
        dataFine.type = 'date';
        var today = now.toISOString().split('T')[0];
        dataInizio.setAttribute("min", today);
        dataFine.setAttribute("min", today);
    } else if (tipoPermesso === '2') {
        dataInizio.type = 'date';
        dataFine.type = 'date';
        var today = now.toISOString().split('T')[0];
        dataInizio.setAttribute("min", today);
        dataFine.setAttribute("min", today);
    } else {
        dataInizio.type = 'datetime-local';
        dataFine.type = 'datetime-local';
        dataInizio.setAttribute("min", todayWithTime);
        dataFine.setAttribute("min", todayWithTime);
    }
});