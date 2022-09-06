package com.hanium.gabojago.util.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OAuthProperties {
    public static String REDIRECT_PROD;
    public static String REDIRECT_TEST;

    public OAuthProperties(@Value("${oauth2.kakao.redirect-uri}") String redirectProd,
                           @Value("${oauth2.kakao.redirect-uri-test}") String redirectTest) {
        REDIRECT_PROD = redirectProd;
        REDIRECT_TEST = redirectTest;
    }
}
