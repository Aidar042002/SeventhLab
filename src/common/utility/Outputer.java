package common.utility;


public class Outputer {
    public static void print(Object toOut) {
        System.out.print(toOut);
    }


    public static void println() {
        System.out.println();
    }

    public static void println(Object toOut) {
        System.out.println(toOut);
    }

    public static void printerror(Object toOut) {
        System.out.println("error: " + toOut);
    }
}