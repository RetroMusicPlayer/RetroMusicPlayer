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
-dontwarn javax.annotation.**

# RetroFit
-dontwarn retrofit.**
-keep class retrofit.** { *; }

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}

# OkHttp
-keepattributes Signature
-keepattributes *Annotation*
-keep interface com.squareup.okhttp3.** { *; }
-dontwarn com.squareup.okhttp3.**

#-dontwarn
#-ignorewarnings
-dontshrink
-dontobfuscate

-dontwarn org.jaudiotagger.**
-keep class org.jaudiotagger.** { *; }

-keepclassmembers enum * { *; }
-keepattributes *Annotation*, Signature, Exception
-keepnames class androidx.navigation.fragment.NavHostFragment
-keepnames class io.github.muntashirakon.music.model.Home
-keep class * extends androidx.fragment.app.Fragment{}
-keepnames class * extends android.os.Parcelable
-keepnames class * extends java.io.Serializable