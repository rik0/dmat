import it.unipr.aotlab.dmat.scalabindings._;

object MyNetGroup extends NetGroup {
	master("master", "192.168.1.160", 5672) has_nodes(
		"testnode0" at ("192.168.1.160",6000),
		"testnode1" at ("192.168.1.160",6001)
	)
}

MyNetGroup executes { program =>
	import program._;
	
	val n = MyNetGroup node "testnode0";
	val a = define( matrix of elements.INT32 size (4 x 4) named "a" 
			split(
			(          B("block1", 2 x 2)             / B("block2", 2 x 2) ) -
			( B("block3", 2 x 1) / B("block4", 2 x 1) / ( B("block5", 1 x 2) -
						                       B("block6", 1 x 2) ) )
		    ) )
	val b = define( matrix size (4 x 2) of elements.INT32 named "b" split(
		B("block1", 1 x 2) - B("block2", 2 x 2) - B("block3", 1 x 2)
	  ) )
	  
	val c = define( matrix named "c" size(2 x 4) of elements.INT32
			split( B("block1", 2 x 1) / B("block2", 2 x 2) / B("block3", 2 x 1) )
		)
	
	println("<<<<< "+c.getClass+" >>>>>")
	
	println( matrix("a") chunk("block1") )
	println( matrix("a") chunk("block2") )
	println( matrix("a") chunk("block3") )
	println( "block4" chunk_of_: matrix("a") )
	println( matrix("a") chunk("block5") )
	println( matrix("a") chunk("block6") )
	
	matrix("a") ~> n
// 	("block3" chunk_of_: matrix("a")) ~> ("testnode1" node_of_: MyNetGroup)
	//n <~ ("block4" chunk_of_: matrix("a"))
	
	b chunk "block1" assignTo n
	(MyNetGroup node "testnode1") <~ ("block2" chunk_of_: b)
	(b chunk "block3") ~> (MyNetGroup node "testnode1")
	
	matrix("a") loadValuesFromFile("pippo") initializeIn(n)
	
// 	matrix("a") does {  _ loadValuesFromFile "pippo" } and { _ sendTo n }
// 	matrix("a") does { mat => mat loadValuesFromFile "pippo"; mat sendTo n; }
	
	println("Hello world!");
}

