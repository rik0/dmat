package it.unipr.aotlab.dmat.scalabindings


trait AssignableTo[T,R] {
	
	def assignTo(t: T): R
	def ~>(t: T): R = assignTo(t)
	
}

trait CanHaveAssigned[S <: CanHaveAssigned[S,T], T <: AssignableTo[S,_]] {
	
	def <~(t: T): S = { t.assignTo(this.asInstanceOf[S]); return this.asInstanceOf[S] }
	
}
