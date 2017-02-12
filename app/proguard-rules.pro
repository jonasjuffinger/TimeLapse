# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in Android/Sdk/tools/proguard/proguard-android.txt
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
-keep class net.jpountz.** { *; }
-keep class sun.misc.** { *; }
-keep class org.apache.** { *; }
-dontwarn sun.misc.**
-dontwarn javax.annotation.**
-dontwarn javax.inject.**
-dontwarn org.apache.**

# support design library
-dontwarn android.support.design.**
-keep class android.support.design.** { *; }
-keep interface android.support.design.** { *; }
-keep public class android.support.design.R$* { *; }

# v7 support library
-keep public class android.support.v7.widget.** { *; }
-keep public class android.support.v7.internal.widget.** { *; }
-keep public class android.support.v7.internal.view.menu.** { *; }

-keep public class * extends android.support.v4.view.ActionProvider {
    public <init>(android.content.Context);
}

-printmapping mapping.txt
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable

-optimizationpasses 5

-repackageclasses 'fb'
-allowaccessmodification

-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}