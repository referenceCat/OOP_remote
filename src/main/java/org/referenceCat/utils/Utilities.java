package org.referenceCat.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.Character.isDigit;

public class Utilities {
    public static class ValidationResponse {
        public boolean isValid;
        public String message;

        public ValidationResponse() {
            isValid = true;
            message = " ";
        }

        public ValidationResponse(String message) {
            isValid = false;
            this.message = message;
        }
    }

    public static ValidationResponse requiredFieldCheck(String s) {
        if (s.isEmpty()) return new ValidationResponse("This is required field ");
        return new ValidationResponse();
    }
    public static boolean isNumeric(String s) {
        for (Character c: s.toCharArray()) {
            if (!isDigit(c)) return false;
        }
        return true;
    }
    public static ValidationResponse passportValidation(String s) {
        if (!isNumeric(s)) return new ValidationResponse("Passport number must be numeric");
        if (s.length() != 10) return new ValidationResponse("Invalid length (must be 10)");
        return new ValidationResponse();
    }

    public static ValidationResponse licenseValidation(String s) {
        if (!isNumeric(s)) return new ValidationResponse("License number must be numeric");
        if (s.length() != 10) return new ValidationResponse("Invalid length (must be 10)");
        return new ValidationResponse();
    }

    public static Date parseDate(String s) throws ParseException {
        return new SimpleDateFormat("dd.MM.yyyy").parse(s);
    }

    public static String dateToString(Date date) {
        if (date == null) return "";
        return new SimpleDateFormat("dd.MM.yyyy").format(date);
    }

    public static ValidationResponse dateValidation(String s) {
            try {
                parseDate(s);
                return new ValidationResponse();
            } catch (ParseException e) {
                return new ValidationResponse("Wrong data format (must be dd.MM.yyyy) ");
            }
    }

    public static ValidationResponse regNumberValidation(String s) {
        if (s.length() != 8 && s.length() != 9) return new ValidationResponse("Wrong string length ");
        if (!(isCyrillicLetter(s.charAt(0))
                && isDigit(s.charAt(1))
                && isDigit(s.charAt(2))
                && isDigit(s.charAt(3))
        && isCyrillicLetter(s.charAt(4))
        && isCyrillicLetter(s.charAt(5))
                && isDigit(s.charAt(6))
                && isDigit(s.charAt(7))
                && (s.length() == 8 || isDigit(s.charAt(8))))) return new ValidationResponse("Wrong format ");
        return new ValidationResponse();
    }

    public static boolean isCyrillicLetter(char c) {
        return Character.UnicodeBlock.of(c).equals(Character.UnicodeBlock.CYRILLIC);
    }

    public static boolean isInteger(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
