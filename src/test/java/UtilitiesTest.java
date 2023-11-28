import org.junit.*;
import org.referenceCat.utils.Utilities;
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
}