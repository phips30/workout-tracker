package com.phips30.workouttracker;

public class UrlBuilder {

    public static String buildUrl(String baseUrl, String ... pathVars) {
        if(pathVars == null) {
            return baseUrl;
        }
        return baseUrl.endsWith("/") ?
                baseUrl + String.join("/", pathVars) :
                baseUrl + "/" + String.join("/", pathVars);
    }
}
