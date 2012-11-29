import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;


public class Car {
	private Integer id;
	private Road current;
	private Boolean direction; 
	private Double distance;
	private Double speed;
	private Junction begin;
	private Junction destination;
	private Stack<Junction> route;
	//private Iterator<Junction> step;
	private Boolean waiting;
	private Junction waitingJunc; 
	private Integer time;

	Car(Junction begin, Junction destination, Stack<Junction> route, Integer id){	
		this.id = id;
		this.begin = begin;
		this.destination = destination;
		this.setRoute(route);
		//step = route.iterator();
		waiting = true;
		waitingJunc = begin;	
		waitingJunc.addCar(this, null, this.route.pop());
		time = 0;
	}
	//update car
	public Junction update(){
		time++;
		//if on road		
		if(!waiting){
			//System.out.println("Car: "+id+" is on road: "+current.getId());
			//move along road according to speed
			speed = current.getSpeed();
			distance -= speed;
			//if not at end of road yet return false
			if(distance>0){
				return null;
			}
			//if at end of road
			else{				
				//set junction that car arrives at
				if(direction){
					waitingJunc = current.getJunction2();
				}
				else{
					waitingJunc = current.getJunction1();
				}	
				//if at destination, return true
				if (waitingJunc.equals(destination))
					return destination;				
				//find next junction in route
				Junction temp = route.pop();
				//add car to junction and set as waiting
				waitingJunc.addCar(this, current, temp);
				waiting = true;
			}
		}
		//if in junction
		else {
			//System.out.println("Car: "+id+" is at junction: "+waitingJunc.getId());
			//ask to exit
			current = waitingJunc.exit(this);
			//if exited
			if (current != null){
				//set current road
				waiting = false;
				distance = current.getDistance();
				speed = current.getSpeed();
				if(waitingJunc == current.getJunction1())
					direction = true;
				else
					direction = false;
			}
		}
		return null;
	}
	public Stack<Junction> getRoute() {
		return route;
	}
	public void setRoute(Stack<Junction> route) {
		this.route = route;
	}
	public Integer getId(){
		return id;
	}
	public Integer getTime(){
		return time;
	}
	public Junction getBegin(){
		return begin;
	}


}
