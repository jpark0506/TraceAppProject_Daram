package com.example.traceappproject_daram;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import ca.hss.heatmaplib.HeatMap;

public class HeatMapHolder extends HeatMap {
    private int idResource;
    public HeatMapHolder(Context context, int idResource) {
        super(context);
        this.idResource=idResource;
    }
    public HeatMapHolder(Context context, AttributeSet attrs) {

        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,R.styleable.HeatMapHolder,0,0);
        try{

            //이미지 바꾸고 싶으면 여기!!
            this.idResource = R.drawable.crop_reverse_shoes;
            //나중에 여기서 말고 xml에서 지정하게 할 수도 있어요 ㅠㅠ
            //this.idResource = a.getInteger(R.styleable.HeatMapHolder_img_on_top,0);
        }finally {
        }
    }

    public HeatMapHolder(Context context) {
        super(context);
    }

    public void setIdResource(int idResource) {
        this.idResource = idResource;
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        addOverlayImage(canvas);
    }
    //mj01150219 add overlayimage
    public void addOverlayImage(Canvas canvas){
        //점추가하는 코드랑 비슷하게 해보자
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        //paint.setFilterBitmap(true);
        paint.setColor(0xFF0000FF);


        Log.i("mj","overlay alpha : "+paint.getAlpha());
        canvas.drawCircle(500,500,100,paint);
        Resources r = getResources();

        BitmapDrawable bitmapDrawable = (BitmapDrawable) r.getDrawable(idResource);
        Bitmap bitmap = bitmapDrawable.getBitmap();
        int width = 1000;
        int height = 1000;
        Rect frameToDraw = new Rect(0, 0, width, height);
        RectF whereToDraw = new RectF(0, 0, width, height);

        paint = new Paint();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        //paint.setFilterBitmap(true);
        paint.setColor(0xFF0000FF);
        canvas.drawBitmap(bitmap,null,whereToDraw,null);
    }
}
