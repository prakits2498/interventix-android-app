package com.federicocolantoni.projects.interventix.helpers;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;

import com.federicocolantoni.projects.interventix.application.Interventix;

@EBean(scope = Scope.Singleton)
public class Constants {

    // *** API key BugSense ***\\
    public static final String API_KEY = "0ec355e8";

    // *** Account Type for AccountManager *** \\
    public static final String ACCOUNT_TYPE_INTERVENTIX = "com.multiface.interventix";
    public static final String ACCOUNT_AUTH_TOKEN = "com.multiface.interventix://auth_token";

    // *** costanti generali *** \\
    public static final String PREFERENCES = "com.federicocolantoni.projects.interventix.preferences";
    public static final int CONNECTION_TIMEOUT = 16000;
    public static final String REVISION_INTERVENTI = "REVISION_INTERVENTI";
    public static final String REVISION_TECNICI = "REVISION_TECNICI";
    public static final String REVISION_CLIENTI = "REVISION_CLIENTI";
    public static final String NUOVO_DETTAGLIO_INTERVENTO = "NUOVO_DETTAGLIO_INTERVENTO";
    public static final String INTERVENTO_NUOVO = "N";
    public static final String DETTAGLIO_NUOVO = INTERVENTO_NUOVO;
    public static final String INTERVENTO_MODIFICATO = "M";
    public static final String DETTAGLIO_MODIFICATO = INTERVENTO_MODIFICATO;
    public static final String INTERVENTO_SINCRONIZZATO = "U";
    public static final String DETTAGLIO_SINCRONIZZATO = INTERVENTO_SINCRONIZZATO;
    public static final String HASH_CODE_INTERVENTO_SINGLETON = "HASH_CODE_INTERVENTO_SINGLETON";

    // *** costanti per il tempo e le date *** \\
    public static final String TIME_PATTERN = "HH:mm";
    public static final String DATE_PATTERN = "dd/MM/yyyy";
    public static final String TIMEZONE_EUROPE_ROME = "Europe/Rome";

    // *** costanti per i fragment *** \\
    public static final String DETAILS_INTERVENTO_FRAGMENT = "DETAILS_INTERVENTO_FRAGMENT";
    public static final String INFO_DETAIL_INTERVENTO_FRAGMENT = "INFO_DETAIL_INTERVENTO_FRAGMENT";
    public static final String COSTS_INTERVENTO_FRAGMENT = "COSTS_INTERVENTO_FRAGMENT";
    public static final String SIGNATURE_INTERVENTO_FRAGMENT = "SIGNATURE_INTERVENTO_FRAGMENT";
    public static final String OVERVIEW_INTERVENTO_FRAGMENT = "OVERVIEW_INTERVENTO";
    public static final String INFORMATIONS_INTERVENTO_FRAGMENT = "INFORMATIONS_INTERVENTO";
    public static final String CLIENTS_INTERVENTO_FRAGMENT = "CLIENTS_INTERVENTO_FRAGMENT";
    public static final String REFERENCES_INTERVENTO_FRAGMENT = "REFERENCES_INTERVENTO_FRAGMENT";
    public static final String ANNOTATIONS_INTERVENTO_FRAGMENT = "ANNOTATIONS_INTERVENTO_FRAGMENT";
    public static final String CLIENT_INTERVENTO_FRAGMENT = "CLIENT_INTERVENTO_FRAGMENT";
    public static final String CALENDAR_PICKER_INFORMATIONS_INTERVENTO_FRAGMENT = "CALENDAR_PICKER_INFORMATIONS_INTERVENTO_FRAGMENT";
    public static final String TIMER_PICKER_INFORMATIONS_INTERVENTO_FRAGMENT = "TIMER_PICKER_INFORMATIONS_INTERVENTO_FRAGMENT";

    // *** costanti per le dialogs *** \\
    public static final String EXIT_INTERVENTO_DIALOG_FRAGMENT = "EXIT_INTERVENTO_DIALOG_FRAGMENT";

    // *** costanti dell'intervento *** \\
    public static final String INTERVENTO = "INTERVENTO";
    public static final String DETTAGLIO_N_ESIMO = "DETTAGLIO_NESIMO";
    public static final String CLIENTE = "CLIENTE";
    public static final double IVA = 0.21;

    // *** costanti per i dati di login *** \\
    public static final String USERNAME = "USERNAME";
    public static final String PASSWORD = "PASSWORD";

