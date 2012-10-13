package it.unipr.aotlab.dmat.scalabindings.matrices.operations

import it.unipr.aotlab.dmat.scalabindings.Enum
import it.unipr.aotlab.dmat.scalabindings.matrices._

import MatrixExpressionExceptions._


import it.unipr.aotlab.dmat.core.matrices.Compare;


sealed abstract class MatrixComparison extends MatrixComparisons.Value {
  
  def evaluateIn(ctx: MatrixOperationsContext): (Boolean, MatrixOperationsContext)
  
}


object MatrixComparisons extends Enum[MatrixComparison] {
  
  case class MatrixEquality(lhs: MatrixExpression, rhs: MatrixExpression) extends MatrixComparison {
    assert(lhs.resultSize == rhs.resultSize, {throw new MatrixComparisonIncompatibleSizesException(this) } )
    
    override def evaluateIn(ctx: MatrixOperationsContext): (Boolean, MatrixOperationsContext) = {
      println("[SCALA] evaluating a matrix comparison")
      val op: it.unipr.aotlab.dmat.core.matrices.Compare = new it.unipr.aotlab.dmat.core.matrices.Compare();
      val r1: (Matrix, MatrixOperationsContext) = lhs.evaluateIn(ctx);
      val r2: (Matrix, MatrixOperationsContext) = rhs.evaluateIn(r1._2);
      op.setOperands(r1._1.getImplementation,r2._1.getImplementation);
      op.exec();
      return (op.answer(),r2._2);
    }
    
  }
  
}


