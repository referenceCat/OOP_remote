import org.junit.*;
import org.referenceCat.utils.Utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UtilitiesTest {
    int counter;
    @Test
    public void testIsInteger() {
        Assert.assertTrue(Utilities.isInteger("123"));
        Assert.assertTrue(Utilities.isInteger("0"));
        Assert.assertTrue(Utilities.isInteger("-1"));

        Assert.assertFalse(Utilities.isInteger(null));
        Assert.assertFalse(Utilities.isInteger(""));
        Assert.assertFalse(Utilities.isInteger("aklsnv"));
        Assert.assertFalse(Utilities.isInteger("123456789087654321234567890098765432"));
        Assert.assertFalse(Utilities.isInteger("-123456789087654321234567890098765432"));
    }

    @Test
    public void testRequiredFieldCheck() {
        Assert.assertFalse(Utilities.requiredFieldCheck("").isValid);
        Assert.assertTrue(Utilities.requiredFieldCheck("123").isValid);
        Assert.assertFalse(Utilities.requiredFieldCheck(null).isValid);
    }

    @Test
    public void testIsNumeric() {
        Assert.assertTrue(Utilities.isNumeric("1234567890987654321234567890"));
        Assert.assertFalse(Utilities.isNumeric(null));
        Assert.assertFalse(Utilities.isNumeric(""));
        Assert.assertFalse(Utilities.isNumeric("hsdjkjbn1234"));
    }

    @Test
    public void testPassportValidation() {
        Assert.assertTrue(Utilities.passportValidation("4018219557").isValid);
        Assert.assertFalse(Utilities.passportValidation("23u7ioq8453572i481o3").isValid);
        Assert.assertFalse(Utilities.passportValidation("4019557").isValid);
        Assert.assertFalse(Utilities.passportValidation("qwertyuiou").isValid);
        Assert.assertFalse(Utilities.passportValidation("").isValid);
        Assert.assertFalse(Utilities.passportValidation(null).isValid);
    }

    @Test
    public void testLicenseValidation() {
        Assert.assertTrue(Utilities.licenseValidation("4018219557").isValid);
        Assert.assertFalse(Utilities.licenseValidation("23u7ioq8453572i481o3").isValid);
        Assert.assertFalse(Utilities.licenseValidation("4019557").isValid);
        Assert.assertFalse(Utilities.licenseValidation("qwertyuiou").isValid);
        Assert.assertFalse(Utilities.licenseValidation("").isValid);
        Assert.assertFalse(Utilities.licenseValidation(null).isValid);
    }

    @Test
    public void testParseDate()  {
        Assert.assertThrows(ParseException.class, () -> Utilities.parseDate("erhml", Utilities.DATE_FORMAT));
        Assert.assertThrows(ParseException.class, () -> Utilities.parseDate("", Utilities.DATE_FORMAT));
        Assert.assertThrows(NullPointerException.class, () -> Utilities.parseDate(null, Utilities.DATE_FORMAT));
        try {
            Assert.assertEquals(Utilities.parseDate("17.11.2004", Utilities.DATE_FORMAT), new SimpleDateFormat("dd.MM.yyyy").parse("17.11.2004"));
        } catch (Exception ignored) {}

    }

    @Test
    public void testDateValidation() {
        Assert.assertFalse(Utilities.dateValidation("", Utilities.DATE_FORMAT).isValid);
        Assert.assertFalse(Utilities.dateValidation(null, Utilities.DATE_FORMAT).isValid);
        Assert.assertFalse(Utilities.dateValidation("sfuvykbjk", Utilities.DATE_FORMAT).isValid);
        Assert.assertTrue(Utilities.dateValidation("17.11.2044", Utilities.DATE_FORMAT).isValid);
    }

    @Test
    public void testRegNumberValidation() {
        Assert.assertFalse(Utilities.regNumberValidation(null).isValid);
        Assert.assertFalse(Utilities.regNumberValidation("").isValid);
        Assert.assertFalse(Utilities.regNumberValidation("tvuwybjnf").isValid);
        Assert.assertFalse(Utilities.regNumberValidation("f123hh21").isValid);
        Assert.assertFalse(Utilities.regNumberValidation("hh123j87").isValid);
        Assert.assertTrue(Utilities.regNumberValidation("р123рр178").isValid);

    }

    @Test
    public void testIsCyrillic() {
        Assert.assertFalse(Utilities.isCyrillicLetter('1'));
        Assert.assertFalse(Utilities.isCyrillicLetter(' '));
        Assert.assertFalse(Utilities.isCyrillicLetter('\0'));
        Assert.assertFalse(Utilities.isCyrillicLetter('\n'));
        Assert.assertFalse(Utilities.isCyrillicLetter('g'));
        Assert.assertFalse(Utilities.isCyrillicLetter('='));
        Assert.assertFalse(Utilities.isCyrillicLetter('`'));
        Assert.assertTrue(Utilities.isCyrillicLetter('р'));

    }

    @BeforeClass // Фиксируем начало тестирования
    public static void allTestsStarted() {
        System.out.println("Начало тестирования");
    }
    @AfterClass // Фиксируем конец тестирования
    public static void allTestsFinished() {
        System.out.println("Конец тестирования");
    }
    @Before // Фиксируем запуск теста
    public void testStarted() {
        System.out.println("Запуск теста");
    }
    @After // Фиксируем завершение теста
    public void testFinished() {
        System.out.println("Завершение теста");
    }

    private static Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }
}