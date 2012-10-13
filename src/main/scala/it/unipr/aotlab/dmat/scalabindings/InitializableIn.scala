package it.unipr.aotlab.dmat.scalabindings



trait InitializableIn[T,R] {
	
	def initializeIn(t: T): R
	
	def :=>(ts: Iterable[T]) { for (t: T <- ts) { initializeIn(t) } }
	def <=:(ts: Iterable[T]) { for (t: T <- ts) { initializeIn(t) } }
	
	def :=>(t: T): R = initializeIn(t)
	def <=:(t: T): T = { initializeIn(t); return t }
	
	
	
}

