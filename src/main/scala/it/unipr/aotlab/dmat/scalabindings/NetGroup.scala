package it.unipr.aotlab.dmat.scalabindings


class NetGroup {
	
	implicit def stringToName(s: String): HostName = new HostName(s,this)
	
	def master(name: String, ip: String, port: Int): Master = { mymaster = new Master(name,ip,port); mymaster }
	def master(): Master = mymaster
	def node(name: String): Host = mynodes(name)
	def node_of_:(name: String): Host = mynodes(name)
	def executes(f: Program => Unit) = f(new Program)
	
	
	def register(name: String, host: Host)(implicit auth: HostName.AuthToken with NotNull) {
		println("Registering host "+name+"...");
		mynodes.put(name,host)
	}
	
	private var mymaster: Master = null
	private val mynodes = scala.collection.mutable.Map[String,Host]()
}
