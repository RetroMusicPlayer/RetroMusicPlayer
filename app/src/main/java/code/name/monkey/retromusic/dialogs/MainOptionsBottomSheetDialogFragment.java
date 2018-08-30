package code.name.monkey.retromusic.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.ui.activities.MainActivity;

public class MainOptionsBottomSheetDialogFragment extends BottomSheetDialogFragment implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.navigation_view)
    NavigationView navigationView;

    public static MainOptionsBottomSheetDialogFragment newInstance(int home) {

        Bundle args = new Bundle();
        args.putInt("selected", home);
        MainOptionsBottomSheetDialogFragment fragment = new MainOptionsBottomSheetDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_main_options, container, false);
        ButterKnife.bind(this, layout);
        return layout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navigationView.setNavigationItemSelectedListener(this);
        switch (getArguments().getInt("selected")) {
            default:
            case MainActivity.HOME:
                navigationView.setCheckedItem(R.id.action_home);
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_home:
                Toast.makeText(getContext(), menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;
        }
    }
}
