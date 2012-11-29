import java.util.Hashtable;


public interface Junction {	
	public String getType();
	public void addCar(Car car, Road from, Junction to);
	public Road exit(Car car);
	public Road connect(Junction junction, Double distance,  Double speed, Integer id);
	public Road connect(Junction junction, Double distance,  Double speed, Integer id, Road road);
	public Hashtable<Junction,Road> getExits();
	public Integer getId();
}
