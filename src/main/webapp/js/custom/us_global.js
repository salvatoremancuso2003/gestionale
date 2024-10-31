/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

$(document).ready(function () {
    var table = $('#notificheTable').DataTable({
        "ajax": {
            "url": "GetNotificheServlet",
            "type": "POST",
            data: function (d) {
                d.dataCreazione = $("#dataCreazione").val();
                d.data = $("#esito").val();
            },
            "dataSrc": function (json) {
                $('#notificaBadge .SmartOOP-text-standard').text(json.totalNotifiche || 0);
                return json.aaData || [];
            },
            "dataType": "json"
        },
        "columns": [
            {"data": "id"},
            {"data": "messaggio"},
            {"data": "dataCreazione"},
            {"data": "esito"}
        ],
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
    $("#dataCreazione").change(function () {
        table.ajax.reload(null, false);
    });
    $("#esito").change(function () {
        table.ajax.reload(null, false);
    });
});
function mostraNotifiche() {
    var notificheModal = new bootstrap.Modal(document.getElementById('notificheModal'));
    var segnaComeLettoButton = document.getElementById('segnaComeLettoButton');
    $.ajax({
        url: 'GetNotificheServlet',
        type: 'POST',
        dataType: 'json',
        success: function (data) {
            $('#notificaBadge .SmartOOP-text-standard').text(data.totalNotifiche);
        },
        error: function () {
            console.error("Errore nel recuperare il conteggio delle notifiche.");
        }
    });
    $.ajax({
        url: 'GetNotificheServlet',
        type: 'POST',
        dataType: 'json',
        success: function (data) {

            if (data.totalNotifiche === 0) {
                segnaComeLettoButton.style.display = 'none';
                $('#notificheTable_wrapper').hide();
                $('#noNotificheMessage').show();
            } else {
                $('#noNotificheMessage').hide();
                $('#notificheTable_wrapper').show();
                segnaComeLettoButton.style.display = 'block';
                // Ricarica i dati della DataTable
                $('#notificheTable').DataTable().clear().rows.add(data.aaData).draw();
            }
        },
        error: function () {
            console.error("Errore nel recuperare le notifiche.");
        }
    });
    notificheModal.show();
}

function segnaComeLetto() {
    $.ajax({
        url: 'NotificaServlet',
        type: 'POST',
        data: {read: true},
        success: function () {
            $('#notificaBadge .SmartOOP-text-standard').text('0');
            console.log("Notifiche segnate come lette.");
            location.reload();
        },
        error: function () {
            console.error("Errore nel segnare le notifiche come lette.");
        }
    });
}


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
                    window.location.href = "US_gestionale.jsp?esito=OK&codice=009";
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


document.getElementById('segnaComeLettoButton').addEventListener('click', segnaComeLetto);
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
                console.log("Risposta del servlet:", data); // Debug per vedere la risposta del servlet

                if (data.success) {
                    // Cambia isCreate a true per la creazione della richiesta
                    document.getElementById("isCheck").value = "false";
                    document.getElementById("isCreate").value = "true";
                    document.getElementById("richiediPermessoForm").submit();
                } else {
                    showErrorIns(data.message); // Mostra il messaggio di errore
                }
            })
            .catch(error => {
                showErrorModal("Errore durante la verifica delle ore disponibili.");
                console.error("Errore:", error); // Debug per eventuali errori
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