    // *** costanti per recuperare le informazioni sulle risposte JSON *** \\
    public static final String JSON_USERNAME = "username";
    public static final String ORMLITE_USERNAME = JSON_USERNAME;
    public static final String JSON_PASSWORD = "password";
    public static final String JSON_TYPE = "type";
    public static final String JSON_USER_SECTION = "users";
    public static final String JSON_LOGIN_ACTION = "login";
    public static final String JSON_INTERVENTIONS_SECTION = "interventions";
    public static final String JSON_MYSYNCRO_INTERVENTIONS_ACTION = "mysyncro";
    public static final String JSON_RESPONSE = "response";
    public static final String JSON_DATA = "data";
    public static final String JSON_RESPONSE_SUCCESS = "success";
    public static final String JSON_TECNICIINTERVENTO = "tecniciintervento";
    public static final String JSON_DETTAGLIINTERVENTO = "dettagliintervento";
    public static final String JSON_MOD = "mod";
    public static final String JSON_DEL = "del";
    public static final String JSON_INTERVENTS = "intervents";
    public static final String JSON_CLIENTS_SECTION = "clients";
    public static final String JSON_SYNCRO_CLIENTS_ACTION = "syncro";
    public static final String JSON_USERS_SECTION = "users";
    public static final String JSON_SYNCRO_USERS_ACTION = "syncro";
    public static final String JSON_ADD_INTERVENTIONS_ACTION = "add";
    public static final String JSON_MOD_INTERVENTIONS_ACTION = "mod";
    public static final String JSON_TIPO = "tipo";
    public static final String JSON_OGGETTO = "oggetto";
    public static final String JSON_INIZIO = "inizio";
    public static final String JSON_FINE = "fine";
    public static final String JSON_DESCRIZIONE = "descrizione";
    public static final String JSON_TECNICI = "tecnici";
    public static final String JSON_CLIENTE = "cliente";
    public static final String ORMLITE_CLIENTE = JSON_CLIENTE;
    public static final String JSON_TECNICO = "tecnico";
    public static final String ORMLITE_TECNICO = JSON_TECNICO;
    public static final String JSON_TIPOLOGIA = "tipologia";
    public static final String JSON_MODALITA = "modalita";
    public static final String JSON_PRODOTTO = "prodotto";
    public static final String JSON_MOTIVO = "motivo";
    public static final String JSON_NOMINATIVO = "nominativo";
    public static final String JSON_DATAORA = "dataora";
    public static final String ORMLITE_DATAORA = JSON_DATAORA;
    public static final String JSON_RIF_FATTURA = "riffattura";
    public static final String JSON_RIF_SCONTRINO = "rifscontrino";
    public static final String JSON_FIRMA = "firma";
    public static final String JSON_CHIUSO = "chiuso";
    public static final String JSON_NOTE = "note";
    public static final String JSON_TOTALE = "totale";
    public static final String JSON_IVA = "iva";
    public static final String JSON_IMPORTO = "importo";
    public static final String JSON_IDINTERVENTO = "idintervento";
    public static final String JSON_REVISIONE = "revisione";
    public static final String JSON_SALDATO = "saldato";
    public static final String JSON_ACTION = "action";
    public static final String JSON_SECTION = "section";
    public static final String JSON_NUMERO = "numero";

    // *** costanti per i nomi delle colonne di ORMLite *** \\
    public static final String JSON_COSTOACCESSORI = "costoaccessori";
    public static final String JSON_COSTOCOMPONENTI = "costocomponenti";
    public static final String JSON_COSTOMANODOPERA = "costomanodopera";
    public static final String ORMLITE_NOME = "nome";
    public static final String ORMLITE_COGNOME = "cognome";
    public static final String ORMLITE_IDUTENTE = "idutente";
    public static final String ORMLITE_NUMERO = "numero";
    public static final String ORMLITE_CONFLITTO = "conflitto";
    public static final String ORMLITE_MODIFICATO = "modificato";
    public static final String ORMLITE_NUOVO = "nuovo";
    public static final String ORMLITE_CHIUSO = "chiuso";
    public static final String ORMLITE_IDINTERVENTO = "idintervento";
    public static final String ORMLITE_IDDETTAGLIOINTERVENTO = "iddettagliointervento";

    // *** dati per il buffer *** \\
    public static final int MILLISECONDI = 1000;
    public static final int SECONDI = 1;
    public static final int MINUTI = 30 * SECONDI * MILLISECONDI;
    public static final String ACTION_GET_INTERVENTI = Interventix.getContext().getPackageName() + "." + BUFFER_TYPE.BUFFER_INTERVENTO.name();
    public static final String ACTION_GET_CLIENTI = Interventix.getContext().getPackageName() + "." + BUFFER_TYPE.BUFFER_CLIENTE.name();
    public static final String ACTION_FINISH_BUFFER = com.federicocolantoni.projects.interventix.application.Interventix_.getContext().getPackageName() + "." + "BUFFER_FINISH";

    public static final String SCHEDULER_NAME = "SCHEDULER_SEND_INTERVENTIONS";

    public static final long PERIOD_BETWEEN_SUBSEQUENT_EXCECUTIONS = MINUTI;

    public static final long DELAY_SCHEDULER_START = PERIOD_BETWEEN_SUBSEQUENT_EXCECUTIONS;

    public static int sNuovoId = 1;

    public static final int NOTIFICATIOND_ID_INTERVENTIX = 23;

    public static final String TECNICO = "TECNICO";

    public static int sNumberOfNotificationEvents = 0;

    public static enum BUFFER_TYPE {

	BUFFER_INTERVENTO, BUFFER_CLIENTE;
    }

    public static enum USER_TYPE {

	TECNICO, AMMINISTRATORE
    }

    // *** altre stringhe ***\\
    public static final String CONTRATTUALIZZATO = "Contrattualizzato";
    public static final String PAGAMENTO_DIRETTO = "Pagamento Diretto";
    public static final String POST_VENDITA = "Post-Vendita (Garanzia)";
    public static final String COMODATO_USO = "Comodato d'uso";

    public static enum MODE_INTERVENTO {

	CONTRACT("contract"), PAYMENT("payment"), POSTSALE("postsale"), RENTUSE("rentuse");

	private String name;

	private MODE_INTERVENTO(String name) {

	    this.name = name;
	}

	public String getName() {

	    return name;
	}
    }

    public static final String RICHIESTA = "Su richiesta";
    public static final String PROGRAMMATA = "Programmata";

    public static enum TYPE_INTERVENTO {

	REQUEST("request"), PROGRAMMED("programmed");

	private String name;

	private TYPE_INTERVENTO(String name) {

	    this.name = name;
	}

	public String getName() {

	    return name;
	}
    }

    public static final String DATA_POST_PARAMETER = "DATA";
}
