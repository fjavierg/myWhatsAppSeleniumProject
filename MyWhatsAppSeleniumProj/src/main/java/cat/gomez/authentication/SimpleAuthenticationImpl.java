package cat.gomez.authentication;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.codec.binary.Hex;

/**
 * Simple Authentication based in HMAC an Time
 */
public class SimpleAuthenticationImpl implements Authentication{
    /**
     * Authentication HTTP_HEADER: {@value #HTTP_HEADER}
     */
    public static final String HTTP_HEADER = "Authorization";
    /**
     * Authentication SCHEME name: {@value #SCHEME}
     */
    public static final String SCHEME = "torch";
    /**
     * Default expire: {@value #DEFAULT_EXPIRE} seconds
     */
    public static final int DEFAULT_EXPIRE = (5 * 60);

    // Excluded visual similar chars 01Ol
    private final static char[] DEF_ALPHABET = ("23456789" + "ABCDEFGHIJKLMNPQRSTUVWXYZ"
            + "abcdefghijkmnopqrstuvwxyz" + "#$!:.=+-/_").toCharArray();
    private static final Charset UTF8 = Charset.forName("UTF-8");

    private int expire = DEFAULT_EXPIRE;
    private String key = "changeit";

    /**
     * Create SimpleAuth with empty Key ("") and {@link #DEFAULT_EXPIRE}
     * 
     * @see #setPreSharedKey(String)
     * @see #setExpire(int)
     */
    public SimpleAuthenticationImpl() {
    }

    private static final String generateRandomKey() {
        final Random r = new SecureRandom();
        final byte[] bX = new byte[32];
        final char[] bY = new char[32];
        r.nextBytes(bX);
        for (int i = 0; i < bX.length; i++) {
            final int j = ((bX[i] & 0xFF) % DEF_ALPHABET.length);
            bY[i] = DEF_ALPHABET[j];
        }
        return new String(bY);
    }

    /**
     * Get Pre-shared-key
     * 
     * @return key
     */
    public String getPreSharedKey() {
        return key;
    }

    /**
     * Set Pre-shared-key
     * 
     * @param key for signature / verification
     * @return this
     */
    public SimpleAuthenticationImpl setPreSharedKey(final String key) {
        this.key = ((key == null) ? "" : key);
        return this;
    }

    /**
     * Set Pre-shared-key with a Random value
     * 
     * @return this
     */
    public SimpleAuthenticationImpl setPreSharedKeyRandom() {
        this.key = generateRandomKey();
        return this;
    }

    /**
     * Get Expire time of tokens used in decode / verify 
     * 
     * @return expiration expressed in seconds
     * @see #DEFAULT_EXPIRE
     */
    public int getExpire() {
        return expire;
    }

    /**
     * Set Expire time of tokens used in decode / verify
     * 
     * @param expire in seconds
     * @return this
     */
    public SimpleAuthenticationImpl setExpire(final int expire) {
        if (expire <= 0) {
            throw new IllegalArgumentException("expire must be greater than 0");
        }
        this.expire = expire;
        return this;
    }

    /**
     * Produce signed token using default algorithm
     * 
     * @return signed token
     * @throws GeneralSecurityException if signature fail
     * @throws UnsupportedEncodingException 
     */
    public String sign() throws GeneralSecurityException, UnsupportedEncodingException {
        return sign(HashAlg.SHA256, null);
    }

    /**
     * Produce signed token
     * 
     * @param alg hashing algorithm
     * @return signed token
     * @throws GeneralSecurityException if signature fail
     * @throws UnsupportedEncodingException 
     */
    public String sign(final HashAlg alg) throws GeneralSecurityException, UnsupportedEncodingException {
        return sign(alg, null);
    }

    /**
     * Produce signed token
     * 
     * @param alg hashing algorithm
     * @param data key-value pairs to sign
     * @return signed token
     * @throws GeneralSecurityException if signature fail
     * @throws UnsupportedEncodingException 
     */
    public String sign(final HashAlg alg, final Map<String, String> data) throws GeneralSecurityException, UnsupportedEncodingException {
        String d;
        try {
            d = encodeMap(data);
        } catch (UnsupportedEncodingException e) {
            throw new GeneralSecurityException(e);
        }
        final long now = System.currentTimeMillis() / 1000L;
        String b = sign(alg, d.getBytes(UTF8), String.valueOf(now));
        final StringBuilder sb = new StringBuilder(d.length() + b.length() + 20);
        sb.append(String.valueOf(alg)); // ALG
        sb.append(",");
        sb.append(String.valueOf(now)); // TS
        sb.append(",");
        sb.append(d); // DATA
        sb.append(",");
        sb.append(b); // HASH
        return sb.toString();
    }

    /**
     * Decode data in signed token
     * 
     * @param signed data
     * @return data of null if verify fail
     */
    public Map<String, String> decode(final String signed) {
        final Map<String, String> map = new LinkedHashMap<String, String>();
        if (verify(signed, map)) {
            return map;
        }
        return null;
    }

