# Problem solving

## Binary Search

```java
/**
 * @param array to search inside
 * @param value the target value that we are searching for
 * @param start from left the start index
 * @return either index of found element or -1
 */
private int binarySearch(int[] array, int value, int start){
        int left = start;
        int right = array.length - 1;
        while (left < right) {
            int M = (left + right) / 2;
            if (array[M] < value) {
                left = M + 1;
            } else {
                right = M;
            }
        }
        return (left == right && array[left] == value) ? left : -1;
    }
```