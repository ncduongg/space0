package com.space.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TimeConvert {
  private static Logger logger = Logger.getLogger(TimeConvert.class.getName());

  private static final DateFormat yyyyMMddTHHmmssZ = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
  private static final DateFormat yyyy_MM_ddTHH_mm_ssZ = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
  private static final TimeZone timeZone = TimeZone.getTimeZone("GMT+7");

  public static String iso8601(Date d) {
    if (d == null)
      return null;
    yyyyMMddTHHmmssZ.setTimeZone(timeZone);
    return yyyyMMddTHHmmssZ.format(d);
  }

  public synchronized static Date iso8601(String s) {
    try {
      if (s != null && !"".equals(s))
        return yyyyMMddTHHmmssZ.parse(s.replaceAll("[-:]", ""));
    } catch (Exception e) {
      logger.log(Level.SEVERE, "", e);
    }
    return null;
  }

  public static Date addSecondsFromNow(int seconds) {
    try {
      Calendar calendar = Calendar.getInstance(timeZone);
      calendar.add(Calendar.SECOND, seconds);
      return calendar.getTime();
    } catch (Exception e) {
      logger.log(Level.SEVERE, "", e);
    }
    return null;
  }
}
