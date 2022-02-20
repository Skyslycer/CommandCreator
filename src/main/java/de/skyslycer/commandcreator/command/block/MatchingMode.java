package de.skyslycer.commandcreator.command.block;

import java.util.regex.Pattern;

public enum MatchingMode {

    EQUALS,
    STARTS_WITH,
    CONTAINS,
    REGEX;

    public boolean match(String input, String matcher) {
        switch (this) {
            case EQUALS: return matcher.equals(input);
            case STARTS_WITH: return input.startsWith(matcher);
            case CONTAINS: return input.contains(matcher);
            case REGEX: {
                Pattern pattern = Pattern.compile(matcher);
                return pattern.matcher(input).find();
            }
        }
        return false;
    }

    public static MatchingMode match(String config) {
        try {
            return MatchingMode.valueOf(config);
        } catch (IllegalArgumentException exception) {
            return MatchingMode.STARTS_WITH;
        }
    }

}
