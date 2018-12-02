package util

// return a sequence which lazily loops the input list forever
fun <T : Any> infiniteList(xs: List<T>): Sequence<T> {
    var index = 0
    return generateSequence { xs[index++ % xs.size] }
}

// taken from https://youtrack.jetbrains.com/issue/KT-7657 (the issue asking for the very function i hoped was in the core language)
fun <R, T> Sequence<T>.scan(seed: R, transform: (a: R, b: T) -> R): Sequence<R> = object : Sequence<R> {
    override fun iterator(): Iterator<R> = object : Iterator<R> {
        val it = this@scan.iterator()
        var last: R = seed
        var first = true

        override fun next(): R {
            if (first) first = false else last = transform(last, it.next())
            return last
        }

        override fun hasNext(): Boolean = it.hasNext()
    }
}

// useful for day 2 part 1
fun Boolean.toInt(): Int {
    return if (this) 1 else 0
}

// return a lazy sequence containing all possible pairs of two input collections
fun <A, B> lazyAllPossiblePairs(`as`: Collection<A>, bs: Collection<B>): Sequence<Pair<A, B>> {
    return `as`.asSequence().flatMap { a -> bs.asSequence().map { b -> Pair(a, b) } }
}