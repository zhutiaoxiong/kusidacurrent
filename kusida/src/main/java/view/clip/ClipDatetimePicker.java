package view.clip;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.client.proj.kusida.R;
import com.kulala.staticsview.LinearLayoutBase;
import com.kulala.staticsfunc.static_system.ODateTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import common.GlobalContext;
import model.common.DataPicker;
import view.clip.child.ClipPicker;
public class ClipDatetimePicker extends LinearLayoutBase{
	private TextView	title;
	public Button		btn_confirm,btn_cancel;	// super 提供外部事件生成
	private ClipPicker	pick_year, pick_month, pick_date, pick_hour, pick_minute;
	private int			year, month, day, hour, min;
	private int			beforeMaxDay;
	public ClipDatetimePicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.clip_datetime_picker, this, true);
		title = (TextView) findViewById(R.id.title);
		btn_confirm = (Button) findViewById(R.id.btn_confirm);
		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		pick_year = (ClipPicker) findViewById(R.id.pick_year);
		pick_month = (ClipPicker) findViewById(R.id.pick_month);
		pick_date = (ClipPicker) findViewById(R.id.pick_date);
		pick_hour = (ClipPicker) findViewById(R.id.pick_hour);
		pick_minute = (ClipPicker) findViewById(R.id.pick_minute);
		initViews();
		initEvents();
	}
	@Override
	public void initViews() {
		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH) + 1;
		day = c.get(Calendar.DAY_OF_MONTH);
		hour = c.get(Calendar.HOUR_OF_DAY);
		min = c.get(Calendar.MINUTE);
		pick_year.setDataArray(getPairArray(year - 18, 20, getResources().getString(R.string.year)), year, "year", ClipDatetimePicker.this);
		pick_month.setDataArray(getPairArray(1, 12, getResources().getString(R.string.month)), month, "month", ClipDatetimePicker.this);
		pick_date.setDataArray(getPairArrayDate(year, month), day, "day", ClipDatetimePicker.this);
		pick_hour.setDataArray(getPairArray(0, 24, ":"), hour, "hour", ClipDatetimePicker.this);
		pick_minute.setDataArray(getPairArray(0, 60, ""), min, "min", ClipDatetimePicker.this);

	}
	@Override
	public void callback(String key, Object value) {
		if (key.equals("year") || key.equals("month")) {
			year = pick_year.getCurrentNumber();
			month = pick_month.getCurrentNumber();
			int maxday = getMaxDaysByYearMonth(year, month);
			if (beforeMaxDay != maxday) {
				pick_date.setDataArray(getPairArrayDate(year, month), maxday - 1, "day", ClipDatetimePicker.this);
			}
		}
		super.callback(key, value);
	}
	// =============set========================
	public void setTypeOnlyShowDate() {
		pick_hour.setVisibility(View.GONE);
		pick_minute.setVisibility(View.GONE);
	}
	public void setTitle(String str) {
		title.setText(str);
	}
	// =============end=======================
	@Override
	public void initEvents() {
	}

	@Override
	protected void invalidateUI() {

	}

	// ========================get time==============================
	public long getPickerTime() {
		year = pick_year.getCurrentNumber();
		month = pick_month.getCurrentNumber();
		day = pick_date.getCurrentNumber();
		hour = pick_hour.getCurrentNumber();
		min = pick_minute.getCurrentNumber();
		return ODateTime.time2long(year, month, day, hour, min, 0);
	}
	// ====================================================
	private List<DataPicker> getPairArray(int from, int count, String end) {
		List<DataPicker> arr = new ArrayList<DataPicker>();
		for (int i = 0; i < count; i++) {
			DataPicker data = new DataPicker();
			// 加入月日头未10的情况
			String str = String.valueOf(from + i);
			if (str.length() == 1)
				str = "0" + str;
			data.name = str + end;
			data.value = from + i;
			arr.add(data);
		}
		return arr;
	}
	private List<DataPicker> getPairArrayDate(int year, int month) {
		beforeMaxDay = getMaxDaysByYearMonth(year, month);
		List<DataPicker> arr = new ArrayList<DataPicker>();
		for (int i = 0; i < beforeMaxDay; i++) {
			String weekday = getDayOfWeekByDate(year, month, i + 1);
			DataPicker data = new DataPicker();
			// 加入月日头未10的情况
			String str = String.valueOf(i + 1);
			if (str.length() == 1)
				str = "0" + str;
			data.name = str + " " + weekday;
			data.value = i + 1;
			arr.add(data);
		}
		return arr;
	}
	/** 根据年 月 获取对应的月份 天数 **/
	private static int getMaxDaysByYearMonth(int year, int month) {
		Calendar a = Calendar.getInstance();
		a.set(Calendar.YEAR, year);
		a.set(Calendar.MONTH, month - 1);
		a.set(Calendar.DATE, 1);
		a.roll(Calendar.DATE, -1);
		int maxDate = a.get(Calendar.DATE);
		return maxDate;
	}
	/** 根据日期 找到对应日期的 星期"2012-12-25" */
	private static String getDayOfWeekByDate(int year, int month, int day) {
		Calendar a = Calendar.getInstance();
		a.set(Calendar.YEAR, year);
		a.set(Calendar.MONTH, month - 1);
		a.set(Calendar.DATE, day - 1);
		int dayofweek = a.get(Calendar.DAY_OF_WEEK);
		String str = "";
		switch (dayofweek) {
			case 1 :
				str = GlobalContext.getContext().getResources().getString(R.string.monday);
				break;
			case 2 :
				str = GlobalContext.getContext().getResources().getString(R.string.tuesday);
				break;
			case 3 :
				str = GlobalContext.getContext().getResources().getString(R.string.wednesday);
				break;
			case 4 :
				str = GlobalContext.getContext().getResources().getString(R.string.thursday);
				break;
			case 5 :
				str =GlobalContext.getContext().getResources().getString(R.string.friday);
				break;
			case 6 :
				str = GlobalContext.getContext().getResources().getString(R.string.saturday);
				break;
			case 7 :
				str = GlobalContext.getContext().getResources().getString(R.string.sunday);
				break;
		}
		return str;
	}
}
