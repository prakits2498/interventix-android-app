package com.federicocolantoni.projects.interventix;

public final class Constants {

	// *** API key BugSense ***\\
	public static final String API_KEY = "0ec355e8";

	// *** Account Type for AccountManager ***\\
	public static final String ACCOUNT_TYPE_INTERVENTIX = "com.multiface.interventix";
	public static final String ACCOUNT_AUTH_TOKEN = "com.multiface.interventix://auth_token";
	public static final String AUTHENTICATOR_TOKEN = "AUTHEHTICATOR_TOKEN";
	public static final String ACCOUNT_NAME = "ACCOUNT_NAME";
	public static final String ACCOUNT_TYPE = "ACCOUNT_TYPE";
	public static final String TAG_AUTHENTICATOR = "InterventiAuthenticator";
	public static final String ADDING_NEW_ACCOUNT = "ADDING_NEW_ACCOUNT";
	public static final String AUTHENTICATED = "AUTHENTICATED";

	// *** costanti generali ***\\
	public static final String ACCESS_ALLOWED = "ACCESSO CONSENTITO";
	public static final String ACCESS_DINIED = "ACCESSO NEGATO";
	public static final String PREFERENCES = "com.federicocolantoni.projects.interventix.preferences";
	public static final String INTERVENTIX_TITLE = "Interventix";
	public static final String USER_ID = "USER_ID";
	public static final String USER_NOMINATIVO = "USER_NOMINATIVO";
	public static final int CONNECTION_TIMEOUT = 16000;
	public static final String REVISION_INTERVENTI = "REVISION_INTERVENTI";
	public static final String REVISION_TECNICI = "REVISION_TECNICI";
	public static final String REVISION_CLIENTI = "REVISION_CLIENTI";
	public static final String NUOVO_DETTAGLIO_INTERVENTO = "NUOVO_DETTAGLIO_INTERVENTO";
	public static final String DETTAGLIO_INTERVENTO_ESISTENTE = "DETTAGLIO_INTERVENTO_ESISTENTE";
	public static final int ERRORE_LOGIN = -2;
	public static final int ERRORE_NO_CONNECTION = -3;

	public static final String INTERVENTO_NUOVO = "N";
	public static final String INTERVENTO_MODIFICATO = "M";
	public static final String INTERVENTO_SINCRONIZZATO = "U";

	public static final String DETTAGLIO_NUOVO = "N";
	public static final String DETTAGLIO_MODIFICATO = "M";
	public static final String DETTAGLIO_SINCRONIZZATO = "U";

	public static final int WHAT_MESSAGE_GET_CLIENTI = -1;

	public static int contatoreIdNuovoDettaglio = 1;

	// *** costanti per i fragment ***\\
	public static final String DETAILS_INTERVENTO_FRAGMENT = "DETAILS_INTERVENTO_FRAGMENT";
	public static final String NEW_DETAIL_INTERVENTO_FRAGMENT = "NEW_DETAIL_INTERVENTO_FRAGMENT";
	public static final String INFO_DETAIL_INTERVENTO_FRAGMENT = "INFO_DETAIL_INTERVENTO_FRAGMENT";
	public static final String COSTS_INTERVENTO_FRAGMENT = "COSTS_INTERVENTO_FRAGMENT";
	public static final String SIGNATURE_INTERVENTO_FRAGMENT = "SIGNATURE_INTERVENTO_FRAGMENT";
	public static final String DRAWER_FRAGMENT = "DRAWER_FRAGMENT";
	public static final String OVERVIEW_INTERVENTO_FRAGMENT = "OVERVIEW_INTERVENTO";
	public static final String INFORMATIONS_INTERVENTO_FRAGMENT = "INFORMATIONS_INTERVENTO";
	public static final String ADD_USERS_TO_DETAIL_FRAGMENT = "ADD_USERS_TO_DETAIL_FRAGMENT";
	public static final String TECHNICIANS_DETAIL_FRAGMENT = "TECHNICIANS_DETAIL_FRAGMENT";
	public static final String CLIENTS_INTERVENTO_FRAGMENT = "CLIENTS_INTERVENTO_FRAGMENT";
	public static final String REFERENCES_INTERVENTO_FRAGMENT = "REFERENCES_INTERVENTO_FRAGMENT";
	public static final String ANNOTATIONS_INTERVENTO_FRAGMENT = "ANNOTATIONS_INTERVENTO_FRAGMENT";

