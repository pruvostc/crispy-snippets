package com.crispysnippets.utils;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

/** This class contains classes to work with Dates and Time. 
 * @author christian pruvost 
 */
public class DateTime {
  private static final Logger LOGGER = Logger.getLogger(DateTime.class.getName());  
  
  /**
   * This Method returns the current UTC time in milliseconds.
   * @return long the UTC time in milliseconds
   */
  public static long getUtcTimeMilliseconds() {
    Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    long timeUtcMilliseconds = c.getTimeInMillis();
    LOGGER.log(Level.FINE, "UTC Time Milliseconds: " + timeUtcMilliseconds);
    LOGGER.log(Level.FINE, "System Milliseconds: " + System.currentTimeMillis());
    return timeUtcMilliseconds;
  }
}
