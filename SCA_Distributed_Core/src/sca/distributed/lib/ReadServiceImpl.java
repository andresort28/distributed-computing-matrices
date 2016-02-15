package sca.distributed.lib;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import sca.distributed.api.ReadService;

public class ReadServiceImpl implements ReadService {

	public ReadServiceImpl (){
		System.out.println("Read_Component used");
	}
	
	@Override
	public Object loadFile(String path) {
		
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
			String[] info = in.readLine().split(" ");
			int m = Integer.valueOf(info[0]);
			int n = Integer.valueOf(info[2]);			
			double[][] matrix = new double[m][n];

			for (int i = 0; i < matrix.length; i++) {
				info = in.readLine().split(" ");
				for (int j = 0; j < matrix[i].length; j++) {
					matrix[i][j] = Double.valueOf(info[j]);
				}
			}
			in.close();
			return matrix;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean existFile(String path) {
		File f = new File(path);
		if(f.exists() || !f.isDirectory())
			return true;
		return false;
	}

}
