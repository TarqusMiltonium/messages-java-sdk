package com.messagemedia.messages;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Formatter;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.messagemedia.messages.exceptions.APIException;
import com.messagemedia.messages.http.request.HttpMethod;

// TODO: Auto-generated Javadoc
/**
 * The Class AuthManager.
 */
public class AuthManager {
    
    /** The Constant HMAC_SHA1_ALGORITHM. */
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";


    /**
     * Apply.
     *
     * @param queryUrl the query url
     * @param headers the headers
     * @throws APIException the API exception
     */
    public static void apply(String queryUrl, Map<String, String> headers, HttpMethod method)
            throws APIException {
        apply(queryUrl, headers, null, method);
    }


    /**
     * Apply.
     *
     * @param queryUrl the query url
     * @param headers the headers
     * @param body the body
     * @param method the method
     * @throws APIException the API exception
     */
    public static void apply(String queryUrl, Map<String, String> headers, String body, HttpMethod method)
            throws APIException {
        if (hmacIsConfigured()) {
            addHmacHeaderTo(headers, queryUrl, body, method);
        } else {
            headers.put("Authorization", getBasicAuthForClient());
        }
    }

    /**
     * Build authorization header value for basic auth.
     *
     * @return Authorization header value for this client
     */
    private static String getBasicAuthForClient() {
        String val = Configuration.basicAuthUserName + ":" + Configuration.basicAuthPassword;
        return "Basic " + new String(Base64.getEncoder().encode(val.getBytes()));
    }

    /**
     * Gets the RFC 7231 date time.
     *
     * @return the RFC 7231 date time
     */
    private static String getRFC7231DateTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(calendar.getTime());
    }

    /**
     * Adds the hmac header to.
     *
     * @param headers the headers
     * @param url the url
     * @param body the body
     * @param method the method
     * @throws APIException the API exception
     */
    private static void addHmacHeaderTo(Map<String, String> headers, String url, String body, HttpMethod method) throws APIException {
        if (!hmacIsConfigured()) {
            return;
        }

        try {
            String dateHeader = getRFC7231DateTime();
            String contentHash = "";
            String contentSignature = "";

            if (body != null) {
                contentHash = getMd5HashFor(body);
                contentSignature = "x-Content-MD5: " + contentHash + "\n";
            }

            headers.put("date", dateHeader);

            if (contentHash != null && !contentHash.isEmpty()) {
                headers.put("x-Content-MD5", contentHash);
            }

            String signature = createHmacEncodedSignatureFrom(dateHeader, contentSignature, url, method);
            String authorizationHeader = "hmac username=\"" + Configuration.hmacAuthUserName
                    + "\", algorithm=\"hmac-sha1\", headers=\"date" + (body != null ? " x-Content-MD5" : "")
                    + " request-line\", signature=\"" + signature + "\"";

            headers.put("Authorization", authorizationHeader);
        } catch (NoSuchAlgorithmException e) {
            throw new APIException("Could not find the MD5 algorithm. Check machine configuration");
        } catch (InvalidKeyException e) {
            throw new APIException("Invalid HMAC authorization key. Check machine configuration");
        }
    }

    /**
     * Gets the md 5 hash for.
     *
     * @param body the body
     * @return the md 5 hash for
     * @throws NoSuchAlgorithmException the no such algorithm exception
     */
    private static String getMd5HashFor(String body) throws NoSuchAlgorithmException {
        byte[] messageBytes = body.getBytes(StandardCharsets.UTF_8);
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] md5Bytes = md5.digest(messageBytes);

        return toHexString(md5Bytes);
    }

    /**
     * To hex string.
     *
     * @param bytes the bytes
     * @return the string
     */
    private static String toHexString(byte[] bytes) {
        Formatter formatter = new Formatter();

        for (byte b : bytes) {
            formatter.format("%02x", b);
        }

        String hexString = formatter.toString();
        formatter.close();

        return hexString;
    }

    /**
     * Creates the hmac encoded signature from.
     *
     * @param dateHeader the date header
     * @param contentSignature the content signature
     * @param url the url
     * @param method the method
     * @return the string
     * @throws InvalidKeyException the invalid key exception
     * @throws NoSuchAlgorithmException the no such algorithm exception
     */
    private static String createHmacEncodedSignatureFrom(String dateHeader, String contentSignature, String url, HttpMethod method)
            throws InvalidKeyException, NoSuchAlgorithmException {
        String signingString = "date: " + dateHeader + "\n" + contentSignature
                + method.name() + " " + url.replace(Configuration.baseUri, "")
                + " HTTP/1.1";

        return getHmacEncodingFor(signingString);
    }

    /**
     * Checks if HMAC authentication is configured.
     * 
     * @return True if it is, False otherwise.
     */
    private static boolean hmacIsConfigured() {
        return Configuration.hmacAuthUserName != null && !Configuration.hmacAuthUserName.isEmpty()
                && Configuration.hmacAuthPassword != null && !Configuration.hmacAuthPassword.isEmpty();
    }

    /**
     * Gets the hmac encoding for.
     *
     * @param signature the signature
     * @return the hmac encoding for
     * @throws NoSuchAlgorithmException the no such algorithm exception
     * @throws InvalidKeyException the invalid key exception
     */
    private static String getHmacEncodingFor(String signature)
            throws NoSuchAlgorithmException, InvalidKeyException {
        Mac hasher = Mac.getInstance(HMAC_SHA1_ALGORITHM);
        hasher.init(new SecretKeySpec(Configuration.hmacAuthPassword.getBytes(), HMAC_SHA1_ALGORITHM));

        byte[] hash = hasher.doFinal(signature.getBytes());

        String base64Encoded = Base64.getEncoder().encodeToString(hash);

        return base64Encoded;
    }
}
