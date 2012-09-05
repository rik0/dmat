package it.unipr.aotlab.dmat.scalabindings.matrices

import it.unipr.aotlab.dmat.scalabindings.Program
import it.unipr.aotlab.dmat.scalabindings.Host
import it.unipr.aotlab.dmat.scalabindings.AssignableTo
import it.unipr.aotlab.dmat.scalabindings.SendableTo
import it.unipr.aotlab.dmat.scalabindings.typewire.MatrixElementType

trait MatrixInterface
		extends AssignableTo[Host,Unit]
		with SendableTo[Host,Unit] {
	
	def split(struct: MatrixChunkStructure)
	
	def loadValuesFromFile(fname: String)
	
	def does(f: MatrixInterface => Unit): MatrixInterface = { f(this); this }
	def and (f: MatrixInterface => Unit): MatrixInterface = { f(this); this }
	
}

object Matrix {
	sealed trait AuthToken
	private implicit object Auth extends AuthToken
}

class Matrix(val name: String, size: MatrixDims, impl: MatrixImpl, eltype: MatrixElementType, prog: Program) extends MatrixInterface {
	import Matrix._
	prog.register(name,this)
	import IntToDims._
	private val mychunks = scala.collection.mutable.Map[String,MatrixChunk]()
	private val basechunk = new MatrixChunk(this,0 x 0,null,size)
	
	def split(struct: MatrixChunkStructure) = chunk(null).split(struct)
	
	def assignTo(node: Host) = basechunk.assignTo(node)
	def sendTo(node: Host) = basechunk.sendTo(node)
	def loadValuesFromFile(fname: String) = basechunk.loadValuesFromFile(fname)
	
	def chunk(name: String): MatrixChunk = if (name == null) basechunk else mychunks(name)
	def chunk_of_:(name: String): MatrixChunk = if (name == null) basechunk else mychunks(name)
	
	
	def register(name: String, chunk: MatrixChunk)(implicit auth: MatrixChunk.AuthToken with NotNull) {
		println("Registering matrix chunk "+name+" in matrix "+this.name+"...");
		mychunks.put(name,chunk)
	}
	
}
