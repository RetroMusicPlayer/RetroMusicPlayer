package code.name.monkey.retromusic.views;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import code.name.monkey.retromusic.R;

import static code.name.monkey.retromusic.util.RetroUtil.openUrl;

public class ContributorsView extends FrameLayout {
    public ContributorsView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public ContributorsView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ContributorsView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {
        final TypedArray attributes = context.obtainStyledAttributes(attributeSet, R.styleable.ContributorsView, 0, 0);
        if (attributes != null) {
            final View layout = LayoutInflater.from(context).inflate(R.layout.item_contributor, this);

            NetworkImageView networkImageView = layout.findViewById(R.id.image);
            String url = attributes.getString(R.styleable.ContributorsView_profile_url);
            networkImageView.setImageUrl(url);

            String name = attributes.getString(R.styleable.ContributorsView_profile_name);
            TextView title = layout.findViewById(R.id.title);
            title.setText(name);

            String summary = attributes.getString(R.styleable.ContributorsView_profile_summary);
            TextView text = layout.findViewById(R.id.text);
            text.setText(summary);

            String link = attributes.getString(R.styleable.ContributorsView_profile_link);
            layout.setOnClickListener(v -> {
                if (link == null) {
                    return;
                }
                openUrl((Activity) getContext(), link);
            });
            attributes.recycle();
        }
    }
}
