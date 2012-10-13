package it.unipr.aotlab.dmat.scalabindings.matrices

case class BySize(val size: MatrixDims) extends ( (Matrix) => Boolean ) {

  def apply(m: Matrix): Boolean = m.getSize == size

}

