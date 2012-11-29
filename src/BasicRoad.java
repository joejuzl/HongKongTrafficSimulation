import java.util.ArrayList;


public class BasicRoad implements Road {
	private Integer id;
	private Junction junction1;
	private Junction junction2;
	private Double distance;
	private ArrayList<Car> cars;
	private Double speed;
	

	public BasicRoad(Junction junction1, Junction junction2, Double distance,  Double speed, Integer id) {
		super();
		this.junction1 = junction1;
		this.junction2 = junction2;
		this.distance = distance;
		this.speed = speed;
		this.id = id;
	}

	@Override
	public Junction getJunction1() {
		return junction1;
	}

	@Override
	public Junction getJunction2() {
		return junction2;
	}

	@Override
	public Double getDistance() {
		return distance;
	}

	@Override
	public ArrayList<Car> getCars() {
		return cars;
	}

	@Override
	public void addCar(Car car) {
		cars.add(car);

	}

	@Override
	public void removeCar(Car car) {
		cars.remove(car);

	}

	@Override
	public Double getSpeed() {
		return speed;
	}

	@Override
	public Integer getId() {
		return id;
	}

}
