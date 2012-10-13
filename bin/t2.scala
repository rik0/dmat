import it.unipr.aotlab.dmat.scalabindings._;

object MyNetGroup extends NetGroup {
	master("master", "192.168.1.160", 5672) has_nodes(
		"testnode0" at ("192.168.1.160",6000),
		"testnode1" at ("192.168.1.160",6001)
	)
}

MyNetGroup executes { program =>
	import program._;
	
	val n0 = MyNetGroup node "testnode0";
	val n1 = MyNetGroup node "testnode1";
	val a = define( matrix of elements.INT32 size (400 x 400) named "a" )
	val b = define( matrix size (400 x 400) of elements.INT32 named "b" split(
		B("block1", 200 x 400) - B("block2", 200 x 400)
	  ) )
	  
	val c = define( matrix named "c" size(400 x 400) of elements.INT32
			split( B("block1", 400 x 200) / B("block2", 400 x 200) )
		)
	
	println("<<<<< "+c.getClass+" >>>>>")
	
	a ~> n0
	(b chunk "block1") ~> n0
	(b chunk "block2") ~> n1
	(c chunk "block1") ~> n0
	(c chunk "block2") ~> n1
	
	println(a chunk (null))
	println(b chunk ("block1"))
	println(b chunk ("block2"))
	println(c chunk ("block1"))
	println(c chunk ("block2"))
	
	MyNetGroup <=: b("file://" + System.getProperty("user.dir") + "/example_matrices/large")
	MyNetGroup <=: c("file://" + System.getProperty("user.dir") + "/example_matrices/large")
	
	
	a := b*c
	
	println("Hello world!");
}

