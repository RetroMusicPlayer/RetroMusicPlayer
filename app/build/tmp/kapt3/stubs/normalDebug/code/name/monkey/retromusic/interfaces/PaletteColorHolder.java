package code.name.monkey.retromusic.interfaces;

import java.lang.System;

/**
 * * @author Aidan Follestad (afollestad)
 */
@kotlin.Metadata(mv = {1, 1, 13}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001R\u0014\u0010\u0002\u001a\u00020\u00038gX\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005\u00a8\u0006\u0006"}, d2 = {"Lcode/name/monkey/retromusic/interfaces/PaletteColorHolder;", "", "paletteColor", "", "getPaletteColor", "()I", "app_normalDebug"})
public abstract interface PaletteColorHolder {
    
    @androidx.annotation.ColorInt()
    public abstract int getPaletteColor();
}