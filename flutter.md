# Flutter

## References

- [Dart Cheat Sheet](https://dart.dev/codelabs/dart-cheatsheet)
- [Language Tour](https://dart.dev/guides/language/language-tour)
- [From Java to Dart](https://developers.google.com/codelabs/from-java-to-dart#1)
- [Create Web App using Dart](https://dart.academy/flutter-for-web-a-complete-guide-to-create-run-a-web-application/)
- [Flutter/Dart DevTools](https://docs.flutter.dev/development/tools/devtools/overview)
- [Flutter Test-drive](https://docs.flutter.dev/get-started/test-drive?tab=terminal)
- [Android Emulator Setup](https://docs.flutter.dev/get-started/install/windows#set-up-the-android-emulator)
- [Android command line tools][cmd-tools]
- [SDKManager](https://developer.android.com/studio/command-line/sdkmanager.html#list_installed_and_available_packages)
- [Android Platforms](https://developer.android.com/studio/releases/platforms)
- [Flutter Supported Platforms](https://docs.flutter.dev/development/tools/sdk/release-notes/supported-platforms)
- [Flutter Doctor to run after installation](https://docs.flutter.dev/get-started/install/windows#run-flutter-doctor)
- [Packages Repo for Flutter & Dart](https://pub.dev)
- [Github Awesome Flutter](https://github.com/Solido/awesome-flutter) & [Awesome Flutter](https://flutterawesome.com)

## After download Flutter & Android SDK

Once [command line tools downloaded][cmd-tools]

- Extract Android Command Line Tools into `AndroidSDK/cmdline-tools/latest` then run

```powershell
.\cmdline-tools\latest\bin\sdkmanager.bat --update
# For build-tools probably latest version would be required
.\cmdline-tools\latest\bin\sdkmanager.bat "platform-tools" "platforms;android-29" "build-tools;29.0.3"
.\cmdline-tools\latest\bin\sdkmanager.bat "system-images;android-29;google_apis;x86_64"

# Set android SDK / android studio / intellij idea path
flutter config --android-sdk "D:\Apps\AndroidSDK"
flutter config --android-studio-dir D:\Apps\idea

# Validate config
flutter doctor -v

```

- Change gradle version for Android in `gradle-wrapper.properties` and add the following line in `gradle.properties`

```properties
org.gradle.jvmargs=-Xmx1536M --add-exports=java.base/sun.nio.ch=ALL-UNNAMED --add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.base/java.lang.reflect=ALL-UNNAMED --add-opens=java.base/java.io=ALL-UNNAMED --add-exports=jdk.unsupported/sun.misc=ALL-UNNAMED
```

## Flutter Commands

```powershell
# Dev run
flutter devices
flutter run -d chrome

# Release run and build
flutter help build
flutter build <target>
flutter run --release

# Retrieve dependencies
flutter packages get

# Update flutter
flutter upgrade

# pub package manager
# https://dart.dev/tools/pub/cmd

# Update to the latest compatible versions of all the dependencies listed in the pubspec.yaml file
flutter pub upgrade

# Identify out-of-date package dependencies and get advice on how to update them
flutter pub outdated
```

## Flutter Common Classes & functions

- [Material Library](https://api.flutter.dev/flutter/material/material-library.html)
- [MaterialApp](https://api.flutter.dev/flutter/material/MaterialApp-class.html)
- [StatelessWidget](https://api.flutter.dev/flutter/widgets/StatelessWidget-class.html)
- [StatefulWidget](https://api.flutter.dev/flutter/widgets/StatefulWidget-class.html)
- [Column](https://api.flutter.dev/flutter/widgets/Column-class.html) & [Row](https://api.flutter.dev/flutter/widgets/Row-class.html)
- [Expanded](https://api.flutter.dev/flutter/widgets/Expanded-class.html)
- [Container](https://api.flutter.dev/flutter/widgets/Container-class.html)
- [compute()](https://api.flutter.dev/flutter/foundation/compute-constant.html) to run in a background [isolate](https://api.flutter.dev/flutter/dart-isolate/Isolate-class.html)

## Rule of thumb

- `build` should be used only for layout. While `initState` is usually used for variable initialization

## Dart

```powershell
# Create dart project and run it
dart create my_cli
dart run

# pub package manager
# https://dart.dev/tools/pub/cmd

# pub package manager
# Identify out-of-date package dependencies and get advice on how to update them
dart pub outdated
```

### Asynchronous programming: futures, async, await

- Synchronous operation: A synchronous operation blocks other operations from executing until it completes.
- Synchronous function: A synchronous function only performs synchronous operations.
- Asynchronous operation: Once initiated, an asynchronous operation allows other operations to execute before it completes.
- Asynchronous function: An asynchronous function performs at least one asynchronous operation and can also perform synchronous operations.
- Return type should be `Future<void>` or `Future<T>`

#### async and await
- To define an async function, add async before the function body
- The await keyword works only in async functions.
- You can use the await keyword to wait for a future to complete

  ```Dart
  print(await createOrderMessage());
  ```
- The `await` keyword only works within an `async` function.

## Android & IOS

### Permissions

For location permissions as example

in `ios/Runner/Info.plist`

```xml
<!-- for iOS 11 + -->
<key>NSLocationWhenInUseUsageDescription</key>
<string>Reason why app needs location</string>
<key>NSLocationAlwaysAndWhenInUseUsageDescription</key>
<string>Reason why app needs location</string>

<!-- additionally for iOS 9/10, if you need always permission -->
<key>NSLocationAlwaysUsageDescription</key>
<string>Reason why app needs location</string>
```

in `android/app/src/main/AndroidManifest.xml`

```xml
<!-- for iOS 11 + -->
<manifest xmlns:android="http://schemas.android.com/apk/res/android">
  <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
  <!-- or -->
  <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
</manifest>
```

[cmd-tools]: https://developer.android.com/studio#cmdline-tools

