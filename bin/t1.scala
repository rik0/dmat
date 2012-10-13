import it.unipr.aotlab.dmat.scalabindings._;

object MyNetGroup extends NetGroup {
	val IP_ADDRESS: String = "192.168.1.160"
	master("master", IP_ADDRESS, 5672) has_nodes(
		"testnode0" at (IP_ADDRESS,6000)
	)
}

MyNetGroup executes { program =>
	import program._;
	
	val n = MyNetGroup node "testnode0";
	val a = define( matrix of elements.INT32 size (800 x 800) named "A" )
	val b = define( matrix size (800 x 800) of elements.INT32 named "B" )
	val c = define( matrix named "C" size(800 x 800) of elements.INT32 )
	
	a ~> n
	b ~> n
	c ~> n
	
	n <=: b("file://" + System.getProperty("user.dir") + "/example_matrices/large")
	c("file://" + System.getProperty("user.dir") + "/example_matrices/large") :=> n
	
	a := b*c
	
	println("Hello world!");
}

