package model;

import com.google.gson.JsonArray;

import java.util.List;

import model.score.DataScore;

public class ManagerScore {
	public int                     scoreAll;//总积分
	public         List<DataScore> everyDayInfos;//每日任务
	public         List<DataScore> newComerInfos;//新手任务
	public         List<DataScore> scoreInfos;//积分明细
	// ========================out======================
	private static ManagerScore    _instance;
	private ManagerScore() {
		init();
	}
	public static ManagerScore getInstance() {
		if (_instance == null)
			_instance = new ManagerScore();
		return _instance;
	}
	private void init() {
	}
	// ========================get=========================
	// ========================put======================
	public void saveScoreAll(int scoreAll1) {
		scoreAll = scoreAll1;
	}
	public void saveEveryDayInfos(JsonArray everyDayInfos1) {
		if (everyDayInfos1 == null) return;
		this.everyDayInfos = DataScore.fromJsonArray(everyDayInfos1);
	}
	public void saveNewComerInfos(JsonArray newComerInfos1) {
		if (newComerInfos1 == null) return;
		this.newComerInfos = DataScore.fromJsonArray(newComerInfos1);
	}
	public void saveScoreInfos(JsonArray scoreInfos1) {
		if (scoreInfos1 == null) return;
		this.scoreInfos = DataScore.fromJsonArray(scoreInfos1);
	}
}
