package it.unipr.aotlab.dmat.scalabindings.matrices.operations

import it.unipr.aotlab.dmat.scalabindings.Enum;

import it.unipr.aotlab.dmat.scalabindings.matrices.Matrix


sealed abstract class MatrixExpressionException extends Exception with MatrixExpressionExceptions.Value

object MatrixExpressionExceptions extends Enum[MatrixExpressionException] {
  
  case class MatrixOperationIncompatibleSizesException(op: MatrixOperation) extends MatrixExpressionException {
    
    override def toString: String = "Incompatible size of operands in "+op
    
  }  

  case class MatrixComparisonIncompatibleSizesException(cm: MatrixComparison) extends MatrixExpressionException {
    override def toString: String = "Incompatible size of terms in "+cm
  }

  case class MatrixOperationInvalidOutputSizeException(op: MatrixOperation, out: Matrix) extends MatrixExpressionException {
    
    override def toString: String = "Cannot store result of "+op+" in matrix "+out+" of size "+out.getSize
    
  }
  
	case class MatrixOperationNotEnoughTemporariesInContextException(op: MatrixOperation, ctx: MatrixOperationsContext) extends MatrixExpressionException {
		
		override def toString: String = "Operation "+op+" queried context "+ctx+" for a temporary matrix, but none was appropriate"
		
	}

}

object MatrixExpressionImplementationException extends Enum[MatrixExpressionException] {
	
	case class MatrixExpressionNotImplementedOperatorException(opname: String) extends MatrixExpressionException {
		
		override def toString: String = "Operator "+opname+" isn't implemented (yet)"
		
	}
	
	case class MatrixExpressionDMatWillFailException(opsymb: String, opname: String, opimpl: String) extends MatrixExpressionException {
		
		override def toString: String = "Operator "+opsymb+" ("+opname+") has not a direct mapping in the underlying Java implementation of DMat. So it has been implemented as:\n"+opimpl+"\nThis way you would have an operation in which the result matrix appears as one of the operands. The library doesn't support this kind of things (yet), so you can't use "+opname
		
	}
	
}

