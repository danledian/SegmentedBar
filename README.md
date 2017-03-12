# SegmentedBar
分段选择自定义控件


    分段选择自定义控件，类似QQ的主界面的效果，效果图以QQ里面的效果为例



![效果图](https://github.com/danledian/SegmentedBar/blob/master/image/seg.gif)


## 使用介绍

### xml如下


    <lib.hs.segmentedbarview.SegmentedBarView
        android:layout_width="130dp"
        android:layout_height="34dp"
        android:id="@+id/segmentedBarView"
        android:layout_gravity="center"
        app:seg_selectedTextColor="#00A5E0"
        app:seg_unSelectedTextColor="@android:color/white"
        app:seg_selectedColor="@android:color/white"
        app:seg_unSelectedColor="#12B7F5"
        app:seg_textSize="15sp"
        app:seg_outerRadii="2dp"
        app:seg_outerMarginWidth="1dp"/>

###  初始化

        List<SegmentItem> mList = new ArrayList<>();
        mList.add(new SegmentItem(getResources().getString(R.string.message)));
        mList.add(new SegmentItem(getResources().getString(R.string.phone)));
        mSegmentedBarView.addSegmentedBars(mList);

###  添加监听

        mSegmentedBarView.setOnSegItemClickListener(new SegmentedBarView.OnSegItemClickListener() {
            @Override
            public void onSegItemClick(SegmentItem item, int position) {
                Toast
                .makeText(MainActivity.this, String.format(Locale.ENGLISH, "select:%d", position), Toast.LENGTH_SHORT)
                .show();
            }
        });


## License

MIT
