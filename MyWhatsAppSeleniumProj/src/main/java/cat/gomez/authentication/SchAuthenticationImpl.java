package cat.gomez.authentication;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Hex;

import cat.gomez.whatsapp.model.UserDao;
import cat.gomez.whatsapp.model.UserDaoImplXML;

/**
 * Authentication based in HMAC an Time
 * 
 * HTTP request is expected to contain Authorizaton header with 
 * 
 * Authoritatio: sch Alg,Userid,HMAC(URI+Date Header)
 */

public class SchAuthenticationImpl implements Authentication{

    /**
     * Default expire: {@value #DEFAULT_EXPIRE} seconds
     */
    public static final int DEFAULT_EXPIRE = (5 * 60);

    private int expire = DEFAULT_EXPIRE;

    private UserDao userDao = new UserDaoImplXML();

    @Override
    public boolean verify(HttpServletRequest req) {
        try {
            String httpAuhorizationHeader = req.getHeader("Authorization");
            //String httpDateHeader = req.getHeader("Date");
            final String uri = req.getRequestURI();
                    
            final String[] sp = httpAuhorizationHeader.split(" ");
            final String scheme = sp[0], credentials = sp[1];
            final String[] atdh = credentials.split(",");
            // ALG,UserId,HASH
            final String a = atdh[0], t = atdh[1], userId = atdh[2], hash1 = atdh[3];
            final HashAlg alg = HashAlg.valueOf(a);

            //ZonedDateTime zdt = ZonedDateTime.parse(httpDateHeader, DateTimeFormatter.RFC_1123_DATE_TIME);
            //if (zdt.plusSeconds(expire).compareTo(ZonedDateTime.now()) < 0) {
            //    // Expired
            //    return false;
            //}
            
            final long ts = Long.parseLong(t);
            if ((ts + expire) < System.currentTimeMillis() / 1000L) {
                // Expired
                return false;
            }
            
            final String hash2 = sign(alg, uri + t, userDao.getUser(userId).getSecret());
            
            if (hash2.equalsIgnoreCase(hash1)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String generate(HashAlg alg, String data, String key) {
        final long now = System.currentTimeMillis() / 1000L;
        final String b = sign(alg,String.valueOf(now), key);
        final StringBuilder sb = new StringBuilder(b.length() + 20);
        sb.append(String.valueOf(alg)); // ALG
        sb.append(",");
        sb.append(String.valueOf(now)); // TS
        sb.append(",");
        sb.append(b); // HASH
        return sb.toString();
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
    private String sign(final HashAlg alg,final String ts, String key) {
        Mac sha256_HMAC;
        try {
            final SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
            sha256_HMAC = Mac.getInstance("HmacSHA256");
            sha256_HMAC.init(secret_key);
            return Hex.encodeHexString(sha256_HMAC.doFinal(ts.getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException | IllegalStateException | UnsupportedEncodingException | InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
    }
}
