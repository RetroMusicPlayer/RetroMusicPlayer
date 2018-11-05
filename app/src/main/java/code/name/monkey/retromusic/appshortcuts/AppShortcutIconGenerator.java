package code.name.monkey.retromusic.appshortcuts;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import androidx.annotation.RequiresApi;
import android.util.TypedValue;

import code.name.monkey.appthemehelper.ThemeStore;

import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.util.PreferenceUtil;
import code.name.monkey.retromusic.util.RetroUtil;

/**
 * @author Adrian Campos
 */
@RequiresApi(Build.VERSION_CODES.N_MR1)
public final class AppShortcutIconGenerator {
    public static Icon generateThemedIcon(Context context, int iconId) {
        if (PreferenceUtil.getInstance().coloredAppShortcuts()){
            return generateUserThemedIcon(context, iconId);
        } else {
            return generateDefaultThemedIcon(context, iconId);
        }
    }

    private static Icon generateDefaultThemedIcon(Context context, int iconId) {
        // Return an Icon of iconId with default colors
        return generateThemedIcon(context, iconId,
                context.getColor(R.color.app_shortcut_default_foreground),
                context.getColor(R.color.app_shortcut_default_background)
        );
    }

    private static Icon generateUserThemedIcon(Context context, int iconId) {
        // Get background color from context's theme
        final TypedValue typedColorBackground = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.colorBackground, typedColorBackground, true);

        // Return an Icon of iconId with those colors
        return generateThemedIcon(context, iconId,
                ThemeStore.accentColor(context),
                typedColorBackground.data
        );
    }

    private static Icon generateThemedIcon(Context context, int iconId, int foregroundColor, int backgroundColor) {
        // Get and tint foreground and background drawables
        Drawable vectorDrawable = RetroUtil.getTintedVectorDrawable(context, iconId, foregroundColor);
        Drawable backgroundDrawable = RetroUtil.getTintedVectorDrawable(context, R.drawable.ic_app_shortcut_background, backgroundColor);

        // Squash the two drawables together
        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{backgroundDrawable, vectorDrawable});

        // Return as an Icon
        return Icon.createWithBitmap(drawableToBitmap(layerDrawable));
    }

    private static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
