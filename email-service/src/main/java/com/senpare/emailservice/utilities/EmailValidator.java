package com.senpare.emailservice.utilities;

import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

@Service
public class EmailValidator implements Predicate<String> {


    @Override
    public boolean test(String s) {
        Pattern emailPattern = Pattern.compile("^(?=.{1,64}@)[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)*@" +
                "[^-][a-zA-Z0-9-]+(\\.[a-zA-Z]+)*(\\.[a-zA_Z]{2,})$");
        Matcher emailMatcher = emailPattern.matcher(s);
        return emailMatcher.matches();
    }
}