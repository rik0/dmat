import it.unipr.aotlab.dmat.scalabindings._;

object MyNetGroup extends NetGroup {
  val IP_ADDRESS: String = "192.168.1.160"
  
  master("master",IP_ADDRESS,5672) has_nodes(
    "testnode0" at (IP_ADDRESS,6000),
    "testnode1" at (IP_ADDRESS,6001),
    "testnode2" at (IP_ADDRESS,6002)
  )
  
}

MyNetGroup executes { program =>
  import program._
  
  val a = define( matrix of elements.INT32 size(10 x 20) named "A" )
  val e = define( matrix of elements.INT32 size(10 x 20) named "E" )
  
  val b = define( matrix named "B" size(10 x 15) of elements.INT32 split(
            ( B("left",10 x 10) / ( B("right-top",4 x 5) - B("right-bottom",6 x 5) ) )
      ) )
  val c = define( matrix named "C" size(15 x 20) of elements.INT32 split(
            ( ( B("left-top", 6 x 10) - ( B("left-bottom-left",9 x 5) / B("left-bottom-right",9 x 5) ) ) / ( B("center",15 x 5) / (B("right-top",4 x 5) - B("right-bottom",11 x 5)) ) )
      ) )

  e ~> (MyNetGroup node "testnode2")
  a ~> (MyNetGroup node "testnode2")

  (MyNetGroup node "testnode1") <~ (b chunk "left")
  (MyNetGroup node "testnode0") <~ (b chunk "right-top")
  (MyNetGroup node "testnode2") <~ (b chunk "right-bottom")

  (MyNetGroup node "testnode0") <~ (c chunk "left-top")
  (MyNetGroup node "testnode1") <~ (c chunk "left-bottom-left")
  (MyNetGroup node "testnode0") <~ (c chunk "left-bottom-right")
  (MyNetGroup node "testnode1") <~ (c chunk "center")
  (MyNetGroup node "testnode0") <~ (c chunk "right-top")
  (MyNetGroup node "testnode1") <~ (c chunk "right-bottom")

  println(c chunk "left-top")
  println(c chunk "left-bottom-left")
  println(c chunk "left-bottom-right")
  println(c chunk "center")
  println(c chunk "right-top")
  println(c chunk "right-bottom")

  MyNetGroup <=: b("file://" + System.getProperty("user.dir") + "/example_matrices/e2")
  List[Host]( (MyNetGroup node "testnode0"),(MyNetGroup node "testnode1") ) <=: c("file://" + System.getProperty("user.dir") + "/example_matrices/e1")
  (MyNetGroup node "testnode2") <=: e("file://" + System.getProperty("user.dir") + "/example_matrices/e2xe1")
  
  a := b*c
  //println("A as expected? "+(a =?= e))
  
}

