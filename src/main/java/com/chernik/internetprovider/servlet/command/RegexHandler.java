package com.chernik.internetprovider.servlet.command;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class RegexHandler {
    private ArrayList<Pattern> patterns = new ArrayList<>();
    private ArrayList<RequestParameter> values = new ArrayList<>();

    public void put(RequestParameter parameter) {
        String uriRegularExpression = parameter.getUri().replace("{", "").replace("}", "");
        patterns.add(Pattern.compile(uriRegularExpression));
        values.add(parameter);
    }

    public RequestParameter get(RequestParameter parameter) {
        for (int i = 0; i < patterns.size(); i++) {
            if (patterns.get(i).matcher(parameter.getUri()).matches() && parameter.getType().equals(values.get(i).getType())) {
                return values.get(i);
            }
        }
        return null;
    }
}