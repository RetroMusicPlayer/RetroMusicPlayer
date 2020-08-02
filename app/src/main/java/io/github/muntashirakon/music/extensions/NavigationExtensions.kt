package io.github.muntashirakon.music.extensions

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController

fun Fragment.navigate(@IdRes id: Int) = findNavController().navigate(id)

fun Fragment.navController(@IdRes id: Int): NavController {
    val fragment = childFragmentManager.findFragmentById(id) as NavHostFragment
    return fragment.navController
}

fun AppCompatActivity.navController(@IdRes id: Int): NavController {
    val fragment = supportFragmentManager.findFragmentById(id) as NavHostFragment
    return fragment.navController
}