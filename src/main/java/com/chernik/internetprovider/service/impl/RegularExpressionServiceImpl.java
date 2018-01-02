package com.chernik.internetprovider.service.impl;

import com.chernik.internetprovider.context.Service;
import com.chernik.internetprovider.service.RegularExpressionService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RegularExpressionServiceImpl implements RegularExpressionService {
    private static final Logger LOGGER = LogManager.getLogger(RegularExpressionServiceImpl.class);


    @Override
    public boolean checkToRegularExpression(String text, String regularExpression) {
        Pattern pattern = Pattern.compile(regularExpression);
        Matcher matcher = pattern.matcher(text);

        LOGGER.log(Level.TRACE, "Checking \"{}\" by regular expression \"{}\"", text, regularExpression);
        return matcher.matches();
    }
}
