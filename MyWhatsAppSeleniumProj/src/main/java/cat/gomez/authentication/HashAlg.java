package cat.gomez.authentication;

import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

import javax.crypto.Mac;

public enum HashAlg {
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
