<%-- 
    Document   : dettagliPermesso
    Created on : 19 set 2024, 16:31:27
    Author     : Salvatore
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="Utils.Utility"%>
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
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Dettagli - Permessi</title>
        <!-- Custom Global CSS -->
        <link rel="stylesheet" href="css/custom/SmartOOP-bootstrap.min.css"/>
        <link rel="stylesheet" href="css/custom/global.css"/>
        
        <link rel="icon" href="assets/logo.png"/>
        <!-- Fancybox css -->
        <link rel="stylesheet" href="css/External/jquery.fancybox.css"/>
        <!-- DataTables CSS -->
        <link rel="stylesheet" href="css/External/dataTables.bootstrap5.css"/>
        <style>
           
            #details_table th{
                color: #0d6efd;
            }
        </style>
    </head>

    <body>
        <% 
            String richiestaId =  Utility.sanitize(request.getParameter("richiestaId"));
            String esito =  Utility.sanitize(request.getParameter("esito"));
            String codice =  Utility.sanitize(request.getParameter("codice"));
        %>

        <div class="container-fluid">
            <br>
            <h2 class="SmartOOP-text-standard text-center">Dettagli Permessi</h2>
            <div class="table-responsive">
                <table class="table table-responsive table-hover table-striped" id="details_table">
                    <thead>
                        <tr>
                            <th>Tipo</th>
                            <th>Nome completo</th>
                            <th>Data inizio</th>
                            <th>Data fine</th>
                            <th>Note</th>
                            <th>Stato</th>
                            <th>Allegato</th>
                            <th>Data di upload</th>
                            <th>Gestisci</th>
                        </tr>
                    </thead>
                    <tbody>
                    </tbody>
                </table>
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

        <!-- jQuery e Bootstrap JS -->
        <script src="js/bootstrap.bundle.min.js"></script>
        <script src="js/External/jquery-3.7.1.min.js"></script>
        <script src="js/custom/global.js"></script>
        <!-- DataTables JS -->
        <script src="js/External/dataTables.js"></script>
        <script src="js/External/dataTables.bootstrap5.js"></script>
        <!-- Fancybox JS -->
        <script src="js/External/jquery.fancybox.min.js"></script>

        <script src="js/custom/globalModal.js"></script>


        <script>
            function closeFancyboxAndSubmit(event) {
                event.preventDefault();
                setTimeout(function () {
                    event.target.submit();
                    
                    //window.parent.location.href = 'AD_calendar.jsp?esito=OK&codice=004';
                }, 300);
            }
        </script>


        <script>
            $(document).ready(function () {
                var richiestaId = "<%= richiestaId %>";

                $('#details_table').DataTable({
                    "ajax": {
                        "url": "GetDettagliPermessiServlet",
                        "type": "POST",
                        "data": {
                            "richiestaId": richiestaId
                        },
                        "dataSrc": "aaData"
                    },
                    "columns": [
                        {"data": "tipo"},
                        {"data": "nomeCompleto"},
                        {"data": "data_inizio"},
                        {"data": "data_fine"},
                        {"data": "note"},
                        {"data": "stato"},
                        {"data": "allegato"},
                        {"data": "uploadDate"},
                        {"data": "gestisci"}
                    ],
                    "paging": true,
                    "searching": true,
                    "info": true,
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
                    order: [[0, 'desc']],
                    paginate: {
                        first: "Inizio",
                        last: "Fine",
                        next: "Avanti",
                        previous: "Indietro"
                    }
                },
                        );
            });

        </script>

    </body>
</html>

