package test.java.task1;

import main.java.task1.SecSeries;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SecSeriesTest {

    // Допустимое отклонение
    private static final double EPSILON = 1e-6;

    @Test
    public void testSecAtZero() {
        // Проверяем корректность в точке x=0
        double expected = 1.0; // sec(0) = 1
        double actualSeries = SecSeries.secBySeries(0.0, 5);
        double actualMath = SecSeries.secByMath(0.0);

        // Сравниваем серию с эталоном (Math) и с теоретическим значением
        assertEquals(expected, actualSeries, EPSILON, "Серия и теоретическое значение в нуле не совпадают");
        assertEquals(expected, actualMath, EPSILON, "Встроенная функция даёт неверный результат в нуле");
    }

    @Test
    public void testSecAtSmallValue() {
        // Небольшое значение x, где разложение хорошо работает
        double x = 0.1;
        double actualSeries = SecSeries.secBySeries(x, 5);
        double actualMath = SecSeries.secByMath(x);

        // Проверяем, что результаты близки
        assertEquals(actualMath, actualSeries, EPSILON,
                "Серия и Math.sec не совпадают на малом аргументе x=0.1");
    }

    @Test
    public void testSecAtNegativeValue() {
        // Проверяем чётность sec(x) = sec(-x)
        double x = -0.2;
        double actualSeries = SecSeries.secBySeries(x, 5);
        double actualMath = SecSeries.secByMath(x);

        assertEquals(actualMath, actualSeries, EPSILON,
                "Серия и Math.sec не совпадают при x = -0.2");
    }

    @Test
    public void testSecWithIncreasedTerms() {
        // Проверяем, что при увеличении числа членов точность повышается
        double x = 0.5;
        double seriesApproxLow = SecSeries.secBySeries(x, 3);
        double seriesApproxHigh = SecSeries.secBySeries(x, 6);
        double actualMath = SecSeries.secByMath(x);

        // Проверим, что приближение на 6 шагах точнее, чем на 3
        double errorLow = Math.abs(seriesApproxLow - actualMath);
        double errorHigh = Math.abs(seriesApproxHigh - actualMath);
        assertTrue(errorHigh < errorLow, "Увеличение числа членов ряда не улучшило точность");
    }

    @Test
    public void testSecNearBoundary() {
        // x, близкий к точке, где cos(x) очень мал, но не равен 0
        // Например, чуть меньше π/2 (~1.5708), возьмем 1.55
        double x = 1.55;
        // Вблизи π/2 разложение может работать плохо, можно проверить устойчивость
        double actualSeries = SecSeries.secBySeries(x, 12);
        double actualMath = SecSeries.secByMath(x);

        // Тут может быть большее EPSILON из-за роста ошибки
        double localEpsilon = 1e-2;
        assertEquals(actualMath, actualSeries, localEpsilon,
                "Серия и Math.sec слишком расходятся около точки разрыва");
    }

    @Test
    public void testSecRandomValues() {
        Random random = new Random();
        int testCount = 100;

        for (int i = 0; i < testCount; i++) {
            double x = -0.5 + random.nextDouble();

            double expected = 1.0 / Math.cos(x);

            double actual = SecSeries.secBySeries(x, 10);

            assertEquals(expected, actual, EPSILON,
                    "Не совпадает sec(x) при x = " + x);
        }
    }
}