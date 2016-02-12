package sca.distributed.lib;

import sca.distributed.api.MultiplyService;

public class MultiplyServiceImpl implements MultiplyService{

	public MultiplyServiceImpl (){
		System.out.println("Multiply_Component used");
	}
	
	@Override
	public Object multiply(Object a, Object b) {
		
		double[][] m1 = (double[][])a;
		double[][] m2 = (double[][])b;
		double[][] matrix = new double[m1.length][m2[0].length];
 		
		for (int p = 0; p < m2[0].length; p++) {
			for (int m = 0; m < m1.length; m++) {
				for (int n = 0; n < m1[m].length; n++) {
					matrix[m][p] += m1[m][n] * m2[n][p];
				}
			}
		}		
		return matrix;
	}

}
