package model;

import com.kulala.dispatcher.OEventName;
import com.kulala.dispatcher.param.ODispatcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import common.GlobalContext;
import model.address.DataAddress;

public class ManagerAddress {
	public List<DataAddress>      addressList;//地址列表
	// ========================out======================
	private static ManagerAddress _instance;
	private ManagerAddress() {
		init();
	}
	public static ManagerAddress getInstance() {
		if (_instance == null)
			_instance = new ManagerAddress();
		return _instance;
	}
	private void init() {
	}
	// =================================================
	public void loadAddress(){
		final String path = "assets/citylist.txt";
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					InputStream       is   = GlobalContext.getContext().getClass().getClassLoader().getResourceAsStream(path);
					InputStreamReader read = new InputStreamReader(is, "utf-8");//考虑到编码格式
					BufferedReader bufferedReader = new BufferedReader(read);
					String         lineTxt        = null;
					addressList = new ArrayList<DataAddress>();
					while ((lineTxt = bufferedReader.readLine()) != null) {
//                    宁夏省:银川市 石嘴山市 吴忠市 固原市 中卫市
						if (lineTxt.length() > 0) {
							String[] arr = lineTxt.split(":");
							if (arr.length >= 2) {
								DataAddress data = new DataAddress();
								data.province = arr[0];
								data.saveCitys(arr[1]);
								addressList.add(data);
							}
						}
					}
					ODispatcher.dispatchEvent(OEventName.ADDRESS_LOAD_OK);
				}catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}catch (IOException e){
					e.printStackTrace();
				}
			}
		}).start();
	}
}
