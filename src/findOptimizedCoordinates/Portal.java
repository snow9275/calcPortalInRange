package findOptimizedCoordinates;

import java.util.*;

public class Portal {

	private String portalName;
	//このポータル座標x,y(経度、緯度)
	private double coordinateX;
	private double coordinateY;
	//このポータルの最適な座標x,y(経度、緯度)
//	private double bestCoordinateX;
//	private double bestCoordinateY;
	//最適座標でのレンジ内ポータル数
	private int bestValue;
	//マップ上の位置 map[0]:x 経度, map[1]:y 緯度
	private int[] map = new int[2];
	private List<ArrayList<Double> > inRangeCoordinateListX = new ArrayList<ArrayList<Double> >();
	private List<ArrayList<Double> > inRangeCoordinateListY = new ArrayList<ArrayList<Double> >();
	Portal(){}
	Portal(String name, double x, double y){
		portalName = name;
		coordinateX = x;
		coordinateY = y;
	}

	String getPortalName(){
		return portalName;
	}

	double getCoordinateX(){
		return coordinateX;
	}

	double getCoordinateY(){
		return coordinateY;
	}
/*	void setBestCoordinateX(double x){
		bestCoordinateX = x;
	}

	void setBestCoordinateY(double y){
		bestCoordinateY = y;
	}

	double getBestCoordinateX(){
		return bestCoordinateX;
	}

	double getBestCoordinateY(){
		return bestCoordinateY;
	}
*/
	void setBestValue(int value){
		bestValue = value;
	}

	int getBestValue(){
		return bestValue;
	}

	void setMapNumber(int[] num){
		map[0] = num[0];
		map[0] = num[1];
	}

	int getMapNumber(int x){
		return map[x];
	}

	void addCoordinateListX(ArrayList<Double> list){
		inRangeCoordinateListX.add(list);
	}

	void addCoordinateListY(ArrayList<Double> list){
		inRangeCoordinateListY.add(list);
	}
	
	int getInRangeCoordinateListSize(){
		return inRangeCoordinateListX.size();
	}

	int getInRangeCoordinateSize(int i){
		return inRangeCoordinateListX.get(i).size();
	}
	
	double getInRangeCoordinateX(int i, int j){
		return inRangeCoordinateListX.get(i).get(j);
	}

	double getInRangeCoordinateY(int i, int j){
		return inRangeCoordinateListY.get(i).get(j);
	}

	void clearInRangeCoordinateList(){
		
		for(int i = 0; i < inRangeCoordinateListX.size(); i++){
		inRangeCoordinateListX.clear();
		inRangeCoordinateListY.clear();
		}
		
	}
}
