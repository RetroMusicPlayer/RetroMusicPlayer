package code.name.monkey.retromusic;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0003\u001a\u00020\u0004J\u0006\u0010\u0005\u001a\u00020\u0006J\u0006\u0010\u0007\u001a\u00020\b\u00a8\u0006\t"}, d2 = {"Lcode/name/monkey/retromusic/Injection;", "", "()V", "provideKuGouApiService", "Lcode/name/monkey/retromusic/rest/service/KuGouApiService;", "provideRepository", "Lcode/name/monkey/retromusic/providers/interfaces/Repository;", "provideSchedulerProvider", "Lcode/name/monkey/retromusic/util/schedulers/BaseSchedulerProvider;", "app_normalDebug"})
public final class Injection {
    public static final code.name.monkey.retromusic.Injection INSTANCE = null;
    
    @org.jetbrains.annotations.NotNull()
    public final code.name.monkey.retromusic.providers.interfaces.Repository provideRepository() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final code.name.monkey.retromusic.util.schedulers.BaseSchedulerProvider provideSchedulerProvider() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final code.name.monkey.retromusic.rest.service.KuGouApiService provideKuGouApiService() {
        return null;
    }
    
    private Injection() {
        super();
    }
}