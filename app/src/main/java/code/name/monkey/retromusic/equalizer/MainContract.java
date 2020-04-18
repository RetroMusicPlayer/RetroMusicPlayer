package code.name.monkey.retromusic.equalizer;

import code.name.monkey.retromusic.model.Band;
import code.name.monkey.retromusic.mvp.BaseView;

public interface MainContract {
    interface View extends BaseView {
        void showBandInfo(Band[] bands);

        void showPresetList(String[] presetNames);

        void showBandLevel(short[] levels);
    }

    abstract class Presenter extends BasePresenter<View> {
        abstract void initEqualizer();

        abstract void changePreset(short presetId);

        abstract void changeAudioSession(int audioSession);

        abstract public void effectOnOff(boolean effectOn);
    }
}