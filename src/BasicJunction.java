import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;


public class BasicJunction implements Junction {
	private Integer id;
	private Queue<Integer> carQueue;
	private Hashtable<Integer,Car> cars;
	private Hashtable<Integer,Road> destinations;
	private Hashtable<Junction,Road> exits;
	private Integer count;
	BasicJunction(Integer id){
		exits = new Hashtable<Junction,Road>();		
		carQueue =  new LinkedList<Integer>();
		cars = new Hashtable<Integer,Car>();
		destinations = new Hashtable<Integer,Road>();
		count = 0;
		this.id = id;
	}

	@Override
	public String getType() {		
		return "basic";
	}

	@Override
	public void addCar(Car car, Road from, Junction to) {
		cars.put(count, car);
		carQueue.add(count);		
		destinations.put(count, exits.get(to));
		count++;
	}		
	

	@Override
	public Road exit(Car car) {
		if(cars.get(carQueue.peek()) == car)
			return destinations.get(carQueue.remove());			
		return null;
	}

	@Override
	public Road connect(Junction junction, Double distance,  Double speed, Integer id) {
		if(junction.getExits().containsKey(this)){
			Road temp = junction.getExits().get(this);
			exits.put(junction, temp);
			return temp;
		}
		else{
			Road temp = new BasicRoad(this,junction,distance,speed,id);
			exits.put(junction, temp);
			junction.connect(this,distance,speed,id,temp);
			return temp;
		}
		
	}
	
	@Override
	public Road connect(Junction junction, Double distance,  Double speed, Integer id, Road road) {
		exits.put(junction,road);
		return road;
		
	}

	@Override
	public Hashtable<Junction, Road> getExits() {
		return exits;
	}

	@Override
	public Integer getId() {
		return id;
	}

}
