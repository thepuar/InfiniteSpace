package es.thepuar.InfiniteSpace.utils;

public class PrinterUtil {
    private final int TOKENS = 10;
    public static void printParte(int parte){
        System.out.print(parte+" ");
        if(parte%10==0)
            System.out.println();
    }
}
