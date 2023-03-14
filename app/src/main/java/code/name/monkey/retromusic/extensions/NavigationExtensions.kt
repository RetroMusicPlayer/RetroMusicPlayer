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

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController

fun Fragment.navigate(@IdRes id: Int) = findNavController().navigate(id)

fun Fragment.findNavController(@IdRes id: Int): NavController {
    val fragment = childFragmentManager.findFragmentById(id) as NavHostFragment
    return fragment.navController
}

fun Fragment.findActivityNavController(@IdRes id: Int): NavController {
    return requireActivity().findNavController(id)
}

fun AppCompatActivity.findNavController(@IdRes id: Int): NavController {
    val fragment = supportFragmentManager.findFragmentById(id) as NavHostFragment
    return fragment.navController
}
