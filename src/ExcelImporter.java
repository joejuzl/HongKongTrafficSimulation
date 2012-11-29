import java.io.File;
import java.util.Hashtable;
import java.util.LinkedList;

import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;

public class ExcelImporter {
	private Hashtable<Integer,Road> roads;
	private Hashtable<Integer,Junction> junctions;
	private Double[][] adjacencyMatrix;
	private Hashtable<Integer,LinkedList<Integer>> adjacencyList; 
	private Workbook w;
	private Sheet sheet;
	ExcelImporter(String filename){
		adjacencyList = new Hashtable<Integer,LinkedList<Integer>>();
		roads = new Hashtable<Integer,Road>();
		junctions = new Hashtable<Integer,Junction>();		
		File inputWorkbook = new File(filename);		
		try {
			w = Workbook.getWorkbook(inputWorkbook);
			sheet = w.getSheet(0);			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void read(){		
		int roadId = 0;
		try {
			//move along columns
			for (int i = 1; i < sheet.getColumns();i++){
				//split data. first bit is junction id, second is junction type
				String[] data = sheet.getCell(i,0).getContents().split("-");
				Integer id = new Integer(data[0]);
				//if basic junction then
				if (data[1].equals("b")){
					junctions.put(id, new BasicJunction(id));
					System.out.println("added basic junction "+id);
					adjacencyList.put(id,new LinkedList<Integer>());
				}
			}
			//initiate adjacenyMatrix
			adjacencyMatrix = new Double[junctions.size()+1][junctions.size()+1];
			//for each cell in sheet (not including junction names). the roads 
			for(int x = 1; x < sheet.getColumns(); x++){
				for(int y = 1; y<sheet.getRows();y++){
					//pretty much if not null
					if(sheet.getCell(x,y).getType()==CellType.LABEL){
						System.out.println("Road ID: "+roadId+" X: "+x+" Y: "+y+": "+sheet.getCell(x,y).getContents());
						//split data. first is distance, second is speed, third is type
						String[] data = sheet.getCell(x,y).getContents().split("-");
						//update adjacency matrix
						adjacencyMatrix[x][y] = new Double(data[0]);
						//add the road and connect the junctions
						roads.put(roadId, junctions.get(x).connect(junctions.get(y), new Double(data[0]), new Double(data[1]), roadId));
						roadId++;
						adjacencyList.get(x).add(y);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public Hashtable<Integer, Road> getRoads() {
		return roads;
	}

	public Hashtable<Integer, Junction> getJunctions() {
		return junctions;
	}

	public Double[][] getAdjacencyMatrix() {
		return adjacencyMatrix;
	}
	public Hashtable<Integer,LinkedList<Integer>> getAdjacencyList() {
		return adjacencyList;
	}

	

}



