package it.unipr.aotlab.dmat.scalabindings.matrices.operations

import it.unipr.aotlab.dmat.scalabindings.Enum
import it.unipr.aotlab.dmat.scalabindings.matrices._

import MatrixExpressionExceptions._


import it.unipr.aotlab.dmat.core.matrices.CopyMatrix
import it.unipr.aotlab.dmat.core.matrices.AdditionAssignment
import it.unipr.aotlab.dmat.core.matrices.Multiplication


sealed abstract class MatrixOperation extends MatrixOperations.Value with MatrixExpression {
   
  // order a vector of matrices by fitness
  def fit(ms: scala.collection.Map[Matrix,Int]): Iterable[Matrix] = ms.filterKeys(BySize(resultSize)).toList.sortWith(_._2 < _._2).map { p: (Matrix,Int) => p._1 } 
  
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

    override def toString: String = lhs.getName+" := "+rhs
    
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
    
    override def toString: String = lhs.getName+" :+= "+rhs
    
  }
	
	case class MatrixAddition(lhs: MatrixExpression, rhs: MatrixExpression) extends MatrixNonAssignmentOperation {
		println("[SCALA] addition : "+lhs.resultSize+" vs "+rhs.resultSize) 
    assert(lhs.resultSize == rhs.resultSize, { throw new MatrixOperationIncompatibleSizesException(this) } )
    
    override def resultSize = lhs.resultSize
    
    override def compute(result: Matrix, ctx: MatrixOperationsContext): MatrixOperationsContext = {
      println("[SCALA] computing a matrix addition: "+lhs+" + "+rhs+" >> "+result)
      if (result.getSize != resultSize) throw new MatrixOperationInvalidOutputSizeException(this,result)
			// save a temporary if the lhs needs one
			if (lhs.isInstanceOf[MatrixNonAssignmentOperation]) {
				val c1 = lhs.asInstanceOf[MatrixNonAssignmentOperation].compute(result,ctx)
				val r2: (Matrix, MatrixOperationsContext) = rhs.evaluateIn(c1)
				implicit var unused_context: MatrixOperationsContext = r2._2
				result :+= r2._1
				return r2._2
			}
			// else you don't need a temporary for the lhs
			val r1: (Matrix, MatrixOperationsContext) = lhs.evaluateIn(ctx)
			// evaluation of this line preserves left-to-right evaluation
			// save a temporary if the rhs isn't an assignment
			if (rhs.isInstanceOf[MatrixNonAssignmentOperation]) {
				val c2 = rhs.asInstanceOf[MatrixNonAssignmentOperation].compute(result,r1._2)
				implicit var unused_context: MatrixOperationsContext = c2
				// we can do this because addition on semirings is commutative
				result :+= r1._1
				return c2
			}
			// ok, we need a copy
      val r2: (Matrix, MatrixOperationsContext) = rhs.evaluateIn(r1._2)
			implicit var unused_context: MatrixOperationsContext = r2._2
			result := r1._1
			result :+= r2._1
      return r2._2;
    }
    
    override def toString: String = lhs+" * "+rhs
    

	}
	
  case class MatrixMultiplication(lhs: MatrixExpression, rhs: MatrixExpression) extends MatrixNonAssignmentOperation {
   
    println("[SCALA] multiplication : "+lhs.resultSize+" vs "+rhs.resultSize) 
    assert(lhs.resultSize.cols.number == rhs.resultSize.rows.number, { throw new MatrixOperationIncompatibleSizesException(this) } )
    
    override def resultSize = lhs.resultSize.rows x rhs.resultSize.cols
    
    override def compute(result: Matrix, ctx: MatrixOperationsContext): MatrixOperationsContext = {
      println("[SCALA] computing a matrix multiplication: "+lhs+" x "+rhs+" >> "+result)
      if (result.getSize != resultSize) throw new MatrixOperationInvalidOutputSizeException(this,result)
      val op: it.unipr.aotlab.dmat.core.matrices.Multiplication = new it.unipr.aotlab.dmat.core.matrices.Multiplication();
      val r1: (Matrix, MatrixOperationsContext) = lhs.evaluateIn(ctx)
      val r2: (Matrix, MatrixOperationsContext) = rhs.evaluateIn(r1._2)
      op.setOperands(result.getImplementation,r1._1.getImplementation,r2._1.getImplementation);
      op.exec();
      return r2._2;
    }
    
    override def toString: String = lhs+" * "+rhs
    
  }


}


