package code.name.monkey.retromusic

import android.content.Context
import android.content.ContextWrapper
import android.os.LocaleList
import code.name.monkey.appthemehelper.util.VersionUtils.hasLollipop
import code.name.monkey.appthemehelper.util.VersionUtils.hasNougatMR
import java.util.*

class LanguageContextWrapper(base: Context?) : ContextWrapper(base) {

    companion object {
        fun wrap(context: Context?, newLocale: Locale?): LanguageContextWrapper {
            val res = context?.resources
            val configuration = res?.configuration

            when {
                hasNougatMR() -> {
                    configuration?.setLocale(newLocale)
                    val localeList = LocaleList(newLocale)
                    LocaleList.setDefault(localeList)
                    configuration?.setLocales(localeList)
                }
                hasLollipop() -> configuration?.setLocale(newLocale)
                else -> {
                    configuration?.locale = newLocale
                    res?.updateConfiguration(configuration, res.displayMetrics)
                }
            }

            return LanguageContextWrapper(context)
        }
    }
}