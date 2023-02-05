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
		inner.forEach { t -> result.add(t) }
	}
	
	return result
}
