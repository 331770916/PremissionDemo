package com.lib.weight.svgmap;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.graphics.Region;



/**
 * 北京各个辖区实体类
 * @author
 */

public class Area {

    private Context context;

    private Paint paint, paintText;
    private boolean isTouch;
    private Region region;
    //属性
    private Path path;
    private String colorValue;
    private String nameValue;





    public Area(Context context) {
        this.context = context;
        paint = new Paint();
        paint.setStrokeWidth(3);
        paint.setAntiAlias(true);

        paintText = new Paint();
        paintText.setAntiAlias(true);
        paintText.setStyle(Paint.Style.FILL);
        paintText.setStrokeWidth(5);
        paintText.setTextSize(20);
        paintText.setColor(Color.BLACK);
    }

    public boolean isTouch() {
        return isTouch;
    }

    public void setTouch(boolean touch) {
        isTouch = touch;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public void setColorValue(String colorValue) {
        this.colorValue = colorValue;
    }

    public String getColorValue() {
        return colorValue;
    }

    public void setNameValue(String nameValue) {
        this.nameValue = nameValue;
    }

    public String getNameValue() {
        return nameValue;
    }

    public void draw(Canvas canvas) {


        if (isTouch()) {
            int color = getRanColor();
            paint.setColor(color);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            paint.setShadowLayer(8, 3, 3, Color.DKGRAY);


        } else {

            paint.setColor(Color.parseColor(getColorValue()));
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
//            paint.setShadowLayer(8, 3, 3, Color.DKGRAY);



        }

//        pathCenterText(canvas);

        canvas.drawPath(path, paint);
    }

    public void pathCenterText(Canvas canvas) {

        PathMeasure measure = new PathMeasure(getPath(), false);
        float[] pos1 = new float[2];
        measure.getPosTan(measure.getLength() / 2, pos1, null);

        /**
         * 计算字体的宽高
         */
        Rect rect = new Rect();
        paintText.getTextBounds(getNameValue(), 0, getNameValue().length(), rect);

        int w = rect.width();
        int h = rect.height();


        canvas.drawText(getNameValue(), pos1[0] - w / 2, pos1[1] + h, paintText);

    }

    private int getRanColor() {
        int[] colors = {Color.GREEN,Color.RED, Color.BLUE};
        return colors[(int) (Math.random() * 3)];
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }
}
