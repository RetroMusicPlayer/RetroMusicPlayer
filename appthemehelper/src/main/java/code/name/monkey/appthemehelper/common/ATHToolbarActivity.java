package code.name.monkey.appthemehelper.common;

import android.graphics.drawable.ColorDrawable;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;

import code.name.monkey.appthemehelper.ATHActivity;
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper;


public class ATHToolbarActivity extends ATHActivity {
    private Toolbar toolbar;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Toolbar toolbar = getATHToolbar();
        ToolbarContentTintHelper.handleOnCreateOptionsMenu(this, toolbar, menu, getToolbarBackgroundColor(toolbar));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        ToolbarContentTintHelper.handleOnPrepareOptionsMenu(this, getATHToolbar());
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void setSupportActionBar(@Nullable Toolbar toolbar) {
        this.toolbar = toolbar;
        super.setSupportActionBar(toolbar);
    }

    protected Toolbar getATHToolbar() {
        return toolbar;
    }

    public static int getToolbarBackgroundColor(Toolbar toolbar) {
        if (toolbar != null) {
            if (toolbar.getBackground() instanceof ColorDrawable) {
                return ((ColorDrawable) toolbar.getBackground()).getColor();
            }
        }
        return 0;
    }
}