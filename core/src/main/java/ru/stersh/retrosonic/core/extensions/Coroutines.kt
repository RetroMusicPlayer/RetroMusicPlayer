package ru.stersh.retrosonic.core.extensions

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

val ApplicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)