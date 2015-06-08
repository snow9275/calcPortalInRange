package findOptimizedCoordinates;

import java.io.*;

public class findOptimizedCoordinates {

	public static void main(String args[]){
		PortalsMap map = new PortalsMap();
		//レンジ内最小ポータル数
		int minFarmValue = 4;
		try{
			//サンプルファイルの読み込み出力ファイルの設定
			File rfile  = new File("file/coordinatesOfPortal.csv");
			File wfile = new File("file/farmPoint.csv");
			BufferedReader br = new BufferedReader(new FileReader(rfile));
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(wfile)));
			String str = br.readLine();
			while(str != null){
				String[] strAry = str.split(",");
				map.setPortal(strAry[0], Double.parseDouble(strAry[1]), Double.parseDouble(strAry[2]));
				str = br.readLine();

			}
			//ポータルをマップ上にマッピング
			map.portalMapping();
			pw = map.printResult(pw, minFarmValue);
			System.out.println("Print \"farmPoint.scv\" was finished.");
			//クローズ
			br.close();
			pw.close();
		} catch(FileNotFoundException e) {
			System.out.println(e);
		} catch(IOException e) {
			System.out.println(e);
		}


	}
}
