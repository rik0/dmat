import it.unipr.aotlab.dmat.scalabindings._;

object MyNetGroup extends NetGroup {
  val IP_ADDRESS: String = "192.168.1.160"
  
  master("master",IP_ADDRESS,5672) has_nodes(
    "testnode0" at (IP_ADDRESS,6000),
    "testnode1" at (IP_ADDRESS,6001),
    "testnode2" at (IP_ADDRESS,6002),
    "testnode3" at (IP_ADDRESS,6003)
  )
  
}

MyNetGroup executes { program =>
  import program._
  
  val a = define( matrix of elements.INT32 size(10 x 20) named "A" split( B("top",7 x 20) - B("bottom",3 x 20) ) )
  val e = define( matrix of elements.INT32 size(10 x 20) named "E" )
  
  val b = define( matrix named "B" size(10 x 15) of elements.INT32 split( B("top",7 x 15) - B("bottom",3 x 15) ) )
  val c = define( matrix named "C" size(15 x 20) of elements.INT32 split( B("top",7 x 20) - B("bottom",8 x 20) ) )

  (MyNetGroup node "testnode0") <~ e
  
  (MyNetGroup node "testnode0") <~ (a chunk "top")
  (MyNetGroup node "testnode3") <~ (a chunk "bottom")

  (MyNetGroup node "testnode1") <~ (b chunk "top")
  (MyNetGroup node "testnode2") <~ (b chunk "bottom")

  (MyNetGroup node "testnode1") <~ (c chunk "top")
  (MyNetGroup node "testnode2") <~ (c chunk "bottom")

  (MyNetGroup node "testnode0") <=: e("file://" + System.getProperty("user.dir") + "/example_matrices/e2xe1")
  List[Host]( (MyNetGroup node "testnode2"),(MyNetGroup node "testnode1") ) <=: b("file://" + System.getProperty("user.dir") + "/example_matrices/e2")
  List[Host]( (MyNetGroup node "testnode2"),(MyNetGroup node "testnode1") ) <=: c("file://" + System.getProperty("user.dir") + "/example_matrices/e1")
  
  a := b*c
  println("A as expected? "+(a =?= e))
  
}

