package code.name.monkey.retromusic.views;

import android.graphics.drawable.GradientDrawable;

public class DrawableGradient extends GradientDrawable {
    public DrawableGradient(Orientation orientations, int[] colors, int shape) {
        super(orientations, colors);
        try {
            setShape(shape);
            setGradientType(GradientDrawable.LINEAR_GRADIENT);
            setCornerRadius(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DrawableGradient SetTransparency(int transparencyPercent) {
        this.setAlpha(255 - ((255 * transparencyPercent) / 100));
        return this;
    }

}
