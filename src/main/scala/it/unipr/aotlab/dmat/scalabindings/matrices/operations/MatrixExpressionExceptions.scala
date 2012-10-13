package it.unipr.aotlab.dmat.scalabindings.matrices.operations

import it.unipr.aotlab.dmat.scalabindings.Enum;

import it.unipr.aotlab.dmat.scalabindings.matrices.Matrix


sealed abstract class MatrixExpressionException extends Exception with MatrixExpressionExceptions.Value

object MatrixExpressionExceptions extends Enum[MatrixExpressionException] {
  
  case class MatrixOperationIncompatibleSizesException(op: MatrixOperation) extends MatrixExpressionException {
    
    override def toString = "Incompatible size of operands in "+op
    
  }  

  case class MatrixComparisonIncompatibleSizesException(cm: MatrixComparison) extends MatrixExpressionException {
    override def toString = "Incompatible size of terms in "+cm
  }

  case class MatrixOperationInvalidOutputSize(op: MatrixOperation, out: Matrix) extends MatrixExpressionException {
    
    override def toString = "Cannot store result of "+op+" in matrix "+out+" of size "+out.getSize
    
  }
  

}

