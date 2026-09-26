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

### Code example

```rust

```

## Questions

