package common.pinyinzhuanhuan;

import java.util.Comparator;

import model.loginreg.DataUser;

/**
 * 
 * @author xiaanming
 *
 */
public class PinyinComparator implements Comparator<DataUser> {

	public int compare(DataUser o1, DataUser o2) {
		if (o1!=null&&o2!=null){
		if (o1.sortLetters.equals("@")
				|| o2.sortLetters.equals("#")) {
			return -1;
		} else if (o1.sortLetters.equals("#")
				|| o2.sortLetters.equals("@")) {
			return 1;
		} else {

		}
		}
		return o1.sortLetters.compareTo(o2.sortLetters);
	}
}



