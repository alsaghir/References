# Cracking the Coding Interview

## Big O

- When you have a recursive function that makes multiple calls, the runtime will
often (but not always) look like 0(branchesd^Pth), where branches is the number of times each recursive
call branches. In this case, this gives us 0( 2N).
- Memoization is a very common technique to optimize exponential time recursive algorithms. E.g. passing an array to cache result of recursive call in static array.
- Perspective to look at 0(Log N). If N goes from P to P+l, the number of calls to powersOfTwo might not change at all. When will the number of calls to powersOfTwo increase? It will increase by 1 each time n doubles in size. So, each time n doubles, the number of calls to powersOfTwo increases by 1. Therefore, the number of calls to powersOfTwo is the number of times you can double 1 until you get n. It is x in the equation 2x = n. What is x? The value of xis log n. This is exactly what meant by x log n.