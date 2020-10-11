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

/** @author Hemanth S (h4h13). */
public class TempUtils {

  // Enums
  public static final int TEMPO_STROLL = 0;
  public static final int TEMPO_WALK = 1;
  public static final int TEMPO_LIGHT_JOG = 2;
  public static final int TEMPO_JOG = 3;
  public static final int TEMPO_RUN = 4;
  public static final int TEMPO_SPRINT = 5;
  public static final int TEMPO_UNKNOWN = 6;

  // take BPM as an int
  public static int getTempoFromBPM(int bpm) {

    // STROLL less than 60
    if (bpm < 60) {
      return TEMPO_STROLL;
    }

    // WALK between 60 and 70, or between 120 and 140
    else if (bpm < 70 || bpm >= 120 && bpm < 140) {
      return TEMPO_WALK;
    }

    // LIGHT_JOG between 70 and 80, or between 140 and 160
    else if (bpm < 80 || bpm >= 140 && bpm < 160) {
      return TEMPO_LIGHT_JOG;
    }

    // JOG between 80 and 90, or between 160 and 180
    else if (bpm < 90 || bpm >= 160 && bpm < 180) {
      return TEMPO_JOG;
    }

    // RUN between 90 and 100, or between 180 and 200
    else if (bpm < 100 || bpm >= 180 && bpm < 200) {
      return TEMPO_RUN;
    }

    // SPRINT between 100 and 120
    else if (bpm < 120) {
      return TEMPO_SPRINT;
    }

    // UNKNOWN
    else {
      return TEMPO_UNKNOWN;
    }
  }

  // take BPM as a string
  public static int getTempoFromBPM(String bpm) {
    // cast to an int from string
    try {
      // convert the string to an int
      return getTempoFromBPM(Integer.parseInt(bpm.trim()));
    } catch (NumberFormatException nfe) {

      //
      return TEMPO_UNKNOWN;
    }
  }
}
