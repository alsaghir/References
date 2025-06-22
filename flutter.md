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
- [Widget catalog](https://docs.flutter.dev/development/ui/widgets)
- [Riverpod](https://riverpod.dev/docs/concepts/reading)

## State Management

- Ephemeral state: UI state or local state which is the state you can neatly contain in a single widget. Managed by using `StatefulWidget` and usage of `setState()` method
- App state: Shared state across many parts of the app
- Riverpod
  - StatelessWidget > ConsumerWidget > HookConsumerWidget
  - StatefulWidget > ConsumerStatefulWidget > StatefulHookConsumerWidget
  - State > ConsumerState

### Provider package

- `ChangeNotifier`: Like `Observable`. Gets extended by models which are representing the state. Must call `notifyListeners()` inside any model method that would change the state. This class is easily testable without widgets and without other dependencies than flutter.
- `ChangeNotifierProvider`: is the widget that provides an instance of a `ChangeNotifier` to its descendants. It comes from the `provider` package. `MultiProvider` [can be used for providing multiple models](https://docs.flutter.dev/data-and-backend/state-mgmt/simple#changenotifierprovider). Note that You don’t want to place `ChangeNotifierProvider` higher than necessary to prevent rebuilding unneeded widgets that are irrelative to the state update.
- `Consumer`: Uses the model to consume data from it. Should be as deep as possible and could use the builder third param to get child widgets and reuse them avoiding rebuilding them whenever this consumer consumes new values. `Consumer` is called on every `notifyListeners()` call.

### Riverpod

- `Provider` are used to provide states and states sources and can be read from anywhere in flutter in general like classes or widgets.
- `WidgetRef` used to read providers.
  - `ref.watch()` reads value from provider and subscribe to the provider for any other change.
    - Do not call asynchronously, like inside an onPressed of an ElevatedButton.
    - Nor should it be used inside initState and other State life-cycles. In those cases, consider using `ref.read` instead.
  - `ref.read()` obtaining the value of a provider while ignoring changes. This is useful when we need the value of a provider in an event such as "on click".
    - Should be avoided as much as possible because it is not reactive. It exists for cases where using `watch` or `listen` would cause issues. If you can, it is almost always better to use `watch`/`listen`, especially `watch`.
    - DON'T use `ref.read` inside the `build` method directly.
  - `ref.listen` is similar to `watch` but rather than rebuilding the widget/provider if the listened to provider changes, using `ref.listen` will instead call a custom function.
    - Useful for performing actions when a certain change happens, such as showing a snackbar when an error happens.
    - `ref.listen` method needs 2 positional arguments, the first one is the Provider and the second one is the callback function that we want to execute when the state changes. The callback function when called will be passed 2 values, the value of the previous State and the value of the new State.
    - Should not be called asynchronously, like inside an onPressed of an ElevatedButton. Nor should it be used inside initState and other State life-cycles

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

- For Intellij. Set android sdk path, flutter sdk and install flutter plugin.

## Flutter Commands

```powershell
# Init
flutter create my_app
flutter create --platforms=web my_app
flutter create --platforms=web --project-name=spring_petclinic_ui .
fvm flutter create --platforms="windows,web" spring_petclinic_ui

# Dev run
flutter devices
flutter run -d chrome

# Release run and build
flutter help build
flutter build <target>
flutter build web
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

# Standard
flutter pub add dev:lint dev:custom_lint dev:riverpod_lint hooks_riverpod flutter_hooks dio go_router dev:test dev:mockito meta
flutter pub add 'dev:integration_test:{"sdk":"flutter"}'

# Errors logging
flutter pub add talker_flutter talker_dio_logger talker_riverpod_logger

# URL launch handler
# https://pub.dev/packages/url_launcher
flutter pub add url_launcher

# Look INto
# https://pub.dev/packages/shimmer


# Per usage
flutter pub add stomp_dart_client
flutter pub add yaml
flutter pub add flutter_form_builder
flutter pub add flutter_localizations --sdk=flutter
flutter pub add intl:any
dart pub add dev:json_serializable

# Different endpoints
$env:PUB_HOSTED_URL="https://pub.flutter-io.cn"
$ENV:FLUTTER_STORAGE_BASE_URL="https://storage.flutter-io.cn"

# https://docs.flutter.dev/ui/accessibility-and-internationalization/internationalization
# Generate files in ${FLUTTER_PROJECT}/.dart_tool/flutter_gen/gen_l10n without running the app
flutter gen-l10n

# Code generation
dart run build_runner build --delete-conflicting-outputs

# Watcher for code generations
dart run build_runner watch --delete-conflicting-outputs

# Flutter configurations using FVM
fvm flutter upgrade
fvm flutter config --list
fvm flutter config --enable-web --no-enable-linux-desktop --no-enable-macos-desktop --no-enable-windows-desktop --no-enable-android --no-enable-ios --no-enable-fuchsia
fvm global stable

```

## Internationalization

- `pubspec.yaml` Should contains the following

```yaml
# The following section is specific to Flutter.
flutter:
  generate: true # Add this line
```

- `l10n.yaml` should be in the root folder with the following content

```yaml
arb-dir: lib/infra/l10n # Put the App Resource Bundle (.arb) input files in ${FLUTTER_PROJECT}/lib/infra/l10n. The .arb provide localization resources for your app
template-arb-file: app_en.arb # Set the English template as app_en.arb
output-localization-file: app_localizations.dart # Told Flutter to generate localizations in the app_localizations.dart file
```

- Generate `.arb` files

```powershell
# https://docs.flutter.dev/ui/accessibility-and-internationalization/internationalization
# Generate files in ${FLUTTER_PROJECT}/.dart_tool/flutter_gen/gen_l10n without running the app
flutter gen-l10n
```

- Add `.arb` files as you need with specific local like `app_es.arb`

- Use `AppLocalizations.delegate` and `AppLocalizations.supportedLocale` for `MaterialApp` configurations.

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
  - Some of its common properties related to axis size, axis alignment, direction.
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
- [AnimationController](https://api.flutter.dev/flutter/animation/AnimationController-class.html) & [TickerProvider](https://api.flutter.dev/flutter/scheduler/TickerProvider-class.html) & [CurvedAnimation](https://api.flutter.dev/flutter/animation/CurvedAnimation-class.html) & [Curves](https://api.flutter.dev/flutter/animation/Curves-class.html)
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

- Always wrap `Column` in `SingleChildScrollView` either directly or indirectly. Use `SingleChildScrollView` to fix size of `Card` or `Container` to prevent resizing the window to make a mess of the height.
- Wrap widgets like `TextField` into `Flexible` or `Expanded` to tell `Row` [how much space expected to be taken](https://stackoverflow.com/a/45990477/7054574).
- Beware to [box constraints](https://docs.flutter.dev/development/ui/layout/box-constraints#flex)
- Generate icons from [appicon](https://appicon.co/) and add it to Android path `android/app/src/main/res` and iOS path `ios/Runner/Assets.xcassets/AppIcon.appiconset`
- Stateful widgets lifecycle controlled by `initState`, `build` and `deactivate` methods. - `build` should be used only for layout. While `initState` is usually used for variable initialization.
- `MaterialApp` > `Scaffold` > `SafeArea`.
- Use `Container` for everything like every child in a `Column`.
- Use `SizedBox` to space between elements with `Divider` child if needed.
- Use `Card` and `ListTile` instead of `Container` if possible with parent `Padding` widget.
- Use `Flexible` or `Expanded` as direct child of `Row` to tell `Row` [how much space expected to be taken](https://stackoverflow.com/a/45990477/7054574). `Expanded` will fill as big as possible in vertical/horizontal directions (column/row) and use its `flex` property to determine its percentage sizing relative to other children.
- For using theme [data define a them](https://docs.flutter.dev/cookbook/design/themes#extending-the-parent-theme) or use the default provided one then [use the theme](https://docs.flutter.dev/cookbook/design/themes#using-a-theme) to get its data like `Theme.of(context).colorScheme.secondary`
- Use [ListView](https://api.flutter.dev/flutter/widgets/ListView-class.html) for scrollable list of widgets arranged linearly. Mostly the first row/column in the scaffold. A ListView is basically a `CustomScrollView` with a single SliverList in its `CustomScrollView.slivers` property. An optimization to the combination of `SingleChildScrollView` & `Column` which is another option if you have full column to render at once instead of rendering them dynamically. For dynamic rendering use `Listview.builder()` for optimization.
- Avoid [using `cast` specially for json decoding](https://dart.dev/guides/language/effective-dart/usage#dont-use-cast-when-a-nearby-operation-will-do).
- Use LayoutBuilder for responsive design. If the child should be smaller than the parent, consider wrapping the child in an Align widget. If the child might want to be bigger, consider wrapping it in a SingleChildScrollView or OverflowBox.

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
