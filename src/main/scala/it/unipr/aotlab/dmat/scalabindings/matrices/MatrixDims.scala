package it.unipr.aotlab.dmat.scalabindings.matrices



case class MatrixRows(val number: Int) {

	def x(cols: MatrixCols): MatrixDims = new MatrixDims(this,cols);
	
	def +(rhv: MatrixRows): MatrixRows = new MatrixRows(number+rhv.number)
	
	override def equals(that: Any) = {
		that match {
			case r: MatrixRows => number == r.number
			case _ => false
		}
	}
	
	override def toString = "rows: "+number
	
}

case class MatrixCols(val number: Int) {

	def +(rhv: MatrixCols): MatrixCols = new MatrixCols(number+rhv.number)
	
	override def equals(that: Any) = {
		that match {
			case c: MatrixCols => number == c.number
			case _ => false
		}
	}
	
	override def toString = "cols: "+number
	
}


class MatrixDims(val rows: MatrixRows, val cols: MatrixCols) {
	
	def +(rhv: MatrixDims): MatrixDims = (rows+rhv.rows) x (cols+rhv.cols)
	
	override def equals(that: Any) = {
		that match {
			case d: MatrixDims => rows == d.rows && cols == d.cols
			case _ => false
		}
	}
	
	override def toString = rows.number+"x"+cols.number
	
}


object IntToDims {

	implicit def integer_to_MatrixRows(r: Int): MatrixRows = new MatrixRows(r)
	implicit def integer_to_MatrixCols(c: Int): MatrixCols = new MatrixCols(c)

}
