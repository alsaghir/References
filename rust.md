# Rust

## Tips for other language

- `match` is used like `switch` in other languages.
- Data types are `Scalar` and `Compound`. Scalar types include integers, floating-point numbers, Booleans, and characters. Compound types include tuples and arrays. Access elements in a tuple with a dot and an index, e.g., `tuple.0`. Access elements in an array with square brackets, e.g., `array[0]`.
- `&str` is a string slice, which is a reference to a string. `String` is a growable, heap-allocated data structure. Use `String::from("text")` or `to_string()` to create a `String` from a string literal.
- i32 and similar types of the language are copied on reassignment because they implement the `Copy` trait and saved on the stack, while types like `String` are moved on reassignment because they do not implement the `Copy` trait and saved on the heap.
- `trait` is similar to an interface in other languages. It defines shared behavior that types can implement. Use `impl TraitName for TypeName` to implement a trait for a type.
- `&` / `mut`
  - Reference is safe bet. Use `&` to create immutable reference to a value, and `&mut` to create a mutable reference. References allow you to **borrow** values without taking ownership. Note that the operator `&` on a value used to create a reference but same operator on a type in function signature is used to indicate that the function takes a reference to that type.
  - `&mut` is used to create a mutable reference to a value. Mutable references allow you to modify the value they point to. However, you can only have **one mutable reference** to a value at a time, and you **cannot** have mutable and immutable references to the same value simultaneously.
  - `mut` on a variable always means "I can reassign this variable.". Now the content or value that it points to can be modified or not is another matter.
    - If the variable directly owns data (not a reference), it also means the data's contents can be modified. And this is a side effect like `String` or `Vec` because it unlocks calling `&mut self` methods on it.
    - If the variable is a reference, `mut` on the variable tells you nothing about whether you can modify what it points to — that's determined separately by whether the reference type is `&T` or `&mut` T.
    - In a pattern, `&` works backwards from an expression. In an expression, `&x` means "make a reference to `x`". In a pattern, `&x` means "this value IS a reference, so unpack it and bind the value it points to to `x`". In a pattern, `&mut` works similarly, but for mutable references.
    - `*` is the dereference operator. Given a reference, it gives you the value it points to. It's the expression-side counterpart of the `&` pattern.
- The rule is, at any given time, you can have either **one mutable reference** or **any number of immutable references**.
- `{}` and `{:?}` invoke two different formatting traits, and which one works depends on what the type implements. `{:#?}` is "pretty debug".
- Error handling
  - When have a `Result<T, E>` where `T` matters and you want the value with a computed fallback on error then reach for `unwrap_or_else`.
  - When have a `Result<(), E>` (or any case where you only care about the `Err` branch) and you want to just react to failure, nothing else then reach for `if let Err(e) = ...`
  - When have either branch, doing meaningfully different things, values on both sides and you want full control over both arms then reach for `match`.
  - `panic!` is used to cause a panic, which will terminate the program and print an error message. Use it for unrecoverable errors. Can be used for unimplemented functions.
- Lifetimes or lifetimes specifiers are a way of describing the scope for which a reference is valid. They are used to prevent dangling references and ensure memory safety. In Rust, every reference has a lifetime, which is the scope for which that reference is valid. Lifetimes are denoted using an apostrophe followed by a name, e.g., `'a`. When you see `&'static str`, it means that the string slice has a static lifetime, meaning it is valid for the entire duration of the program.
- Smart pointers are data structures that act like pointers but have additional metadata and capabilities. They are used to manage memory and resources in a more controlled way. Common smart pointers.
  - `Box<T>` - used for heap allocation
  - `Rc<T>` - reference-counted pointer for shared ownership. Safe in single-threaded scenarios. For multi-threaded scenarios, use `Arc<T>`. No mutation allowed but can be used with `RefCell<T>` to allow mutation.
  - `RefCell<T>` - allows for interior mutability, enabling mutation of data even when there are immutable references to it.
  - `Weak<T>` - a non-owning reference to a value managed by `Rc<T>`. It does not contribute to the reference count and can be used to break reference cycles.
- `Deref` and `DerefMut` traits allow smart pointers to behave like regular references. Implementing these traits allows you to use the `*` operator to dereference a smart pointer and access the value it points to. This enables you to call methods on the underlying value as if you were working with a regular reference. Use `Drop` trait to specify what should happen when a value is dropped, such as releasing resources or performing cleanup tasks.
- Closures are anonymous functions in syntax `|parameters| expression`.
  - They can capture variables from their surrounding environment.
  - Use `move` keyword to force the closure to take ownership of the captured variables like this: `move |parameters| expression`.
  - Closures can be used as function arguments, return values, or stored in variables.
  - Types of closures:
    - `Fn` - captures variables by reference and can be called multiple times.
    - `FnMut` - captures variables by mutable reference and can be called multiple times.
    - `FnOnce` - captures variables by value and can only be called once.
