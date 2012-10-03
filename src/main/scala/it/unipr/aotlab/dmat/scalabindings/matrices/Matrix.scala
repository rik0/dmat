package it.unipr.aotlab.dmat.scalabindings.matrices

import it.unipr.aotlab.dmat.scalabindings.Program
import it.unipr.aotlab.dmat.scalabindings.Host
import it.unipr.aotlab.dmat.scalabindings.AssignableTo
import it.unipr.aotlab.dmat.scalabindings.InitializableIn
import it.unipr.aotlab.dmat.scalabindings.typewire.MatrixElementType

import it.unipr.aotlab.dmat.core.matrices.Chunk;
import it.unipr.aotlab.dmat.core.generated.OrderSetMatrixWire.OrderSetMatrixBody;
import it.unipr.aotlab.dmat.core.generated.TypeWire;
import it.unipr.aotlab.dmat.core.net.Message;

object MatrixInterface {
	sealed trait AuthToken
}

trait MatrixInterface extends AssignableTo[Host,Unit] {
	import MatrixInterface._
	
	def does(f: MatrixInterface => Unit): MatrixInterface = { f(this); this }
	def and (f: MatrixInterface => Unit): MatrixInterface = { f(this); this }
	
}

object Matrix {
	sealed trait AuthToken extends MatrixInterface.AuthToken
	private implicit object Auth extends AuthToken
}

object MatrixChunk {
	sealed trait AuthToken extends MatrixInterface.AuthToken
	private implicit object Auth extends AuthToken
}

class MatrixChunk(val parent: Matrix, impl: Chunk) extends MatrixInterface {
	import MatrixChunk._
	
	val name = impl.getChunkId()
	
	def assignTo(node: Host) = {
		node.chunkToNodeAssignationJImplementation(jimpl);
		println("[SCALA] Chunk "+jimpl+" of matrix "+parent.name+" assigned to node //"+node.ip+":"+node.port+"/")
	}
	
	override def toString(): String = impl.toString();
	
	private val jimpl: Chunk = impl
	
}

class Matrix(val name: String, size: MatrixDims, impl: MatrixImpl, eltype: MatrixElementType, prog: Program)
		extends MatrixInterface {
	import Matrix._
	prog.register(name,this)
	import IntToDims._
	
	import scala.collection.JavaConversions._
	
	def assignTo(node: Host) = {
		for (c: Chunk <- jimpl.getChunks()) {
			println("[SCALA] sending chunk "+chunk(c.getChunkId)+" to node //"+node.ip+":"+node.port+"/")
			node.chunkToNodeAssignationJImplementation(c);
		}
		println("[SCALA] Matrix "+name+" assigned to node //"+node.ip+":"+node.port+"/")
	}
	
	
	class WithLoadedVals extends InitializableIn[Host,Unit] {
		private var jMsgBuilder: OrderSetMatrixBody.Builder = OrderSetMatrixBody.newBuilder();
		jMsgBuilder.setMatrixId(Matrix.this.jimpl.getMatrixId());
		
		def loadFromURL(url: String): WithLoadedVals = {
			this.url = url
			jMsgBuilder.setURI(url);
			println("[SCALA] Matrix "+name+" loads values from file "+url+".");
			return this
		}
		
		def initializeIn(node: Host) = {
			println("[SCALA] Matrix "+name+" initialized in node //"+node.ip+":"+node.port+"/")
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
	
	private var jimpl: it.unipr.aotlab.dmat.core.matrices.Matrix = _;
	
}
