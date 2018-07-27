package code.name.monkey.appthemehelper.common;

import android.support.v7.widget.Toolbar;

import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper;

public class ATHActionBarActivity extends ATHToolbarActivity {

    @Override
    protected Toolbar getATHToolbar() {
        return ToolbarContentTintHelper.getSupportActionBarView(getSupportActionBar());
    }
}
