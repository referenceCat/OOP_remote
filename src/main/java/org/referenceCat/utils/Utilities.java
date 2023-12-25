package org.referenceCat.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.Character.isDigit;

public class Utilities {

    public static final String DATE_FORMAT = "dd.MM.yyyy";
    public static final String DATE_TIME_FORMAT = "dd.MM.yyyy HH:mm";
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
        if (s == null || s.isEmpty()) return new ValidationResponse("Необходимое поле ");
        return new ValidationResponse();
    }
    public static boolean isNumeric(String s) {
        if (s == null || s.isEmpty()) return false;
        for (Character c: s.toCharArray()) {
            if (!isDigit(c)) return false;
        }
        return true;
    }
    public static ValidationResponse passportValidation(String s) {
        if (!isNumeric(s)) return new ValidationResponse("Номер паспотра должен быть целым числом ");
        if (s.length() != 10) return new ValidationResponse("Неправильная длина строки (необходимо 10) ");
        return new ValidationResponse();
    }

    public static ValidationResponse licenseValidation(String s) {
        if (!isNumeric(s)) return new ValidationResponse("Номер лицензии должен быть целым числом ");
        if (s.length() != 10) return new ValidationResponse("Неправильная длина строки (необходимо 10)");
        return new ValidationResponse();
    }

    public static boolean isDate(String s, String format_s) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(format_s);
            format.setLenient(false);
            format.parse(s);
        } catch (ParseException | IllegalArgumentException e) {
            return false;
        }

        return true;
    }

    public static Date parseDate(String s, String format_s) throws ParseException {
        return new SimpleDateFormat(format_s).parse(s);
    }

    public static String dateToString(Date date, String format_s) {
        if (date == null) return "";
        return new SimpleDateFormat(format_s).format(date);
    }

    public static ValidationResponse dateValidation(String s, String format_s) {
            if (isDate(s, format_s)) {
                return new ValidationResponse();
            } else {
                return new ValidationResponse("Неправильный формат даты (" + format_s + ") или даты не существует");
            }
    }

    public static ValidationResponse regNumberValidation(String s) {
        if(s == null) return new ValidationResponse("Wrong format ");
        if (s.length() != 8 && s.length() != 9) return new ValidationResponse("Неправильная длина строки");
        if (!(isCyrillicLetter(s.charAt(0))
                && isDigit(s.charAt(1))
                && isDigit(s.charAt(2))
                && isDigit(s.charAt(3))
        && isCyrillicLetter(s.charAt(4))
        && isCyrillicLetter(s.charAt(5))
                && isDigit(s.charAt(6))
                && isDigit(s.charAt(7))
                && (s.length() == 8 || isDigit(s.charAt(8))))) return new ValidationResponse("Неправильный формат РН");
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