- Concurrency:
  - Use the `std::thread` module to create and manage threads like using `std::thread::spawn()` which returns a `JoinHandle` that can be used to wait for the thread to complete.
  - Use channels (`std::sync::mpsc`) for message passing between threads.
  - Use `Arc<T>` for shared ownership of data across threads with immutable data. Wrapping the data in `Arc<T>` then use `Arc::clone()` passing reference to data with `&` to create multiple references to the same data across threads.
  - Use `Mutex<T>` wrapped in `Arc<T>` for safe concurrent access to shared mutable data. Use `lock()` method on `Mutex<T>` returned from `Arc::clone()` to acquire a lock and access the data safely. Similarly, use `RwLock<T>` for read-write locks, allowing multiple readers or one writer at a time.
  - For ownership to be moved into thread safely, the data must implement the `Send` trait. For shared references across threads, the data must implement the `Sync` trait. std lib implement these traits.
  - `Semaphore` is a synchronization primitive that can be used to control access to a shared resource by multiple threads. It maintains a count of available resources and allows threads to acquire or release resources in a controlled manner. It is same as `Mutex` but with a count of available resources instead of a single lock. Use `tokio::sync::Semaphore` or `async_std::sync::Semaphore` for async semaphore support.
  - Async programming is using tasks instead of threads directly. It is suitable for io bound operations while threads are better for cpu bound operations. Async needs reactor/event-loop, scheduler and executor. Use the `tokio` or `async-std` crates for async runtime support.
    - `async fn` returns a `Future` while tasks execute `Future`.
    - Use `await` to wait for a `Future` to complete and get its result.
    - Use `tokio::spawn()` or `async_std::task::spawn()` to spawn an async task.
- Package is set of crates. It's managed by `Cargo.toml` file.
  - Can contain at most one `lib` crate and any number of `bin` crates.
  - `lib` crate is a library crate that can be used by other crates. It is defined in `src/lib.rs` file.
  - `workspace` is a set of packages that share the same `Cargo.lock` and output directory. It is defined in `Cargo.toml` file with `[workspace]` section.
  - `module` is a way to organize code into separate namespaces. It is defined using the `mod` keyword and can be declared in the same file or in separate files. Use `pub` keyword to make a module or its items public and accessible from other modules. i.e. when using `mod components` in `src/lib.rs` file, it will look for `src/components.rs` file or `src/components/mod.rs` file.
    - `mod` declares a module
    - `use` brings a module or its items into scope
    - `pub use` re-exports a module or its items to make them accessible from other modules
  - Features are a way to conditionally compile code based on the specified configuration. They are defined in `Cargo.toml` file with `[features]` section. Use `#[cfg(feature = "feature_name")]` attribute in rust code to conditionally compile code based on the specified feature.
    - To enable a feature for a dependency in `Cargo.toml`, use the following syntax:

      ```toml
      [dependencies]
      dependency_name = { version = "x.y.z", features = ["feature_name"] }
      ```

    - To enable a feature for your own crate, use the following syntax:

      ```toml
      [dependencies]
      dioxus = { workspace = true, features = ["router"] }
      ```

      enables Dioxus’s `router` feature whenever this dependency is used. 

    - Example

      ```toml
      default = ["web"]       # Enable our package's "web" feature by default.
      web = ["dioxus/web"]    # When our "web" feature is enabled, also enable Dioxus's "web" feature.
      ```

- Cargo’s built-in build profiles
  - A **profile controls how code is compiled**—optimisation, debug information, overflow checks, etc. **Features control what capabilities/code are enabled.** Separate concepts
  - dev - normally used by `cargo build`, `cargo run` & `cargo check`. Default behavior is fast compilation, no optimization, debug information and checks enabled.
  - release - normally used by `cargo build --release` & `cargo run --release`. Default behavior is slower compilation, optimizations enabled, no debug information and checks disabled.
  - test - normally used by `cargo test`. Inherits dev settings.
  - bench - normally used by `cargo bench`. Inherits release settings.


