package com.chernik.internetprovider.servlet.command;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class RegexHandler {
    private ArrayList<Pattern> patterns = new ArrayList<>();
    private ArrayList<String> values = new ArrayList<>();

    public void put(String uri) {
        String uriRegularExpression = uri.replace("{", "").replace("}", "");
        patterns.add(Pattern.compile(uriRegularExpression));
        values.add(uri);
    }

    public String get(String uri) {
        for (int i = 0; i < patterns.size(); i++) {
            if (patterns.get(i).matcher(uri).matches()) {
                return values.get(i);
            }
        }
        return null;
    }
}