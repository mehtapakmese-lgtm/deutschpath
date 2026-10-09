# DeutschPath Android app project

This is an Android Studio project that wraps the A1/A2 DeutschPath prototype in an Android WebView. The HTML, CSS, mission data and application code are bundled in the app, so the learning interface can run without a web server.

## Current functionality and limits

- Android launcher app named DeutschPath.
- A1/A2 mission interface and existing learning/progress screens bundled in the app.
- On-device account profiles (email + password hash) and progress stored in WebView local storage.
- Progress stays on this device. It is not cloud-synced and cannot automatically move between phones.
- AI assessment and true server-backed accounts are not enabled in this APK shell. They require a deployed HTTPS backend and API configuration; never embed an OpenAI API key in the Android app.
- This is a development prototype, not a public-release-ready app. Add privacy review, secure cloud authentication, account recovery, backups, accessibility checks and device testing before publishing.

## Build the APK with Android Studio

1. Install Android Studio and its recommended Android SDK (compile SDK 35).
2. Open this folder (`deutschpath-android`) as an existing project.
3. Allow Gradle to sync and download the Android Gradle Plugin and AndroidX WebKit dependency.
4. Use **Build → Build Bundle(s) / APK(s) → Build APK(s)**.
5. Android Studio will show the generated APK location, usually `app/build/outputs/apk/debug/app-debug.apk`.
6. Transfer that APK to your Android phone and allow installation from that source when prompted.

## Requirements

- Android Studio with a compatible JDK (Android Studio's bundled JDK is recommended).
- Internet during the first Gradle sync to download build dependencies.

The project source is provided here; this environment did not have the Android SDK/Gradle build toolchain, so a compiled APK could not be generated or verified in this session.
