package org.masonord.delivery.filter;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.masonord.delivery.config.PropertiesConfig;
import org.masonord.delivery.service.classes.LocationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
public class RateLimitFilter implements HandlerInterceptor {
    private final long capacity = Long.parseLong(PropertiesConfig.getConfigValue("capacity"));
    private final long tokens =  Long.parseLong(PropertiesConfig.getConfigValue("tokens"));;

    private final Bucket bucket = Bucket.builder()
            .addLimit(limit -> limit.capacity(capacity).refillGreedy(tokens, Duration.ofMinutes(1)))
            .build();

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();
    private final long defaultTokens =  Long.parseLong(PropertiesConfig.getConfigValue("defaultTokens"));;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String clientIP = getClientIP(request);

        Bucket bucketByClientIP;
        if (buckets.containsKey(clientIP)) {
            bucketByClientIP = buckets.get(clientIP);
        } else {
            bucketByClientIP = this.bucket;
            buckets.put(clientIP, bucketByClientIP);
        }

        ConsumptionProbe probe = bucketByClientIP.tryConsumeAndReturnRemaining(this.defaultTokens);
        if (probe.isConsumed()) {
            response.addHeader("X-Rate-Limit-Remaining",
                    Long.toString(probe.getRemainingTokens()));
            return true;
        }

        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.addHeader("X-Rate-Limit-Retry-After-Milliseconds",
                Long.toString(TimeUnit.NANOSECONDS.toMillis(probe.getNanosToWaitForRefill())));

        return false;
    }

    private String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader("X-FORWARDED-FOR");

        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }

}
