package com.phips30.workouttracker;

public class UrlBuilder {

    public static String buildUrl(String templateUrl, Object... values) {
        if (templateUrl.contains("%s")) {
            // If template has placeholders, format it
            return String.format(templateUrl, values);
        } else if (values == null) {
            return templateUrl;
        }

        StringBuilder sb = new StringBuilder(templateUrl);
        for (Object value : values) {
            if (!templateUrl.endsWith("/")) {
                sb.append("/");
            }
            sb.append(value);
        }
        return sb.toString();
    }
}
