package util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking

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

// useful for day 5 part 1
fun Char.sameLetterDifferentCase(that: Char): Boolean {
    return this.equals(that, true) && !this.equals(that, false)
}

// parallel map with kotlin coroutines
fun <A, B> Collection<A>.parallelMap(mapper: (A) -> B): Collection<B> {
    return runBlocking {
        map { async(Dispatchers.IO) { mapper(it) } }.awaitAll()
    }
}

// useful for day 7 parsing steps
fun <T> MutableCollection<T>.addAndGet(t: T): T {
    this.add(t)
    return t
}

//
fun <A, B> cartesianProduct(`as`: Iterable<A>, bs: Iterable<B>): List<Pair<A, B>> {
    return `as`.flatMap { a -> bs.map { b -> Pair(a, b) } }
}
