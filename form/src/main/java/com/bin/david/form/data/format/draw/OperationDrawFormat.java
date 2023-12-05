package com.bin.david.form.data.format.draw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.Log;

import com.bin.david.form.R;
import com.bin.david.form.core.TableConfig;
import com.bin.david.form.data.CellInfo;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.format.OperationBean;
import com.bin.david.form.utils.DrawUtils;

import java.util.List;

/**
 * Created by huang on 2017/10/30.
 * Bitmap绘制格式化
 */

public abstract class OperationDrawFormat<T> implements IDrawFormat<T> {

    private int imageWidth;
    private int imageHeight;
    private Rect imgRect;
    private Rect drawSpaceRect;
    private int textColor = Color.BLUE;
    private int[] backgroundColor = {Color.BLACK, Color.DKGRAY, Color.GRAY};
    private int fontSize = 20;


    private OnTextOperateItemClickListener onTextOperateItemClickListener;

    public void setOnTextOperateItemClickListener(OnTextOperateItemClickListener onTextOperateItemClickListener) {
        this.onTextOperateItemClickListener = onTextOperateItemClickListener;
    }

    public interface OnTextOperateItemClickListener {

        void onClick(OperationBean bean, CellInfo cellInfo);
    }

    public OperationDrawFormat(int imageWidth, int imageHeight) {
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.imgRect = new Rect();
        this.drawSpaceRect = new Rect();
    }

    public OperationDrawFormat(int imageWidth, int imageHeight, int fontSize, int textColor, int[] backgroundColor) {
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.imgRect = new Rect();
        this.drawSpaceRect = new Rect();
        this.fontSize = fontSize;
        this.textColor = textColor;
        this.backgroundColor = backgroundColor;
    }

    @Override
    public int measureWidth(Column<T> column, int position, TableConfig config) {
        return imageWidth;
    }

    @Override
    public int measureHeight(Column<T> column, int position, TableConfig config) {
        return imageHeight;
    }


    @Override
    public void draw(Canvas canvas, Rect rect, CellInfo<T> cellInfo, TableConfig config) {
        Paint paint = config.getPaint();
        paint.setStyle(Paint.Style.FILL);
        List<OperationBean> data = (List<OperationBean>) cellInfo.data;
        if (data != null && data.size() > 0) {
            // 获取每份宽度
            float spaceWidth = (rect.right - rect.left) / data.size();
            for (int i = 0; i < data.size(); i++) {
                float spaceRight = rect.right - spaceWidth * (data.size() - (i + 1));
                OperationBean operationBean = data.get(i);
                Object item = operationBean.getShow();
                if (item != null) {
                    // 绘制背景颜色
                    paint.setColor(backgroundColor.length > i ? backgroundColor[i] : backgroundColor[0]);
                    canvas.drawRect(rect.left + (spaceWidth * i), rect.top, spaceRight, rect.bottom, paint);
                    if (item instanceof String) {
                        // 绘制文字
                        paint.setColor(textColor);
                        paint.setTextSize(fontSize * config.getZoom()); //以px为单位，乘以缩放
                        paint.setTypeface(Typeface.DEFAULT_BOLD);
                        paint.setTextAlign(Paint.Align.CENTER);
                        // (每份宽度 * 当前份)-(每份除以2)+左坐标
                        float textX = spaceWidth * (i + 1) - (spaceWidth / 2) + rect.left;
                        float textY = DrawUtils.getTextCenterY(rect.centerY(), paint);
                        canvas.drawText(String.valueOf(item), textX, textY, paint);
                    } else if (item instanceof Bitmap){
                        Bitmap bitmap = (Bitmap) item;
                        paint.setColor(Color.BLACK);
                        paint.setStyle(Paint.Style.FILL);
                        int width = bitmap.getWidth();
                        int height = bitmap.getHeight();
                        imgRect.set(0, 0, width, height);
                        float scaleX = (float) width / imageWidth;
                        float scaleY = (float) height / imageHeight;
                        if (scaleX > 1 || scaleY > 1) {
                            if (scaleX > scaleY) {
                                width = (int) (width / scaleX);
                                height = imageHeight;
                            } else {
                                height = (int) (height / scaleY);
                                width = imageWidth;
                            }
                        }
                        width = (int) (width * config.getZoom());
                        height = (int) (height * config.getZoom());
                        int spaceRectL = (int) ((spaceWidth * (i + 1) + rect.left) - (spaceWidth));
                        int spaceRectR = (int) ((spaceWidth * (i + 1) + rect.left));
                        int disXS = (spaceRectR - spaceRectL - width) / 2;
                        int disY = (rect.bottom - rect.top - height) / 2;

                        drawSpaceRect.left = spaceRectL + disXS;
                        drawSpaceRect.top = rect.top + disY;
                        drawSpaceRect.right = spaceRectR - disXS;
                        drawSpaceRect.bottom = rect.bottom - disY;
                        canvas.drawBitmap(bitmap, imgRect, drawSpaceRect, paint);
                    }

                    // 点击事件
                    if (DrawUtils.isClick(rect.left, rect.top, (int) spaceRight, rect.bottom, getClickPoint())) {
                        if (onTextOperateItemClickListener != null) {
                            onTextOperateItemClickListener.onClick(operationBean, cellInfo);
                        }
                        getClickPoint().set(-1, -1);
                    }
                }
            }
        }
    }

    protected abstract Context getContext();

    protected abstract PointF getClickPoint();

}
