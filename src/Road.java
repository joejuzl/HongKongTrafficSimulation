import java.util.ArrayList;


public interface Road {
	
	public Junction getJunction1();
	public Junction getJunction2();
	public Double getDistance();
	public ArrayList<Car> getCars();
	public void addCar(Car car);
	public void removeCar(Car car);
	public Double getSpeed();
	public Integer getId();

}
