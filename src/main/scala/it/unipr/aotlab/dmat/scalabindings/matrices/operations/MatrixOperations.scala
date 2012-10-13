package it.unipr.aotlab.dmat.scalabindings.matrices.operations

import it.unipr.aotlab.dmat.scalabindings.Enum
import it.unipr.aotlab.dmat.scalabindings.matrices._

import MatrixExpressionExceptions._


import it.unipr.aotlab.dmat.core.matrices.CopyMatrix
import it.unipr.aotlab.dmat.core.matrices.AdditionAssignment
import it.unipr.aotlab.dmat.core.matrices.Multiplication


sealed abstract class MatrixOperation extends MatrixOperations.Value with MatrixExpression {
   
  // order a vector of matrices by fitness
  def fit(ms: scala.collection.Map[Matrix,Int]): Iterable[Matrix] = ms.filterKeys(BySize(resultSize)).toList.map { p: (Matrix,Int) => p._1 } 
  
}

sealed abstract class MatrixAssignmentOperation(lhs: Matrix) extends MatrixOperation {
    
    override def resultSize = lhs.resultSize
    
    override def evaluateIn(ctx: MatrixOperationsContext): (Matrix,MatrixOperationsContext)
    
}

sealed abstract class MatrixNonAssignmentOperation extends MatrixOperation {
   
  // compute the result in the given context
  protected[operations] def compute(result: Matrix, ctx: MatrixOperationsContext): MatrixOperationsContext
  
  override def evaluateIn(ctx: MatrixOperationsContext): (Matrix,MatrixOperationsContext) = {
    val r: (Matrix,MatrixOperationsContext) = ctx.getTemporary(this)
    compute(r._1,r._2)
    return r
  }
  
}

object MatrixOperations extends Enum[MatrixOperation] {
  
  case class MatrixAssignment(lhs: Matrix, rhs: MatrixExpression) extends MatrixAssignmentOperation(lhs) {
    
    assert(lhs.resultSize == rhs.resultSize, { throw new MatrixOperationIncompatibleSizesException(this) } )
    
    override def evaluateIn(ctx: MatrixOperationsContext): (Matrix,MatrixOperationsContext) = {
      println("[SCALA] evaluating a matrix assignment")
      if (rhs.isInstanceOf[Matrix] || rhs.isInstanceOf[MatrixAssignmentOperation]) {
        val op: it.unipr.aotlab.dmat.core.matrices.CopyMatrix = new it.unipr.aotlab.dmat.core.matrices.CopyMatrix();
        val (rhRes,ctx1) = rhs.evaluateIn(ctx)
        op.setOperands(lhs.getImplementation,rhRes.getImplementation);
        op.exec();
        return (lhs,ctx1);
      }
      val ctx1: MatrixOperationsContext = rhs.asInstanceOf[MatrixNonAssignmentOperation].compute(lhs,ctx);
      return (lhs,ctx1)
    }
    
  }
  
  case class MatrixAdditionAssignment(lhs: Matrix, rhs: MatrixExpression) extends MatrixAssignmentOperation(lhs) {
    
    assert(lhs.resultSize == rhs.resultSize, { throw new MatrixOperationIncompatibleSizesException(this) } )
    
    override def evaluateIn(ctx: MatrixOperationsContext): (Matrix,MatrixOperationsContext) = {
      println("[SCALA] evaluating a matrix addition assignment")
      val op: it.unipr.aotlab.dmat.core.matrices.AdditionAssignment = new it.unipr.aotlab.dmat.core.matrices.AdditionAssignment();
      val r: (Matrix, MatrixOperationsContext) = rhs.evaluateIn(ctx)
      op.setOperands(lhs.getImplementation,r._1.getImplementation);
      op.exec();
      return r;
    }
    
  }

  case class MatrixMultiplication(lhs: MatrixExpression, rhs: MatrixExpression) extends MatrixNonAssignmentOperation {
   
    println("[SCALA] multiplication : "+lhs.resultSize+" vs "+rhs.resultSize) 
    assert(lhs.resultSize.cols.number == rhs.resultSize.rows.number, { throw new MatrixOperationIncompatibleSizesException(this) } )
    
    override def resultSize = lhs.resultSize.rows x rhs.resultSize.cols
    
    override def compute(result: Matrix, ctx: MatrixOperationsContext): MatrixOperationsContext = {
      println("[SCALA] computing a matrix multiplication")
      val op: it.unipr.aotlab.dmat.core.matrices.Multiplication = new it.unipr.aotlab.dmat.core.matrices.Multiplication();
      val r1: (Matrix, MatrixOperationsContext) = lhs.evaluateIn(ctx)
      val r2: (Matrix, MatrixOperationsContext) = rhs.evaluateIn(r1._2)
      op.setOperands(result.getImplementation,r1._1.getImplementation,r2._1.getImplementation);
      op.exec();
      return r2._2;
    }
    
  }


}


