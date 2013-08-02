/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package multiface.crypto.cr2.response;

/**
 * 
 * @author Diego64
 */
public interface MessageResponse {

    public static final String GENERIC = "Generic Error";
    public static final String PARSE_JSON = "Error Parse Json Input (DATA) Parameter";
    public static final String PARSE_DATA = "Parse Util.FormatStringData(Data)";
    public static final String INPUT_JSON = "Input Parameter (DATA) Not Found";
    public static final String CRYPTO = "Error to Cypto (decrypt-encrypt)";
    public static final String DUPLICATE_ENTRY = "Duplicate Entry";
    public static final String ACCESS_DENIED = "Access Denied";

    public static final String SECTION_OR_ACTION_MISSED = "Section or Action not exists";
    public static final String PARAMETERS_MISSED = "Parameter Missed or Incomplete";
    public static final String PARAMETERS_ERROR = "Parameter Error";

    public static final String NO_RESULT = "No results";

    public static final String NO_ADD_INTERVENTS = "Connector: Error to insert new intervent";

    public static final String MOD_INTERVENT_USER_NOT_FOUND = "User not found in the modification of the intervention";
    public static final String MOD_INTERVENT_CLIENT_NOT_FOUND = "Client not found in the modification of the intervention";

    public static final String TIMEOUT = "Request timeout";
    public static final String OBFUSCATOR = "Request already present among the obfuscator";
    public static final String CONFLICT_INTERVENTS = "Conflict Intervention";
}
