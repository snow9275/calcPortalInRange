
//x: 経度
//y: 緯度
package findOptimizedCoordinates;

import java.io.PrintWriter;

public class PortalsMap {

	//プロット可能ポータル最大数
	final private int max = 1000;
	//縦横最大マス目
	final private int grid = 20;
	final private int cap = 30;
	private int gridX;
	private int gridY;
	int[][][] portalMap;
	Portal[] portal = new Portal[max];
	private int portalNum;
	//各ポータルによるの各座標最大値
	private double left;
	private double right;
	private double top;
	private double bottom;
	//gridを分割した際の各X,Y値
	private double ratioX;
	private double ratioY;
	//レンジ直径設定のためのサンプル点a,bの座標
	//会津若松駅
	final private double aX = 139.9304;
	final private double aY = 37.507713;
	final private double bX = 139.931231;
	final private double bY = 37.507622;
	private double radius;

	PortalsMap(){
		portalNum = 0;
		right = Double.MIN_VALUE;
		left = Double.MAX_VALUE;
		top = Double.MIN_VALUE;
		bottom = Double.MAX_VALUE;
		ratioX = 0;
		ratioY = 0;
		radius = 0;
	}
	//引数は名前、緯度、経度
	void setPortal(String str, double y, double x){

		portal[portalNum] = new Portal(str, x, y);
		portalNum++;
		if(portalNum > max){
			throw new RuntimeException("Possible registration number of portals is overed.");
		}

		if(x > right) right = x;
		else if(x < left) left = x;
		if(y > top) top  = y;
		else if(y < bottom) bottom = y;
	}

	void portalMapping(){
		//マップのグリッド見積もり
		//横縦、縦横の比率
		double ratxy = Math.abs(right-left)/Math.abs(top-bottom);
		double ratyx = Math.abs(top-bottom)/Math.abs(right-left);
		//1gridに対するx,yの長さratioXとratioYを計算
		//縦横に必要なグリッド数を計算

		if(ratxy > 1){

			ratioX = Math.abs(right-left)/ratxy/grid;
			ratioY = Math.abs(top-bottom)/grid;
			gridX = (int)(grid*ratxy)+1;
			gridY = grid+1;
		} else {

			ratioX = Math.abs(right-left)/grid;
			ratioY = Math.abs(top-bottom)/ratyx/grid;
			gridX = grid+1;
			gridY = (int)(grid*ratyx)+1;
		}

		portalMap = new int[gridY][gridX][cap];
		for(int i = 0; i < gridY; i++)
			for(int j = 0; j < gridX; j++)
				for(int k = 0; k < cap; k++)
					portalMap[i][j][k] = -1;

		for(int i = 0; i < portalNum; i++){
			int[] add = addressDecision(portal[i].getCoordinateX(),
					portal[i].getCoordinateY());
			portal[i].setMapNumber(add);
			//ポータルのグリッドx,y(add[0],add[1])からportalMapにi番目のポータルの存在を書き込み
			portal[i].setMapNumber(add);
			for(int j = 0; j < cap ;j++){
				if(portalMap[add[1]][add[0]][j] == -1){
					portalMap[add[1]][add[0]][j] = i;
					break;
				}
			}
		}
		calculate();
	}

	double calcDistance(double x1, double y1, double x2, double y2){
		return Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
	}

	int[] addressDecision(double x, double y){

		double r = left+ratioX;
		double b = bottom+ratioY;
		int gX = 0;
		int gY = 0;

		while(x > r){
			r+=ratioX;
			gX++;		
		}
		while(y > b ){
			b+=ratioY;
			gY++;
		}
		if(gX < 0) gX = 0;
		else if(gY >= gridY) gY = gridY-1;
		if(gY < 0) gY = 0;
		else if(gX >= gridX) gX = gridX-1;

		int[] g = {gX, gY};
		return g;
	}

