import it.unipr.aotlab.dmat.scalabindings._;

object MyNetGroup extends NetGroup {
	val IP_ADDRESS: String = "192.168.1.160"
	
	master("master", IP_ADDRESS, 5672) has_nodes(
		"testnode0" at (IP_ADDRESS, 6000),
		"testnode1" at (IP_ADDRESS, 6001),
		"testnode2" at (IP_ADDRESS, 6002),
		"testnode3" at (IP_ADDRESS, 6003)
	)
	
}

MyNetGroup executes { program =>
	import program._;
	
	val a = define( matrix of elements.INT32 size (400 x 400) named "A" )
	val b = define( matrix size (400 x 400) of elements.INT32 named "B" split(
		(  B("top-left", 200 x 200)   /   B("top-right", 200 x 200) ) -
		( B("bottom-left", 200 x 200) / B("bottom-right", 200 x 200) )
	  ) )
	  
	val c = define( matrix named "C" size(400 x 400) of elements.INT32 split(
		(  B("top-left", 200 x 200)   /   B("top-right", 200 x 200) ) -
		( B("bottom-left", 200 x 200) / B("bottom-right", 200 x 200) )
	      ) )
	
	a ~> (MyNetGroup node "testnode0")
	("top-left" chunk_of_: b) ~> ("testnode0" node_of_: MyNetGroup)
	("top-right" chunk_of_: b) ~> ("testnode1" node_of_: MyNetGroup)
	("bottom-left" chunk_of_: b) ~> ("testnode2" node_of_: MyNetGroup)
	("bottom-right" chunk_of_: b) ~> ("testnode3" node_of_: MyNetGroup)
	(MyNetGroup node "testnode1") <~ (c chunk "top-left")
	(MyNetGroup node "testnode2") <~ (c chunk "top-right")
	(MyNetGroup node "testnode3") <~ (c chunk "bottom-left")
	(MyNetGroup node "testnode0") <~ (c chunk "bottom-right")
	
	MyNetGroup.nodes.foreach { _ . print() }
	MyNetGroup <=: b("file://" + System.getProperty("user.dir") + "/example_matrices/large")
	MyNetGroup <=: c("file://" + System.getProperty("user.dir") + "/example_matrices/large")
	
	println("[SCALA] b =?= c: "+(b=?=c))
	
// 	a = b *[_+,_*] d // Won't work
// 	a = b *(_+,_*) d
// 	a _* b
	a := b * c

	println("Hello world!");
}

