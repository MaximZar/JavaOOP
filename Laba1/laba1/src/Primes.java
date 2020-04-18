public class Primes {
    // проверка всех чисел до 100
    public static void main(String args[]) {
        for (int i = 2; i <= 100 ; i ++) {
            if (isPrime(i) == true) {
                System.out.println(i);
            }
        }
    }
    // проверка числа на простоту
    public static boolean isPrime(int n) {
        for (int i = 2; i < n; i++) {
            if (n % i == 0) return false;
        }
        return true;
    }
}