	void calculate(){
		int ERLeftX;
		int ERRightX;
		int ERTopY = -1;
		int ERBottomY = -1;
		int tmp[] = new int[2];
		double centerX = 0;
		double centerY = 0;
		int rangePortalCount;
		radius = calcDistance(aX, aY, bX, bY)/2;
		double rad = radius;
		int divide = 32;
		//半径調整,16分割
		double dividedValue = (double)(rad/divide);
		//初期位置X,Y
		for(int i = 0; i < portalNum; i++){
			//System.out.println(portal[i].getPortalName());
			portal[i].setBestValue(-1);
			portal[i].setBestCoordinateX(centerX);
			portal[i].setBestCoordinateY(centerY);
	
			//find best coordinate of portal[i]	
			for(int n = 0; n < 400; n++){
				rad = radius;
				dividedValue = (double)(rad/divide);
				for(int p = 0; p < divide; p++){
					rad -= dividedValue;
					centerX = portal[i].getCoordinateX() + rad*Math.cos(Math.toRadians(n));
					centerY = portal[i].getCoordinateY() + rad*Math.sin(Math.toRadians(n));
					rangePortalCount = 0;

					tmp = addressDecision(centerX-radius,centerY);
					ERLeftX = tmp[0];
					tmp = addressDecision(centerX+radius,centerY);
					ERRightX = tmp[0];
					tmp = addressDecision(centerX,centerY+radius);	
					ERTopY = tmp[1];
					tmp = addressDecision(centerX,centerY-radius);
					ERBottomY = tmp[1];
					for(int j = ERBottomY; j <= ERTopY; j++){
						for(int k = ERLeftX; k <= ERRightX; k++){
							for(int m = 0; m < cap; m++){
								if(portalMap[j][k][m] == -1) break;
								int x = portalMap[j][k][m];
								//					System.out.printf(" j:%d k:%d m:%d x:%d ",j,k,m,x);
								/*					System.out.println("x:"+x+"   \t"+portal[x].getPortalName()+"\t"+calcDistance(portal[x].getCoordinateX(), portal[x].getCoordinateY(), centerX, centerY)+" ? "+radius);
						System.out.println(portal[x].getCoordinateY()+" "+portal[x].getCoordinateX()+" | "+centerY+" "+centerX);*/
								if(calcDistance(portal[x].getCoordinateX(), portal[x].getCoordinateY(), centerX, centerY) <=radius){ rangePortalCount++;}

							}
						}
					}
					if(portal[i].getBestValue() < rangePortalCount){
						portal[i].clearInRangeCoordinate();
						portal[i].addCoordinate(centerX, centerY);
						portal[i].setBestValue(rangePortalCount);
						portal[i].setBestCoordinateX(centerX);
						portal[i].setBestCoordinateY(centerY);
					} else if(portal[i].getBestValue() == rangePortalCount){
						portal[i].addCoordinate(centerX, centerY);
					}
					/*			System.out.println(n+" finisied. :"+rangePortalCount);*/
					rangePortalCount = 0;
				}
			}//System.out.println("------------------------------");
		}
	}

	PrintWriter printResult(PrintWriter pw, int x){
		/*
		for(int j = 0; j < portalNum; j++)
			System.out.println(portal[j].getPortalName()
					+"   x: "+portal[j].getCoordinateX()
					+" y: "+portal[j].getCoordinateY());

	for(int i = 0; i < gridY; i++)
		for(int j = 0; j < gridX; j++){
			System.out.printf("%d %d:",i,j);
			for(int k = 0; portalMap[i][j][k] != -1; k++)
				System.out.printf("%d ",portalMap[i][j][k]);
			System.out.println("");
		}


		System.out.println("x: "+gridX+" y: "+gridY);
	for(int i = 0; i < gridY; i++){
		for(int j = 0; j < gridX; j++){
			System.out.printf("%d %d:",i,j);
			for(int k = 0; k < cap; k++)
			if(portalMap[i][j][k] == -1) {System.out.println(k+" 番目で終了"); break;}
			else System.out.printf("%d ",k);
		}
	}
		 */
		for(int i = 0; i < portalNum; i++){
			//			System.out.println(portal[i].getBestValue());
			if(x <= portal[i].getBestValue()){
				//print the best coordinates of portal
				/*
								pw.println(portal[i].getPortalName()+","+portal[i].getBestCoordinateY()+","+portal[i].getBestCoordinateX());
								System.out.println(portal[i].getPortalName()+":"+portal[i].getBestValue()+" "+portal[i].getBestCoordinateY()+","+portal[i].getBestCoordinateX());
				*/
				//print the best all points to stand
				/*
				for(int j = 0; j < portal[i].getInRangeCoordinateSize(); j++){
					pw.println(portal[i].getPortalName()+","+portal[i].getInRangeCoordinateX(j)+","+portal[i].getInRangeCoordinateY(j));
				}
				*/
				//print the best optimized coordinates of portal
				/*
				pw.println(portal[i].getPortalName()+","+portal[i].getInRangeCoordinateX(0)+","+portal[i].getInRangeCoordinateY(0));
				*/pw.println(portal[i].getPortalName()+","+portal[i].getInRangeCoordinateX(portal[i].getInRangeCoordinateSize()-1)+","+portal[i].getInRangeCoordinateY(portal[i].getInRangeCoordinateSize()-1));
			
			}
		}
		return pw;
	}
}