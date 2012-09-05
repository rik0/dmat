package it.unipr.aotlab.dmat.scalabindings

import it.unipr.aotlab.dmat.scalabindings.matrices._
import it.unipr.aotlab.dmat.scalabindings.typewire.MatrixElementTypes


class Program {
	
	val matrix: MatrixBuilder = new MatrixBuilder(this)
	
	val elements = MatrixElementTypes
	
	def define(what: MatrixBuilder): Matrix = what.build;
	
	def get_matrix(name: String)(implicit auth: MatrixBuilder.AuthToken with NotNull): Matrix = mymatrices(name)
	
	implicit def integer_to_MatrixRows(r: Int): MatrixRows = new MatrixRows(r)
	implicit def integer_to_MatrixCols(c: Int): MatrixCols = new MatrixCols(c)
	
	def B(name: String, size: MatrixDims): MatrixChunkStructure = { println("B("+name+")"); MatrixChunkStructure(size,name) }
	
	
	def register(name: String, matrix: Matrix)(implicit auth: Matrix.AuthToken with NotNull) {
		println("Registering matrix "+name+"...");
		mymatrices.put(name,matrix)
	}
	
	private val mymatrices = scala.collection.mutable.Map[String,Matrix]()
}
