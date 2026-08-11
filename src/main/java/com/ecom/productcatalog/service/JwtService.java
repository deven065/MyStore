package com.ecom.productcatalog.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {
    /*
    *   @Value reads the value of "jwt.secret"
    *   from applications.properties
    *
    *   For example:
    *
    *   jwt.secret=VGhpc0lzQVN1cGVy...
    *
    *   Spring takes that value and puts it inside secretKet
     */
    @Value("${jwt.secret}")
    private String secretKey;

    /*
        * This method creates a JWT token for a user.
        *
        * We pass the user's email because email is the
        * identity we are currently using to identify the user.
     */

    public String generateToken(String email) {

        /*
            *   Our secret key is currently stored as a String.
            *
            * Cryptographic operations work with bytes,
            * so we convert the String into a byte array.
            *
            * UTF_8 tells Java how to convert the String into bytes.
         */

        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);

        /*
            *   Convert our byte array into a cryptographic SecretKey.
            *   HMAC-SHA is the signing algorithm used by our JWT.
            *   This SecretKey will later be used to sign the JWT.
         */

        SecretKey key = Keys.hmacShaKeyFor(keyBytes);

        /*
            *   Jwts.builder() starts creating a new JWT
         */

        return Jwts.builder()
                /*
                subject() identifies who the token belongs to.
                we are storing the user's email as the subject.
                Example:
                subject = "devenrikame55@gmail.com"
                 */
                .subject(email)
                /*
                issuesAt() records the time when the JWT was created.
                new Date() represents the current date / time.
                 */
                .issuedAt(new Date())
                /*
                expiration tells us when the JWT should expire.
                System.currentTimeMills() gives the current time in milliseconds.
                1000 milliseconds = 1 second
                60 seconds = 1 minute
                60 minutes = 1 hour.
                 */
                .expiration(
                        new Date(
                                System.currentTimeMillis() + 1000L * 60 * 60
                        )
                )
                /*
                Sign the JWT using our SecretKey.
                This signature allows the backend to verify that the token was created by our application
                and has not been modified.
                 */
                .signWith(key)
                /*
                compact() converts the JWT builder into the final String representations.
                The result will look similar to:
                eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ...
                 */
                .compact();
    }

    /*
    * Extract the email from the JWT.
    * Earlier, while creating the token, I used:
    * .subject(email)
    * Therefore the enail is stored inside the JWT's "sub"
    * (subject) claim.
     */
    public String extractEmail(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())    //Tells Jwts which secret key should be used to verify the token's signature.
                .build()    //  Build the JWT parser
                .parseSignedClaims(token)   //  Parse the signed JWT
                .getPayload()   //  Get the claims stored inside the JWT
                .getSubject();  //  Get the subject, this is the user's email
    }

    /*
    * Create the Secret key used by both:
    * 1. JWT generation
    * 2. JWT verification
    *
    * It is important that both operations use the same secret key.
     */

    private SecretKey getSigningKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);   //  Convert the secret String into bytes.
        return Keys.hmacShaKeyFor(keyBytes);    //  Convert those bytes into a cryptogrpahic SecretKey.
    }
}
