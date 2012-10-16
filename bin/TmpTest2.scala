import it.unipr.aotlab.dmat.scalabindings._;

object MyNetGroup extends NetGroup {
  val IP_ADDRESS: String = "192.168.1.160"
  
  master("master",IP_ADDRESS,5672) has_nodes(
    "testnode0" at (IP_ADDRESS,6000)
  )
  
}

MyNetGroup executes { program =>
  import program._
  
  val n = MyNetGroup node "testnode0"
  
  val a = define( matrix of elements.INT32 size(20 x 20) named "A" )
  val b = define( matrix of elements.INT32 size(20 x 20) named "B" )
  val c = define( matrix of elements.INT32 size(20 x 20) named "C" )
  val ab = define( matrix of elements.INT32 size(20 x 20) named "AB" )
  val bc = define( matrix of elements.INT32 size(20 x 20) named "BC" )
  val abc = define( matrix of elements.INT32 size(20 x 20) named "ABC" )
  val r = define( matrix of elements.INT32 size(20 x 20) named "R" )
  
	define_temp( matrix of elements.INT32 size(20 x 20) named "temp_T1_temp" ) ~> n
	//define_temp( matrix of elements.INT32 size(20 x 20) named "temp_T2_temp" ) ~> n

  a ~> n
  b ~> n
  c ~> n
  ab ~> n
  bc ~> n
  abc ~> n
	r ~> n 
 
  n <=: a("file://" + System.getProperty("user.dir") + "/example_matrices/ma.mtx")
  n <=: b("file://" + System.getProperty("user.dir") + "/example_matrices/mb.mtx")
  n <=: c("file://" + System.getProperty("user.dir") + "/example_matrices/mc.mtx")
  n <=: ab("file://" + System.getProperty("user.dir") + "/example_matrices/mab.mtx")
  n <=: bc("file://" + System.getProperty("user.dir") + "/example_matrices/mbc.mtx")
  n <=: abc("file://" + System.getProperty("user.dir") + "/example_matrices/mabc.mtx")

  println("[SCALA] A*B =?= AB: "+(a*b =?= ab))
  println("[SCALA] B*C =?= BC: "+(b*c =?= bc))
	println("[SCALA] A*B*C =?= ABC: "+(a*b*c =?= abc))
	
	r := a*b
	println("[SCALA] R*C =?= ABC: "+(r*c =?= abc))
	


}
 
