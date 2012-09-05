package it.unipr.aotlab.dmat.scalabindings.matrices

import it.unipr.aotlab.dmat.scalabindings.Host

object MatrixChunk {
	sealed trait AuthToken
	private implicit object Auth extends AuthToken
}

class MatrixChunk(val parent: Matrix, offset: MatrixDims, name: String, size: MatrixDims) extends MatrixInterface {
	import MatrixChunk._
	if (name != null) parent.register(name,this)
	
	import IntToDims._
	
	def split(struct: MatrixChunkStructure) = {
		struct match {
			case MatrixHJoinedChunkStructure(l,r) =>
				println("H split")
				new MatrixChunk(parent,offset+l.offset,l.name,l.size).split(l)
				new MatrixChunk(parent,offset+(0 x l.size.cols)+r.offset,r.name,r.size).split(r)
			case MatrixVJoinedChunkStructure(l,r) =>
				println("V split")
				new MatrixChunk(parent,offset+l.offset,l.name,l.size).split(l)
				new MatrixChunk(parent,offset+(l.size.rows x 0)+r.offset,r.name,r.size).split(r)
			case MatrixChunkStructure(s,n,o) =>
				println("Chunk "+n)
			case _ =>
				println("What happened???")
		}
	}
	
	def assignTo(node: Host) = { println("Chunk "+name+" of matrix "+parent.name+" assigned to node //"+node.ip+":"+node.port+"/") }
	def sendTo(node: Host) = { println("Chunk "+name+" of matrix "+parent.name+" sent to node //"+node.ip+":"+node.port+"/") }
	
	def loadValuesFromFile(fname: String) = { println("Chunk "+name+" of matrix "+parent.name+" loads values from file "+fname+"."); }
	
	
	def test(): MatrixChunk = { println("Chunk "+name+", "+size.rows.number+"x"+size.cols.number+" of matrix "+parent.name+", offset: "+offset.rows.number+"x"+offset.cols.number); return this }
	
	
	/*
	private def splitHorizontally(newName: String, newStartCol: Int): MatrixChunk = {
		return new MatrixChunk(parent,offset+(0 x newStartCol),newName,size.rows x (size.cols.number+newStartCol))
	}
	
	private def splitVertically(newName: String, newStartRow: Int): MatrixChunk = {
		return new MatrixChunk(parent,offset+(newStartRow x 0),newName,(size.rows.number-newStartRow) x size.cols)
	}
	*/
	
}