    /**
     * Verify signed token
     * 
     * @param signed data
     * @return true if signature good
     */
    public boolean verify(final String signed) {
        return verify(signed, null);
    }
    /**
     * Verify signed token
     * 
     * @param signed data
     * @return true if signature good
     */
    public boolean verify(final HttpServletRequest req) {
        String httpAuhorizationHeader = req.getHeader("Authorization");
                
        final String[] sp = httpAuhorizationHeader.split(" ");
        final String scheme = sp[0], credentials = sp[1];
        return verify(credentials, null);
    }
    /**
     * Verify and Decode in specified mapDecode data in signed token
     * 
     * @param signed data
     * @param mapDecode where put decoded data or null for no decoding
     * @return true if ok
     */
    public boolean verify(final String signed, final Map<String, String> mapDecode) {
        try {
            final long now = System.currentTimeMillis() / 1000L;
            final String[] atdh = signed.split(",");
            // ALG,TS,DATA,HASH
            final String a = atdh[0], t = atdh[1], d = atdh[2], h1 = atdh[3];
            //
            final HashAlg alg = HashAlg.valueOf(a);
            
            final long ts = Long.parseLong(t);
            if ((ts + expire) < now) {
                // Expired
                return false;
            }
            final String h2 = sign(alg, d.getBytes(UTF8), t);
            if (h2.equalsIgnoreCase(h1)) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }


    /**
     * Hex decoding
     * 
     * @param input string
     * @return
     */
    private byte[] decodeHex(final String input) {
        return DatatypeConverter.parseHexBinary(input);
    }

    /**
     * Hex encoding
     * 
     * @param input buffer
     * @return
     */
    private static String encodeHex(final byte[] input) {
        return DatatypeConverter.printHexBinary(input);
    }

    /**
     * Long to byte[]
     * 
     * @param value
     * @return
     */
    private static final byte[] longToByteArray(final long value) {
        return new byte[] {
                (byte) (value >>> 56), //
                (byte) (value >>> 48), //
                (byte) (value >>> 40),  //
                (byte) (value >>> 32), //
                (byte) (value >>> 24), //
                (byte) (value >>> 16), //
                (byte) (value >>> 8),  //
                (byte) value
        };
    }

    /**
     * Signature
     * 
     * @param alg
     * @param buf
     * @param ts
     * @return
     * @throws UnsupportedEncodingException 
     * @throws SignatureException
     */
    private String sign(final HashAlg alg, final byte[] buf, final String ts) throws GeneralSecurityException, UnsupportedEncodingException {
        final Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        sha256_HMAC.init(secret_key);

        return Hex.encodeHexString(sha256_HMAC.doFinal(ts.getBytes("UTF-8")));

    }
    private byte[] sign_old(final HashAlg alg, final byte[] buf, final long ts) throws GeneralSecurityException {
        final Mac m = alg.getMac();
        m.init(new SecretKeySpec(key.getBytes(UTF8), m.getAlgorithm()));
        m.update(longToByteArray(ts));
        m.update(buf);
        return m.doFinal();
    }
    /**
     * Like application/x-www-form-urlencoded
     * 
     * @param map to encode
     * @return
     * @throws UnsupportedEncodingException
     */
    private static String encodeMap(final Map<String, String> map) throws UnsupportedEncodingException {
        if ((map == null) || map.isEmpty()) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        for (final Entry<String, String> e : map.entrySet()) {
            sb.append(URLEncoder.encode(e.getKey(), "UTF-8"));
            sb.append("=");
            sb.append(URLEncoder.encode(e.getValue(), "UTF-8"));
            sb.append("&");
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }

    /**
     * Like application/x-www-form-urlencoded
     * 
     * @param data to decode
     * @return
     * @throws UnsupportedEncodingException
     */
    private static void decodeMap(final String data, final Map<String, String> map)
            throws UnsupportedEncodingException {
        final char[] buf = data.toCharArray();
        if ((buf == null) || (buf.length <= 0)) {
            return;
        }
        final StringBuilder sb = new StringBuilder();
        final int len = buf.length;
        boolean readValue = false; // finding = (reading key)
        String key = "", value = "";
        for (int i = 0; i < len; i++) {
            final char c = buf[i];
            if (c == '=') {
                key = URLDecoder.decode(sb.toString(), "UTF-8");
                value = "";
                sb.setLength(0);
                readValue = true; // finding & (reading value)
            } else if (c == '&') {
                value = URLDecoder.decode(sb.toString(), "UTF-8");
                sb.setLength(0);
                map.put(key, value);
                key = value = "";
                readValue = false;
            } else {
                sb.append(c);
            }
        }
        if (sb.length() > 0) {
            if (readValue) {
                value = URLDecoder.decode(sb.toString(), "UTF-8");
            } else {
                key = URLDecoder.decode(sb.toString(), "UTF-8");
            }
            sb.setLength(0);
            map.put(key, value);
        }
    }

    public static enum HashAlg {
        /**
         * HmacSHA256
         */
        SHA256("HmacSHA256"), //
        /**
         * HmacSHA512
         */
        SHA512("HmacSHA512"), //
        ;
        private final String mac;

        HashAlg(final String mac) {
            this.mac = mac;
            // Check Early Runtime Exception
            try {
                getMac();
            } catch (SignatureException e) {
                throw new RuntimeException(e);
            }
        }

        public Mac getMac() throws SignatureException {
            try {
                return Mac.getInstance(mac);
            } catch (NoSuchAlgorithmException e) {
                throw new SignatureException(e);
            }
        }
    }
}
