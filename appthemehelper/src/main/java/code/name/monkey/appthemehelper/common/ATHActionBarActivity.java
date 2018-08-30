package code.name.monkey.appthemehelper.common;

import androidx.appcompat.widget.Toolbar;

import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper;

public class ATHActionBarActivity extends ATHToolbarActivity {

    @Override
    protected Toolbar getATHToolbar() {
        return ToolbarContentTintHelper.getSupportActionBarView(getSupportActionBar());
    }
}
