package com.bin.david.smarttable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.core.TableConfig;
import com.bin.david.form.data.CellInfo;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.format.IFormat;
import com.bin.david.form.data.format.OperationBean;
import com.bin.david.form.data.format.bg.BaseBackgroundFormat;
import com.bin.david.form.data.format.bg.BaseCellBackgroundFormat;
import com.bin.david.form.data.format.count.ICountFormat;
import com.bin.david.form.data.format.draw.ImageResDrawFormat;
import com.bin.david.form.data.format.draw.TextImageDrawFormat;
import com.bin.david.form.data.format.draw.OperationDrawFormat;
import com.bin.david.form.data.format.title.TitleImageDrawFormat;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.data.table.PageTableData;
import com.bin.david.form.data.table.TableData;
import com.bin.david.form.listener.OnColumnItemClickListener;
import com.bin.david.form.utils.DensityUtils;
import com.bin.david.smarttable.bean.ChildListData;
import com.bin.david.smarttable.bean.UserInfoDemo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class PagerModeDemoActivity extends AppCompatActivity implements View.OnClickListener {

    private SmartTable<UserInfoDemo> table;
    private PageTableData<UserInfoDemo> tableData;
    private PointF clickPoint;
    private int fontSize = 14;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);
        FontStyle.setDefaultTextSize(DensityUtils.sp2px(this, 15)); //设置全局字体大小
        table = (SmartTable<UserInfoDemo>) findViewById(R.id.table);
        table.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                clickPoint.x = event.getX();
                clickPoint.y = event.getY();
                return false;
            }
        });
        clickPoint = new PointF(-1, -1);

        final List<UserInfoDemo> testData = new ArrayList<>();
        Random random = new Random();
        OperationBean detailBean = new OperationBean();
        detailBean.setShow("详情");
        detailBean.setOperation("详情");

        OperationBean editBean = new OperationBean();
        editBean.setShow("编辑");
        editBean.setOperation("编辑");

        Bitmap bitmap = BitmapFactory.decodeResource(PagerModeDemoActivity.this.getResources(), R.mipmap.check);
        OperationBean delBean = new OperationBean();
        delBean.setShow(bitmap);
        delBean.setOperation("删除");

        for (int i = 0; i < 500; i++) {
            ChildListData childListData = new ChildListData();
            List<OperationBean> list= new ArrayList<>();
            if (i % 2 == 0){
                list.add(detailBean);
                list.add(editBean);
                list.add(delBean);
            } else if (i % 3 == 0){
                list.add(detailBean);
            } else if (i % 5 == 0){
                list.add(editBean);
                list.add(delBean);
            } else {
            }
            testData.add(new UserInfoDemo("用户" + i, random.nextInt(70), System.currentTimeMillis()
                    - random.nextInt(70) * 3600 * 1000 * 24, false, list, childListData));
        }
        final Column<String> nameColumn = new Column<>("姓名", "name");
        nameColumn.setAutoCount(true);
        final Column<Integer> ageColumn = new Column<>("年龄", "age");
        ageColumn.setAutoCount(true);
        final IFormat<Long> format = new IFormat<Long>() {
            @Override
            public String format(Long aLong) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(aLong);
                return calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);
            }
        };
        final Column<Long> timeColumn = new Column<>("时间", "time", format);
        timeColumn.setCountFormat(new ICountFormat<Long, Long>() {
            private long maxTime;

            @Override
            public void count(Long aLong) {
                if (aLong > maxTime) {
                    maxTime = aLong;
                }
            }

            @Override
            public Long getCount() {
                return maxTime;
            }

            @Override
            public String getCountString() {
                return format.format(maxTime);
            }

            @Override
            public void clearCount() {
                maxTime = 0;
            }
        });
        int size = DensityUtils.dp2px(this, 15);
        Column<Boolean> columnCheck1 = new Column<>("勾选1", "isCheck", new ImageResDrawFormat<Boolean>(size, size) {
            @Override
            protected Context getContext() {
                return PagerModeDemoActivity.this;
            }

            @Override
            protected int getResourceID(Boolean isCheck, String value, int position) {
                if (isCheck != null && isCheck) {
                    return R.mipmap.check;
                } else {
                    return R.mipmap.clock_fill;
                }
            }
        });
        Column<Boolean> columnCheck2 = new Column<>("勾选2", "isCheck", new TextImageDrawFormat<Boolean>(size, size, TextImageDrawFormat.LEFT, 10) {
            @Override
            protected Context getContext() {
                return PagerModeDemoActivity.this;
            }

            @Override
            protected int getResourceID(Boolean isCheck, String value, int position) {
                if (isCheck) {
                    return R.mipmap.clock_fill;
                }
                return 0;
            }
        });

        int[] colors = new int[]{getResources().getColor(R.color.github_con_1), getResources().getColor(R.color.github_con_2), getResources().getColor(R.color.github_con_3)};
        OperationDrawFormat<List> operationDrawFormat = new OperationDrawFormat<List>(size, size, fontSize, getResources().getColor(R.color.colorAccent), colors) {
            @Override
            public int measureWidth(Column<List> column, int position, TableConfig config) {
                return DensityUtils.dp2px(PagerModeDemoActivity.this, 120);
            }

            @Override
            public int measureHeight(Column<List> column, int position, TableConfig config) {
                return DensityUtils.dp2px(PagerModeDemoActivity.this, 30);
            }

            @Override
            public void draw(Canvas canvas, Rect rect, CellInfo<List> cellInfo, TableConfig config) {
                super.draw(canvas, rect, cellInfo, config);
            }

            @Override
            protected Context getContext() {
                return PagerModeDemoActivity.this;
            }

            @Override
            protected PointF getClickPoint() {
                return clickPoint;
            }

        };
        operationDrawFormat.setOnTextOperateItemClickListener(new OperationDrawFormat.OnTextOperateItemClickListener() {
            @Override
            public void onClick(OperationBean item, CellInfo cellInfo) {
                if (item != null){
                    Toast.makeText(PagerModeDemoActivity.this, "点击了" + item.getOperation() + " 用户：" +  testData.get(cellInfo.row).getName(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        Column<List> columnOperate = new Column<>("操作", "operationList", operationDrawFormat);


        tableData = new PageTableData<>("测试", testData, columnCheck1, nameColumn, ageColumn, columnCheck2, timeColumn, columnOperate);
        tableData.setTitleDrawFormat(new TitleImageDrawFormat(size, size, TitleImageDrawFormat.RIGHT, 10) {
            @Override
            protected Context getContext() {
                return PagerModeDemoActivity.this;
            }

            @Override
            protected int getResourceID(Column column) {
                if (!column.isParent()) {
                    if (tableData.getSortColumn() == column) {
                        setDirection(TextImageDrawFormat.RIGHT);
                        if (column.isReverseSort()) {
                            return R.mipmap.sort_up;
                        }
                        return R.mipmap.sort_down;

                    } else {
                        setDirection(TextImageDrawFormat.LEFT);
                        if (column == nameColumn) {
                            return R.mipmap.name;
                        } else if (column == ageColumn) {
                            return R.mipmap.age;
                        } else if (column == timeColumn) {
                            return R.mipmap.update;
                        }
                    }
                    return 0;
                }
                setDirection(TextImageDrawFormat.LEFT);
                int level = tableData.getTableInfo().getMaxLevel() - column.getLevel();
                if (level == 0) {
                    return R.mipmap.level1;
                } else if (level == 1) {
                    return R.mipmap.level2;
                }
                return 0;
            }
        });
        tableData.setOnRowClickListener(new TableData.OnRowClickListener<UserInfoDemo>() {
            @Override
            public void onClick(Column column, UserInfoDemo userInfoDemo, int col, int row) {
                if ("操作".equals(column.getColumnName())){
                    return;
                }
                userInfoDemo.setCheck(!userInfoDemo.isCheck());
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        table.invalidate();
                        table.notifyDataChanged();
                    }
                }, 200);
            }
        });
        columnOperate.setOnColumnItemClickListener(new OnColumnItemClickListener<List>() {
            @Override
            public void onClick(Column<List> column, String value, List list, int position) {

            }
        });
        ageColumn.setOnColumnItemClickListener(new OnColumnItemClickListener<Integer>() {
            @Override
            public void onClick(Column<Integer> column, String value, Integer integer, int position) {
                Toast.makeText(PagerModeDemoActivity.this, "点击了" + value, Toast.LENGTH_SHORT).show();
            }
        });
        columnCheck1.setOnColumnItemClickListener(new OnColumnItemClickListener<Boolean>() {
            @Override
            public void onClick(Column<Boolean> column, String value, Boolean aBoolean, int position) {
                UserInfoDemo userInfoDemo = testData.get(position);
                userInfoDemo.setCheck(!userInfoDemo.isCheck());
//                table.setData(testData);
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        table.invalidate();
                        table.notifyDataChanged();
                    }
                }, 200);

            }
        });
        table.getConfig().setCountBackground(new BaseBackgroundFormat(getResources().getColor(R.color.windows_bg)))
                .setShowXSequence(false).setShowYSequence(false);
        table.getConfig().setTableTitleStyle(new FontStyle(this, 15, getResources().getColor(R.color.arc1)));
        table.getConfig().setContentCellBackgroundFormat(new BaseCellBackgroundFormat<CellInfo>() {
            @Override
            public int getBackGroundColor(CellInfo cellInfo) {
                if (cellInfo.row % 2 == 0) {
                    return ContextCompat.getColor(PagerModeDemoActivity.this, R.color.content_bg);
                }
                return TableConfig.INVALID_COLOR;
            }
        });

        tableData.setPageSize(9);
        table.setTableData(tableData);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left:
                tableData.setCurrentPage(tableData.getCurrentPage() - 1);
                table.notifyDataChanged();
                break;
            case R.id.right:
                tableData.setCurrentPage(tableData.getCurrentPage() + 1);
                table.notifyDataChanged();
                break;
        }


    }


}
