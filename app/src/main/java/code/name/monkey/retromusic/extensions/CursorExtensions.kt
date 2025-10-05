/*
 * Copyright (c) 2020 Hemanth Savarla.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 */
package code.name.monkey.retromusic.extensions

import android.database.Cursor

// exception is rethrown manually in order to have a readable stacktrace

internal fun Cursor.getInt(columnName: String): Int {
    try {
        return getInt(getColumnIndexOrThrow(columnName))
    } catch (ex: Throwable) {
        throw IllegalStateException("invalid column $columnName", ex)
    }
}

internal fun Cursor.getLong(columnName: String): Long {
    try {
        return getLong(getColumnIndexOrThrow(columnName))
    } catch (ex: Throwable) {
        throw IllegalStateException("invalid column $columnName", ex)
    }
}

internal fun Cursor.getString(columnName: String): String {
    try {
        val columnIndex = getColumnIndexOrThrow(columnName)
        val rawString = getString(columnIndex) // Use the native cursor getString with index
        return rawString?.let { fixStringEncoding(it) } ?: ""
    } catch (ex: Throwable) {
        throw IllegalStateException("invalid column $columnName", ex)
    }
}

internal fun Cursor.getStringOrNull(columnName: String): String? {
    try {
        val columnIndex = getColumnIndexOrThrow(columnName)
        val rawString = getString(columnIndex) // Use the native cursor getString with index
        return rawString?.let { fixStringEncoding(it) }
    } catch (ex: Throwable) {
        throw IllegalStateException("invalid column $columnName", ex)
    }
}

/**
 * Fixes character encoding issues in metadata strings.
 * Handles both HTML entities and JAudioTagger safe character substitutions.
 */
private fun fixStringEncoding(text: String): String {
    if (text.isEmpty()) return text
    
    return try {
        var result = text
        
        // Handle HTML entities first
        if (text.contains("&")) {
            result = result
                .replace("&deg;", "°")
                .replace("&#176;", "°")
                .replace("&amp;", "&")
                .replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&quot;", "\"")
                .replace("&#39;", "'")
                .replace("&auml;", "ä")
                .replace("&ouml;", "ö")
                .replace("&uuml;", "ü")
                .replace("&Auml;", "Ä")
                .replace("&Ouml;", "Ö")
                .replace("&Uuml;", "Ü")
                .replace("&oslash;", "ø")
                .replace("&Oslash;", "Ø")
                .replace("&apos;", "'")
                .replace("°", "°")
                .replace("&#8451;", "°")
        }
        
        // Handle JAudioTagger safe character markers back to original characters
        result = result
            .replace("___DEG___", "°")
            .replace("___ae___", "ä")
            .replace("___oe___", "ö")
            .replace("___ue___", "ü")
            .replace("___AE___", "Ä")
            .replace("___OE___", "Ö")
            .replace("___UE___", "Ü")
            .replace("___o_slash___", "ø")
            .replace("___O_SLASH___", "Ø")
        
        // Handle the specific corruption from the original issue
        result.replace("起院", "°_°")
        
    } catch (e: Exception) {
        text
    }
}
