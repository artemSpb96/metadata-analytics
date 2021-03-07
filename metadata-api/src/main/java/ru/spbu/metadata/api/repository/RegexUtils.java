package ru.spbu.metadata.api.repository;

import java.util.regex.Pattern;

public class RegexUtils {
    private static final Pattern SPECIAL_REGEX_CHARS = Pattern.compile("[{}()\\[\\].+*?^$\\\\|]");

    private RegexUtils() {

    }

    public static String escapeSpecialChars(String str) {
        return SPECIAL_REGEX_CHARS.matcher(str).replaceAll("\\\\$0");
    }
}
