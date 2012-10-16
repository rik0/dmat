package it.unipr.aotlab.dmat.scalabindings.matrices

import it.unipr.aotlab.dmat.scalabindings.Program
import it.unipr.aotlab.dmat.scalabindings.Host
import it.unipr.aotlab.dmat.scalabindings.AssignableTo
import it.unipr.aotlab.dmat.scalabindings.InitializableIn
import it.unipr.aotlab.dmat.scalabindings.typewire.MatrixElementType

import it.unipr.aotlab.dmat.scalabindings.matrices.operations.MatrixExpression
import it.unipr.aotlab.dmat.scalabindings.matrices.operations.MatrixOperationsContext

import it.unipr.aotlab.dmat.core.matrices.Chunk;
import it.unipr.aotlab.dmat.core.generated.OrderSetMatrixWire.OrderSetMatrixBody;
import it.unipr.aotlab.dmat.core.generated.TypeWire;
import it.unipr.aotlab.dmat.core.net.messages.MessageSetMatrix;



class Matrix(name: String, size: MatrixDims, impl: MatrixImpl, prog: Program)
		extends MatrixExpression with AssignableTo[Host,Unit] {
	prog.register(name,this)
	import IntToDims._
	
	import scala.collection.JavaConversions._

	def getName: String = jimpl.getMatrixId();
	def getSize: MatrixDims = jimpl.getNofRows() x jimpl.getNofCols();
	
	override def resultSize: MatrixDims = getSize
	override protected[matrices] def evaluateIn(ctx: MatrixOperationsContext): (Matrix, MatrixOperationsContext) = (this,ctx)
	
	override def assignTo(node: Host) = {
		for (c: Chunk <- jimpl.getChunks()) {
			println("[SCALA] sending chunk "+chunk(c.getChunkId)+" to node //"+node.ip+":"+node.port+"/")
			c.assignChunkToNode(node.getImplementation)
		}
		println("[SCALA] Matrix "+getName+" assigned to node //"+node.ip+":"+node.port+"/")
	}
	
	def exposeValues {
		for (c: Chunk <- jimpl.getChunks()) {
			println("[SCALA] exposing values of chunk "+chunk(c.getChunkId))
			c.sendMessageExposeValues();
		}
	}
	
	class WithLoadedVals extends InitializableIn[Host,Unit] {
		private var jMsgBuilder: OrderSetMatrixBody.Builder = OrderSetMatrixBody.newBuilder();
		jMsgBuilder.setMatrixId(Matrix.this.jimpl.getMatrixId());
		
		def loadFromURL(url: String): WithLoadedVals = {
			this.url = url
			jMsgBuilder.setURI(url);
			println("[SCALA] Matrix "+getName+" loads values from file "+url+".");
			return this
		}
		
		def initializeIn(node: Host) = {
			// need one for each chunk to initialize
			for (c: Chunk <- jimpl.getChunks()) if (node.getImplementation.getNodeId() == c.getAssignedNodeId())
			  node.getImplementation.sendMessage(new MessageSetMatrix(jMsgBuilder))
			println("[SCALA] Matrix "+getName+" initialized in node //"+node.ip+":"+node.port+"/")
		}
		
		private var url: String = _
	}
	
	def loadValuesFromFile(fname: String): WithLoadedVals = {
		return new WithLoadedVals loadFromURL(fname)
	}
	
	def apply(fname: String): WithLoadedVals = loadValuesFromFile(fname)
	
	def chunk(name: String): MatrixChunk = new MatrixChunk(this,jimpl.getChunk(name));
	def chunk_of_:(name: String): MatrixChunk = new MatrixChunk(this,jimpl.getChunk(name));
	
	def setImplementation(impl: it.unipr.aotlab.dmat.core.matrices.Matrix)(implicit auth: MatrixBuilder.AuthToken with NotNull): Matrix = {
		jimpl = impl;
		return this;
	}
	
	protected[matrices] def getImplementation: it.unipr.aotlab.dmat.core.matrices.Matrix = jimpl
	
	private var jimpl: it.unipr.aotlab.dmat.core.matrices.Matrix = _;
	
}

