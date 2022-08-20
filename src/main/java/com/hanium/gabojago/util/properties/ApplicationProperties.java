package com.hanium.gabojago.util.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApplicationProperties {
    public static String HOST_IMAGE_URL;
    public static String PROFILE_PATH;

    public ApplicationProperties(@Value("${host-image-url}") String hostImgUrl,
                                 @Value("${images.path.profile}") String profilePath) {
        HOST_IMAGE_URL = hostImgUrl;
        PROFILE_PATH = profilePath;
    }
}
