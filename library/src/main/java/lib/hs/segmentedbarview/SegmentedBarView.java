package lib.hs.segmentedbarview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by husong on 2017/3/10.
 * github：https://github.com/danledian/SegmentedBar
 */

public class SegmentedBarView extends View{


    private int outerMarginColor;
    private float outerMarginWidth;
    private float outerRadii;
    private int selectedColor;
    private int unSelectedColor;
    private int selectedTextColor;
    private int unSelectedTextColor;
    private boolean isShowLine;
    private int mTextSize;
    private boolean isCircleFrame;

    private Paint mBgPaint;
    private Paint mSegPaint;
    private Paint mTextPaint;
    private Rect mRect;
    private Shape mRoundRectShape, mLeftRoundRectShape, mRightRoundRectShape, mRectShape;

    private List<SegmentItem> mShapeEntities = new ArrayList<>();

    private int select;

    private OnSegItemClickListener mOnSegItemClickListener;

    public interface OnSegItemClickListener{
        void onSegItemClick(SegmentItem item, int position);
    }

    public SegmentedBarView(Context context) {
        this(context, null);
    }

    public SegmentedBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        if(context != null){
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SegmentedBarView);

            outerRadii = ta.getDimensionPixelSize(R.styleable.SegmentedBarView_seg_outerRadii, dip2px(5));
            outerMarginWidth = ta.getDimensionPixelSize(R.styleable.SegmentedBarView_seg_outerMarginWidth, dip2px(1));
            selectedColor = ta.getColor(R.styleable.SegmentedBarView_seg_selectedColor, Color.DKGRAY);
            unSelectedColor = ta.getColor(R.styleable.SegmentedBarView_seg_unSelectedColor, Color.BLUE);
            outerMarginColor = ta.getColor(R.styleable.SegmentedBarView_seg_outerMarginColor, Color.WHITE);
            isShowLine = ta.getBoolean(R.styleable.SegmentedBarView_seg_isShowLine, false);
            selectedTextColor = ta.getColor(R.styleable.SegmentedBarView_seg_selectedTextColor, Color.WHITE);
            unSelectedTextColor = ta.getColor(R.styleable.SegmentedBarView_seg_unSelectedTextColor, Color.MAGENTA);
            mTextSize = ta.getDimensionPixelSize(R.styleable.SegmentedBarView_seg_textSize, sp2px(15));
            isCircleFrame = ta.getBoolean(R.styleable.SegmentedBarView_seg_isCircleFrame, false);

