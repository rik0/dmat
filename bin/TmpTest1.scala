import it.unipr.aotlab.dmat.scalabindings._;

object MyNetGroup extends NetGroup {
  val IP_ADDRESS: String = "192.168.1.160"
  
  master("master",IP_ADDRESS,5672) has_nodes(
    "testnode0" at (IP_ADDRESS,6000)
  )
  
}

MyNetGroup executes { program =>
  import program._
  
  val a = define( matrix of elements.INT32 size(20 x 20) named "A" )
  val b = define( matrix of elements.INT32 size(20 x 20) named "B" )
  val acopy = define( matrix of elements.INT32 size(20 x 20) named "Acopy" )
  val e = define( matrix of elements.INT32 size(20 x 20) named "E" )

  a ~> (MyNetGroup node "testnode0")
  b ~> (MyNetGroup node "testnode0")
  acopy ~> (MyNetGroup node "testnode0")
  e ~> (MyNetGroup node "testnode0")
  
  (MyNetGroup node "testnode0") <=: a("file://" + System.getProperty("user.dir") + "/example_matrices/ma.mtx")
  (MyNetGroup node "testnode0") <=: b("file://" + System.getProperty("user.dir") + "/example_matrices/mb.mtx")
  (MyNetGroup node "testnode0") <=: acopy("file://" + System.getProperty("user.dir") + "/example_matrices/ma.mtx")
  (MyNetGroup node "testnode0") <=: e("file://" + System.getProperty("user.dir") + "/example_matrices/mab.mtx")
 
  a := a*b
  
  println("[SCALA] (after a := a*b ) A =?= E: "+(a =?= e))
  
  a := acopy*b
  
  println("[SCALA] (after a := acopy*b) A =?= E: "+(a =?= e))

}
 
