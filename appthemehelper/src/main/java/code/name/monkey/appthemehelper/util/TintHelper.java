package code.name.monkey.appthemehelper.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.CheckResult;
import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Field;

import code.name.monkey.appthemehelper.R;

/**
 * @author afollestad, plusCubed
 */
public final class TintHelper {

    @SuppressWarnings("JavaReflectionMemberAccess")
    public static void colorHandles(@NonNull TextView view, int color) {
        try {
            Field editorField = TextView.class.getDeclaredField("mEditor");
            if (!editorField.isAccessible()) {
                editorField.setAccessible(true);
            }

            Object editor = editorField.get(view);
            Class<?> editorClass = editor.getClass();

            String[] handleNames = {"mSelectHandleLeft", "mSelectHandleRight", "mSelectHandleCenter"};
            String[] resNames = {"mTextSelectHandleLeftRes", "mTextSelectHandleRightRes", "mTextSelectHandleRes"};

            for (int i = 0; i < handleNames.length; i++) {
                Field handleField = editorClass.getDeclaredField(handleNames[i]);
                if (!handleField.isAccessible()) {
                    handleField.setAccessible(true);
                }

                Drawable handleDrawable = (Drawable) handleField.get(editor);

                if (handleDrawable == null) {
                    Field resField = TextView.class.getDeclaredField(resNames[i]);
                    if (!resField.isAccessible()) {
                        resField.setAccessible(true);
                    }
                    int resId = resField.getInt(view);
                    handleDrawable = view.getResources().getDrawable(resId);
                }

                if (handleDrawable != null) {
                    Drawable drawable = handleDrawable.mutate();
                    drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
                    handleField.set(editor, drawable);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @CheckResult
    @NonNull
    public static Drawable createTintedDrawable(Context context,
                                                @DrawableRes int res, @ColorInt int color) {
        Drawable drawable = ContextCompat.getDrawable(context, res);
        return createTintedDrawable(drawable, color);
    }

    // This returns a NEW Drawable because of the mutate() call. The mutate() call is necessary because Drawables with the same resource have shared states otherwise.
    @CheckResult
    @NonNull
    public static Drawable createTintedDrawable(@Nullable Drawable drawable, @ColorInt int color) {
        if (drawable == null) {
            return null;
        }
        drawable = DrawableCompat.wrap(drawable.mutate());
        DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN);
        DrawableCompat.setTint(drawable, color);
        return drawable;
    }

    // This returns a NEW Drawable because of the mutate() call. The mutate() call is necessary because Drawables with the same resource have shared states otherwise.
    @CheckResult
    @Nullable
    public static Drawable createTintedDrawable(@Nullable Drawable drawable, @NonNull ColorStateList sl) {
        if (drawable == null) {
            return null;
        }
        Drawable temp = DrawableCompat.wrap(drawable.mutate());
        DrawableCompat.setTintList(temp, sl);
        return temp;
    }

    public static void setCursorTint(@NonNull EditText editText, @ColorInt int color) {
        try {
            Field fCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            fCursorDrawableRes.setAccessible(true);
            int mCursorDrawableRes = fCursorDrawableRes.getInt(editText);
            Field fEditor = TextView.class.getDeclaredField("mEditor");
            fEditor.setAccessible(true);
            Object editor = fEditor.get(editText);
            Class<?> clazz = editor.getClass();
            Field fCursorDrawable = clazz.getDeclaredField("mCursorDrawable");
            fCursorDrawable.setAccessible(true);
            Drawable[] drawables = new Drawable[2];
            drawables[0] = ContextCompat.getDrawable(editText.getContext(), mCursorDrawableRes);
            drawables[0] = createTintedDrawable(drawables[0], color);
            drawables[1] = ContextCompat.getDrawable(editText.getContext(), mCursorDrawableRes);
            drawables[1] = createTintedDrawable(drawables[1], color);
            fCursorDrawable.set(editor, drawables);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setTint(@NonNull RadioButton radioButton, @ColorInt int color, boolean useDarker) {
        ColorStateList sl = new ColorStateList(new int[][]{
                new int[]{-android.R.attr.state_enabled},
                new int[]{android.R.attr.state_enabled, -android.R.attr.state_checked},
                new int[]{android.R.attr.state_enabled, android.R.attr.state_checked}
        }, new int[]{
                // Rdio button includes own alpha for disabled state
                ColorUtil.INSTANCE.stripAlpha(ContextCompat.getColor(radioButton.getContext(),
                        useDarker ? R.color.ate_control_disabled_dark : R.color.ate_control_disabled_light)),
                ContextCompat.getColor(radioButton.getContext(),
                        useDarker ? R.color.ate_control_normal_dark : R.color.ate_control_normal_light),
                color
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            radioButton.setButtonTintList(sl);
        } else {
            Drawable d = createTintedDrawable(
                    ContextCompat.getDrawable(radioButton.getContext(), R.drawable.abc_btn_radio_material), sl);
            radioButton.setButtonDrawable(d);
        }
    }

    public static void setTint(@NonNull SeekBar seekBar, @ColorInt int color, boolean useDarker) {
        final ColorStateList s1 = getDisabledColorStateList(color, ContextCompat.getColor(seekBar.getContext(),
                useDarker ? R.color.ate_control_disabled_dark : R.color.ate_control_disabled_light));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            seekBar.setThumbTintList(s1);
            seekBar.setProgressTintList(s1);
        } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
            Drawable progressDrawable = createTintedDrawable(seekBar.getProgressDrawable(), s1);
            seekBar.setProgressDrawable(progressDrawable);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                Drawable thumbDrawable = createTintedDrawable(seekBar.getThumb(), s1);
                seekBar.setThumb(thumbDrawable);
            }
        } else {
            PorterDuff.Mode mode = PorterDuff.Mode.SRC_IN;
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
                mode = PorterDuff.Mode.MULTIPLY;
            }
            if (seekBar.getIndeterminateDrawable() != null) {
                seekBar.getIndeterminateDrawable().setColorFilter(color, mode);
            }
            if (seekBar.getProgressDrawable() != null) {
                seekBar.getProgressDrawable().setColorFilter(color, mode);
            }
        }
    }

    public static void setTint(@NonNull ProgressBar progressBar, @ColorInt int color) {
        setTint(progressBar, color, false);
    }

    public static void setTint(@NonNull ProgressBar progressBar, @ColorInt int color, boolean skipIndeterminate) {
        ColorStateList sl = ColorStateList.valueOf(color);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            progressBar.setProgressTintList(sl);
            progressBar.setSecondaryProgressTintList(sl);
            if (!skipIndeterminate) {
                progressBar.setIndeterminateTintList(sl);
            }
        } else {
            PorterDuff.Mode mode = PorterDuff.Mode.SRC_IN;
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
                mode = PorterDuff.Mode.MULTIPLY;
            }
            if (!skipIndeterminate && progressBar.getIndeterminateDrawable() != null) {
                progressBar.getIndeterminateDrawable().setColorFilter(color, mode);
            }
            if (progressBar.getProgressDrawable() != null) {
                progressBar.getProgressDrawable().setColorFilter(color, mode);
            }
        }
    }

