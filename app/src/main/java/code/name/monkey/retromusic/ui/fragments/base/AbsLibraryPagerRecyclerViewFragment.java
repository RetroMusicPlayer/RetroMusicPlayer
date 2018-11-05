package code.name.monkey.retromusic.ui.fragments.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import code.name.monkey.appthemehelper.ThemeStore;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.helper.MusicPlayerRemote;
import code.name.monkey.retromusic.util.ViewUtil;


public abstract class AbsLibraryPagerRecyclerViewFragment<A extends RecyclerView.Adapter, LM extends RecyclerView.LayoutManager> extends
        AbsLibraryPagerFragment implements OnOffsetChangedListener {

    public static final String TAG = AbsLibraryPagerRecyclerViewFragment.class.getSimpleName();
    @BindView(R.id.container)
    ViewGroup container;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(android.R.id.empty)
    TextView empty;

    private Unbinder unbinder;
    private A adapter;
    private LM layoutManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutRes(), container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getLibraryFragment().addOnAppBarOffsetChangedListener(this);
        initLayoutManager();
        initAdapter();
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        if (recyclerView instanceof FastScrollRecyclerView) {
            //noinspection ConstantConditions
            ViewUtil.setUpFastScrollRecyclerViewColor(getActivity(),
                    ((FastScrollRecyclerView) recyclerView), ThemeStore.accentColor(getActivity()));
        }
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onQueueChanged() {
        super.onQueueChanged();
        checkForPadding();
    }

    @Override
    public void onServiceConnected() {
        super.onServiceConnected();
        checkForPadding();
    }

    private void checkForPadding() {
        int height = (MusicPlayerRemote.getPlayingQueue().isEmpty() ? getResources()
                .getDimensionPixelSize(R.dimen.mini_player_height) : 0);
        recyclerView.setPadding(0, 0, 0, height);
    }

    protected void invalidateLayoutManager() {
        initLayoutManager();
        recyclerView.setLayoutManager(layoutManager);
    }

    protected void invalidateAdapter() {
        initAdapter();
        checkIsEmpty();
        recyclerView.setAdapter(adapter);
    }

    private void initAdapter() {
        adapter = createAdapter();
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                checkIsEmpty();
                checkForPadding();
            }
        });
    }

    private void initLayoutManager() {
        layoutManager = createLayoutManager();
    }

    protected A getAdapter() {
        return adapter;
    }

    protected LM getLayoutManager() {
        return layoutManager;
    }

    protected RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public ViewGroup getContainer() {
        return container;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        container.setPadding(container.getPaddingLeft(), container.getPaddingTop(),
                container.getPaddingRight(), getLibraryFragment().getTotalAppBarScrollingRange() + i);
    }

    private void checkIsEmpty() {
        empty.setText(getEmptyMessage());
        empty.setVisibility(adapter == null || adapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    @StringRes
    protected int getEmptyMessage() {
        return R.string.empty;
    }

    @LayoutRes
    protected int getLayoutRes() {
        return R.layout.fragment_main_activity_recycler_view;
    }

    protected abstract LM createLayoutManager();

    @NonNull
    protected abstract A createAdapter();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getLibraryFragment().removeOnAppBarOffsetChangedListener(this);
        unbinder.unbind();
    }
}
