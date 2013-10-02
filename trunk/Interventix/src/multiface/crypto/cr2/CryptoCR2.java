package multiface.crypto.cr2;

import java.security.Key;
import java.security.Security;
import java.util.Formatter;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author Diego64 by www.multiface.it
 */
public class CryptoCR2 {
    
    private static final String ALGORITHM = "AES/ECB/PKCS7Padding";
    private static final byte[] keyValue = new byte[] {
    'a', 'b', 'c', '7', '8', 'j', 'N', 'i', 'K', 'u', 'm', 'H', '6', '5', '6', 'p'
    };
    protected static final char[] kDigits = {
    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };
    protected static final byte[] Hexhars = {
    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };
    
    public static String encrypt(String valueToCR2) throws Exception {
	valueToCR2 = CryptoCR2.escapeUnicode(valueToCR2);
	String crypt = encrypt_(valueToCR2);
	byte[] dat = crypt.getBytes("UTF-8");
	return encodeHEX(dat);
    }
    
    public static String decrypt(String valueToCR2) throws Exception {
	valueToCR2 = CryptoCR2.escapeUnicode(valueToCR2);
	byte[] dat = decodeHEX(valueToCR2);
	String crypt = new String(dat, "UTF-8");
	return decrypt_(crypt);
    }
    
    private static String encrypt_(String valueToEnc) throws Exception {
	Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	
	Key key = generateKey();
	Cipher c = Cipher.getInstance(ALGORITHM, "BC");
	c.init(Cipher.ENCRYPT_MODE, key);
	byte[] encValue = c.doFinal(valueToEnc.getBytes());
	byte[] eee = org.bouncycastle.util.encoders.Base64.encode(encValue);
	return new String(eee, "UTF-8");
    }
    
    private static String decrypt_(String encryptedValue) throws Exception {
	
	Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	
	Key key = generateKey();
	Cipher c = Cipher.getInstance(ALGORITHM, "BC");
	c.init(Cipher.DECRYPT_MODE, key);
	byte[] decordedValue = org.bouncycastle.util.encoders.Base64.decode(encryptedValue);
	byte[] decValue = c.doFinal(decordedValue);
	String decryptedValue = new String(decValue, "UTF-8");
	return decryptedValue;
    }
    
    private static Key generateKey() throws Exception {
	// System.out.println(keyValue[0]);
	Key key = new SecretKeySpec(keyValue, ALGORITHM);
	
	// SecretKeyFactory keyFactory =
	// SecretKeyFactory.getInstance(ALGORITHM);
	// key = keyFactory.generateSecret(new DESKeySpec(keyValue));
	return key;
    }
    
    public static String escapeUnicode(String input) {
	StringBuilder b = new StringBuilder(input.length());
	Formatter f = new Formatter(b);
	for (char c : input.toCharArray()) {
	    if (c < 128) {
		b.append(c);
	    }
	    else {
		f.format("\\u%04x", (int) c);
	    }
	}
	f.close();
	return b.toString();
    }
    
    // HEX
    private static byte[] decodeHEX(char[] hex) {
	int length = hex.length / 2;
	byte[] raw = new byte[length];
	for (int i = 0; i < length; i++) {
	    int high = Character.digit(hex[i * 2], 16);
	    int low = Character.digit(hex[i * 2 + 1], 16);
	    int value = high << 4 | low;
	    if (value > 127) {
		value -= 256;
	    }
	    raw[i] = (byte) value;
	}
	return raw;
    }
    
    public static byte[] decodeHEX(String hex) {
	return decodeHEX(hex.toCharArray());
    }
    
    public static String encodeHEX(byte[] b) {
	
	StringBuilder s = new StringBuilder(2 * b.length);
	
	for (byte element : b) {
	    
	    int v = element & 0xff;
	    
	    s.append((char) Hexhars[v >> 4]);
	    s.append((char) Hexhars[v & 0xf]);
	}
	
	return s.toString();
    }
}
