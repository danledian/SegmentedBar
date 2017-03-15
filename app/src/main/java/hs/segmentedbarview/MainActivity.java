package hs.segmentedbarview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import lib.hs.segmentedbarview.SegmentItem;
import lib.hs.segmentedbarview.SegmentedBarView;

/**
 * @author husong
 * github：https://github.com/danledian/SegmentedBar
 *
 */
public class MainActivity extends AppCompatActivity {


    private SegmentedBarView mSegmentedBarView;
    private SegmentedBarView mSegmentedBarView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSegmentedBarView = (SegmentedBarView)findViewById(R.id.segmentedBarView);
        mSegmentedBarView1 = (SegmentedBarView)findViewById(R.id.segmentedBarView1);

        List<SegmentItem> mList = new ArrayList<>();
        mList.add(new SegmentItem(getResources().getString(R.string.message)));
        mList.add(new SegmentItem(getResources().getString(R.string.phone)));
        mSegmentedBarView.addSegmentedBars(mList);

        mSegmentedBarView.setOnSegItemClickListener(new SegmentedBarView.OnSegItemClickListener() {
            @Override
            public void onSegItemClick(SegmentItem item, int position) {
                Toast
                .makeText(MainActivity.this, String.format(Locale.ENGLISH, "select:%d", position), Toast.LENGTH_SHORT)
                .show();
            }
        });

        mList.clear();
        mList.add(new SegmentItem("F"));
        mList.add(new SegmentItem("J"));
        mList.add(new SegmentItem("S"));

        mSegmentedBarView1.addSegmentedBars(mList);

    }
}
