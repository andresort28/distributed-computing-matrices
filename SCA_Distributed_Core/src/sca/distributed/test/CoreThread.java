package sca.distributed.test;

import sca.distributed.api.MultiplyService;

public class CoreThread extends Thread
{
	private Controller control;
	private MultiplyService multiplyService;
	private int i;
	private int j;
	private int node;
	
	public CoreThread (Controller control, MultiplyService multiplyService, int i, int j, int node)
	{
		this.control = control;
		this.multiplyService = multiplyService;
		this.i = i;
		this.j = j;
		this.node = node;
	}
	
	
	@Override
	public void run ()
	{
		System.out.println("Node "+ (node+1) + " running..." );
		Object matrix = multiplyService.multiply(control.getMatrix(i), control.getMatrix(j));
		control.receiveMatrix((double[][])matrix, i, j, node);
	}
	
	
}