    public static void setTint(@NonNull EditText editText, @ColorInt int color, boolean useDarker) {
        final ColorStateList editTextColorStateList = new ColorStateList(new int[][]{
                new int[]{-android.R.attr.state_enabled},
                new int[]{android.R.attr.state_enabled, -android.R.attr.state_pressed, -android.R.attr.state_focused},
                new int[]{}
        }, new int[]{
                ContextCompat.getColor(editText.getContext(),
                        useDarker ? R.color.ate_text_disabled_dark : R.color.ate_text_disabled_light),
                ContextCompat.getColor(editText.getContext(),
                        useDarker ? R.color.ate_control_normal_dark : R.color.ate_control_normal_light),
                color
        });
        if (editText instanceof AppCompatEditText) {
            ((AppCompatEditText) editText).setSupportBackgroundTintList(editTextColorStateList);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            editText.setBackgroundTintList(editTextColorStateList);
        }
        setCursorTint(editText, color);
    }

    public static void setTint(@NonNull CheckBox box, @ColorInt int color, boolean useDarker) {
        ColorStateList sl = new ColorStateList(new int[][]{
                new int[]{-android.R.attr.state_enabled},
                new int[]{android.R.attr.state_enabled, -android.R.attr.state_checked},
                new int[]{android.R.attr.state_enabled, android.R.attr.state_checked}
        }, new int[]{
                ContextCompat.getColor(box.getContext(),
                        useDarker ? R.color.ate_control_disabled_dark : R.color.ate_control_disabled_light),
                ContextCompat.getColor(box.getContext(),
                        useDarker ? R.color.ate_control_normal_dark : R.color.ate_control_normal_light),
                color
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            box.setButtonTintList(sl);
        } else {
            Drawable drawable = createTintedDrawable(
                    ContextCompat.getDrawable(box.getContext(), R.drawable.abc_btn_check_material), sl);
            box.setButtonDrawable(drawable);
        }
    }

    public static void setTint(@NonNull ImageView image, @ColorInt int color) {
        image.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
    }

    public static void setTint(@NonNull Switch switchView, @ColorInt int color, boolean useDarker) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            return;
        }
        if (switchView.getTrackDrawable() != null) {
            switchView.setTrackDrawable(modifySwitchDrawable(switchView.getContext(),
                    switchView.getTrackDrawable(), color, false, false, useDarker));
        }
        if (switchView.getThumbDrawable() != null) {
            switchView.setThumbDrawable(modifySwitchDrawable(switchView.getContext(),
                    switchView.getThumbDrawable(), color, true, false, useDarker));
        }
    }

    public static void setTint(@NonNull SwitchCompat switchView, @ColorInt int color, boolean useDarker) {
        if (switchView.getTrackDrawable() != null) {
            switchView.setTrackDrawable(modifySwitchDrawable(switchView.getContext(),
                    switchView.getTrackDrawable(), color, false, true, useDarker));
        }
        if (switchView.getThumbDrawable() != null) {
            switchView.setThumbDrawable(modifySwitchDrawable(switchView.getContext(),
                    switchView.getThumbDrawable(), color, true, true, useDarker));
        }
    }

    public static void setTintAuto(final @NonNull View view, final @ColorInt int color,
                                   boolean background) {
        setTintAuto(view, color, background, ATHUtil.INSTANCE.isWindowBackgroundDark(view.getContext()));
    }

    @SuppressWarnings("deprecation")
    public static void setTintAuto(final @NonNull View view, final @ColorInt int color,
                                   boolean background, final boolean isDark) {
        if (!background) {
            if (view instanceof FloatingActionButton) {
                setTint((FloatingActionButton) view, color, isDark);
            } else if (view instanceof RadioButton) {
                setTint((RadioButton) view, color, isDark);
            } else if (view instanceof SeekBar) {
                setTint((SeekBar) view, color, isDark);
            } else if (view instanceof ProgressBar) {
                setTint((ProgressBar) view, color);
            } else if (view instanceof EditText) {
                setTint((EditText) view, color, isDark);
            } else if (view instanceof CheckBox) {
                setTint((CheckBox) view, color, isDark);
            } else if (view instanceof ImageView) {
                setTint((ImageView) view, color);
            } else if (view instanceof Switch) {
                setTint((Switch) view, color, isDark);
            } else if (view instanceof SwitchCompat) {
                setTint((SwitchCompat) view, color, isDark);
            } else {
                background = true;
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
                    !background && view.getBackground() instanceof RippleDrawable) {
                // Ripples for the above views (e.g. when you tap and hold a switch or checkbox)
                RippleDrawable rd = (RippleDrawable) view.getBackground();
                @SuppressLint("PrivateResource") final int unchecked = ContextCompat.getColor(view.getContext(),
                        isDark ? R.color.ripple_material_dark : R.color.ripple_material_light);
                final int checked = ColorUtil.INSTANCE.adjustAlpha(color, 0.4f);
                final ColorStateList sl = new ColorStateList(
                        new int[][]{
                                new int[]{-android.R.attr.state_activated, -android.R.attr.state_checked},
                                new int[]{android.R.attr.state_activated},
                                new int[]{android.R.attr.state_checked}
                        },
                        new int[]{
                                unchecked,
                                checked,
                                checked
                        }
                );
                rd.setColor(sl);
            }
        }
        if (background) {
            // Need to tint the background of a view
            if (view instanceof FloatingActionButton || view instanceof Button) {
                setTintSelector(view, color, false, isDark);
            } else if (view.getBackground() != null) {
                Drawable drawable = view.getBackground();
                if (drawable != null) {
                    drawable = createTintedDrawable(drawable, color);
                    ViewUtil.INSTANCE.setBackgroundCompat(view, drawable);
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    public static void setTintSelector(@NonNull View view, @ColorInt final int color, final boolean darker,
                                       final boolean useDarkTheme) {
        final boolean isColorLight = ColorUtil.INSTANCE.isColorLight(color);
        final int disabled = ContextCompat.getColor(view.getContext(),
                useDarkTheme ? R.color.ate_button_disabled_dark : R.color.ate_button_disabled_light);
        final int pressed = ColorUtil.INSTANCE.shiftColor(color, darker ? 0.9f : 1.1f);
        final int activated = ColorUtil.INSTANCE.shiftColor(color, darker ? 1.1f : 0.9f);
        final int rippleColor = getDefaultRippleColor(view.getContext(), isColorLight);
        final int textColor = ContextCompat.getColor(view.getContext(),
                isColorLight ? R.color.ate_primary_text_light : R.color.ate_primary_text_dark);

        final ColorStateList sl;
        if (view instanceof Button) {
            sl = getDisabledColorStateList(color, disabled);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
                    view.getBackground() instanceof RippleDrawable) {
                RippleDrawable rd = (RippleDrawable) view.getBackground();
                rd.setColor(ColorStateList.valueOf(rippleColor));
            }

            // Disabled text color state for buttons, may get overridden later by ATE tags
            final Button button = (Button) view;
            button.setTextColor(getDisabledColorStateList(textColor, ContextCompat.getColor(view.getContext(),
                    useDarkTheme ? R.color.ate_button_text_disabled_dark : R.color.ate_button_text_disabled_light)));
        } else if (view instanceof FloatingActionButton) {
            // FloatingActionButton doesn't support disabled state?
            sl = new ColorStateList(new int[][]{
                    new int[]{-android.R.attr.state_pressed},
                    new int[]{android.R.attr.state_pressed}
            }, new int[]{
                    color,
                    pressed
            });

            final FloatingActionButton fab = (FloatingActionButton) view;
            fab.setRippleColor(rippleColor);
            fab.setBackgroundTintList(sl);
            if (fab.getDrawable() != null) {
                fab.setImageDrawable(createTintedDrawable(fab.getDrawable(), textColor));
            }
            return;
        } else {
            sl = new ColorStateList(
                    new int[][]{
                            new int[]{-android.R.attr.state_enabled},
                            new int[]{android.R.attr.state_enabled},
                            new int[]{android.R.attr.state_enabled, android.R.attr.state_pressed},
                            new int[]{android.R.attr.state_enabled, android.R.attr.state_activated},
                            new int[]{android.R.attr.state_enabled, android.R.attr.state_checked}
                    },
                    new int[]{
                            disabled,
                            color,
                            pressed,
                            activated,
                            activated
                    }
            );
        }

        Drawable drawable = view.getBackground();
        if (drawable != null) {
            drawable = createTintedDrawable(drawable, sl);
            ViewUtil.INSTANCE.setBackgroundCompat(view, drawable);
        }

        if (view instanceof TextView && !(view instanceof Button)) {
            final TextView tv = (TextView) view;
            tv.setTextColor(getDisabledColorStateList(textColor, ContextCompat.getColor(view.getContext(),
                    isColorLight ? R.color.ate_text_disabled_light : R.color.ate_text_disabled_dark)));
        }
    }

    @SuppressLint("PrivateResource")
    @ColorInt
    private static int getDefaultRippleColor(@NonNull Context context, boolean useDarkRipple) {
        // Light ripple is actually translucent black, and vice versa
        return ContextCompat.getColor(context, useDarkRipple ?
                R.color.ripple_material_light : R.color.ripple_material_dark);
    }

    @NonNull
    private static ColorStateList getDisabledColorStateList(@ColorInt int normal, @ColorInt int disabled) {
        return new ColorStateList(new int[][]{
                new int[]{-android.R.attr.state_enabled},
                new int[]{android.R.attr.state_enabled}
        }, new int[]{
                disabled,
                normal
        });
    }

    private static Drawable modifySwitchDrawable(@NonNull Context context, @NonNull Drawable from, @ColorInt int tint,
                                                 boolean thumb, boolean compatSwitch, boolean useDarker) {
        if (useDarker) {
            tint = ColorUtil.INSTANCE.shiftColor(tint, 1.1f);
        }
        tint = ColorUtil.INSTANCE.adjustAlpha(tint, (compatSwitch && !thumb) ? 0.5f : 1.0f);
        int disabled;
        int normal;
        if (thumb) {
            disabled = ContextCompat.getColor(context,
                    useDarker ? R.color.ate_switch_thumb_disabled_dark : R.color.ate_switch_thumb_disabled_light);
            normal = ContextCompat.getColor(context,
                    useDarker ? R.color.ate_switch_thumb_normal_dark : R.color.ate_switch_thumb_normal_light);
        } else {
            disabled = ContextCompat.getColor(context,
                    useDarker ? R.color.ate_switch_track_disabled_dark : R.color.ate_switch_track_disabled_light);
            normal = ContextCompat.getColor(context,
                    useDarker ? R.color.ate_switch_track_normal_dark : R.color.ate_switch_track_normal_light);
        }

        // Stock switch includes its own alpha
        if (!compatSwitch) {
            normal = ColorUtil.INSTANCE.stripAlpha(normal);
        }

        final ColorStateList sl = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_enabled},
                        new int[]{android.R.attr.state_enabled, -android.R.attr.state_activated,
                                -android.R.attr.state_checked},
                        new int[]{android.R.attr.state_enabled, android.R.attr.state_activated},
                        new int[]{android.R.attr.state_enabled, android.R.attr.state_checked}
                },
                new int[]{
                        disabled,
                        normal,
                        tint,
                        tint
                }
        );
        return createTintedDrawable(from, sl);
    }

    private static void setTint(final FloatingActionButton view, final int color, final boolean isDark) {
        view.setImageTintList(ColorStateList.valueOf(color));
    }
}