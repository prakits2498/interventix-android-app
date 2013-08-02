package multiface.crypto.cr2;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import multiface.crypto.cr2.response.CodeResponse;
import multiface.crypto.cr2.response.MessageResponse;
import multiface.crypto.cr2.response.StaticResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Classe per la gestione della comunicazione in modalità criptata CR2 AES128
 * bit AES/ECB/PKCS7Padding con dato in formato json/cr2
 * 
 * @author Diego Falcinelli Multiface - www.multiface.it
 */
public class JsonCR2 {

    /**
     * DEBUG di response
     */
    private static final boolean DEBUG_RESPONSE = false;
    /**
     * DEBUG di request
     */
    private static final boolean DEBUG_REQUEST = false;
    private static final boolean DEBUG_READ = false;

    // -------------------------- READ --------------------------------
    /**
     * Decodifica di un richiesta JsonCR2 da una stringa criptata CR2
     * 
     * @param stringCR2
     * @return Oggetto Json in Chiaro
     * @throws ParseException
     * @throws Exception
     */
    @Deprecated
    public static JSONObject readRequest(String stringCR2)
	    throws ParseException, Exception {
	return JsonCR2.read(stringCR2);
    }

    /**
     * Decodifica di un richiesta JsonCR2 da una stringa criptata CR2
     * 
     * @param stringCR2
     * @return Oggetto Json in Chiaro
     * @throws ParseException
     * @throws Exception
     */
    public static JSONObject read(String stringCR2) throws ParseException,
	    Exception {
	if (DEBUG_READ) {
	    JSONObject json = (JSONObject) new JSONParser().parse(stringCR2);
	    return json;
	} else {
	    JSONObject json = (JSONObject) new JSONParser().parse(CryptoCR2
		    .decrypt(stringCR2));
	    return json;
	}

    }

    // -------------------------- CREATE REQUEST
    // --------------------------------
    /**
     * Richiesta di login-CryptoCR2
     * 
     * @param username
     *            Username per il login
     * @param password
     *            password in chiaro per il login
     * @return Stringa della richiesta creata in JSON/CR2
     * @throws Exception
     */
    public static String createRequestLogin(String username, String password)
	    throws Exception {
	Map<String, Object> m = new HashMap<String, Object>();
	m.put("username", username);
	m.put("password", username);
	return JsonCR2.createRequest("users", "login", m, -1);
    }

    /**
     * Metodo per inviare una richiesta-JSON/CR2 ad un WebService di tipo
     * Json/CR2
     * 
     * @param section
     *            Sezione della richiesta
     * @param action
     *            Azione della richiesta
     * @param parameters
     *            Mappa dei parametri (String,Object) aggiuntivi. null richiesta
     *            senza parametri
     * @param user
     *            ID-identificativo dell'utente che effettua la richiesta
     * @return Stringa della richiesta creata in JSON/CR2
     * @throws Exception
     */
    public static String createRequest(String section, String action,
	    Map parameters, int iduser) throws Exception {

	// utile per Android
	JSONObject json = new JSONObject();
	Random random = new Random();
	Date date = new Date();

	// Offuscatore nella richiesta JSON/CR2
	json.put(
		"obfuscator",
		Long.toHexString(date.getTime() * date.hashCode()) + ""
			+ Long.toHexString(Math.abs(random.nextLong())));

	// Identificativo Utente
	json.put("iduser", iduser);

	// Datetime
	json.put("datetime", date.getTime());

	// Sezione e azione della richiesta
	json.put("section", section);
	json.put("action", action);

	if (parameters != null) {
	    if (parameters.size() > 0) {
		json.put("parameters", parameters);
	    }
	}

	if (DEBUG_REQUEST) {
	    return json.toJSONString();
	} else {
	    return CryptoCR2.encrypt(json.toJSONString());
	}

    }

    // -------------------------- CREATE RESPONSE
    // --------------------------------
    /**
     * Crea una risposta generica in formato Json/cr2
     * 
     * @param response
     *            Stringa di risposta
     * @param section
     *            Sezionde della richesta effettuata
     * @param action
     *            Azione della richiesta effettuata
     * @param code
     *            Codice di risposta da
     *            Multiface.crypto.cr2.response.CodeResponse
     * @param message
     *            Messaggio di risposta da
     *            Multiface.crypto.cr2.response.MessageResponse oppure null per
     *            non avere nessun messaggio
     * @param data
     *            List/Map/[value] di dati che verranno inseriti nella risposta,
     *            oppure null se non si vuole nessun dato di risposta
     * @return Stringa della risposta creata in JSON/CR2
     */
    public static String createResponse(String response, String section,
	    String action, String code, String message, Object data) {
	JSONObject json = new JSONObject();
	json.put("response", response);

	Map<String, String> req = new HashMap<String, String>(2);
	req.put("action", action);
	req.put("section", section);
	json.put("request", req);

	json.put("code", code);

	Random random = new Random();
	Date date = new Date();

	// Offuncatore nella richiesta JSON/CR2
	json.put(
		"obfuscator",
		Long.toHexString(date.getTime() * date.hashCode()) + ""
			+ Long.toHexString(Math.abs(random.nextLong())));

	// Datetime
	json.put("datetime", date.getTime());
	if (message != null) {
	    json.put("message", message);
	}
	if (data != null) {
	    json.put("data", data);
	}
	try {
	    if (DEBUG_RESPONSE) {
		return json.toJSONString();
	    } else {
		return CryptoCR2.encrypt(json.toJSONString());
	    }
	} catch (Exception ex) {
	    Logger.getLogger(JsonCR2.class.getName()).log(Level.SEVERE, null,
		    ex);
	    return JsonCR2.createResponseError(section, action,
		    CodeResponse.SEND_REQUEST_FAILED, ex.getMessage());
	}
    }

