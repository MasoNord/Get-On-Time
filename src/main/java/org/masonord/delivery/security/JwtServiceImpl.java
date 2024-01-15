package org.masonord.delivery.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class JwtServiceImpl implements JwtService{

    private String secretKey;

    private long jwtExpiration;

    private long refreshExpiration;

    public JwtServiceImpl () {
        secretKey = "DDAAB91CDE43F5C82D3FAEBB33A49";
        jwtExpiration = 604800000;
        refreshExpiration = 86400000;
    }

    @Override
    public String extractUserName(String token) {
        return null;
    }

    @Override
    public String generateRefreshToken(UserDetails userDetails) {
        return null;
    }

    @Override
    public String generateAccessToken(Map<String, String> extraClaims, UserDetails userDetails, long expiration) {
        return null;
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        return false;
    }

    @Override
    public Date extractExpiration(String token) {
        return null;
    }
}