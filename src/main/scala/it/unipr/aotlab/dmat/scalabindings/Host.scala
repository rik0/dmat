package it.unipr.aotlab.dmat.scalabindings

import it.unipr.aotlab.dmat.scalabindings.matrices.MatrixInterface
import it.unipr.aotlab.dmat.scalabindings.matrices.Matrix

import it.unipr.aotlab.dmat.core.net.Node;
import it.unipr.aotlab.dmat.core.matrices.Chunk;


class Host(val ip: String, val port: Int)
		extends (Host CanHaveAssigned MatrixInterface) {
	
	def print() = println("[SCALA] //"+ip+":"+port+"/")
	
	def setImplementation(impl: it.unipr.aotlab.dmat.core.net.Node)(implicit auth: NetGroup.AuthToken with NotNull): Host = {
		jimpl = impl;
		return this;
	}
	
	def chunkToNodeAssignationJImplementation(cimpl: Chunk)(implicit auth: MatrixInterface.AuthToken with NotNull) {
		cimpl.assignChunkToNode(jimpl);
	}
	
// 	def getJImplementation(implicit auth: MatrixInterface.AuthToken with NotNull): it.unipr.aotlab.dmat.core.net.Node = {
// 		return jimpl
// 	}
	
	
	private var jimpl: it.unipr.aotlab.dmat.core.net.Node = _;
	
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
		return h
	}
}