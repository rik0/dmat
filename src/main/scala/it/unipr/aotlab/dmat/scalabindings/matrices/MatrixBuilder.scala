package it.unipr.aotlab.dmat.scalabindings.matrices

import it.unipr.aotlab.dmat.scalabindings.Program
import it.unipr.aotlab.dmat.scalabindings.typewire.MatrixElementType


object MatrixBuilder {
	sealed trait AuthToken
	private implicit object Auth extends AuthToken
}

class MatrixBuilder(prog: Program) {
	import MatrixBuilder._
	
	def apply(id: String): Matrix = prog.get_matrix(id)
	
	def build(): Matrix = new Matrix(id,dim,impl,eltype,prog)
	
	def named(id: String): MatrixBuilder = { this.id = id; return this; }
	
	def row_major(): MatrixBuilder = { impl = MatrixRowMajor(); return this; }
	def col_major(): MatrixBuilder = { impl = MatrixRowMajor(); return this; }
	def triplets(): MatrixBuilder = { impl = MatrixRowMajor(); return this; }
	def compr_row(): MatrixBuilder = { impl = MatrixRowMajor(); return this; }
	def compr_col(): MatrixBuilder = { impl = MatrixRowMajor(); return this; }
	
	def size(dim: MatrixDims): MatrixBuilder = { this.dim = dim; return this; }
	
	def of(eltype: MatrixElementType): MatrixBuilder = { this.eltype = eltype; return this; }
	
	private var id: String = null;
	private var dim: MatrixDims = null;
	private var eltype: MatrixElementType = null;
	private var impl: MatrixImpl = null;
	
}
