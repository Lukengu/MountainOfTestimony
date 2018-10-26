package pro.novatechsolutions.mountainoftestimony.ui.utils;

public class TimerUtils {

    public static String millisecondsToDays(long timeInMilliSeconds) {
        long seconds = timeInMilliSeconds / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        long months = days/30;
        
        return months % 12  +" months "+ days % 30 + " days " + hours % 24 + " h " + minutes % 60 + " m " + seconds % 60+" s";

    }
}