    /**
     * Crea una risposta di errore semplice
     * 
     * @param error_code
     * @param error_message
     * @return
     */
    public static String createResponseError(String error_code,
	    String error_message) {
	return JsonCR2.createResponseError(null, null, error_code,
		error_message, null);
    }

    /**
     * Crea una risposta di errore senza eccezione
     * 
     * @param section
     *            Sezionde della richesta effettuata
     * @param action
     *            Azione della richiesta effettuata
     * @param error_code
     *            Codice di errore da Multiface.crypto.cr2.response.CodeResponse
     * @param error_message
     *            Messaggio di risposta da
     *            Multiface.crypto.cr2.response.MessageResponse oppure null per
     *            non avere nessun messaggio
     * @return Stringa della risposta creata in JSON/CR2
     */
    public static String createResponseError(String section, String action,
	    String error_code, String error_message) {
	return JsonCR2.createResponseError(section, action, error_code,
		error_message, null);
    }

    /**
     * Crea una risposta di errore con l'eccezione
     * 
     * @param section
     *            Sezionde della richesta effettuata
     * @param action
     *            Azione della richiesta effettuata
     * @param error_code
     *            Codice di errore da Multiface.crypto.cr2.response.CodeResponse
     * @param error_message
     *            Messaggio di risposta da
     *            Multiface.crypto.cr2.response.MessageResponse oppure null per
     *            non avere nessun messaggio
     * @param exception
     *            Eccezione generata oppure null per nessuna eccezione
     * @return Stringa della risposta creata in JSON/CR2
     */
    public static String createResponseError(String section, String action,
	    String error_code, String error_message, Exception exception) {

	JSONObject json = new JSONObject();
	json.put("response", StaticResponse.ERROR);

	Map<String, String> req = new HashMap<String, String>();
	if (section != null)
	    req.put("action", action);
	if (action != null)
	    req.put("section", section);

	if (req.size() > 0)
	    json.put("request", req);
	json.put("code", error_code);
	Random random = new Random();
	Date date = new Date();

	// Offuncatore nella richiesta JSON/CR2
	json.put(
		"obfuscator",
		Long.toHexString(date.getTime() * date.hashCode()) + ""
			+ Long.toHexString(Math.abs(random.nextLong())));

	// Datetime
	json.put("datetime", date.getTime());

	if (error_message != null && exception != null) {
	    json.put("message", error_message + " - " + exception.getMessage());
	}
	if (error_message != null && exception == null) {
	    json.put("message", error_message);
	}
	if (error_message == null && exception != null) {
	    json.put("message", exception.getMessage());
	}

	System.out.println("RESPONSE: " + json.toJSONString());
	try {
	    if (DEBUG_RESPONSE) {
		return json.toJSONString();
	    } else {
		return CryptoCR2.encrypt(json.toJSONString());
	    }
	} catch (Exception ex) {
	    Logger.getLogger(JsonCR2.class.getName()).log(Level.SEVERE, null,
		    ex);
	}
	return CodeResponse.ERRCODE;

    }

    /**
     * Crea la risposta di successo
     * 
     * @param section
     *            Sezionde della richesta effettuata
     * @param action
     *            Azione della richiesta effettuata
     * @return Stringa della risposta creata in JSON/CR2
     */
    public static String createResponseSuccess(String section, String action) {
	return JsonCR2.createResponse("success", section, action,
		CodeResponse.SUCCESS, null, null);
    }

    /**
     * Crea la risposta di successo
     * 
     * @param section
     *            Sezionde della richesta effettuata
     * @param action
     *            Azione della richiesta effettuata
     * @param data
     *            List/Map/[value] di dati che verranno inseriti nella risposta,
     *            oppure null se non si vuole nessun dato di risposta
     * @return Stringa della risposta creata in JSON/CR2
     */
    public static String createResponseSuccess(String section, String action,
	    Object data) {
	return JsonCR2.createResponse(StaticResponse.SUCCESS, section, action,
		CodeResponse.SUCCESS, null, data);
    }

    /**
     * Crea una risposta di Accesso Consentito sull'output
     * 
     * @param userid
     *            Identificativo dell'utente che ha effetutato il login
     * @return Stringa della risposta creata in JSON/CR2
     */
    public static String createResponseLoginOK(int iduser) {
	JSONObject json = new JSONObject();
	json.put("response", StaticResponse.SUCCESS);
	json.put("code", CodeResponse.SUCCESS);
	Random random = new Random();
	Date date = new Date();

	// Offuncatore nella richiesta JSON/CR2
	json.put(
		"obfuscator",
		Long.toHexString(date.getTime() * date.hashCode()) + ""
			+ Long.toHexString(Math.abs(random.nextLong())));

	// Datetime
	json.put("datetime", date.getTime());
	json.put("iduser", iduser);
	System.out.println("RESPONSE: " + json.toJSONString());
	try {
	    if (DEBUG_RESPONSE) {
		return json.toJSONString();
	    } else {
		return CryptoCR2.encrypt(json.toJSONString());
	    }
	} catch (Exception ex) {
	    return JsonCR2.createResponseError(StaticResponse.LOGIN,
		    StaticResponse.LOGIN, CodeResponse.GENERIC,
		    MessageResponse.GENERIC, ex);
	}
	// return CodeResponse.ERRCODE;
    }

    /**
     * Crea una risposta di Accesso Negato sull'output
     * 
     * @return Stringa della risposta creata in JSON/CR2
     */
    public static String createResponseLoginERROR() {
	return createResponseError(StaticResponse.LOGIN, StaticResponse.LOGIN,
		CodeResponse.ACCESS_DENIED, MessageResponse.ACCESS_DENIED);
    }
}
