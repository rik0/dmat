package it.unipr.aotlab.dmat.scalabindings.matrices

import it.unipr.aotlab.dmat.scalabindings.Host
import it.unipr.aotlab.dmat.scalabindings.AssignableTo


// "filters" dangerous operations that cannot be done on a temporary (like using explicitly in a computation)
class MatrixTmpWrapper(private val wrapped: Matrix) extends AssignableTo[Host,Unit] {
	
	def getName: String = wrapped.getName
	def getSize: MatrixDims = wrapped.getSize
	
	override def assignTo(node: Host) = wrapped.assignTo(node)
	def exposeValues = wrapped.exposeValues
	
	def chunk(name: String): MatrixTmpChunkWrapper = new MatrixTmpChunkWrapper(wrapped.chunk(name));
	def chunk_of_:(name: String): MatrixTmpChunkWrapper = new MatrixTmpChunkWrapper(wrapped.chunk(name));
	
}

class MatrixTmpChunkWrapper(private val wrapped: MatrixChunk) extends AssignableTo[Host,Unit] {
	
	def getName: String = wrapped.getName
	def getSize: MatrixDims = wrapped.getSize
	
	override def assignTo(node: Host) = wrapped.assignTo(node)
	def exposeValues = wrapped.exposeValues

}




