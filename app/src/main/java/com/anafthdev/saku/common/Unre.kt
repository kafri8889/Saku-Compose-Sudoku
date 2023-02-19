package com.anafthdev.saku.common

class Unre<T> {
	
	private var listener: UnreListener<T>? = null
	
	val stacks: ArrayList<UnreData<T>> = arrayListOf()
	var currentStack: Int = -1
	
	private fun modifyStack() {
		val newStacks = arrayListOf<UnreData<T>>()
		
		stacks.forEachIndexed { i, ud ->
			if (ud.stack == currentStack) {
				currentStack = i
			}
			
			newStacks.add(ud.copy(stack = i))
		}
		
		stacks.apply {
			clear()
			addAll(newStacks)
		}
	}
	
	private fun toUnreData(vararg data: T): List<UnreData<T>> {
		val newStacks = arrayListOf<UnreData<T>>()
		
		data.forEachIndexed { i, t ->
			newStacks.add(UnreData(i, t))
		}
		
		return newStacks
	}
	
	fun swap(vararg newData: T) {
		stacks.apply {
			clear()
			
			newData.forEach { t ->
				addStack(t)
			}
		}
	}
	
	fun addStack(t: T) {
		val lastStack = stacks.lastOrNull()?.stack ?: -1
		
		val newStack = UnreData(
			stack = if (currentStack == -1) 0 else lastStack + 1,
			data = t
		)
		
		val lastCurrentStack = currentStack
		
		currentStack = newStack.stack
		
		println("sem wit las: ${lastCurrentStack} == ${lastStack}")
		println("sem: ${currentStack} == ${lastStack + 1}")
		
		if (lastCurrentStack!= lastStack) {
//			val newStacks = stacks.dropLast((lastCurrentStack?.stack ?: -1) + 1)
			val newStacks = ArrayList(stacks).slice(0..lastCurrentStack)
			println("nyu staks: $newStacks")
			stacks.apply {
				clear()
				addAll(newStacks)
			}
		}
		
		stacks.add(newStack)
		
		modifyStack()
		
		println("siz: ${stacks.size} -> ${stacks.map { it.stack }}")
		stacks.forEachIndexed { i, t ->
			println("$i = ${t.stack} -> ${t.data}")
		}
		println("cur stak: ${currentStack}")
	}
	
	fun undo() {
		val previousStack = stacks.getOrNull(currentStack - 1)
		
		println("undo | prev: ${previousStack?.stack}, cur: ${currentStack}")
		
		if (previousStack != null) {
			currentStack = previousStack.stack
			listener?.onUndo(previousStack.data)
		}
	}
	
	fun redo() {
		val nextStack = stacks.getOrNull(currentStack + 1)
		
		if (nextStack != null) {
			currentStack = nextStack.stack
			listener?.onRedo(nextStack.data)
		}
	}
	
	fun setListener(l: UnreListener<T>) {
		listener = l
	}
	
	interface UnreListener<T> {
		fun onUndo(data: T)
		
		fun onRedo(data: T)
	}
	
}

data class UnreData<T>(
	val stack: Int,
	val data: T
)
