package github.mrh0.eclang.util

class StackMap<K, V> {
    private val stack = mutableListOf<MutableMap<K, V>>()

    init {
        stack.add(mutableMapOf())
    }

    fun add(key: K, value: V) {
        stack.last()[key] = value
    }

    fun push() {
        stack.add(mutableMapOf())
    }

    fun pop() {
        if (stack.size > 1) {
            stack.removeAt(stack.size - 1)
        } else {
            throw IllegalStateException("Cannot pop the last scope")
        }
    }

    fun get(key: K): V? {
        for (i in stack.size - 1 downTo 0) {
            if (stack[i].containsKey(key)) {
                return stack[i][key]
            }
        }
        return null
    }
}