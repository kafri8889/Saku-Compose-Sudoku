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
