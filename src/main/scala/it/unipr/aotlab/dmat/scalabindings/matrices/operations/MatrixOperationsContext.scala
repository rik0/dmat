package it.unipr.aotlab.dmat.scalabindings.matrices.operations

import it.unipr.aotlab.dmat.scalabindings.matrices.Matrix

import MatrixExpressionExceptions.MatrixOperationNotEnoughTemporariesInContextException

// Immutable object that represents an expression context

class MatrixOperationsContext(temporaries: Set[Matrix]) {
  private var temp_usage: scala.collection.immutable.Map[Matrix,Int] = temporaries.map({ m: Matrix => (m,0) }).toMap
  
  def getTemporary(op: MatrixOperation): (Matrix,MatrixOperationsContext) = {
    val candidates: Iterable[Matrix] = op.fit(temp_usage)
    if (candidates isEmpty) throw new MatrixOperationNotEnoughTemporariesInContextException(op,this)
    setUsedTemporary(candidates.head)
    return (candidates.head, this)
  }
  
  def resetUsages { temp_usage = temp_usage mapValues { x: Int => 0 } }
  
  private def setUsedTemporary(ut: Matrix) { temp_usage = temp_usage.map(updateUsedTemporary(ut) _).toMap }
  private def updateUsedTemporary(ut: Matrix)(p: (Matrix,Int)): (Matrix,Int) = { if (p._1 == ut) return (p._1,p._2+1); return p;  }
  
}

