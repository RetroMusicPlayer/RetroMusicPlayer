# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/hemanths/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-dontwarn java.lang.invoke.*
-dontwarn **$$Lambda$*

# RetroFit
-dontwarn retrofit.**
-keep class retrofit.** { *; }
-keepattributes Signature
-keepattributes Exceptions
-dontwarn javax.annotation.**

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}

-keep class !android.support.v7.internal.view.menu.**,** {*;}

-dontwarn
-ignorewarnings

# ------- FastScrollRecycleView START -------
-keep class com.simplecityapps.recyclerview_fastscroll.views.FastScrollPopup { *; }
# ------- FastScrollRecycleView END -------

-keep public class android.support.design.widget.BottomNavigationView { *; }
-keep public class android.support.design.internal.BottomNavigationMenuView { *; }
-keep public class android.support.design.internal.BottomNavigationPresenter { *; }
-keep public class android.support.design.internal.BottomNavigationItemView { *; }

#-dontwarn android.support.v8.renderscript.*
#-keepclassmembers class android.support.v8.renderscript.RenderScript {
#  native *** rsn*(...);
#  native *** n*(...);
#}

#-keep class org.jaudiotagger.** { *; }

#For cast
-keep class code.name.monkey.retromusic.cast.CastOptionsProvider { *; }
-keep class android.support.** { *; }
-keep class com.google.** { *; }
-keep class java.nio.file.** { *; }