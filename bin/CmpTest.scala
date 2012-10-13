import it.unipr.aotlab.dmat.scalabindings._;

object MyNetGroup extends NetGroup {
  val IP_ADDRESS: String = "192.168.1.160"
  
  master("master",IP_ADDRESS,5672) has_nodes(
    "testnode0" at (IP_ADDRESS,6000)
  )
  
}

MyNetGroup executes { program =>
  import program._
  
  val a = define( matrix of elements.INT32 size(10 x 20) named "A" )
  val e = define( matrix of elements.INT32 size(10 x 20) named "E" )

  a ~> (MyNetGroup node "testnode0")
  e ~> (MyNetGroup node "testnode0")
  
  (MyNetGroup node "testnode0") <=: a("file://" + System.getProperty("user.dir") + "/example_matrices/e2xe1")
  (MyNetGroup node "testnode0") <=: e("file://" + System.getProperty("user.dir") + "/example_matrices/e2xe1")
 
  println("[SCALA] A =?= E: "+(a =?= e))

}
 
