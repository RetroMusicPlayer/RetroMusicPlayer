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
package code.name.monkey.retromusic.helper

import androidx.navigation.NavController
import code.name.monkey.retromusic.extensions.navigateToNextTab
import code.name.monkey.retromusic.extensions.navigateToPreviousTab
import code.name.monkey.retromusic.util.PreferenceUtil
import com.google.android.material.navigation.NavigationBarView

/**
 * Manages horizontal swipe navigation between tabs
 * Decoupled from UI components for better testability and maintainability
 */
class NavigationSwipeManager {
    
    private var navController: NavController? = null
    private var navigationView: NavigationBarView? = null
    private var isNavigationEnabled: () -> Boolean = { true }
    
    fun initialize(
        navController: NavController,
        navigationView: NavigationBarView,
        isNavigationEnabled: () -> Boolean = { true }
    ) {
        this.navController = navController
        this.navigationView = navigationView
        this.isNavigationEnabled = isNavigationEnabled
    }
    
    fun handleSwipe(direction: HorizontalSwipeHelper.SwipeDirection): Boolean {
        // Check if swipe navigation is enabled
        if (!PreferenceUtil.horizontalSwipeNavigation) return false
        
        // Check if navigation is currently allowed
        if (!isNavigationEnabled()) return false
        
        // Ensure we have valid references
        val controller = navController ?: return false
        val navView = navigationView ?: return false
        
        return when (direction) {
            HorizontalSwipeHelper.SwipeDirection.LEFT -> {
                controller.navigateToNextTab(navView)
            }
            HorizontalSwipeHelper.SwipeDirection.RIGHT -> {
                controller.navigateToPreviousTab(navView)
            }
        }
    }
    
    fun cleanup() {
        navController = null
        navigationView = null
    }
}