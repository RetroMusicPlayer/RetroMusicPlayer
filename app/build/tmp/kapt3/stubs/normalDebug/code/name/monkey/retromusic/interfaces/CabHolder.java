package code.name.monkey.retromusic.interfaces;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H&\u00a8\u0006\b"}, d2 = {"Lcode/name/monkey/retromusic/interfaces/CabHolder;", "", "openCab", "Lcom/afollestad/materialcab/MaterialCab;", "menuRes", "", "callback", "Lcom/afollestad/materialcab/MaterialCab$Callback;", "app_normalDebug"})
public abstract interface CabHolder {
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.afollestad.materialcab.MaterialCab openCab(int menuRes, @org.jetbrains.annotations.NotNull()
    com.afollestad.materialcab.MaterialCab.Callback callback);
}