package com.lib.weight.dashboard.type;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;

import com.lib.weight.dashboard.help.DialChart;
import com.lib.weight.dashboard.help.GraphicalView;
import com.lib.weight.dashboard.help.PlotAttrInfo;
import com.lib.weight.dashboard.help.XEnum;

import java.util.ArrayList;
import java.util.List;



public class DialChart05View extends GraphicalView {
	
	private String TAG = "DialChart05View";
	private DialChart chart = new DialChart();
	private float mPercentage = 0.1f;
	

	public DialChart05View(Context context) {
		super(context);
		initView();
	}
	
	public DialChart05View(Context context, AttributeSet attrs){
        super(context, attrs);   
        initView();
	 }
	 
	 public DialChart05View(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
			initView();
	 }
	 
	 private void initView()
	 {
		chartRender();
	 }
	 
	 @Override
	    protected void onSizeChanged(int w, int h, int oldw, int oldh) {  
	        super.onSizeChanged(w, h, oldw, oldh);  
	        chart.setChartRange(w ,h ); 
	    }  		
			
	 public void chartRender()
		{
			try {								
							
				//设置标题背景			
				chart.setApplyBackgroundColor(true);
				chart.setBackgroundColor( Color.WHITE );
				//绘制边框
//				chart.showRoundBorder();
						
				//设置当前百分比
				chart.getPointer().setPercentage(mPercentage);
				
				//设置指针长度
				chart.getPointer().setLength(0.6f);
				chart.getPointer().getBaseCirclePaint().setColor(Color.parseColor("#296cf7"));
				chart.getPointer().getPointerPaint().setColor(Color.parseColor("#296cf7"));

				//增加轴
				addAxis();						
				/////////////////////////////////////////////////////////////
				addPointer();
				//设置附加信息
				addAttrInfo();
				/////////////////////////////////////////////////////////////
												
			} catch (Exception e) {
				Log.e(TAG, e.toString());
			}
			
		}
		
		public void addAxis()
		{
		
			List<String> rlabels  = new ArrayList<String>();

			chart.addOuterTicksAxis(0.7f, rlabels);
			
			//环形颜色轴
			List<Float> ringPercentage = new ArrayList<Float>();
			ringPercentage.add( 0.33f);
			ringPercentage.add( 0.33f);
			ringPercentage.add( 1 - 2 * 0.33f);
			
			List<Integer> rcolor  = new ArrayList<Integer>();
			rcolor.add(Color.rgb(133, 206, 130));
			rcolor.add(Color.rgb(252, 210, 9));
			rcolor.add(Color.rgb(229, 63, 56));

			chart.addStrokeRingAxis(0.7f,0.6f, ringPercentage, rcolor);
									
			List<String> rlabels2  = new ArrayList<String>();

			for(int i = 0; i <= 1000; i = i+100) {
				rlabels2.add(Integer.toString(i));
			}

			chart.addInnerTicksAxis(0.6f, rlabels2);
								
			chart.getPlotAxis().get(1).getFillAxisPaint().setColor(Color.WHITE);
			
			chart.getPlotAxis().get(0).hideAxisLine();
			chart.getPlotAxis().get(2).hideAxisLine();
			chart.getPlotAxis().get(0).getTickMarksPaint().setColor(Color.WHITE);
			chart.getPlotAxis().get(2).getTickMarksPaint().setColor(Color.BLACK);
			chart.getPlotAxis().get(2).getTickLabelPaint().setColor(Color.BLACK);//内部的文字颜色
		
			
		}
		
		
		private void addAttrInfo()
		{
			PlotAttrInfo plotAttrInfo = chart.getPlotAttrInfo();

			//设置附加信息

			Paint paintBT = new Paint();
			paintBT.setColor(Color.parseColor("#4c4c4c"));
			paintBT.setTextAlign(Paint.Align.CENTER);
			paintBT.setTextSize(30);


			plotAttrInfo.addAttributeInfo(XEnum.Location.BOTTOM, "哈哈哈", 0.8f, paintBT);
			
		}
		
		public void addPointer()
		{				

		}
		public void setCurrentStatus(float percentage)
		{
			//清理
			chart.clearAll();
					
			mPercentage =  percentage;
			//设置当前百分比
			chart.getPointer().setPercentage(mPercentage);
			addAxis();						
			addPointer();
			addAttrInfo();
		}


		@Override
		public void render(Canvas canvas) {
			// TODO Auto-generated method stub
			 try{
		            chart.render(canvas);
		        } catch (Exception e){
		        	Log.e(TAG, e.toString());
		        }
		}

}
