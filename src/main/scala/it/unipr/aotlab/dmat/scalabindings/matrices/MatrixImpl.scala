package it.unipr.aotlab.dmat.scalabindings.matrices


sealed abstract class MatrixImpl {}
case class MatrixRowMajor() extends MatrixImpl { /* To be implemented */}
case class MatrixColMajor() extends MatrixImpl { /* To be implemented */}
case class MatrixTriplets() extends MatrixImpl { /* To be implemented */}
case class MatrixCompressedRow() extends MatrixImpl { /* To be implemented */}
case class MatrixCompressedCol() extends MatrixImpl { /* To be implemented */}