	// *** costanti per le dialog ***\\
	public static final String FIRST_RUN_DIALOG_FRAGMENT = "FIRST_RUN_DIALOG_FRAGMENT";
	public static final String LOGOUT_DIALOG_FRAGMENT = "LOGOUT_DIALOG_FRAGMENT";
	public static final String TIPOLOGIA_DIALOG_FRAGMENT = "TIPOLOGIA_DIALOG_FRAGMENT";
	public static final String MODALITA_DIALOG_FRAGMENT = "MODALITA_DIALOG_FRAGMENT";
	public static final String PRODOTTO_DIALOG_FRAGMENT = "PRODOTTO_DIALOG_FRAGMENT";
	public static final String MOTIVO_DIALOG_FRAGMENT = "MOTIVO_DIALOG_FRAGMENT";
	public static final String NOMINATIVO_DIALOG_FRAGMENT = "NOMINATIVO_DIALOG_FRAGMENT";
	public static final String DATAORA_DIALOG_FRAGMENT = "DATAORA_DIALOG_FRAGMENT";
	public static final String MANODOPERA_DIALOG_FRAGMENT = "MANODOPERA_DIALOG_FRAGMENT";
	public static final String COMPONENTI_DIALOG_FRAGMENT = "COMPONENTI_DIALOG_FRAGMENT";
	public static final String ACCESSORI_DIALOG_FRAGMENT = "ACCESSORI_DIALOG_FRAGMENT";
	public static final String EXIT_INTERVENTO_DIALOG_FRAGMENT = "EXIT_INTERVENTO_DIALOG_FRAGMENT";
	public static final String TIPO_DETTAGLIO_DIALOG_FRAGMENT = "TIPO_DETTAGLIO_DIALOG_FRAGMENT";
	public static final String OGGETTO_DETTAGLIO_DIALOG_FRAGMENT = "OGGETTO_DETTAGLIO_DIALOG_FRAGMENT";
	public static final String DESCRIZIONE_DETTAGLIO_DIALOG_FRAGMENT = "DESCRIZIONE_DETTAGLIO_DIALOG_FRAGMENT";

	// *** costanti dell'intervento ***\\
	public static final String INTERVENTO = "INTERVENTO";
	public static final String ID_INTERVENTO = "ID_INTERVENTO";
	public static final String ID_DETTAGLIO_INTERVENTO = "ID_DETTAGLIO_INTERVENTO";
	public static final String DETTAGLIO_N_ESIMO = "DETTAGLIO_NESIMO";
	public static final String NUMERO_INTERVENTO = "NUMERO_INTERVENTO";
	public static final String NOMINATIVO = "NOMINATIVO";
	public static final String CLIENTE = "CLIENTE";
	public static final String LIST_DETAILS_INTERVENTO = "LIST_DETAILS_INTERVENTO";
	public static final double IVA = 0.21;

	// *** costanti per il token delle AsyncQueryHandler ***\\
	public static final int WRITE_INTERV_TOKEN = 0;
	public static final int WRITE_DETT_INTERV_TOKEN = 1;
	public static final int TOKEN_TIPO_DETTAGLIO = 2;
	public static final int TOKEN_OGGETTO_DETTAGLIO = 3;
	public static final int TOKEN_DESCRIZIONE_DETTAGLIO = 4;
	public static final int TOKEN_ORA_INIZIO_DETTAGLIO = 5;
	public static final int TOKEN_ORA_FINE_DETTAGLIO = 6;
	public static final int TOKEN_RIPRISTINO_INTERVENTO = 7;
	public static final int TOKEN_INFO_TIPOLOGIA = 8;
	public static final int TOKEN_INFO_NOMINATIVO = 9;
	public static final int TOKEN_INFO_PRODOTTO = 10;
	public static final int TOKEN_INFO_MODALITA = 11;
	public static final int TOKEN_INFO_MOTIVO = 12;
	public static final int TOKEN_INFO_DATA_ORA = 12;
	public static final int TOKEN_COSTO_MANODOPERA = 14;
	public static final int TOKEN_COSTO_COMPONENTI = 15;
	public static final int TOKEN_COSTO_ACCESSORI = 16;
	public static final int TOKEN_TECNICI_DETTAGLIO = 17;
	public static final int TOKEN_RECUPERO_INTERVENTI = 18;
	public static final int TOKEN_RECUPERO_CLIENTI = 19;
	public static final int TOKEN_INTERVENTO = 20;

	// token per il salvataggio dei dati del singleton
	public static final int TOKEN_SAVE_TECNICO = 21;
	public static final int TOKEN_SAVE_CLIENTE = 22;
	public static final int TOKEN_SAVE_DETTAGLIO = 23;
	public static final int TOKEN_SAVE_INTERVENTO = 24;

	public static final String ARRAY_DETTAGLI = "ARRAY_DETTAGLI";

	public static final String HASH_CODE_INTERVENTO_SINGLETON = "HASH_CODE_INTERVENTO_SINGLETON";

	// *** costanti per i dati di login ***\\
	public static final String LOGIN_DATA = "LOGIN_DATA";
	public static final String USERNAME = "USERNAME";
	public static final String PASSWORD = "PASSWORD";

	// *** dati per il buffer ***\\
	public static final String ACTION_GET_INTERVENTI = Interventix.getContext().getPackageName() + "." + BUFFER_TYPE.BUFFER_INTERVENTO.name();
	public static final String ACTION_GET_CLIENTI = Interventix.getContext().getPackageName() + "." + BUFFER_TYPE.BUFFER_CLIENTE.name();
	public static final String ACTION_FINISH_BUFFER = com.federicocolantoni.projects.interventix.Interventix_.getContext().getPackageName() + "." + "BUFFER_FINISH";

	public static enum BUFFER_TYPE {

		BUFFER_INTERVENTO, BUFFER_CLIENTE;
	}
}
