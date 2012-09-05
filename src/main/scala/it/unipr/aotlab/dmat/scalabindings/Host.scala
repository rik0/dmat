package it.unipr.aotlab.dmat.scalabindings

import it.unipr.aotlab.dmat.scalabindings.matrices.MatrixInterface


class Host(val ip: String, val port: Int)
		extends (Host CanHaveAssigned MatrixInterface)
		with (Host CanReceive MatrixInterface) {
	
	def print() = println("//"+ip+":"+port+"/")
	
}

object HostName {
	sealed trait AuthToken
	private implicit object Auth extends AuthToken
}

class HostName(it: String, ng: NetGroup) {
	import HostName._
	def this(name: String) = this(name,null)
	
	def at(ip: String, port: Int): Host = {
		val h: Host = new Host(ip,port)
		if (ng != null) ng.register(it,h)
		h
	}
}