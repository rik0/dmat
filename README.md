DMAT LIBRARY
============

### Description

DMat is a library for distributed linear algebra and social networking algorithms

### Installing

#### Requirements

In order to compile the zeroMQ or language branches of DMat, you need the library [zeroMQ](http://www.zeromq.org/) &ge; 2.2.0
and its java bindings ([JZMQ](https://github.com/zeromq/jzmq) &ge; 1.1.0-SNAPSHOT) installed on your system.

##### Compile dependencies in a local folder

The suggested installation of DMat requirements is in a local folder.
In the below example it will be installed in `$BASE/usr`

1. *Install zeromq*

	# Move to the folder where you want to place zeromq sources
	wget 'http://download.zeromq.org/zeromq-2.2.0.tar.gz'
	tar xf zeromq-2.2.0.tar.gz
	cd zeromq-2.2.0
	./autogen.sh
	./configure prefix="$BASE/usr" --with-pgm
	make
	make install

2. *Install JZMQ*

	# Move to the folder where you want to place jzmq sources
	git clone 'git://github.com/zeromq/jzmq.git'
	cd jzmq
	./autogen.sh
	LDFLAGS="-Wl,-rpath -Wl,$BASE/usr/lib" ./configure --with-zeromq="$BASE/usr" --prefix="$BASE/usr"
	make
	make install
	mvn package
	mvn install:install-file -Dfile="`readlink -e target/jzmq-1.1.0-SNAPSHOT-sources.jar`" -DgroupId=org.zeromq -DartifactId=jzmq -Dversion=1.1.0-SNAPSHOT -Dpackaging=jar

3. *Install DMat* read on, but modify pom.xml: in the tag extraJvmArguments change the java.library.path to `$BASE/usr`, i.e. where you have the zeromq natives


#### Compiling DMat

Installation is done via [github](https://github.com) and [maven](http://maven.apache.org).
Simply run a

	git clone git://github.com/rik0/dmat.git

to download the repository in the current folder, then

	mvn package

to compile and package the library.

In the language branch it is strongly recommended that you do a 

	mvn clean

before every mvn compile or package.


LANGUAGE BRANCH
---------------

### Description

The language branch provides what you need to write scripts for the code of the master,
instead of a more verbose java source.
Moreover, you won't have to recompile anything after changes - it's a script!

### Scripts syntax

The scripts are written in the [Scala](http://www.scala-lang.org/) programming language.

The scripts structure is often straightforward: you define the nodes available for computations,
the matrices involved (and how they are distributed) and then the computations.

#### Imports

First of all, let's import our library. It's just
`import it.unipr.aotlab.dmat.scalabindings._`
where the underscore stands from *everything*.

#### How to define a NetGroup

The NetGroup represents the set of nodes available for computations.
Typically the declaration looks this way:

	object MyNetGroup extends NetGroup {
		master(MASTER_NAME,MASTER_IP_ADDRESS,MASTER_PORT) has_nodes(
			NODE1_NAME at (NODE1_IP_ADDRESS, NODE1_PORT),
			NODE2_NAME at (NODE2_IP_ADDRESS, NODE2_PORT),
			...
			NODEN_NAME at (NODEN_IP_ADDRESS, NODEN_PORT)
		)
	}

where:

- the names (ending in NAME) are String-s (surrounded by double quotes)
- the ip addresses (ending in IP\_ADDRESS) are also String-s
- the port numbers (ending in PORT) are Int-egers

You can provide the data directly or define constants for them.
You can give your NetGroup object any name you want (MyNetGroup is only an example),
but remember to inherit from NetGroup.

Now you have your NetGroup. To execute your script (including the computations),
you'll have to pass a function to its *executes* method.
This function will take one parameter and will return Unit (a scala type that is like the C 'void').
So in your script will look like this:

	...	

	MyNetGroup executes { program =>
		import program._
		/* All the rest of your code goes here */
	}

What will be passed as program? You don't need to know.
Just remember to put the placeholder and that import statement,
to be able to create the rest of the script.

When the function terminates, the program automatically disconnects the nodes.

#### Matrix definitions

Minimal example:

	define( matrix of elements.INT32 size(20 x 20) named "A" )


The define function also returns the matrix, that you may want to assign to a *val*.
Anyway anywhere in the function scope you can access your matrix doing `matrix("A")`
(matrix followed by the name of the matrix as a String between parentheses).

Similarly you can define a temporary matrix. This is a special kind of matrix you
can't use directly in computations, but that can be selected to store values in compound computations.

	define_temp( matrix of elements.INT32 size(20 x 20) named "T1" )


Besides the name and the size of the matrix, that are mandatory, you have to specify:

- the type of elements: of elements.INT32 (for a complete list of the elements type, see src/main/scala/it/unipr/aotlab/dmat/scalabindings/typewire/MatrixElementTypes.scala)
- the semiring on which operate: on semiring.INT32TROPICAL (for a complete list of the semirings, see src/main/scala/it/unipr/aotlab/dmat/scalabindings/typewire/Semirings.scala)

Then you can specify how the matrix is distributed among the nodes, e.g.:

	define( matrix on semiring.BOOLEANORDINARY size(4 x 7) named "X" split(
		( B("top-left",2 x 3) / B("top-right",2 x 3) ) -
		( B("bottom-left",2 x 4) / B("bottom-right",2 x 4) )
		) )

splits the matrix "X" in 4 chunks, with specified names and dimensions:

	   012 3456 
	  +---+----+
	0 |   |    |
	1 |   |    |
	  +---+----+
	2 |   |    |
	3 |   |    |
	  +---+----+

Note that the symbol `B` has been used as a function to declare blocks, so you can't declare variables with that name.
A more complicated example:

	define( matrix named "Y" size(11 x 24) of elements.UINT16 split(
		 ( B("LT",6 x 14) - 
		   ( (B("LBLT",3 x 7) - B("LBLB",2 x 7) ) ) / B("LBR",5 x 5) ) / B("C",11 x 1) / 
		 ( B("RTL",3 x 5) / B("RTR",3 x 4) - B("RC",1 x 9) - B("RBT",2 x 9) - B("RBB",5 x 9) )
		) )

becomes:

	               1            2
	    0123456 7890123 4 56789 0123
	   +---------------+-+-----+----+
	 0 |               | |     |    |
	 1 |               | | RTL |RTR |
	 2 |               | |     |    |
	   |      LT       | +-----+----+
	 3 |               | |    RC    |
	   |               | +----------+
	 4 |               | |   RBT    |
	 5 |               |C|          |
	   +-------+-------+ +----------+
	 6 |       |       | |          |
	 7 | LBLT  |       | |          |
	 8 |       |  LBR  | |   RBB    |
	   +-------+       | |          |
	 9 | LBLB  |       | |          |
	10 |       |       | |          |
	   +-------+-------+-+----------+


##### A note on temporaries

As already said, temporaries are a special kind of matrix that you don't use directly, but takes part in compound computations.
As an example, suppose you have 4 matrices `A`,`B`,`C`,`D` of proper sizes, and you want to compute `A*B*C` and store the result in `D`.
The underlying Java implementation of DMat doesn't support the multiplication of three matrices, but only of two.
With the Scala implementation you can do it, like this:

	matrix("D") := matrix("A") * matrix("B") * matrix("C")

But how is this implemented? First the result `A*B` is computed, then it is multiplicated by `C`.
So the program requires a matrix to store the first sub-result (`A*B`). We say this is a _temporary_, because you need it only to reach the final result.
Due to the distributed nature of the library, this temporary must be declared explicitly by the programmer: nobody else knows how this matrix must be associated, if and how it has to be splitted...

Therefore you tipically declare a set of temporary matrices: when the program needs a temporary, the Scala implementation selects one, the _"most suitable"_ for the operation.


#### Nodes assignation

You have to assign each chunk to a node. This node will be responsible of the chunk, and will receive the data that fill the chunk when you perform operations where that chunk belongs to the outputs.
You can access a chunk via any of the following forms:

- `matrix("X").chunk("top-left")`
- `matrix("X") chunk "top-left"`
- `"top-left" chunk_of_: matrix("X")`

The assignation is done via one of the following alternatives:

- `(matrix("X") chunk "top-left") assignTo (MyNetGroup node "nodename1")`
- `(matrix("X") chunk "top-left") ~> (MyNetGroup node "nodename1")`
- `(MyNetGroup node "nodename1") <~ (matrix("X") chunk "top-left")` 

You can also assign a whole matrix to a node: the library will assign each of the matrix chunk to that node.

	matrix("A") ~> (MyNetGroup node "nodename1")

#### Matrix initializations

To initialize the matrix you must provide the filename that contains the values and the nodes in which initialize the matrix.
The most verbose way to do this is, i.e.:

	matrix("A").loadValuesFrom("/path/to/matrix/file")
		   .initializeIn(List[Host](MyNetGroup.node("nodename1"), 
				 MyNetGroup.node("nodename3")))

But you can use the `apply` operator instead of `loadValuesFrom` and/or the `:=>` operator instead of `initializeIn`:
	
	matrix("A")("/path/to/matrix/file") :=> List[Host](MyNetGroup node "nodename1", MyNetGroup node "nodename3")

Moreover, you don't have to provide a List if you want only one node:
	
	matrix("A")("/path/to/matrix/file") :=> (MyNetGroup node "nodename1")

or all the nodes in the NetGroup:
	
	matrix("A")("/path/to/matrix/file") :=> MyNetGroup

Finally, any of the forms you choose, you can invert the order:

	MyNetGroup <=: matrix("A")("/path/to/matrix/file")

#### Computations

You can use directly the matrices with the available operators.
For each operator it is specified if it's built-in in the java
implementation and/or if it's currently available.

##### Assignment

- `:=` simple assignment (copy: built-in)
- `:+=` addition and assignment (built-in)
- `:-=` subtraction and assignment (not implemented)
- `:*=` multiplication and assignment (throws an exception)
- `:=*` pre-multiplication and assignment (throws an exception)

##### Arithmetic

- `-` unary minus (not implemented)
- `+` addition
- `-` subtraction (not implemented)
- `*` product (built-in)

##### Matrix

- `transposed`, `T` transposition (not implemented)

##### Comparison

- `=?=` equality (built-in)
- `=!?=` inequality



