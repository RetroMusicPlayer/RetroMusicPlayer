/*
 * Copyright (c) 2019 Hemanth Savarala.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by
 *  the Free Software Foundation either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */

package code.name.monkey.retromusic.util;

import java.util.Calendar;
import java.util.GregorianCalendar;

/** @author Eugene Cheung (arkon) */
public class CalendarUtil {
  private static final long MS_PER_MINUTE = 60 * 1000;
  private static final long MS_PER_DAY = 24 * 60 * MS_PER_MINUTE;

  private Calendar calendar;

  public CalendarUtil() {
    this.calendar = Calendar.getInstance();
  }

  /**
   * Returns the time elapsed so far today in milliseconds.
   *
   * @return Time elapsed today in milliseconds.
   */
  public long getElapsedToday() {
    // Time elapsed so far today
    return (calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE)) * MS_PER_MINUTE
        + calendar.get(Calendar.SECOND) * 1000
        + calendar.get(Calendar.MILLISECOND);
  }

  /**
   * Returns the time elapsed so far this week in milliseconds.
   *
   * @return Time elapsed this week in milliseconds.
   */
  public long getElapsedWeek() {
    // Today + days passed this week
    long elapsed = getElapsedToday();

    final int passedWeekdays =
        calendar.get(Calendar.DAY_OF_WEEK) - 1 - calendar.getFirstDayOfWeek();
    if (passedWeekdays > 0) {
      elapsed += passedWeekdays * MS_PER_DAY;
    }

    return elapsed;
  }

  /**
   * Returns the time elapsed so far this month in milliseconds.
   *
   * @return Time elapsed this month in milliseconds.
   */
  public long getElapsedMonth() {
    // Today + rest of this month
    return getElapsedToday() + ((calendar.get(Calendar.DAY_OF_MONTH) - 1) * MS_PER_DAY);
  }

  /**
   * Returns the time elapsed so far this month and the last numMonths months in milliseconds.
   *
   * @param numMonths Additional number of months prior to the current month to calculate.
   * @return Time elapsed this month and the last numMonths months in milliseconds.
   */
  public long getElapsedMonths(int numMonths) {
    // Today + rest of this month
    long elapsed = getElapsedMonth();

    // Previous numMonths months
    int month = calendar.get(Calendar.MONTH);
    int year = calendar.get(Calendar.YEAR);
    for (int i = 0; i < numMonths; i++) {
      month--;

      if (month < Calendar.JANUARY) {
        month = Calendar.DECEMBER;
        year--;
      }

      elapsed += getDaysInMonth(month) * MS_PER_DAY;
    }

    return elapsed;
  }

  /**
   * Returns the time elapsed so far this year in milliseconds.
   *
   * @return Time elapsed this year in milliseconds.
   */
  public long getElapsedYear() {
    // Today + rest of this month + previous months until January
    long elapsed = getElapsedMonth();

    int month = calendar.get(Calendar.MONTH) - 1;
    int year = calendar.get(Calendar.YEAR);
    while (month > Calendar.JANUARY) {
      elapsed += getDaysInMonth(month) * MS_PER_DAY;

      month--;
    }

    return elapsed;
  }

  /**
   * Gets the number of days for the given month in the given year.
   *
   * @param month The month (1 - 12).
   * @return The days in that month/year.
   */
  private int getDaysInMonth(int month) {
    final Calendar monthCal = new GregorianCalendar(calendar.get(Calendar.YEAR), month, 1);
    return monthCal.getActualMaximum(Calendar.DAY_OF_MONTH);
  }

  /**
   * Returns the time elapsed so far last N days in milliseconds.
   *
   * @return Time elapsed since N days in milliseconds.
   */
  public long getElapsedDays(int numDays) {
    long elapsed = getElapsedToday();
    elapsed += numDays * MS_PER_DAY;

    return elapsed;
  }
}
