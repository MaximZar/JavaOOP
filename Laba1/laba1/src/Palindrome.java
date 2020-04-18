public class Palindrome {
    public static void main(String[] args) {
        for (int i = 0; i < args.length; i++) {
            String s = args[i];
            if (isPalindrome(s)) {
                System.out.println(s + " - palindrome");
            } else {
                System.out.println(s + " - is not palindrome");
            }
        }
    }
    // реверсирование строки
    public static String reverseString(String Strings) {
        String reverse = "";
        for (int i = Strings.length() - 1; i >= 0; i--) {
            reverse += Strings.charAt(i);
        }
        return reverse;
    }
    // проверка на палиндром
    public static boolean isPalindrome(String s) {
        String reverse = reverseString(s);
        if (s.equals(reverse)) return true;
        else return false;
    }
}
