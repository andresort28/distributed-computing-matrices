package sca.distributed.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import org.osoa.sca.annotations.Reference;

import sca.distributed.api.MultiplyService;
import sca.distributed.api.ReadService;

public class Controller implements Runnable{
	
	public final static int ORIGINAL_NODES = 8;
	public static int NODES = 0;
	public static String PATH = "data";
	public static int MATRICES = -1;
	public static boolean OPTIMIZATION = false;
		
	@Reference 
	protected ReadService readService;
		
	
	//protected MultiplyService multiplyService1, multiplyService2, multiplyService3, multiplyService4, multiplyService5, multiplyService6, multiplyService7, multiplyService8;
	@Reference	
	protected MultiplyService multiplyService1, multiplyService2, multiplyService3, multiplyService4, multiplyService5, multiplyService6, multiplyService7, multiplyService8;
	
	private MultiplyService[] references;
	private Queue<Integer> nodes;
	private Object[] matrices;
	private boolean[] processed;
	private String[] names;
	private int operations;
	
		
	public Controller () {
		System.out.println("\n------------------------------------------------");
		System.out.println("Controller_Component used\n");
	}

	@Override
	public void run() 
	{
		try {
			references = new MultiplyService[ORIGINAL_NODES];
			references[0] = multiplyService1;
			references[1] = multiplyService2;
			references[2] = multiplyService3;
			references[3] = multiplyService4;
			references[4] = multiplyService5;
			references[5] = multiplyService6;
			references[6] = multiplyService7;
			references[7] = multiplyService8;		
			
			
			while(true)
			{				
				System.out.println("Choose a Test:\n");
				BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream("data.txt")));
				int n = Integer.valueOf(in.readLine());	
				for (int i = 0; i < n; i++)
					System.out.println(in.readLine());
				
				boolean config = false;
				while(!config)
				{
					try {
						System.out.println("\nTest: ");
						in = new BufferedReader (new InputStreamReader (System.in));
						int number = Integer.valueOf(in.readLine());
						PATH = "data/" + number + "/";
						if(number>=1 && number<=n)
							config = true;
						else
							System.out.println("Please, type a valid test number");
					} catch (Exception e) {
						System.out.println("Please, type a valid test number");
						config = false;
					}
				}
				
				config = false;
				while(!config)
				{
					try {
						System.out.println("\nTotal Nodes: " + ORIGINAL_NODES);
						System.out.println("Process with: ");
						in = new BufferedReader (new InputStreamReader (System.in));
						int number = Integer.valueOf(in.readLine());
						if(number>=1 && number<=ORIGINAL_NODES){
							NODES = number;
							config = true;
						}
						else
							System.out.println("Please, type a valid number of nodes");
					} catch (Exception e) {
						System.out.println("Please, type a valid number of nodes");
						config = false;
					}
				}
				execute();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	
	public void execute ()
	{
		try 
		{	
			double a = System.currentTimeMillis();	
			
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(PATH + "Data.txt")));
			MATRICES = Integer.valueOf(in.readLine());	
			System.out.println("\nMATRICES: " + MATRICES);
			System.out.println("NODES: " + NODES);			
			
			matrices = new Object[MATRICES];
			processed = new boolean[MATRICES];
			names = new String[MATRICES];
			for (int i = 0; i < names.length; i++)
				names[i] = (i+1)+"";
			operations = 0;
			nodes = new LinkedList<Integer>();
			for (int i = 0; i < NODES; i++)
				nodes.add(i);
			
			
			printAvailableNodes();
			System.out.println("\n\n");
			
			for (int i = 0; i < MATRICES; i++) {
				if(i%2 == 0){ 					
					if(i == MATRICES-1)
						matrices[i] = readService.loadFile(PATH + in.readLine() + ".txt");	
					else {
						matrices[i] = readService.loadFile(PATH + in.readLine() + ".txt");	
						matrices[i+1] = readService.loadFile(PATH + in.readLine() + ".txt");
						executeConcurrentProcessing(i, i+1);
					}
				}
			}
			String name = in.readLine();
			in.close();	
			
			while(operations < MATRICES -1){
//				if((MATRICES-operations) <= (NODES*2 + 1))
//					OPTIMIZATION= true;
				searchMatricesToProcess();	
			}
			
			System.out.println("\nWriting the final matrix...");
			writeMatrix((double[][])matrices[0], name);
			System.out.println("\nProcess completed: " + "("+ PATH + name+".txt)");
			System.out.println("Multiplication process: " + names[0]);			
			double b = System.currentTimeMillis();
			System.out.println("\nTotal processing time: " + ((b-a)/1000) + " seconds");
			System.out.println("------------------------------------------------\n\n");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void executeConcurrentProcessing (int i, int j)
	{
		if(OPTIMIZATION) {			
			int rows = ((double[][])matrices[i]).length;
			processed[i] = true;
			processed[j] = true;
			printAvailableNodes();
			int node = searchNode();
			System.out.println("Will start the concurrent process: M" + names[i] + " - Node " + (node+1));
			for (int r = 0; r < rows; r++) {
				printAvailableNodes();
				node = searchNode();
				CoreThread2 thread = new CoreThread2(this, references[node], i, j, r, node);
				thread.start();
			}
			names[i] = "(" +names[i]+ "," +names[j]+ ")";
		}
		else {
			printAvailableNodes();
			int node = searchNode();
			CoreThread thread = new CoreThread(this, references[node], i, j, node);		
			processed[i] = true;
			processed[j] = true;
			names[i] = "(" +names[i]+ "," +names[j]+ ")";
			System.out.println("Will start the concurrent process: M" + names[i] + " - Node " + (node+1));
			thread.start();
		}
	}
		
	public int searchNode ()
	{
		boolean avalaible = false;
		while(!avalaible)
		{
			try {
				if(nodes.isEmpty()) {
					System.out.println("\nWaiting for free nodes...");
					while(nodes.isEmpty())
						Thread.sleep(100);
					printAvailableNodes();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(checkNode(nodes.peek()))
				avalaible = true;
			else
				nodes.remove();
		}		
		return nodes.remove();		
	}
	
	public void searchMatricesToProcess()
	{		
		for (int i = 0; i < processed.length; i++) {	
			if(i%2 == 0 && processed[i] == false) {
				for (int j = i+1; j < processed.length; j++) {
					if(matrices[j] != null) {
						if(processed[j] == false) {
							executeConcurrentProcessing(i, j);
							break;
						}
						else
							break;
					}
				}
			}
		}			
	}
	
	public boolean checkNode (int i) 
	{
		int port = 2000 +(i+1);
		try {
			Socket socket = new Socket("localhost", port);
			System.out.println("Node " + (i+1) + " is UP");
			socket.close();
			return true;
		} catch (Exception e) {
			System.out.println("Node " + (i+1) + " is DOWN");
			return false;
		}
	}
	
	
	public void receiveMatrix (double[][] matrix, int i, int j, int node) 	
	{		
		matrices[i] = matrix;
		matrices[j] = null;
		processed[i] = false;
		synchronized (nodes) {
			nodes.add(node);	
		}
		operations++;						
		System.out.println("Matrix " + names[i] + " was received - Node: " + (node+1) );
	}
	
	public void receiveRowMatrix (double[][] rowM, int i, int j, int row, int node) 	
	{		
		double[][] m = (double[][])matrices[i];
		m[row] = rowM[0];
		matrices[i] = m;
		if( (row+1) == m.length){
			matrices[j] = null;
			processed[i] = false;
			operations++;	
		}
		synchronized (nodes) {
			nodes.add(node);			
		}							
		System.out.println("Row "+row+" of Matrix " + names[i] + " was received - Node: " + (node+1) );
	}
	
	
	public void writeMatrix (double[][] matrix, String name) 
	{
		try {			
			PrintWriter printer = new PrintWriter(new File(PATH + name + ".txt"));
			String line = "";
			printer.println(matrix.length + " x " + matrix[0].length);
			
			for (int i = 0; i < matrix.length; i++) {
				for (int j = 0; j < matrix[0].length; j++) {
					if(j==0)
						line = matrix[i][j]+"";
					else
						line += " " + matrix[i][j];
				}
				printer.println(line);
			}			
			printer.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Object getMatrix (int i) {
		return matrices[i];
	}
	
	public Object getRowMatrix (int i, int row) {
		double[][] m = (double[][])matrices[i];
		double[][] rowM = new double[1][m[0].length];
		rowM[0] = m[row];
		return rowM;
	}
	
	public void printAvailableNodes () {
		String line = "";
		synchronized (nodes) {
			Iterator<Integer> i = nodes.iterator();
			while(i.hasNext())
				line = (i.next()+1) + "-" + line;
		}
		System.out.println("Nodes available: [ " + line + " ->>");		
	}
}
