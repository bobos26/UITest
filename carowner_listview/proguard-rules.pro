# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/a1000990/work/devel/adt-bundle-mac-x86_64-20140702/sdk/tools/proguard/proguard-android.txt
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

# SettingBase
-keep class * extends com.skp.project4.trunk.model.SettingBase {
    private <fields>;
    public <fields>;
}
-keep class com.skp.project4.trunk.model.SettingBase {
    private <fields>;
    public <fields>;
}

# ActiveAndroid
-keep class com.activeandroid.** { *; }
-keep class com.activeandroid.**.** { *; }
-keep class * extends com.activeandroid.Model
-keep class * extends com.activeandroid.serializer.TypeSerializer
-keep interface com.activeandroid.** { *; }

# volley
-keep class com.android.volley.** { *; }
-keep interface com.android.volley.** { *; }

# http://developer.android.com/google/play-services/setup.html
-keep class * extends java.util.ListResourceBundle {
    protected java.lang.Object[][] getContents();
}
-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
    public static final *** NULL;
}
-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
    @com.google.android.gms.common.annotation.KeepName *;
}
-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

## general keep
-keepattributes *Annotation*
-keepattributes Signature

###################################################################################################
# 3rd party

# crash logger
-keeppackagenames com.skp.crashlogger.**
-keepclasseswithmembers public class com.skp.crashlogger.* {*; }
-keep interface com.skp.crashlogger.* {*; }

# com.skp.Tmap_1.0.25.jar
-keep class com.skp.Tmap.** { *; }
-keep interface com.skp.Tmap.** { *; }

# OCBLogSentinelShuttle-0.1.13_10.15.jar
-keep class com.skplanet.pdp.sentinel.shuttle.** { *; }