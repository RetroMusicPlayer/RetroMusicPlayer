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

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.util.PreferenceUtil
import com.google.android.material.navigation.NavigationBarView

/**
 * Extension functions for navigation-related operations
 */

/**
 * Get the next visible tab in the navigation
 */
fun NavigationBarView.getNextTab(): Int? {
    val currentItemId = selectedItemId
    val visibleTabs = PreferenceUtil.libraryCategory.filter { it.visible }.map { it.category.id }
    val currentIndex = visibleTabs.indexOf(currentItemId)
    
    return if (currentIndex != -1 && currentIndex < visibleTabs.size - 1) {
        visibleTabs[currentIndex + 1]
    } else null
}

/**
 * Get the previous visible tab in the navigation
 */
fun NavigationBarView.getPreviousTab(): Int? {
    val currentItemId = selectedItemId
    val visibleTabs = PreferenceUtil.libraryCategory.filter { it.visible }.map { it.category.id }
    val currentIndex = visibleTabs.indexOf(currentItemId)
    
    return if (currentIndex > 0) {
        visibleTabs[currentIndex - 1]
    } else null
}

/**
 * Navigate to the next tab if available
 */
fun NavController.navigateToNextTab(navigationView: NavigationBarView): Boolean {
    return try {
        val nextTabId = navigationView.getNextTab()
        if (nextTabId != null && currentDestination?.id != nextTabId) {
            val navOptions = NavOptions.Builder()
                .setEnterAnim(R.anim.slide_in_right)
                .setExitAnim(R.anim.slide_out_left)
                .setPopEnterAnim(R.anim.slide_in_left)
                .setPopExitAnim(R.anim.slide_out_right)
                .build()
            navigate(nextTabId, null, navOptions)
            true
        } else false
    } catch (e: Exception) {
        false
    }
}

/**
 * Navigate to the previous tab if available
 */
fun NavController.navigateToPreviousTab(navigationView: NavigationBarView): Boolean {
    return try {
        val previousTabId = navigationView.getPreviousTab()
        if (previousTabId != null && currentDestination?.id != previousTabId) {
            val navOptions = NavOptions.Builder()
                .setEnterAnim(R.anim.slide_in_left)
                .setExitAnim(R.anim.slide_out_right)
                .setPopEnterAnim(R.anim.slide_in_right)
                .setPopExitAnim(R.anim.slide_out_left)
                .build()
            navigate(previousTabId, null, navOptions)
            true
        } else false
    } catch (e: Exception) {
        false
    }
}