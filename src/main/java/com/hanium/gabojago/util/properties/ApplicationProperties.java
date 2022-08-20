package com.hanium.gabojago.util.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApplicationProperties {
    public static String HOST_IMAGE_URL;

    public ApplicationProperties(@Value("${host-image-url}") String hostImgUrl) {
        HOST_IMAGE_URL = hostImgUrl;
    }
}
