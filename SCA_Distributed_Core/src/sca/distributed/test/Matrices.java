package sca.distributed.test;
import java.io.File;
import java.io.PrintWriter;
import java.util.Random;


public class Matrices
{	
	/* Minimun number of columns and rows (Ex: 15 ó 1000) */
	final static int min = 100;			
	/* Maximun number of columns and rows (Ex: 20 ó 10000) */
	final static int max = 300;		
	/* Total number of matrices to create (Ex: 4, 8 ó N) */
	final static int total = 20;
	/* Range of numbers within the matrix (for floating point: between 0 and 1)*/
	final static int rank[] = {0, 1};
	/* Variable flag for numbers within the matrix (true: Floating point - false: Integers */
	final static boolean floating = true;
		
	
	public static void main (String[] arg) throws Exception
	{
		double a = System.currentTimeMillis();		
		
		/* First change the static variables above */
		Matrices.createAllMatrices(min, max, total, "data/");
		
		double b = System.currentTimeMillis();
		System.out.println("Processing time: " + (b-a)/1000 + " seconds");
	}
	
		
	public static void createAllMatrices (int min_length, int max_length, int matrices, String location)
	{
		try {
			File file = new File(location + "Data.txt");
			PrintWriter printer = new PrintWriter(file);
			printer.println(matrices);
			System.out.println("Start creating matrices\n");
						
			Random r = new Random();
			int n = (int)(r.nextDouble()*(max_length-min_length+1) + min_length);			
			
			for (int k = 1; k <= matrices; k++) {				
				int m = n;
				n = (int)(r.nextDouble()*(max_length-min_length+1) + min_length);
				while(m == n)
					n = (int)(r.nextDouble()*(max_length-min_length+1) + min_length);
				
				//Matrices.generateMatrix(2000, 10000, location + "M" + k + ".txt");
				Matrices.generateMatrix(m, n, location + "M" + k + ".txt");
				printer.println("M" + k);				
			}
			printer.println("MatrixResult");
			printer.close();
			System.out.println("Data file created: " +location+ "Data.txt");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void generateMatrix (int m, int n, String name)
	{		
		try {
			PrintWriter printer = new PrintWriter(new File(name));
			printer.println(m + " x " + n);
			
			System.out.println("Producing matrix ( " + m + " x " + n + " )...");
			for (int i = 0; i < m; i++) {				
				for (int j = 0; j < n; j++) {
					Random r = new Random();
					double number = r.nextDouble()*(rank[1]-rank[0]+rank[0]) + rank[0];											
					if(floating)
						if(j==0)
							printer.print(number+"");
						else
							printer.print(" "+ number);
					else
						if(j==0)
							printer.print((int)number+"");
						else
							printer.print(" "+ (int)number);
				}
				printer.print("\n");
			}			
			printer.close();
			System.out.println(name + " generated\n");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	
	
	
	
	
	
}
