DMAT LIBRARY
============

### Description

DMat is a library for distributed linear algebra and social networking algorithms

### Installing

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

Then you can specify a split structure, i.e.:

	define( matrix on semiring.BOOLEANORDINARY size(7 x 4) named "X" split(
		( B("top-left",3 x 2) / B("top-right",3 x 2) ) -
		( B("bottom-left",4 x 2) / B("bottom-right",4 x 2) )
		) )

splits the matrix "X" in 4 chunks, with specified names and dimensions.

#### Nodes assignation

You have to assign each chunk to a node.
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

	matrix("A").loadValuesFrom("/path/to/matrix/file").initializeIn(List[Host](MyNetGroup.node("nodename1"), MyNetGroup.node("nodename3")))

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

##### Assignment

- `:=` simple assignment
- `:+=` addition and assignment

##### Arithmetic

- `*` product

##### Matrix

- `transposed`, `T` transposition

##### Comparison

- `=?=` equality
- `=!?=` inequality



