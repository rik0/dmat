package it.unipr.aotlab.dmat.scalabindings.matrices.operations

import it.unipr.aotlab.dmat.scalabindings.matrices.Matrix


class MatrixOperationsContextHandler {
  private var temporaries: Set[Matrix] = Set[Matrix]()
  
  def addTemporary(nt: Matrix): Matrix = { temporaries += nt; return nt; }
  def removeTemporary(t: Matrix): Matrix = { temporaries -= t; return t; }
  def getTemporary(name: String): Option[Matrix] = { temporaries.find{ m: Matrix => m.getName == name } }
  
  def getContext: MatrixOperationsContext = new MatrixOperationsContext(temporaries)
  
}

