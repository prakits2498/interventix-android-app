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
    public static final String PARSE_JSON = "Parse Json Input Parameter";
    public static final String PARSE_DATA = "Parse Util.FormatStringData(Data)";
    public static final String INPUT_JSON = "Input Parameter (DATA) Not Found";
    public static final String CRYPTO = "Error to Cypto (decrypt-encrypt)";
    public static final String DUPLICATE_ENTRY = "0010";
    public static final String ACCESS_DENIED = "Access Denied";
    
    public static final String SECTION_OR_ACTION_MISSED = "Parameter Section or Action not exists";
    
    public static final String NO_RESULT = "No results";
}
