package com.anafthdev.saku.extension

fun <E> Collection<E>.to2DArray(len: Int): List<List<E>> {
	val result = arrayListOf<ArrayList<E>>()
	
	var counter = 1
	var value = arrayListOf<E>()
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

fun <E> Collection<Collection<E>>.to1DArray(): List<E> {
	val result = arrayListOf<E>()
	
	forEach { inner ->
		inner.forEach { e -> result.add(e) }
	}
	
	return result
}

fun <E> Collection<Collection<E>>.transpose(): List<List<E>> {
	val first = firstOrNull()
	val original = map { it.toList() }
	
	if (first == null) return original
	if (first.isEmpty()) return original
	
	val transposed: ArrayList<MutableList<E>> = ArrayList(original.map { it.toMutableList() })

	for (i in indices) {
		for (j in first.indices) {
			transposed[i][j] = original[j][i]
		}
	}
	
	return transposed
}