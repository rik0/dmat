package it.unipr.aotlab.dmat.scalabindings


class Master(val name: String, ip: String, port: Int) extends Host(ip,port) {
	
	def has_nodes(ns: Host*): Master = { mynodes = ns.toList; this }
	def nodes(): List[Host] = { mynodes }
	
	private var mynodes: List[Host] = Nil
}
