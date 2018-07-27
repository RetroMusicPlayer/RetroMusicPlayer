package code.name.monkey.retromusic.interfaces;

import android.support.annotation.NonNull;

import com.afollestad.materialcab.MaterialCab;


public interface CabHolder {

    @NonNull
    MaterialCab openCab(final int menuRes, final MaterialCab.Callback callback);
}
