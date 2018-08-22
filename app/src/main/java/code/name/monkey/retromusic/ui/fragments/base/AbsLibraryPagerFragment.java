package code.name.monkey.retromusic.ui.fragments.base;

import android.os.Bundle;

import code.name.monkey.retromusic.ui.fragments.mainactivity.LibraryFragment;

public class AbsLibraryPagerFragment extends AbsMusicServiceFragment {


    public LibraryFragment getLibraryFragment() {
        return (LibraryFragment) getParentFragment();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }
}