- Common attributes
  - `#[derive(...)]` - automatically implements common traits for a type.
    - `Copy` says "this is a plain value like a number; duplicating it is free and nobody needs to track copies.". Types like `i32` and `bool` implement `Copy`. Types like `String` and `Vec` do not implement `Copy` because they manage heap memory and duplicating them requires a deep copy.
    - `Clone` says "this is a value that can be duplicated, but duplicating it may be expensive and the caller should be aware of that." Types like `String` and `Vec` implement `Clone`. Types like `i32` and `bool` also implement `Clone`, but their implementation is trivial.
      - The mental model for ownership is
        - `&T` borrow the original; ownership stays with the caller.
        - `T` with `Copy` implicitly duplicate; pass ownership of the copy.
        - `T` otherwise move ownership of the original.
        - `t.clone()` explicitly duplicate; the result can be passed independently.
    - `PartialEq` & `Eq` derived means "equal if every field is equal."
    - `PartialOrd` & `Ord` derived on an enum orders variants by declaration order. For Status, NotStarted < Started < InProgress < Finished might be meaningful. But if someone later reorders the variants for readability, comparisons silently change behavior.
    - `Default` derived means "there's a sensible empty or starting value". 
  - `#[cfg(...)]` - conditionally compiles code based on the specified configuration.
    - `#[cfg(not(target_arch = "wasm32"))]` for example, compiles the annotated code only if the target architecture is not `wasm32`.
  - `#[allow(...)]` - suppresses specific compiler warnings for the annotated item.
  - `#[deny(...)]` - treats specific compiler warnings as errors for the annotated item.
  - `#[inline]` - suggests to the compiler to inline the annotated function or method.
  - `#[test]` - marks a function as a test case to be run by the test harness.
  - `#[bench]` - marks a function as a benchmark to be run by the benchmark harness.

### Code example

```rust

```

## Remember

- For Domain Types, use `derive` attributes to automatically implement common traits:
  - `Debug` almost always. It's free, harmless, and needed for assert_eq! and logging.
  - `Clone` usually, when the type is plain data.
  - `Serialize`/`Deserialize` when the type crosses a boundary (JSON, storage, network).
  - Everything else (`Copy`, `Eq`, `Hash`, `Ord`, `Default`) only when the derived meaning matches your domain's meaning and something needs it now.
- Use `fn whatever(whatever: impl Into<String>)` for functions that accept a string-like argument. It allows the caller to pass in a `String`, `&str`, or any other type that can be converted into a `String`. This makes the function more flexible and easier to use.

## Packages Well Known

- [serde](https://crates.io/crates/serde) - Serialization framework for Rust.
- [thiserror](https://crates.io/crates/thiserror) - A library for deriving error types in Rust.
- [chrono](https://crates.io/crates/chrono) - A date and time library for Rust.
- [ulid](https://crates.io/crates/ulid) - A library for generating ULIDs (Universally Unique Lexicographically Sortable Identifiers) in Rust.
- [futures](https://crates.io/crates/futures) - Async utilities around the core Future trait, including combinators, executors, Stream/sink utility methods and more.
- [tokio](https://crates.io/crates/tokio) - An asynchronous runtime for Rust, providing utilities for async programming, including an event loop, task scheduling, and I/O. In a browser, APIs and event loop already provided and tokio is not needed.
- `wasm-bindgen-futures` - A library for working with JavaScript Promises and Rust Futures in WebAssembly. It provides utilities for converting between the two and allows you to write async code in Rust that can interoperate with JavaScript. Not needed in native Rust.On web, **the browser already supplies the networking and event loop**. `wasm-bindgen-futures` integrates Rust futures with that environment.
- [tracing](https://crates.io/crates/tracing) - A framework for instrumenting Rust programs to collect structured, event-based diagnostic information. Logging too.
- [getrandom](https://crates.io/crates/getrandom) - A library for generating random numbers in Rust, providing a cross-platform interface to the underlying operating system's random number generator. Example is below for wasm32 target. `[target.'cfg(target_arch = "wasm32")'.dependencies]` means that the following dependencies are only included when compiling for the `wasm32` target architecture. The `getrandom` crate is included as a dependency with the `wasm_js` feature enabled, which allows it to use the JavaScript `crypto.getRandomValues()` API for generating random numbers in WebAssembly environments.
- `reqwest` - A high-level HTTP client library for Rust, built on top of the `hyper` crate. It provides a convenient API for making HTTP requests and handling responses, including support for async programming, JSON serialization/deserialization, and more.
- `gloo` - A set of libraries for building web applications in Rust, providing utilities for interacting with the browser, including DOM manipulation, event handling, and more.
- `gloo-timers` - A library for working with timers in Rust, providing utilities for creating and managing timeouts, intervals, and delays in web applications.
- `tokio-tungstenite` - A WebSocket library for Rust, built on top of the `tokio` async runtime and the `tungstenite` WebSocket library. Not for web.
- `gloo-net` - Websocket library for Rust, built on top of the `gloo` library and the `web-sys` crate. Not for native Rust.
- `async-trait` - A library that allows you to define async functions in traits, enabling you to write async code in a more ergonomic way. It provides a procedural macro that generates the necessary boilerplate code for async trait methods, allowing you to use async/await syntax in trait definitions and implementations.
  - Example is `#[async_trait(?Send)]`. `?Send` part prevent macro from adding `Send` bound to the generated future. That is useful when the future returned need not be transferable between threads. `async_trait` attribute macro itself rewrites the annotated trait or implementation to use a boxed future type, which allows the async methods to be called without requiring the caller to know the concrete future type. This is particularly useful when working with trait objects or when you want to hide the implementation details of an async method behind a trait. In other words, it simplifies the usage of async methods in traits by providing a way to define and call them without exposing the underlying future type.
