# Flutter

## References

- [Android SDK Environment Variables](https://developer.android.com/studio/command-line/variables)
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
- [HTTP networking](https://docs.flutter.dev/development/data-and-backend/networking)
- [How to do it in flutter](https://howtodothisinflutter.com/)
- [Flutter Cheat Sheet](https://github.com/Temidtech/Flutter-Cheat-Sheet) & [Dart Cheat Sheet](https://github.com/Temidtech/dart-cheat-sheet)
- [Widget catalog](https://docs.flutter.dev/development/ui/widgets)
- [Flutter Layout Cheat Sheet](https://medium.com/flutter-community/flutter-layout-cheat-sheet-5363348d037e)
- [Navigation Cookbook](https://docs.flutter.dev/cookbook/navigation) & [Navigator](https://api.flutter.dev/flutter/widgets/Navigator-class.html) & [ModalRoute.of()](https://docs.flutter.dev/cookbook/navigation/navigate-with-arguments#2-create-a-widget-that-extracts-the-arguments)

## After download Flutter & Android SDK

Once [command line tools downloaded][cmd-tools]

- Set environment variables `ANDROID_HOME` pointing to the sdk folder. `ANDROID_USER_HOME` & `ANDROID_SDK_HOME` pointing to user folder for configurations.
- Extract Android Command Line Tools into `AndroidSDK/cmdline-tools/latest` then run

```powershell
.\cmdline-tools\latest\bin\sdkmanager.bat --update
# For build-tools probably latest version would be required
.\cmdline-tools\latest\bin\sdkmanager.bat "platform-tools" "platforms;android-30" "build-tools;30.0.3"
.\cmdline-tools\latest\bin\sdkmanager.bat "system-images;android-30;google_apis;x86_64"

# Set android SDK / android studio / intellij idea path
flutter config --android-sdk "D:\Apps\AndroidSDK"
flutter config --android-studio-dir D:\Apps\idea

# Validate config
flutter doctor -v

# Some additional commands
.\cmdline-tools\latest\bin\sdkmanager.bat --list | Select-String -Pattern "system-images;android-30"


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
- [Scaffold](https://api.flutter.dev/flutter/material/Scaffold-class.html)
  - Implements the basic material design visual layout structure.
  - This class provides APIs for showing drawers and bottom sheets.
- [Container](https://api.flutter.dev/flutter/widgets/Container-class.html)
  - Similar to HTML `div` tag.
  - Single child layout Widget
  - Containers with no children try to be as big as possible unless the incoming constraints are unbounded, in which case they try to be as small as possible. Containers with children size themselves to their children. The width, height, and constraints arguments to the constructor override this.
- [Container vs Scaffold](https://stackoverflow.com/a/59243045/7054574)
- [StatelessWidget](https://api.flutter.dev/flutter/widgets/StatelessWidget-class.html)
- [StatefulWidget](https://api.flutter.dev/flutter/widgets/StatefulWidget-class.html)
- [Column](https://api.flutter.dev/flutter/widgets/Column-class.html) & [Row](https://api.flutter.dev/flutter/widgets/Row-class.html)
  - [Usage with SingleChildScrollView](https://www.youtube.com/watch?v=neAn35cY8y0)
  - Multi-children layout widgets
  - Usually used to make row or column scrollable
  - Some of its common properties related to axis size, axis alignment, direction, 
- [Expanded](https://api.flutter.dev/flutter/widgets/Expanded-class.html)
  - A widget that expands a child of a Row, Column, or Flex so that the child fills the available space.
  - Use `flex` factor to determine the width to occupy
- [SizedBox](https://api.flutter.dev/flutter/widgets/SizedBox-class.html) - Which is a box with a specified size.
- [ListView](https://api.flutter.dev/flutter/widgets/ListView-class.html)
  - Multi-child layout widget
  - Scrollable list of widgets arranged linearly
  - A ListView is basically a `CustomScrollView` with a single SliverList in its `CustomScrollView.slivers` property.
  - An optimization to the combination of `SingleChildScrollView` & `Column`.
  - `Listview.builder()` could be used for optimization.
  - Different than [SingleChildScrollView](https://stackoverflow.com/questions/62146197/what-is-the-difference-between-listview-and-singlechildscrollview-in-flutter)

- [compute()](https://api.flutter.dev/flutter/foundation/compute-constant.html) to run in a background [isolate](https://api.flutter.dev/flutter/dart-isolate/Isolate-class.html)
- [GestureDetector](https://api.flutter.dev/flutter/widgets/GestureDetector-class.html)
- [AnimationController ](https://api.flutter.dev/flutter/animation/AnimationController-class.html) & [TickerProvider](https://api.flutter.dev/flutter/scheduler/TickerProvider-class.html) & [CurvedAnimation](https://api.flutter.dev/flutter/animation/CurvedAnimation-class.html) & [Curves](https://api.flutter.dev/flutter/animation/Curves-class.html)
- [Platform](https://api.flutter.dev/flutter/dart-io/Platform-class.html) - Information about the environment in which the current program is running.

- [CircleAvatar](https://api.flutter.dev/flutter/material/CircleAvatar-class.html)
- [Text](https://api.flutter.dev/flutter/widgets/Text-class.html)
- [EdgeInsets](https://api.flutter.dev/flutter/painting/EdgeInsets-class.html) for Padding and Margin
- [Card](https://api.flutter.dev/flutter/material/Card-class.html) - Alternative to `Container` and used with `Padding` usually
- [Padding](https://api.flutter.dev/flutter/widgets/Padding-class.html) - used around `Card` frequently
- [ListTile](https://api.flutter.dev/flutter/material/ListTile-class.html) - icon and text row
- [Divider](https://api.flutter.dev/flutter/material/Divider-class.html) - horizontal line
- [TextField](https://api.flutter.dev/flutter/material/TextField-class.html) & [InputDecoration](https://api.flutter.dev/flutter/material/InputDecoration-class.html) - for text input
- [DropdownButton](https://api.flutter.dev/flutter/material/DropdownButton-class.html) & [CupertinoPicker](https://api.flutter.dev/flutter/cupertino/CupertinoPicker-class.html)

## Rule of thumb

- `build` should be used only for layout. While `initState` is usually used for variable initialization
- Use `Flexible` or `Expanded` to tell `Row` [how much space expected to be taken](https://stackoverflow.com/a/45990477/7054574).
- Beware to [box constraints](https://docs.flutter.dev/development/ui/layout/box-constraints#flex)

### Notes

- Stateful widgets lifecycle controlled by `initState`, `build` and `deactivate` methods

## Dart

```powershell
# Create dart project and run it
dart create my_cli
dart run
# or
# dart run .\bin\my_cli.dart

# Compile for production
dart compile exe bin/cli.dart

# pub package manager
# https://dart.dev/tools/pub/cmd

# pub package manager
# Identify out-of-date package dependencies and get advice on how to update them
dart pub outdated

# Add package called 'args' by command in pubspec.yaml
dart pub add args
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

### Notes

```Dart
// import as for prefix
import 'package:flutter/material.dart' as m;

// import only specific class
import 'package:flutter/material.dart' show runApp;

// Each ?. corresponds to a unique
// path that can cause null to flow into the method chain
thing?.doohickey?.gizmo

// Null-aware cascade:
receiver?..method()..anotherMethod();

// Null-aware index operator:
receiver?[index];

// Exception catching
try {
  breedMoreLlamas();
} on OutOfLlamasException {
  // A specific exception
  buyMoreLlamas();
} on Exception catch (e) {
  // Anything else that is an exception
  print('Unknown exception: $e');
} catch (e, s) { // s is the StackTrace object
  // No specified type, handles all
  print('Something really unknown: $e');
  print('Stack trace:\n $s');
  rethrow; // Allow callers to see the exception.
} finally {
  // Always clean up, even if an exception is thrown.
  cleanLlamaStalls();
}
```

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