            ta.recycle();
        }

        mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.DITHER_FLAG);
        mBgPaint.setAntiAlias(true);
        mBgPaint.setStyle(Paint.Style.STROKE);
        mBgPaint.setStrokeWidth(outerMarginWidth);
        mBgPaint.setColor(outerMarginColor);

        mSegPaint = new Paint(Paint.ANTI_ALIAS_FLAG| Paint.DITHER_FLAG);
        mSegPaint.setAntiAlias(true);
        mSegPaint.setStyle(Paint.Style.FILL);


        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG| Paint.DITHER_FLAG);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(selectedTextColor);

        mRect = new Rect();
        mShapeEntities = new ArrayList<>();

    }

    public OnSegItemClickListener getOnSegItemClickListener() {
        return mOnSegItemClickListener;
    }

    public void setOnSegItemClickListener(OnSegItemClickListener listener) {
        this.mOnSegItemClickListener = listener;
    }

    private void onSegItemClick(SegmentItem item, int position){
        if(mOnSegItemClickListener != null)
            mOnSegItemClickListener.onSegItemClick(item, position);
    }

    public void addSegmentedBars(List<SegmentItem> entities){
        mShapeEntities.addAll(entities);
        invalidate();
    }

    public int getSelect() {
        return select;
    }

    public void setSelect(int select) {
        this.select = select;
    }

    public int getOuterMarginColor() {
        return outerMarginColor;
    }

    public void setOuterMarginColor(int outerMarginColor) {
        this.outerMarginColor = outerMarginColor;
    }

    public int getSelectedColor() {
        return selectedColor;
    }

    public void setSelectedColor(int selectedColor) {
        this.selectedColor = selectedColor;
    }

    public int getUnSelectedColor() {
        return unSelectedColor;
    }

    public void setUnSelectedColor(int unSelectedColor) {
        this.unSelectedColor = unSelectedColor;
    }

    public int getSelectedTextColor() {
        return selectedTextColor;
    }

    public void setSelectedTextColor(int selectedTextColor) {
        this.selectedTextColor = selectedTextColor;
    }

    public int getUnSelectedTextColor() {
        return unSelectedTextColor;
    }

    public void setUnSelectedTextColor(int unSelectedTextColor) {
        this.unSelectedTextColor = unSelectedTextColor;
    }

    public boolean isShowLine() {
        return isShowLine;
    }

    public void setShowLine(boolean showLine) {
        isShowLine = showLine;
    }

    public boolean isCircleFrame() {
        return isCircleFrame;
    }

    public void setCircleFrame(boolean circleFrame) {
        isCircleFrame = circleFrame;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        canvas.getClipBounds(mRect);

        outerRadii = isCircleFrame?getSegHeight()/2:outerRadii;

        drawSegments(canvas);

        drawBackground(canvas);

    }


    private int getCount(){
        return mShapeEntities == null?0:mShapeEntities.size();
    }

    private SegmentItem getItem(int position){
        position = Math.max(Math.min(position, getCount() - 1), 0);
        return mShapeEntities == null?null:mShapeEntities.get(position);
    }

    private void drawSegments(Canvas canvas) {
        if(mShapeEntities.size() < 2){
            return;
        }
        int beforeSave = canvas.save();
        float segWidth = getSegWidth();
        float segHeight = getSegHeight();
        Shape segShape;
        canvas.translate(0, outerMarginWidth);
        for (int i = 0;i< mShapeEntities.size();i++){
            String title = mShapeEntities.get(i).getTitle();
            if(select == i || i == clickPosition){
                mSegPaint.setColor(selectedColor);
                mTextPaint.setColor(selectedTextColor);
            }else {
                mSegPaint.setColor(unSelectedColor);
                mTextPaint.setColor(unSelectedTextColor);
            }
            if(i == 0){
                segShape = createLeftRoundRectShape();
            }else if(i + 1 == mShapeEntities.size()){
                segShape = createRightRoundRectShape();
            }else {
                segShape = createCenterRoundRectShape();
            }
            segShape.resize(segWidth, mRect.bottom - mRect.top - 2 * outerMarginWidth);
            float dx = i==0?outerMarginWidth:segWidth;
            float x = segWidth/2 - mTextPaint.measureText(title)/2;
            float y = segHeight/2 - (mTextPaint.ascent() + mTextPaint.descent())/2;
            canvas.translate(dx, 0);
            int save = canvas.save();
            segShape.draw(canvas, mSegPaint);
            canvas.restoreToCount(save);

            if(isShowLine && i > 0 && i< getCount()){
                canvas.drawLine(0, 0, 0, segHeight, mBgPaint);
            }

            canvas.drawText(title, x, y, mTextPaint);

        }
        canvas.restoreToCount(beforeSave);
    }

    private int clickPosition = -1;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!isEnabled())
            return false;
        if(event.getAction() == MotionEvent.ACTION_DOWN ||
                event.getAction() == MotionEvent.ACTION_MOVE){
            if(isContain(event.getX(), event.getY())){
                clickPosition = getPosition(event.getX());
                invalidate();
            }
            return true;
        }else if(event.getAction() == MotionEvent.ACTION_UP){
            setChangePosition();
        }
        return super.onTouchEvent(event);
    }

    private boolean isContain(float x, float y){
        return x >=outerMarginWidth  && x <= getMeasuredWidth() - outerMarginWidth &&
                y > outerMarginWidth && y <= getMeasuredHeight() - outerMarginWidth;
    }


    public void setChangePosition(){
        if(clickPosition != -1 && clickPosition != select){
            select = clickPosition;
            clickPosition = -1;
            onSegItemClick(getItem(select), select);
            invalidate();
        }
    }

    private int getPosition(float y){
       int position = (int) ((y - outerMarginWidth)/getSegWidth());
        position = Math.max(0, Math.min(position, getCount() - 1));
        return position;
    }


    private float getSegHeight(){
        return mRect.bottom - mRect.top - 2 * outerMarginWidth;
    }

    private float getSegWidth(){
        return (mRect.right - mRect.left - 2 * outerMarginWidth)/mShapeEntities.size();
    }
    private void drawBackground(Canvas canvas) {
        createRoundRectShape();
        canvas.save();
        mRoundRectShape.resize(mRect.right - mRect.left - 2 *outerMarginWidth, mRect.bottom - mRect.top - 2*outerMarginWidth);
        canvas.translate(outerMarginWidth, outerMarginWidth);
        mRoundRectShape.draw(canvas, mBgPaint);
        canvas.restore();
    }


    private Shape createRoundRectShape(){
        if(mRoundRectShape != null)
            return mRoundRectShape;
        float[] allRadii = new float[]{outerRadii, outerRadii, outerRadii, outerRadii, outerRadii, outerRadii, outerRadii, outerRadii};
        mRoundRectShape = new RoundRectShape(allRadii, null, allRadii);
        return mRoundRectShape;
    }

    private Shape createLeftRoundRectShape(){
        if(mLeftRoundRectShape != null)
            return mLeftRoundRectShape;
        float[] allRadii = new float[]{outerRadii, outerRadii, 0f, 0f, 0f, 0f, outerRadii, outerRadii};
        mLeftRoundRectShape = new RoundRectShape(allRadii, null, allRadii);
        return mLeftRoundRectShape;
    }

    private Shape createRightRoundRectShape(){
        if(mRightRoundRectShape != null)
            return mRightRoundRectShape;
        float[] allRadii = new float[]{0f, 0f, outerRadii, outerRadii, outerRadii, outerRadii, 0f, 0f};
        mRightRoundRectShape = new RoundRectShape(allRadii, null, allRadii);
        return mRightRoundRectShape;
    }

    private Shape createCenterRoundRectShape(){
        if(mRectShape != null)
            return mRectShape;
        mRectShape = new RectShape();
        return mRectShape;
    }

    private int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public int sp2px(float spValue) {
        final float fontScale = getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable parcelable = super.onSaveInstanceState();
        PullToLoadState pullToLoadState = new PullToLoadState(parcelable);
        pullToLoadState.select = select;
        return pullToLoadState;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if(!(state instanceof PullToLoadState))
            return;
        PullToLoadState pullToLoadState = ((PullToLoadState)state);
        super.onRestoreInstanceState(pullToLoadState.getSuperState());
        select = pullToLoadState.select;
        invalidate();
    }


    private static class PullToLoadState extends BaseSavedState{

        private int select;

        public static final Creator CREATOR = new Creator<PullToLoadState>(){

            @Override
            public PullToLoadState createFromParcel(Parcel source) {
                return new PullToLoadState(source);
            }

            @Override
            public PullToLoadState[] newArray(int size) {
                return new PullToLoadState[size];
            }
        };

        private PullToLoadState(Parcel superState) {
            super(superState);
            select = superState.readInt();
        }

        private PullToLoadState(Parcelable source) {
            super(source);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(select);
        }

    }

}
