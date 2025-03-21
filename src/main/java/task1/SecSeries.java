package main.java.task1;

public class SecSeries {

    // Метод для вычисления sec(x) через встроенные средства (контрольное значение)
    public static double secByMath(double x) {
        return 1.0 / Math.cos(x);
    }

    // Метод для вычисления sec(x) через частичную сумму ряда Маклорена
    public static double secBySeries(double x, int terms) {
        int[] eulerEven = {1, 1, 5, 61, 1385, 50521, 2702765, 199360981};

        double sum = 0.0;
        for (int n = 0; n < terms && n < eulerEven.length; n++) {
            // E_{2n} * x^{2n} / (2n)!
            double numerator = eulerEven[n] * Math.pow(x, 2 * n);
            double denominator = factorial(2 * n);
            sum += numerator / denominator;
        }
        return sum;
    }

    // Вспомогательный метод для вычисления факториала
    private static long factorial(int n) {
        long result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }
}