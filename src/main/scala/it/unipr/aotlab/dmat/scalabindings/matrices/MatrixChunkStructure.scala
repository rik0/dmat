package it.unipr.aotlab.dmat.scalabindings.matrices

object MatrixChunkStructure {
	import IntToDims._;
	
	def apply(size: MatrixDims): MatrixChunkStructure = new MatrixChunkStructure(size,null,0 x 0)
	def apply(size: MatrixDims,name: String): MatrixChunkStructure = new MatrixChunkStructure(size,name, 0 x 0)
	def apply(size: MatrixDims,offset: MatrixDims): MatrixChunkStructure = new MatrixChunkStructure(size,null,offset)
	def apply(size: MatrixDims, name: String, offset: MatrixDims): MatrixChunkStructure = new MatrixChunkStructure(size,name,offset)
	
	def unapply(mcs: MatrixChunkStructure): Option[(MatrixDims,String,MatrixDims)] = {
		Some((mcs.size,mcs.name,mcs.offset))
	}
	
}

class MatrixChunkStructure(val size: MatrixDims, val name: String, val offset: MatrixDims) {
	
	def /(rhv: MatrixChunkStructure): MatrixChunkStructure = {
		require(size.rows == rhv.size.rows)
		MatrixHJoinedChunkStructure(this,rhv)
	}
	def -(rhv: MatrixChunkStructure): MatrixChunkStructure = {
		require(size.cols == rhv.size.cols)
		MatrixVJoinedChunkStructure(this,rhv)
	}
}



case class MatrixHJoinedChunkStructure(left: MatrixChunkStructure, right: MatrixChunkStructure) extends MatrixChunkStructure(left.size.rows x (left.size.cols+right.size.cols),left.name,left.offset) {
	assert(left.size.rows == right.size.rows)
}

case class MatrixVJoinedChunkStructure(top: MatrixChunkStructure, bottom: MatrixChunkStructure) extends MatrixChunkStructure((top.size.rows+bottom.size.rows) x top.size.cols,top.name,top.offset) {
	assert(top.size.cols == bottom.size.cols)
}
