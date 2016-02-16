package sca.distributed.api;


public interface ReadService {

	public Object loadFile (String path);	
	public boolean existFile (String path);
}
