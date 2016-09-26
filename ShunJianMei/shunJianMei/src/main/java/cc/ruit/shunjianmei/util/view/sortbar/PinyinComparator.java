package cc.ruit.shunjianmei.util.view.sortbar;

import java.util.Comparator;

import cc.ruit.shunjianmei.net.response.RewardListResponse;

/**
 * 
 * @author Mr.Z
 */
public class PinyinComparator implements Comparator<RewardListResponse> {

	public int compare(RewardListResponse o1, RewardListResponse o2) {
		if(o1.getSortLetters().equals("@") || o2.getSortLetters().equals("#")) {
			return 1;
		} else if(o1.getSortLetters().equals("#") || o2.getSortLetters().equals("@")) {
			return -1;
		} else {
			return o1.getSortLetters().compareTo(o2.getSortLetters());
		}
	}

}
