package chatClient;

import java.util.Calendar;
import java.util.Locale;

public class TimeInfo {
    Calendar calendar = Calendar.getInstance(Locale.KOREA);

    public String getTimeInfo() {
        return calendar.get(Calendar.YEAR) + "-" +
                calendar.get(Calendar.MONTH) + "-" +
                calendar.get(Calendar.DAY_OF_MONTH) + " " +
                calendar.get(Calendar.HOUR_OF_DAY) + ":" +
                calendar.get(Calendar.MINUTE) + ":" +
                calendar.get(Calendar.SECOND);
    }
}
