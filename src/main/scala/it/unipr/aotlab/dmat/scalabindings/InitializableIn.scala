package it.unipr.aotlab.dmat.scalabindings



trait InitializableIn[T,R] {
	
	def initializeIn(t: T): R
	def :=>(t: T): R = initializeIn(t)
	def <=:(t: T): T = { initializeIn(t); return t }
	
}

