package com.leafplain.demo.sharedelementexample;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.leafplain.demo.sharedelementexample.databinding.ActivityMainBinding;
import com.leafplain.demo.sharedelementexample.presenter.MainPresenter;

public class MainActivity extends AppCompatActivity {

    private MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new MainPresenter(this);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setMainPresenter(presenter);
    }


    public static class MainOpenType{
        public static final int RECYCLER_TO_ACTIVITY    = 1;
        public static final int RECYCLER_TO_PAGER       = 2;
        public static final int SWIPE_TO_ACTIVITY       = 3;
    }
}
