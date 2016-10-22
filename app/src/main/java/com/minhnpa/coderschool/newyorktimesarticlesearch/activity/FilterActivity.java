package com.minhnpa.coderschool.newyorktimesarticlesearch.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.minhnpa.coderschool.newyorktimesarticlesearch.R;

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

    String sort;
    boolean filterArts;
    boolean filterFashionStyle;
    boolean filterSports;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        ButterKnife.bind(this);

        sort = getIntent().getStringExtra("sort");
        filterArts = getIntent().getBooleanExtra("filter_arts", false);
        filterFashionStyle = getIntent().getBooleanExtra("filter_fashion_style", false);
        filterSports = getIntent().getBooleanExtra("filter_sports", false);

        setSpinnerToValue(spnrSort, sort);
        cbArts.setChecked(filterArts);
        cbFashionStyle.setChecked(filterFashionStyle);
        cbSports.setChecked(filterSports);

        btnOK.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
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
}
