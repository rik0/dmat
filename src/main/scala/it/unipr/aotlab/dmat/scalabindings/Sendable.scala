package it.unipr.aotlab.dmat.scalabindings


trait SendableTo[T,R] {
	
	def sendTo(t: T): R
	def -->(t: T): R = sendTo(t)
	
}

trait CanReceive[S <: CanReceive[S,T], T <: SendableTo[S,_]] {
	
	def <--(t: T): S = { t sendTo(this.asInstanceOf[S]); return this.asInstanceOf[S] }
	
}
