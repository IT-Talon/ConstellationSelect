package com.talon.constellationselect;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_user_detail_birthday)
    TextView tvUserDetailBirthday;
    @BindView(R.id.tv_user_detail_astrology)
    TextView tvUserDetailAstrology;

    private TimePickerView pvTime;

    // astrologyArray="魔羯座, 水瓶座, 双鱼座, 白羊座, 金牛座, 双子座, 巨蟹座, 狮子座, 处女座, 天秤座, 天蝎座, 射手座, 魔羯座";
    private static String[] astrologyArray;

    // String[][] constellations = {{"摩羯座", "水瓶座"}, {"水瓶座", "双鱼座"}, {"双鱼座", "白羊座"}, {"白羊座", "金牛座"}, {"金牛座", "双子座"}, {"双子座", "巨蟹座"}, {"巨蟹座", "狮子座"},
    // {"狮子座", "处女座"}, {"处女座", "天秤座"}, {"天秤座", "天蝎座"}, {"天蝎座", "射手座"}, {"射手座", "摩羯座"}};

    //星座分割时间
    int[] date = {20, 19, 21, 20, 21, 22, 23, 23, 23, 24, 23, 22};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        astrologyArray = getResources().getStringArray(R.array.astrology_array);
        initView();
        initPicker();

    }

    private void initView() {
        tvUserDetailBirthday.setText("0000-00-00");
        tvUserDetailAstrology.setText("");
    }

    private void initPicker() {
        //时间选择器
        pvTime = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        //控制时间范围
        Calendar calendar = Calendar.getInstance();
        pvTime.setRange(calendar.get(Calendar.YEAR) - 100, calendar.get(Calendar.YEAR));//要在setTime 之前才有效果哦
        pvTime.setTime(getDate(tvUserDetailBirthday.getText().toString()));
        pvTime.setCyclic(false);
        pvTime.setCancelable(true);
        //时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                setConstellations(getTime(date));
                tvUserDetailBirthday.setText(getTime(date));
            }
        });
        pvTime.show();
    }

    //星座生成 传进是日期格式为: yyyy-mm-dd
    public void setConstellations(String birthday) {
        String[] data = birthday.split("-");
        int month = Integer.parseInt(data[1]);
        int compareDay = date[month - 1];
        if (Integer.parseInt(data[2]) >= compareDay) {
            tvUserDetailAstrology.setText(astrologyArray[month]);
        } else {
            tvUserDetailAstrology.setText(astrologyArray[month - 1]);
        }
    }

    public static Date getDate(String str) {
        if (str.equals("0000-00-00")) {
            return new Date();
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    @OnClick({R.id.rl_user_detail_birthday, R.id.rl_user_detail_astrology})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_user_detail_birthday:
            case R.id.rl_user_detail_astrology:
                if (!pvTime.isShowing())
                    pvTime.show();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (pvTime.isShowing()) {
                pvTime.dismiss();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
