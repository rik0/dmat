package it.unipr.aotlab.dmat.scalabindings

import it.unipr.aotlab.dmat.scalabindings.matrices.Matrix
import it.unipr.aotlab.dmat.scalabindings.matrices.MatrixChunk

import it.unipr.aotlab.dmat.core.net.Node;
import it.unipr.aotlab.dmat.core.matrices.Chunk;
import it.unipr.aotlab.dmat.core.net.messages.MessageShutdown;


class Host(val ip: String, val port: Int)
		extends (Host CanHaveAssigned AssignableTo[Host,_]) {
	
	def print() = println("[SCALA] //"+ip+":"+port+"/")
	
	def shutdown { jimpl.sendMessage(new MessageShutdown());  }
	
	def setImplementation(impl: it.unipr.aotlab.dmat.core.net.Node)(implicit auth: NetGroup.AuthToken with NotNull): Host = {
		jimpl = impl;
		return this;
	}
	
	def getImplementation: it.unipr.aotlab.dmat.core.net.Node = jimpl
	
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
