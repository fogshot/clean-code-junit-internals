package com.capgemini.pvonnieb;

public class Assert {

    private Assert() {

    }

    public static String format(String message, String fExpected, String fActual) {
        StringBuilder sb = new StringBuilder();
        if (message != null) {
            sb.append(message)
                    .append(" ");
        }
        sb.append("expected:<")
                .append(fExpected)
                .append("> but was:<")
                .append(fActual)
                .append(">");
        return sb.toString();
    }
}
