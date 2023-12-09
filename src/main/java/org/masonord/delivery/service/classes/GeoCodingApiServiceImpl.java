package org.masonord.delivery.service.classes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.masonord.delivery.dto.model.GeoCodingDto;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Getter
@Accessors(chain = true)
@AllArgsConstructor
public class GeoCodingApiServiceImpl implements org.masonord.delivery.service.interfaces.GeoCodingApiService {
    private final WebClient webClient;

    public GeoCodingDto[] getGeoLocation(String address) {
        Mono<GeoCodingDto[]> geoCodingApiMono = webClient
                .get()
                .uri(String.join("", "search?q=", address))
                .retrieve()
                .bodyToMono(GeoCodingDto[].class);

        GeoCodingDto[] res = geoCodingApiMono
                .share().block();

        return res;
    }
}
