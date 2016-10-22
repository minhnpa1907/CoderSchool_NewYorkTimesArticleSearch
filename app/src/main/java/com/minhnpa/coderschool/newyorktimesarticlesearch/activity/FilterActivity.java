package com.minhnpa.coderschool.newyorktimesarticlesearch.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.minhnpa.coderschool.newyorktimesarticlesearch.R;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by MINH NPA on 22 Oct 2016.
 */

public class FilterActivity extends Activity implements View.OnClickListener {
    @BindView(R.id.cbArts)
    CheckBox cbArts;

    @BindView(R.id.cbFashionStyle)
    CheckBox cbFashionStyle;

    @BindView(R.id.cbSports)
    CheckBox cbSports;

    @BindView(R.id.spnrSort)
    Spinner spnrSort;

    @BindView(R.id.btnOK)
    Button btnOK;

    @BindView(R.id.btnCancel)
    Button btnCancel;

    @BindView(R.id.tvBeginDate)
    TextView tvBeginDate;

    String sort;
    boolean filterArts;
    boolean filterFashionStyle;
    boolean filterSports;
    private int year, month, day;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        ButterKnife.bind(this);

        sort = getIntent().getStringExtra("sort");
        filterArts = getIntent().getBooleanExtra("filter_arts", false);
        filterFashionStyle = getIntent().getBooleanExtra("filter_fashion_style", false);
        filterSports = getIntent().getBooleanExtra("filter_sports", false);
        day = getIntent().getIntExtra("day", 21);
        month = getIntent().getIntExtra("month", 6);
        year = getIntent().getIntExtra("year", 2015);

        setBeginDateTextView(year, month, day);
        setSpinnerToValue(spnrSort, sort);
        cbArts.setChecked(filterArts);
        cbFashionStyle.setChecked(filterFashionStyle);
        cbSports.setChecked(filterSports);

        btnOK.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    private void setBeginDateTextView(int year, int month, int day) {
        StringBuilder date = new StringBuilder();
        if (month < 10) {
            date.append("0").append(month);
        } else {
            date.append(month);
        }
        date.append("/");
        if (day < 10) {
            date.append("0").append(day);
        } else {
            date.append(day);
        }
        date.append("/");
        date.append(year);

        tvBeginDate.setText(date);
    }

    public void setSpinnerToValue(Spinner spinner, String value) {
        int index = 0;
        SpinnerAdapter adapter = spinner.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).equals(value)) {
                index = i;
                break;
            }
        }
        spinner.setSelection(index);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOK:
                Intent data = new Intent();
                Bundle bundle = new Bundle();

                bundle.putInt("day_picked", day);
                bundle.putInt("month_picked", month);
                bundle.putInt("year_picked", year);
                bundle.putString("sorted", spnrSort.getSelectedItem().toString());
                bundle.putBoolean("filtered_arts", cbArts.isChecked());
                bundle.putBoolean("filtered_fashion_style", cbFashionStyle.isChecked());
                bundle.putBoolean("filtered_sports", cbSports.isChecked());

                data.putExtras(bundle);
                setResult(RESULT_OK, data);
                finish();
                break;
            case R.id.btnCancel:
                setResult(RESULT_CANCELED);
                finish();
                break;
        }
    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "Pick a date", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            year = arg1;
            month = arg2 + 1;
            day = arg3;
            setBeginDateTextView(year, month, day);
        }
    };
}
