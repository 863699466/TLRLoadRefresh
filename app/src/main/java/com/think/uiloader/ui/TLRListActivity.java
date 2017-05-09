package com.think.uiloader.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.think.tlr.Log;
import com.think.tlr.TLRLinearLayout;
import com.think.tlr.TLRUiHandler;
import com.think.uiloader.App;
import com.think.uiloader.R;
import com.think.uiloader.data.entity.ImageEntity;
import com.think.uiloader.ui.di.components.ActivityComponent;
import com.think.uiloader.ui.di.components.DaggerActivityComponent;
import com.think.uiloader.ui.mvp.contract.ImageContract;
import com.think.uiloader.ui.mvp.presenter.ImagePresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by borney on 4/28/17.
 */
public class TLRListActivity extends AppCompatActivity implements ImageContract.View {
    private ListView mListView;
    private TLRLinearLayout mTLRLinearLayout;
    private ListImageAdapter mAdapter;
    private List<ImageEntity.Image> mImageList = new ArrayList<>();
    private App mApp;
    private int curIndex = 0;

    @Inject
    ImagePresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApp = (App) getApplication();
        initActivityComponent();
        setContentView(R.layout.activity_tlrlistview);
        mListView = (ListView) findViewById(R.id.content);
        mTLRLinearLayout = (TLRLinearLayout) findViewById(R.id.tlrlayout);
        mTLRLinearLayout.addTLRUiHandler(new TLRUiHandler() {
            @Override
            public void onRefreshStatusChanged(TLRLinearLayout.RefreshStatus status) {
                if (status == TLRLinearLayout.RefreshStatus.REFRESHING) {
                    mPresenter.images(curIndex, 10);
                }
            }

            @Override
            public void onLoadStatusChanged(TLRLinearLayout.LoadStatus status) {

            }

            @Override
            public void onOffsetChanged(int totalOffsetY, int totalThresholdY, int offsetY, float threshOffset) {

            }
        });
        mAdapter = new ListImageAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(TLRListActivity.this, "onclick " + position, Toast.LENGTH_SHORT).show();
            }
        });

        mTLRLinearLayout.autoRefresh();
    }

    private void initActivityComponent() {
        ActivityComponent component = DaggerActivityComponent.builder().applicationComponent(
                mApp.getApplicationComponent()).build();
        component.inject(this);
        mPresenter.setView(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void startImages() {

    }

    @Override
    public void imagesSuccess(List<ImageEntity.Image> images) {
        Log.w(images.toString());
        if (images != null) {
            mImageList.addAll(0, images);
            curIndex += images.size();
            mAdapter.notifyImages(mImageList);
        }
    }

    @Override
    public void endImages() {

    }

    @Override
    public void error(int errorCode) {

    }
}
