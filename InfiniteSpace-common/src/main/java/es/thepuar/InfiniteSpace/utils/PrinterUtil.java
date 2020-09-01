package es.thepuar.InfiniteSpace.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.DecimalFormat;

public class PrinterUtil {
    private static final Logger logger = LogManager.getLogger(PrinterUtil.class);
    private final int TOKENS = 10;
    public static void printParte(int parte,int total){
        DecimalFormat df = new DecimalFormat("#.00");
        logger.debug("{} / {} - {} %", parte, total, df.format(((double)parte/total)*100));


    }
}
