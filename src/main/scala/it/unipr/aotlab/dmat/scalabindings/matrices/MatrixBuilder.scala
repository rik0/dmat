package it.unipr.aotlab.dmat.scalabindings.matrices

import it.unipr.aotlab.dmat.scalabindings.Program
import it.unipr.aotlab.dmat.scalabindings.typewire.MatrixElementType


import it.unipr.aotlab.dmat.core.matrices.Matrices;
import it.unipr.aotlab.dmat.core.matrices.Chunk;


object MatrixBuilder {
	sealed trait AuthToken
	private implicit object Auth extends AuthToken
}

class MatrixBuilder(prog: Program) {
	import MatrixBuilder._
	
	def apply(id: String): Matrix = prog.get_matrix(id)
	
	def build(): Matrix = {
		jimpl_last_mat = jimpl.build();
		jimpl.reset();
		return new Matrix(id,dim,impl,eltype,prog).setImplementation(jimpl_last_mat);
	}
	
	def named(id: String): MatrixBuilder = { this.id = id; jimpl.setName(id); return this; }
	
	
// 	def row_major(): MatrixBuilder = { impl = MatrixRowMajor(); return this; }
// 	def col_major(): MatrixBuilder = { impl = MatrixRowMajor(); return this; }
// 	def triplets(): MatrixBuilder = { impl = MatrixRowMajor(); return this; }
// 	def compr_row(): MatrixBuilder = { impl = MatrixRowMajor(); return this; }
// 	def compr_col(): MatrixBuilder = { impl = MatrixRowMajor(); return this; }
	
	
	def size(dim: MatrixDims): MatrixBuilder = {
		this.dim = dim;
		jimpl.setNofRows(dim.rows.number).setNofColumns(dim.cols.number);
		return this;
	}
	
	def of(eltype: MatrixElementType): MatrixBuilder = {
		this.eltype = eltype;
		jimpl.setElementType(eltype.wrapped);
		return this;
	}
	
	def split(struct: MatrixChunkStructure): MatrixBuilder = {
		split_impl(struct,true);
		return this;
	}
	
	private def split_impl(struct: MatrixChunkStructure, owname: Boolean = false) {
		var name: String = struct.name;
		if (owname) name = null;
		struct match {
			case MatrixHJoinedChunkStructure(l,r) =>
				println("[SCALA] H split("+owname+") "+struct.name+"("+name+") ==> ( "+l.name+" / "+r.name+" )")
				jimpl.splitVerticallyChuck(name,l.size.cols.number,l.name,r.name);
				split_impl(l)
				split_impl(r)
			case MatrixVJoinedChunkStructure(t,b) =>
				println("[SCALA] V split("+owname+") "+struct.name+"("+name+") ==> ( "+t.name+" - "+b.name+" )")
				jimpl.splitHorizzontalyChuck(name,t.size.rows.number,t.name,b.name);
				split_impl(t)
				split_impl(b)
			case MatrixChunkStructure(s,n,o) =>
				println("[SCALA] Chunk "+n)
			case _ =>
				println("[SCALA] What happened???")
		}
	}
	
	private var id: String = null;
	private var dim: MatrixDims = null;
	private var eltype: MatrixElementType = null;
	private var impl: MatrixImpl = null;
	
	private var jimpl_last_mat: it.unipr.aotlab.dmat.core.matrices.Matrix = _;
	private var jimpl: Matrices = Matrices.newBuilder();
	
}
