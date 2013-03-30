package com.mpcs.mytravelmemoirs;

import java.util.List;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

class MyOverlay extends Overlay{
	
	
	private Projection projection;
	private List<GeoPoint> geoPointsArray;
	private MapView maps;
	
    public MyOverlay(Projection projection, List<GeoPoint> geoPointsArray, MapView mapView) {

    	this.projection = projection;
    	this.geoPointsArray = geoPointsArray;
    	this.maps = mapView;
    }

	public void draw(Canvas canvas, MapView mapv, boolean shadow){
    super.draw(canvas, mapv, shadow);

     Path p = new Path();
     for (int i = 0; i < geoPointsArray.size(); i++) {
     if (i == geoPointsArray.size() - 1) {
         break;
     }
     Point from = new Point();
     Point to = new Point();
     projection.toPixels(geoPointsArray.get(i), from);
     projection.toPixels(geoPointsArray.get(i + 1), to);
     p.moveTo(from.x, from.y);
     p.lineTo(to.x, to.y);
     }
     Paint mPaint = new Paint();
     mPaint.setStyle(Style.STROKE);
     mPaint.setColor(0xFFFF0000);
     mPaint.setAntiAlias(true);
     canvas.drawPath(p, mPaint);
     super.draw(canvas, maps, shadow);
 }   
}












