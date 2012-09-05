package it.unipr.aotlab.test.dmat

import it.unipr.aotlab.dmat.scalabindings.NetGroup

import junit.framework.Assert.assertEquals;
import junit.framework.Assert.assertTrue;

import org.junit.Test;

class CreateNetgroupTestCase {

  @Test def newWorkGroup {
    object WG extends NetGroup {
	master("master", "192.168.1.2", 5672) has_nodes(
		"testnode0" at ("192.168.1.2",6000),
		"testnode1" at ("192.168.1.2",6001)
	)
    }
    assertEquals(WG.master.name, "master")
    assertEquals(WG.master.ip, "192.168.1.2")
    assertEquals(WG.master.port, 5672)
    assertEquals(WG.master.nodes length, 2)
    assertEquals(WG node "testnode0" ip, "192.168.1.2")
    assertEquals(WG node "testnode0" port, 6000)
    assertEquals(WG node "testnode1" ip, "192.168.1.2")
    assertEquals(WG node "testnode1" port, 6001)
  }

}
