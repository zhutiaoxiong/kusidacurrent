package model.address;
import java.util.ArrayList;
import java.util.List;

public class DataAddress {
	public String province = "";
	public List<String> cityList;

	public void saveCitys(String citys){
		if(citys == null || citys.length()<0)return;
		cityList = new ArrayList<String>();
		String[] arr = citys.split(" ");
		for(String str : arr){
			if(str.length()>0){
				cityList.add(str);
			}
		}
	}
}
