package it.unipr.aotlab.dmat.scalabindings

import it.unipr.aotlab.dmat.core.registers.zeroMQ.NodeWorkGroup;
import it.unipr.aotlab.dmat.core.net.zeroMQ.Nodes;
import it.unipr.aotlab.dmat.core.net.IPAddress;

object NetGroup {
	sealed trait AuthToken
	private implicit object Auth extends AuthToken
}

class NetGroup {
	import NetGroup._
	
	implicit def stringToName(s: String): HostName = new HostName(s,this)
	
	def master(name: String, ip: String, port: Int): Master = {
	  mymaster = new Master(name,ip,port);
	  jimpl_ng = NodeWorkGroup.builder()
			.masterId(name)
			.masterAddress(new IPAddress(ip,port))
			.build();
	  jimpl_ns = new Nodes(jimpl_ng);
	  mymaster
	}
	def master(): Master = mymaster
	def node(name: String): Host = mynodes(name)
	def node_of_:(name: String): Host = mynodes(name)
	def nodes(): Iterable[Host] = mynodes.values
	def executes(f: Program => Unit) = {
	      try {
		jimpl_ng.initialize();
		f(new Program);
		jimpl_ng.shutDown();
	      } finally {
		jimpl_ng.close();
	      }
	}
	
	
	def register(name: String, host: Host)(implicit auth: HostName.AuthToken with NotNull) {
		println("[SCALA] Registering host "+name+"...");
		if (! host.isInstanceOf[Master])
		  host.setImplementation(
		      jimpl_ns.setNodeName(name)
			.setNodeAddress(new IPAddress(host.ip,host.port))
			.build()
		    );
		mynodes.put(name,host)
	}
	
	private var mymaster: Master = null
	private val mynodes = scala.collection.mutable.Map[String,Host]()
	
	private var jimpl_ng: NodeWorkGroup = _;
	private var jimpl_ns: Nodes = _;
}
