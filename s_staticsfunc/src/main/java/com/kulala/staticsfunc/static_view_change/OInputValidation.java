package com.kulala.staticsfunc.static_view_change;

public class OInputValidation {
	public OInputValidation(){
	}
	//=======================================================
	/**����ֻ��Ź淶11λnumber**/
	public static boolean chkInputPhoneNum(String str){
		if(str.matches("^[1-9][0-9]{10}$"))return true;
//		if(str.matches("^[1-9][0-9]{11}$"))return true;
//		if(str.matches("^[1-9]/d{10}$"))return true;
		return false;
	}
	/**����û�����ʽ�淶6-15λ**/
	public static boolean chkInputReg_UserName(String str){
		//[a-zA-Z0-9_] == /w)��^[a-zA-Z][a-zA-Z0-9_]{4,15}
		if(str.matches("^[a-zA-Z][a-zA-Z0-9_]{5,15}$")){
			return true;
		}
		return false;
	}
	/**��������ʽ�淶6-31λ**/
	public static boolean chkInputPassword(String str){
		if(str.matches("^[a-zA-Z0-9_][a-zA-Z0-9_]{5,30}$")){
			return true;
		}
		return false;
	}
	public static boolean chkRepWords(String words1,String words2){
		if(words1.equals(words2)){
			return true;
		}
		return false;
	}
	//=======================================================
}
