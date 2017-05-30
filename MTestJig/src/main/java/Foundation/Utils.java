package Foundation;

import java.util.Calendar;

public final class Utils {

  public static double getDecimalTimeOfDay() {
    Calendar c = Calendar.getInstance();
    int hr = c.get(Calendar.HOUR_OF_DAY);
    int min = c.get(Calendar.MINUTE);
    int sec = c.get(Calendar.SECOND);
    int ms = c.get(Calendar.MILLISECOND);
    double result = hr + min / 60.0 + sec / 3600.0 + ms / (3600000.0);
    return result;
  }

  public static void sleep(long t) {
    sleep(t, 0);
  }

  public static void sleep(long ms, int nano) {
    try {
      Thread.sleep(ms, nano);
    } catch (InterruptedException e) {
    }

  }
}
