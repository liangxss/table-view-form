package com.bin.david.form.data.format.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Typeface;

import com.bin.david.form.core.TableConfig;
import com.bin.david.form.data.CellInfo;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.utils.DrawUtils;

import java.util.List;

/**
 * Created by huang on 2017/10/30.
 * Bitmap绘制格式化
 */

public abstract class OperationDrawFormat<T> implements IDrawFormat<T> {

    private int imageWidth;
    private int imageHeight;
    private int textColor = Color.BLUE;
    private int[] backgroundColor = {Color.BLACK, Color.DKGRAY, Color.GRAY};
    private int fontSize = 20;


    private OnTextOperateItemClickListener onTextOperateItemClickListener;
    public void setOnTextOperateItemClickListener(OnTextOperateItemClickListener onTextOperateItemClickListener){
        this.onTextOperateItemClickListener = onTextOperateItemClickListener;
    }
    public interface OnTextOperateItemClickListener {

        void onClick(String text, CellInfo cellInfo);
    }

    public OperationDrawFormat(int imageWidth, int imageHeight) {
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
    }
    public OperationDrawFormat(int imageWidth, int imageHeight, int textColor, int[] backgroundColor) {
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.textColor = textColor;
        this.backgroundColor = backgroundColor;
    }

    @Override
    public int measureWidth(Column<T>column,int position, TableConfig config) {
        return  imageWidth;
    }

    @Override
    public int measureHeight(Column<T> column,int position, TableConfig config) {

        return imageHeight;
    }


    @Override
    public void draw(Canvas canvas, Rect rect, CellInfo<T> cellInfo ,TableConfig config) {
        Paint paint = config.getPaint();
        paint.setStyle(Paint.Style.FILL);
        List<String> data = (List<String>) cellInfo.data;
        if (data!= null && data.size() > 0){
            // 获取每份宽度
            float right = (rect.right - rect.left) / data.size();
//                    Log.e("PagerModeDemoActivity", "right: " + right);
            for (int i = 0; i < data.size(); i++) {
                // 每块右坐标为：当前rect右坐标 减 每块宽度乘以剩余份数
                // 例如：每份70，右坐标800，
                // 第一块为：800-(70*(3-(0+1)))=660
                // 第二块为：800-(70*(3-(1+1)))=730
                // 第二块为：800-(70*(3-(2+1)))=800
                float rectRight = rect.right - right * (data.size() - (i + 1));

                // 绘制背景颜色
                paint.setColor(backgroundColor.length > i ? backgroundColor[i] : backgroundColor[0]);
                canvas.drawRect(rect.left + (right * i), rect.top, rectRight, rect.bottom, paint);

                // 绘制文字
                paint.setColor(textColor);
                paint.setTextSize(fontSize * config.getZoom()); //以px为单位，乘以缩放
                paint.setTypeface(Typeface.DEFAULT_BOLD);
                paint.setTextAlign(Paint.Align.CENTER);
                // (每份宽度 * 当前份)-(每份除以2)
                float textX = (right * (i + 1) + rect.left) - (right / 2);
                float textY = DrawUtils.getTextCenterY(rect.centerY(),paint);
                canvas.drawText(data.get(i),textX, textY, paint);

                // 点击事件
                if (DrawUtils.isClick(rect.left, rect.top, (int) rectRight, rect.bottom, getClickPoint())) {
                    if (onTextOperateItemClickListener != null) {
                        onTextOperateItemClickListener.onClick(data.get(i), cellInfo);
                    }
                    getClickPoint().set(-1, -1);
                }
            }
        }
    }

    protected abstract Context getContext();

    protected abstract PointF getClickPoint();

}
