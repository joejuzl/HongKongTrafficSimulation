import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.Stack;


public class Map {
	private Hashtable<Integer,Road> roads;
	private Hashtable<Integer,Junction> junctions;
	private Hashtable<Integer,Car> cars;
	private Double[][] adjacencyMatrix;
	private Hashtable<Integer,LinkedList<Integer>> adjacencyList; 
	Map(){
		System.out.println("Starting Simulation");
		//import data
		ExcelImporter imp = new ExcelImporter("dataInputLarge.xls");
		imp.read();
		junctions = imp.getJunctions();
		roads = imp.getRoads();
		cars = new Hashtable<Integer,Car>();
		adjacencyMatrix = imp.getAdjacencyMatrix();
		adjacencyList = imp.getAdjacencyList();
		System.out.println("Graph is connected: "+isConnected());
		//generate 20 cars with random starts and destinations
		carGenerator(20000);
	}
	public boolean update(){
		//update all cars
		
		while(!cars.isEmpty()){
			Enumeration<Car> enumer = cars.elements();
			while (enumer.hasMoreElements()){
				Car car = enumer.nextElement();
				Junction temp = car.update();
				if (temp != null){
					//remove car when at destination
					System.out.println("Car: "+car.getId()+" has arrived at "+temp.getId()+" from "+car.getBegin().getId()+" in time: "+car.getTime());
					cars.remove(car.getId());
				}
			}
		}
		return true;
	}
	//random car generator
	private void carGenerator(Integer quantity){
		Integer start = 0;
		Integer end = 0;
		Random random = new Random();
		for(int i = 0; i < quantity; i++){
			//random start and end
			while(start==end){
				start = random.nextInt(junctions.size() - 1) + 1;
				end = random.nextInt(junctions.size() - 1) + 1;				
			}
			//calc route and add car with route	
			Stack<Junction> route = calcRoute(junctions.get(start),junctions.get(end));	
			cars.put(i,new Car(junctions.get(start),junctions.get(end),route,i));		
			start=end=0;
		}
	}
	
	//calculate route using Dijkstra's algorithm
	private Stack<Junction> calcRoute(Junction start, Junction end){
		
		//set of unexplored nodes
		Set<Integer> unexpl = new HashSet<Integer>();
		//distances from start to node
		Double[] dist = new Double[junctions.size()+1];
		//previous node in path
		Integer[] prev = new Integer[junctions.size()+1];
		//node to be expanded
		Integer current = null;
		//set all nodes to unexplored, distances to infinity and previous nodes to -1 
		unexpl.addAll(junctions.keySet());
		for (int i = 0; i < dist.length; i++){
			dist[i] = Double.MAX_VALUE;
			prev[i] = -1;
		}
		//System.out.println("Array Initialised!");
		//start to start = 0
		dist[start.getId()] = (double) 0;
		//while still unexplored nodes
		while(!unexpl.isEmpty()){
			Double distance = Double.MAX_VALUE;
			//find shortest path to unexplored node
			for(int i = 0; i<dist.length;i++){
				if(unexpl.contains(i)&&dist[i]<distance){
					distance = dist[i];
					current = i;
				}
			}
			//no new paths explored graph
			if(distance==Double.MAX_VALUE)
				break;
			//remove from set	
			unexpl.remove(current);
			//System.out.println("Node: "+current+" picked!");
			Iterator<Integer> a = unexpl.iterator();
			/*System.out.print("Nodes left: ");
			while(a.hasNext())
				System.out.print(" "+a.next());
			System.out.println();*/
			Iterator<Integer> it = adjacencyList.get(current).iterator();
			//update shortest distances and previous nodes in path
			while(it.hasNext()){
				Integer node = it.next();
				if(distance+adjacencyMatrix[current][node]<dist[node]){
					dist[node] = distance+adjacencyMatrix[current][node];
					prev[node] = current;
					//System.out.println(node+" has new distance: "+dist[node]+", prev: "+prev[node]);
				}				
			}			
		}
		//System.out.println("Shortest paths found!");
		Integer stage = end.getId();
		Stack<Junction> route = new Stack<Junction>();
		//build route by working backwards from end node
		while(!stage.equals(start.getId())){
			if(prev[stage]==-1){
				System.out.println("no route found");
				break;
			}
			//System.out.println(stage+"->"+prev[stage]);
			route.push(junctions.get(stage));
			stage = prev[stage];
		}
		System.out.println("Route generated from "+start.getId()+" to "+end.getId());
		return route;		
	}
	//breadth-first search to determine connectivity of graph
	private boolean isConnected(){
		//set of explored nodes
		Set<Integer> expl = new HashSet<Integer>();
		//nodes to be explored
		Queue<Integer> nodes = new LinkedList<Integer>();
		//add the first
		nodes.add(1);
		//expands nodes in queue
		while(!nodes.isEmpty()){
			Integer node = nodes.remove();
			Iterator<Integer> children = adjacencyList.get(node).iterator();
			while(children.hasNext()){
				Integer child = children.next();
				if(!expl.contains(child))
					nodes.add(child);
			}			
			expl.add(node);
		}
		//if all nodes reached return true
		if(expl.size() == junctions.size())
			return true;
		return false;
	}
}
