package it.unipr.aotlab.dmat.scalabindings.matrices

import it.unipr.aotlab.dmat.scalabindings.Host
import it.unipr.aotlab.dmat.scalabindings.AssignableTo

import it.unipr.aotlab.dmat.core.matrices.Chunk;


class MatrixChunk(val parent: Matrix, impl: Chunk) extends AssignableTo[Host,Unit] {
	import IntToDims._
	
	def getName: String = impl.getChunkId()
	def getSize: MatrixDims = (impl.getEndRow()-impl.getStartRow()+1) x (impl.getEndCol()-impl.getStartCol()+1)
	
	override def assignTo(node: Host) = {
		jimpl.assignChunkToNode(node.getImplementation)
		println("[SCALA] Chunk "+jimpl+" of matrix "+parent.getName+" assigned to node //"+node.ip+":"+node.port+"/")
	}
	
	def exposeValues { jimpl.sendMessageExposeValues();  }
	
	override def toString(): String = impl.toString();
	
	private val jimpl: Chunk = impl
	
}

