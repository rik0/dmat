package it.unipr.aotlab.dmat.scalabindings.matrices.operations

import it.unipr.aotlab.dmat.scalabindings.matrices.Matrix
import it.unipr.aotlab.dmat.scalabindings.matrices.MatrixDims

import MatrixOperations._
import MatrixComparisons._


trait MatrixExpression {

  def resultSize: MatrixDims
    
  // if the MatrixExpression is a Matrix, return the Matrix itself
  // else get the result of the operation in the context and a new context
  protected[operations] def evaluateIn(ctx: MatrixOperationsContext): (Matrix, MatrixOperationsContext)
 
  
  // ----- OPERATORS DEFINITIONS -----
  // ASSIGNMENT OPERATORS
  def :=(rhs: MatrixExpression)(implicit ctx: MatrixOperationsContext): Matrix = {
    MatrixAssignment(this.asInstanceOf[Matrix],rhs).evaluateIn(ctx)
    return this.asInstanceOf[Matrix]
  }
  
  def :+=(rhs: MatrixExpression)(implicit ctx: MatrixOperationsContext): Matrix = {
    MatrixAdditionAssignment(this.asInstanceOf[Matrix],rhs).evaluateIn(ctx)
    return this.asInstanceOf[Matrix]
  }

  // ARITHMETIC OPERATORS
  def *(rhs: MatrixExpression): MatrixMultiplication = new MatrixMultiplication(this,rhs)


  // COMPARISON OPERATORS
  def =?=(rhs: MatrixExpression)(implicit ctx: MatrixOperationsContext): Boolean = MatrixEquality(this,rhs).evaluateIn(ctx)._1
  
   
}


