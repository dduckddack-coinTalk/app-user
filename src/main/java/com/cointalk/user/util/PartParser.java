package com.cointalk.user.util;

import org.springframework.http.codec.multipart.Part;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class PartParser {
    public static Mono<String> convertString(Part partData) {
        var result = partData.content()
                .map(data -> {
                    String str = null;
                    try {
                        var aa = data.asInputStream().readAllBytes();
                        str = new String(aa, StandardCharsets.UTF_8);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return str;
                })
                .reduce((a, b) -> a + b);
        return result;
    }
    public static Mono<String> getStringFrom(MultiValueMap<String,Part> dataMap, String key) {
        var partData = dataMap.getFirst(key);
        if(partData == null) {
            return Mono.empty();
        }
        return convertString(partData);
    }
}
