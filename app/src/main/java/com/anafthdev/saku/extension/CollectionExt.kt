package com.anafthdev.saku.extension

fun <T> Collection<T>.to2DArray(len: Int): List<List<T>> {
	val result = arrayListOf<ArrayList<T>>()
	
	var counter = 1
	var value = arrayListOf<T>()
	forEach { t ->
		if (counter == len) {
			counter = 1
			
			value.add(t)
			result.add(value)
			value = arrayListOf()
		} else {
			value.add(t)
			counter++
		}
	}
	
	return result
}

fun <T> Collection<Collection<T>>.to1DArray(): List<T> {
	val result = arrayListOf<T>()
	
	forEach { inner ->
		inner.forEach { e -> result.add(e) }
	}
	
	return result
}

fun <T> Collection<Collection<T>>.transpose(): List<List<T>> {
	val first = firstOrNull()
	val original = map { it.toList() }
	
	if (first == null) return original
	if (first.isEmpty()) return original
	
	val transposed: ArrayList<MutableList<T>> = ArrayList(original.map { it.toMutableList() })

	for (i in indices) {
		for (j in first.indices) {
			transposed[i][j] = original[j][i]
		}
	}
	
	return transposed
}

fun <T> ArrayList<T>.setOrAdd(index: Int, value: T) {
	try {
		set(index, value)
	} catch (e: IndexOutOfBoundsException) {
		add(value)
	}
}

inline operator fun <T> List<T>.component6(): T {
	return get(5)
}

inline operator fun <T> List<T>.component7(): T {
	return get(6)
}

inline operator fun <T> List<T>.component8(): T {
	return get(7)
}

inline operator fun <T> List<T>.component9(): T {
	return get(8)
}

inline operator fun <T> List<T>.component10(): T {
	return get(9)
}
