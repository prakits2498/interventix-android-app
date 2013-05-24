/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package multiface.crypto.cr2.response;

/**
 *
 * @author Diego64
 */
public interface CodeResponse {
    public static final String ERRCODE = "-1";
    
    
    public static final String SUCCESS = "0000";
    public static final String ACCESS_DENIED = "0001";
    public static final String GENERIC = "0002";
    public static final String PARSE_JSON = "0003";
    public static final String INPUT_JSON = "0004";
    public static final String CRYPTO = "0005";
    public static final String DUPLICATE_ENTRY = "0010";
    public static final String PARSE_DATA = "0020";
    public static final String TIMEOUT = "0050";
    public static final String SEND_REQUEST_FAILED = "0100";
    public static final String SECTION_OR_ACTION_MISSED = "0200";
    public static final String PARAMETERS_MISSED = "0230";
    public static final String PARAMETERS_ERROR = "0231";
    public static final String NO_RESULT = "0500";
    public static final String NO_ADD_INTERVENTS = "2000";
    public static final String MOD_INTERVENT_USER_NOT_FOUND = "2500";
    public static final String MOD_INTERVENT_CLIENT_NOT_FOUND = "2550";
    public static final String CONFLICT = "3600";
    
}
