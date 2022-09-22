package ru.stersh.retrosonic.core.storage

import ru.stersh.retrosonic.core.extensions.ApplicationScope
import org.koin.dsl.module
import ru.stersh.retrosonic.core.storage.data.PlayQueueStorageImpl
import ru.stersh.retrosonic.core.storage.domain.PlayQueueStorage

val coreModule = module {
    single<PlayQueueStorage> { PlayQueueStorageImpl(ApplicationScope, get()) }
}