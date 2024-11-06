/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */


var params = new URLSearchParams(window.location.search);
var esito = params.get('esito');
var codice = params.get('codice');
var tipo = params.get('tipo');
var richiesta = params.get('richiestaId');
var visual = params.get('visual');

if (esito !== null && codice !== null) {
    var esitoModal = new bootstrap.Modal(document.getElementById('esitoModal'));
    var esitoModalBody = document.getElementById('esitoModalBody');
    var esitoModalButton = document.getElementById('esitoModalButton');
    var esitoModalHeader = document.getElementById('modal-header');

    esitoModalBody.classList.remove('text-success', 'SmartOOP-text-standard');

    if (esito === "KO" && codice === '000') {
        esitoModalBody.textContent = "Email o/e password errati";
        esitoModalBody.style.color = '#dc3545';
        esitoModalHeader.style.background = '#dc3545';
        esitoModalHeader.style.color = 'white';
        esitoModalHeader.textContent = "Operazione non andata a buon fine!";
        esitoModalButton.style.backgroundColor = '#dc3545';
        esitoModalButton.style.borderColor = '#dc3545';
        esitoModalButton.style.color = 'white';
        esitoModal.show();
        redirect();
    } else if (esito === "KO2" && codice === '000') {
        esitoModalBody.textContent = "Utente disabilitato. Non puoi effettuare l'accesso.";
        esitoModalBody.style.color = '#dc3545';
        esitoModalHeader.style.background = '#dc3545';
        esitoModalHeader.style.color = 'white';
        esitoModalHeader.textContent = "Operazione non andata a buon fine!";
        esitoModalButton.style.backgroundColor = '#dc3545';
        esitoModalButton.style.borderColor = '#dc3545';
        esitoModalButton.style.color = 'white';
        esitoModal.show();
        redirect();
    } else if (esito === "KO3" && codice === '000') {
        esitoModalBody.textContent = "Utente non trovato. Non è stato possibile trovare l'utente.";
        esitoModalBody.style.color = '#dc3545';
        esitoModalHeader.style.background = '#dc3545';
        esitoModalHeader.style.color = 'white';
        esitoModalHeader.textContent = "Operazione non andata a buon fine!";
        esitoModalButton.style.backgroundColor = '#dc3545';
        esitoModalButton.style.borderColor = '#dc3545';
        esitoModalButton.style.color = 'white';
        esitoModal.show();
        redirect();
    } else if (esito === "KO4" && codice === '000') {
        esitoModalBody.textContent = "Utente non trovato. Non è stato possibile impostare la nuova password. Riprova.";
        esitoModalBody.style.color = '#dc3545';
        esitoModalHeader.style.background = '#dc3545';
        esitoModalHeader.style.color = 'white';
        esitoModalHeader.textContent = "Operazione non andata a buon fine!";
        esitoModalButton.style.backgroundColor = '#dc3545';
        esitoModalButton.style.borderColor = '#dc3545';
        esitoModalButton.style.color = 'white';
        esitoModal.show();
        redirect();

    } else if (esito === "OK" && codice === '000') {
        esitoModalBody.textContent = "Logout effettuato con successo!";
        esitoModalBody.style.color = '#198754';
        esitoModalHeader.style.background = '#198754';
        esitoModalHeader.style.color = 'white';
        esitoModalHeader.textContent = "Operazione effettuata con successo!";
        esitoModalButton.style.backgroundColor = '#198754';
        esitoModalButton.style.borderColor = '#198754';
        esitoModalButton.style.color = 'white';
        esitoModal.show();
        redirect();
    } else if (esito === "OK2" && codice === '000') {
        esitoModalBody.textContent = "Password impostata con successo! Prova ad effettuare l'accesso.";
        esitoModalBody.style.color = '#198754';
        esitoModalHeader.style.background = '#198754';
        esitoModalHeader.style.color = 'white';
        esitoModalHeader.textContent = "Operazione effettuata con successo!";
        esitoModalButton.style.backgroundColor = '#198754';
        esitoModalButton.style.borderColor = '#198754';
        esitoModalButton.style.color = 'white';
        esitoModal.show();
        redirect();

    } else if (esito === "OK" && codice === '001') {
        esitoModalBody.textContent = "Richiesta permesso salvata con successo!";
        esitoModalBody.style.color = '#198754';
        esitoModalHeader.style.background = '#198754';
        esitoModalHeader.style.color = 'white';
        esitoModalHeader.textContent = "Operazione effettuata con successo!";
        esitoModalButton.style.backgroundColor = '#198754';
        esitoModalButton.style.borderColor = '#198754';
        esitoModalButton.style.color = 'white';
        esitoModal.show();
        redirect();
    } else if (esito === "KO" && codice === '001') {
        esitoModalBody.textContent = "Non è stato possibile salvare la richiesta. Riprova.";
        esitoModalBody.style.color = '#dc3545';
        esitoModalHeader.style.background = '#dc3545';
        esitoModalHeader.style.color = 'white';
        esitoModalHeader.textContent = "Operazione non andata a buon fine!";
        esitoModalButton.style.backgroundColor = '#dc3545';
        esitoModalButton.style.borderColor = '#dc3545';
        esitoModalButton.style.color = 'white';
        esitoModal.show();
        redirect();
    } else if (esito === "KO2" && codice === '001') {
        esitoModalBody.textContent = "Hai già una richiesta di permesso dello stesso tipo o differente non ad ore per il giorno indicato.";
        esitoModalBody.style.color = '#dc3545';
        esitoModalHeader.style.background = '#dc3545';
        esitoModalHeader.style.color = 'white';
        esitoModalHeader.textContent = "Operazione non andata a buon fine!";
        esitoModalButton.style.backgroundColor = '#dc3545';
        esitoModalButton.style.color = 'white';
        esitoModalButton.style.borderColor = '#dc3545';
        esitoModalButton.style.color = 'white';
        esitoModal.show();
        redirect();
    } else if (esito === "OK" && codice === '002') {
        if (tipo !== null && tipo === 'ingresso') {
            esitoModalBody.textContent = "Ingresso salvato con successo!";
        } else if (tipo !== null && tipo === 'uscita') {
            esitoModalBody.textContent = "Uscita salvata con successo!";
        }
        esitoModalBody.style.color = '#198754';
        esitoModalHeader.style.background = '#198754';
        esitoModalHeader.style.color = 'white';
        esitoModalHeader.textContent = "Operazione effettuata con successo!";
        esitoModalButton.style.backgroundColor = '#198754';
        esitoModalButton.style.borderColor = '#198754';
        esitoModalButton.style.color = 'white';
        esitoModal.show();
        redirect();
    } else if (esito === 'KO' && codice === '002') {
        if (tipo !== null && tipo === 'ingresso') {
            esitoModalBody.textContent = "Non è stato possibile salvare l'ingresso.";
        } else if (tipo !== null && tipo === 'uscita') {
            esitoModalBody.textContent = "Non è stato possibile salvare l'uscita.";
        }
        esitoModalBody.style.color = '#dc3545';
        esitoModalHeader.style.background = '#dc3545';
        esitoModalHeader.style.color = 'white';
        esitoModalHeader.textContent = "Operazione non andata a buon fine!";
        esitoModalButton.style.backgroundColor = '#dc3545';
        esitoModalButton.style.borderColor = '#dc3545';
        esitoModalButton.style.color = 'white';
        esitoModal.show();
        redirect();
    } else if (esito === 'KO2' && codice === '002') {
        esitoModalBody.textContent = "Uscita mancante.";
        esitoModalBody.style.color = '#dc3545';
        esitoModalHeader.style.background = '#dc3545';
        esitoModalHeader.style.color = 'white';
        esitoModalHeader.textContent = "Operazione non andata a buon fine!";
        esitoModalButton.style.backgroundColor = '#dc3545';
        esitoModalButton.style.borderColor = '#dc3545';
        esitoModalButton.style.color = 'white';
        esitoModal.show();
        redirect();
    } else if (esito === 'KO3' && codice === '002') {
        esitoModalBody.textContent = "Ingresso mancante.";
        esitoModalBody.style.color = '#dc3545';
        esitoModalHeader.style.background = '#dc3545';
        esitoModalHeader.style.color = 'white';
        esitoModalHeader.textContent = "Operazione non andata a buon fine!";
        esitoModalButton.style.backgroundColor = '#dc3545';
        esitoModalButton.style.borderColor = '#dc3545';
        esitoModalButton.style.color = 'white';
        esitoModal.show();
        redirect();
    } else if (esito === 'KO4' && codice === '002') {
        esitoModalBody.textContent = "Tipo non valido.";
        esitoModalBody.style.color = '#dc3545';
        esitoModalHeader.style.background = '#dc3545';
        esitoModalHeader.style.color = 'white';
        esitoModalHeader.textContent = "Operazione non andata a buon fine!";
        esitoModalButton.style.backgroundColor = '#dc3545';
        esitoModalButton.style.borderColor = '#dc3545';
        esitoModalButton.style.color = 'white';
        esitoModal.show();
        redirect();
    } else if (esito === 'KO5' && codice === '002') {
        esitoModalBody.textContent = "Parametri assenti.";
        esitoModalBody.style.color = '#dc3545';
        esitoModalHeader.style.background = '#dc3545';
        esitoModalHeader.style.color = 'white';
        esitoModalHeader.textContent = "Operazione non andata a buon fine!";
        esitoModalButton.style.backgroundColor = '#dc3545';
        esitoModalButton.style.borderColor = '#dc3545';
        esitoModalButton.style.color = 'white';
        esitoModal.show();
        redirect();
    } else if (esito === 'OK' && codice === '003') {
        esitoModalBody.textContent = "File modificato con successo!";
        esitoModalBody.style.color = '#198754';
        esitoModalHeader.style.background = '#198754';
        esitoModalHeader.style.color = 'white';
        esitoModalHeader.textContent = "Operazione effettuata con successo!";
        esitoModalButton.style.backgroundColor = '#198754';
        esitoModalButton.style.borderColor = '#198754';
        esitoModalButton.style.color = 'white';
        esitoModal.show();
        redirect();
    } else if (esito === 'KO' & codice === '003') {
        esitoModalBody.textContent = "Non è stato possibile modificare il file selezionato. Riprova.";
        esitoModalBody.style.color = '#dc3545';
        esitoModalHeader.style.background = '#dc3545';
        esitoModalHeader.style.color = 'white';
        esitoModalHeader.textContent = "Operazione non andata a buon fine!";
        esitoModalButton.style.backgroundColor = '#dc3545';
        esitoModalButton.style.borderColor = '#dc3545';
        esitoModalButton.style.color = 'white';
        esitoModal.show();
        redirect();
    } else if (esito === 'OK' && codice === '004') {
        esitoModalBody.textContent = "File eliminato con successo!";
        esitoModalBody.style.color = '#198754';
        esitoModalHeader.style.background = '#198754';
        esitoModalHeader.style.color = 'white';
        esitoModalHeader.textContent = "Operazione effettuata con successo!";
        esitoModalButton.style.backgroundColor = '#198754';
        esitoModalButton.style.borderColor = '#198754';
        esitoModalButton.style.color = 'white';
        esitoModal.show();
        redirect();
    } else if (esito === 'KO' & codice === '004') {
        esitoModalBody.textContent = "Non è stato possibile eliminare il file selezionato. Riprova.";
        esitoModalBody.style.color = '#dc3545';
        esitoModalHeader.style.background = '#dc3545';
        esitoModalHeader.style.color = 'white';
        esitoModalHeader.textContent = "Operazione non andata a buon fine!";
        esitoModalButton.style.backgroundColor = '#dc3545';
        esitoModalButton.style.borderColor = '#dc3545';
        esitoModalButton.style.color = 'white';
        esitoModal.show();
        redirect();
    } else if (esito === 'OK' && codice === '005') {
        esitoModalBody.textContent = "File caricato con successo!";
        esitoModalBody.style.color = '#198754';
        esitoModalHeader.style.background = '#198754';
        esitoModalHeader.style.color = 'white';
        esitoModalHeader.textContent = "Operazione effettuata con successo!";
        esitoModalButton.style.backgroundColor = '#198754';
        esitoModalButton.style.borderColor = '#198754';
        esitoModalButton.style.color = 'white';
        esitoModal.show();
        redirect();
    } else if (esito === 'KO' & codice === '005') {
        esitoModalBody.textContent = "Non è stato possibile caricare il file selezionato. Riprova.";
        esitoModalBody.style.color = '#dc3545';
        esitoModalHeader.style.background = '#dc3545';
        esitoModalHeader.style.color = 'white';
        esitoModalHeader.textContent = "Operazione non andata a buon fine!";
        esitoModalButton.style.backgroundColor = '#dc3545';
        esitoModalButton.style.borderColor = '#dc3545';
        esitoModalButton.style.color = 'white';
        esitoModal.show();
        redirect();

    } else if (esito === 'OK' && codice === '006') {
        esitoModalBody.textContent = "Utente creato con successo!";
        esitoModalBody.style.color = '#198754';
        esitoModalHeader.style.background = '#198754';
        esitoModalHeader.textContent = "Operazione effettuata con successo!";
        esitoModalButton.style.backgroundColor = '#198754';
        esitoModalButton.style.borderColor = '#198754';
        esitoModalButton.style.color = 'white';
        esitoModal.show();
        redirect();
    } else if (esito === 'KO' & codice === '006') {
        esitoModalBody.textContent = "Non è stato possibile creare l'utente. Riprova.";
        esitoModalBody.style.color = '#dc3545';
        esitoModalHeader.style.background = '#dc3545';
        esitoModalHeader.style.color = 'white';
        esitoModalHeader.textContent = "Operazione non andata a buon fine!";
        esitoModalButton.style.backgroundColor = '#dc3545';
        esitoModalButton.style.borderColor = '#dc3545';
        esitoModalButton.style.color = 'white';
        esitoModal.show();
        redirect();
    } else if (esito === 'OK' && codice === '007') {
        esitoModalBody.textContent = "Utente aggiornato con successo!";
        esitoModalBody.style.color = '#198754';
        esitoModalHeader.style.background = '#198754';
        esitoModalHeader.style.color = 'white';
        esitoModalHeader.textContent = "Operazione effettuata con successo!";
        esitoModalButton.style.backgroundColor = '#198754';
        esitoModalButton.style.borderColor = '#198754';
        esitoModalButton.style.color = 'white';
        esitoModal.show();
        redirect();
    } else if (esito === 'KO' & codice === '007') {
        esitoModalBody.textContent = "Non è stato possibile aggiornare l'utente selezionato. Riprova.";
        esitoModalBody.style.color = '#dc3545';
        esitoModalHeader.style.background = '#dc3545';
        esitoModalHeader.style.color = 'white';
        esitoModalHeader.textContent = "Operazione non andata a buon fine!";
        esitoModalButton.style.backgroundColor = '#dc3545';
        esitoModalButton.style.borderColor = '#dc3545';
        esitoModalButton.style.color = 'white';
        esitoModal.show();
        redirect();
    } else if (esito === 'OK2' && codice === '007') {
        esitoModalBody.textContent = "Utente eliminato con successo!";
        esitoModalBody.style.color = '#198754';
        esitoModalHeader.style.background = '#198754';
        esitoModalHeader.style.color = 'white';
        esitoModalHeader.textContent = "Operazione effettuata con successo!";
        esitoModalButton.style.backgroundColor = '#198754';
        esitoModalButton.style.borderColor = '#198754';
        esitoModalButton.style.color = 'white';
        esitoModal.show();
        redirect();
    } else if (esito === 'KO2' & codice === '007') {
        esitoModalBody.textContent = "Non è stato possibile eliminare l'utente selezionato. Riprova.";
        esitoModalBody.style.color = '#dc3545';
        esitoModalHeader.style.background = '#dc3545';
        esitoModalHeader.style.color = 'white';
        esitoModalHeader.textContent = "Operazione non andata a buon fine!";
        esitoModalButton.style.backgroundColor = '#dc3545';
        esitoModalButton.style.borderColor = '#dc3545';
        esitoModalButton.style.color = 'white';
        esitoModal.show();
        redirect();
    } else if (esito === 'OK3' && codice === '007') {
        esitoModalBody.textContent = "Utente riabilitato con successo!";
        esitoModalBody.style.color = '#198754';
        esitoModalHeader.style.background = '#198754';
        esitoModalHeader.style.color = 'white';
        esitoModalHeader.textContent = "Operazione effettuata con successo!";
        esitoModalButton.style.backgroundColor = '#198754';
        esitoModalButton.style.borderColor = '#198754';
        esitoModalButton.style.color = 'white';
        esitoModal.show();
        redirect();
    } else if (esito === 'KO3' & codice === '007') {
        esitoModalBody.textContent = "Non è stato possibile riabilitare l'utente selezionato. Riprova.";
        esitoModalBody.style.color = '#dc3545';
        esitoModalHeader.style.background = '#dc3545';
        esitoModalHeader.style.color = 'white';
        esitoModalHeader.textContent = "Operazione non andata a buon fine!";
        esitoModalButton.style.backgroundColor = '#dc3545';
        esitoModalButton.style.borderColor = '#dc3545';
        esitoModalButton.style.color = 'white';
        esitoModal.show();
        redirect();
    } else if (esito === 'OK' & codice === '008') {
        esitoModalBody.textContent = "Notifica creata con successo";
        esitoModalBody.classList.add('text-success');
        esitoModalHeader.style.background = '#198754';
        esitoModalHeader.style.color = 'white';
        esitoModalHeader.textContent = "OK";
        esitoModalButton.classList.add('btn-success');
        esitoModal.show();
        redirect();

    } else if (esito === 'KO' & codice === '008') {
        visual = true;
        esitoModalBody.textContent = "Non è stato possibile creare la notifica. Riprova.";
        esitoModalBody.classList.add('text-danger');
        esitoModalHeader.style.background = '#dc3545';
        esitoModalHeader.style.color = 'white';
        esitoModalHeader.textContent = "ERRATO";
        esitoModalButton.classList.add('Smartoop-btn-error');
        esitoModal.show();
        redirect();
    } else if (esito === 'KO' & codice === '009') {
        esitoModalBody.textContent = "Il richiedente ha superato la sua disponibilità.";
        esitoModalBody.classList.add('text-danger');
        esitoModalHeader.style.background = '#dc3545';
        esitoModalHeader.style.color = 'white';
        esitoModalHeader.textContent = "ATTENZIONE";
        esitoModalButton.classList.add('Smartoop-btn-error');
        esitoModal.show();
        redirect();
    } else if (esito === 'OK' && codice === '010') {
        esitoModalBody.textContent = "Excel generato con successo!";
        esitoModalBody.style.color = '#198754';
        esitoModalHeader.style.background = '#198754';
        esitoModalHeader.style.color = 'white';
        esitoModalHeader.textContent = "Operazione effettuata con successo!";
        esitoModalButton.style.backgroundColor = '#198754';
        esitoModalButton.style.borderColor = '#198754';
        esitoModalButton.style.color = 'white';
        esitoModal.show();
        redirect();
    } else if (esito === 'KO' & codice === '010') {
        esitoModalBody.textContent = "Excel non ancora generato. Riprova.";
        esitoModalBody.style.color = '#dc3545';
        esitoModalHeader.style.background = '#dc3545';
        esitoModalHeader.style.color = 'white';
        esitoModalHeader.textContent = "Operazione non andata a buon fine!";
        esitoModalButton.style.backgroundColor = '#dc3545';
        esitoModalButton.style.borderColor = '#dc3545';
        esitoModalButton.style.color = 'white';
        esitoModal.show();
        redirect();
    }
}



function redirect() {
    document.getElementById('esitoModalButton').addEventListener('click', function () {
        var currentUrl = new URL(window.location.href);

        currentUrl.searchParams.delete('esito');
        currentUrl.searchParams.delete('codice');
        currentUrl.searchParams.delete('tipo');
        currentUrl.searchParams.delete('richiesta');
        currentUrl.searchParams.delete('visual');

        window.location.href = currentUrl.toString();
    });

}