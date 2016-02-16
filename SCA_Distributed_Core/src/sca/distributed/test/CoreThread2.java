package sca.distributed.test;

import sca.distributed.api.MultiplyService;

public class CoreThread2 extends Thread
{
	private Controller control;
	private MultiplyService multiplyService;
	private int i;
	private int j;
	private int row;
	private int node;
	
	public CoreThread2 (Controller control, MultiplyService multiplyService, int i, int j, int row, int node)
	{
		this.control = control;
		this.multiplyService = multiplyService;
		this.i = i;
		this.j = j;
		this.row = row;
		this.node = node;
	}
	
	
	@Override
	public void run ()
	{
		System.out.println("Node "+ (node+1) + " running..." );
		Object rowMatrix = multiplyService.multiply(control.getRowMatrix(i, row), control.getMatrix(j));
		control.receiveRowMatrix((double[][])rowMatrix, i, j, row, node);
	}
	
	
}
