package srsc2.tp2Srsc.Crypto;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;

public class Utils {
    static String secret = "queremosovinte";
    static String KEY = Base64.getEncoder().encodeToString(secret.getBytes());
    static String SUBJECT = "JSON WEB TOKEN SRSC";
    static int VALIDITY = 1000 * 60 * 60 * 10;

    public static String getNewToken(byte[] iv) {
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put("iv",iv);
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(SUBJECT)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + VALIDITY))
                .signWith(SignatureAlgorithm.HS256, KEY)
                .compact();
    }
}
