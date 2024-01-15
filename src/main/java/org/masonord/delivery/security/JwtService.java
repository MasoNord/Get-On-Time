package org.masonord.delivery.security;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.Map;

public interface JwtService {
    /**
     * get the username by the given token;
     *
     * @param token
     * @return
     */
    String extractUserName (String token);

    /**
     * generate a new refresh token based on a user's information
     *
     * @param userDetails
     * @return
     */

    String generateRefreshToken(UserDetails userDetails);

    /**
     * generate a new access token
     *
     *
     * @param extraClaims
     * @param userDetails
     * @param expiration
     * @return
     */
    String generateAccessToken(Map<String, String> extraClaims, UserDetails userDetails, long expiration);

    /**
     * check if a token valid or not
     *
     * @param token
     * @param userDetails
     * @return
     */

    boolean isTokenValid(String token, UserDetails userDetails);

    /**
     * fetch expiration time for a token
     *
     * @param token
     * @return
     */

    Date extractExpiration(String token);
}
