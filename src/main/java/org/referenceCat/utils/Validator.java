package org.referenceCat.utils;

import static java.lang.Character.isDigit;

public class Validator {
    public boolean isNumeric(String s) {
        for (Character c: s.toCharArray()) {
            if (!isDigit(c)) return false;
        }
        return true;
    }
    public String validationPassport(String s) {
        if (s.length() != 10) return "Invalid length";
        if (isNumeric(s)) return "Contains not digits";
        return null;
    }
}
