package code.name.monkey.retromusic;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.LocaleList;

import com.google.android.gms.common.annotation.KeepName;

import java.util.Locale;

import code.name.monkey.appthemehelper.util.VersionUtils;

public class LanguageContextWrapper extends ContextWrapper {

  public LanguageContextWrapper(Context base) {
    super(base);
  }

  @KeepName
  public static LanguageContextWrapper wrap(Context context, Locale newLocale) {
    Resources res = context.getResources();
    Configuration configuration = res.getConfiguration();

    if (VersionUtils.INSTANCE.hasNougatMR()) {
      configuration.setLocale(newLocale);

      LocaleList localeList = new LocaleList(newLocale);
      LocaleList.setDefault(localeList);
      configuration.setLocales(localeList);
    } else {
      configuration.setLocale(newLocale);
    }
    context = context.createConfigurationContext(configuration);
    return new LanguageContextWrapper(context);
  }
}
