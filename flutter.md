# Flutter

## References

- [Dart Cheat Sheet](https://dart.dev/codelabs/dart-cheatsheet)
- [Language Tour](https://dart.dev/guides/language/language-tour)
- [From Java to Dart](https://developers.google.com/codelabs/from-java-to-dart#1)
- [Create Web App using Dart](https://dart.academy/flutter-for-web-a-complete-guide-to-create-run-a-web-application/)
- [Flutter/Dart DevTools](https://docs.flutter.dev/development/tools/devtools/overview)
- [Flutter Test-drive](https://docs.flutter.dev/get-started/test-drive?tab=terminal)
- [Android Emulator Setup](https://docs.flutter.dev/get-started/install/windows#set-up-the-android-emulator)
- <a name="cmd-tools"></a>[Android command line tools](https://developer.android.com/studio#cmdline-tools)
- [SDKManager](https://developer.android.com/studio/command-line/sdkmanager.html#list_installed_and_available_packages)
- [Android Platforms](https://developer.android.com/studio/releases/platforms)
- [Flutter Supported Platforms](https://docs.flutter.dev/development/tools/sdk/release-notes/supported-platforms)
- [Flutter Doctor to run after installation](https://docs.flutter.dev/get-started/install/windows#run-flutter-doctor)

## After download Flutter & Android SDK

Once [command line tools downloaded](#cmd-tools)

- Extract Android Command Line Tools into `AndroidSDK/cmdline-tools/latest` then run

```powershell
.\cmdline-tools\latest\bin\sdkmanager.bat --update
.\cmdline-tools\latest\bin\sdkmanager.bat "platform-tools" "platforms;android-29" "build-tools;29.0.3"
```