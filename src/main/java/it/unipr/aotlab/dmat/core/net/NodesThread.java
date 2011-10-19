package it.unipr.aotlab.dmat.core.net;

public class NodesThread implements Nodes {

	@Override
	public void configureNode() {
	}

	@Override
	public Node makeNode() {
		return new NodeThread();
	}
}
