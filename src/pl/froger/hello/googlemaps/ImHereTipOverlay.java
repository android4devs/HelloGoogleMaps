package pl.froger.hello.googlemaps;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class ImHereTipOverlay extends Overlay {
    private static final int radius = 5;
    private static final int width = 65;
    private static final int height = 20;
    
    private Double latitude = 0.0;
    private Double longitude = 0.0;
 
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
 
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
 
	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		if (shadow == false) {
			GeoPoint geoPoint = new GeoPoint(latitude.intValue(), longitude.intValue());
			Point point = getPointFrom(mapView, geoPoint);
			Paint spotPaint = getSpotPaint();
			Paint tipPaint = getTipPaint();
			RectF oval = getSpotShape(point);
			RectF backRect = getTipShape(point);
			Path trianglePath = getSpotToTipShape(point);
			canvas.drawOval(oval, spotPaint);
			canvas.drawPath(trianglePath, tipPaint);
			canvas.drawRoundRect(backRect, 5, 5, tipPaint);
			canvas.drawText("Your point", 
					point.x + 3 * radius, 
					point.y	+ radius - height,
					spotPaint);
		}
		super.draw(canvas, mapView, shadow);
	}

	private Point getPointFrom(MapView mapView, GeoPoint geoPoint) {
		Point point = new Point();
		Projection projection = mapView.getProjection();
		projection.toPixels(geoPoint, point);
		return point;
	}
	
	private Paint getSpotPaint() {
		Paint spotPaint = new Paint();
		spotPaint.setARGB(250, 255, 255, 255);
		spotPaint.setAntiAlias(true);
		spotPaint.setFakeBoldText(true);
		return spotPaint;
	}
	
	private Paint getTipPaint() {
		Paint tipPaint = new Paint();
		tipPaint.setARGB(200, 50, 50, 50);
		tipPaint.setAntiAlias(true);
		tipPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		return tipPaint;
	}
	
	private RectF getSpotShape(Point point) {
		RectF oval = new RectF(point.x - radius, 
				point.y - radius, 
				point.x	+ radius, 
				point.y + radius);
		return oval;
	}

	private RectF getTipShape(Point point) {
		RectF backRect = new RectF(point.x + 2 * radius, 
				point.y - 2	* radius - height, 
				point.x + 2 * radius + width, 
				point.y	- 2 * radius);
		return backRect;
	}
	
	private Path getSpotToTipShape(Point point) {
		Path trianglePath = new Path();
		trianglePath.setFillType(Path.FillType.EVEN_ODD);
		trianglePath.moveTo(point.x, point.y);
		trianglePath.lineTo(point.x + radius + 20, point.y - 2 * radius);
		trianglePath.lineTo(point.x + radius + 35, point.y - 2 * radius);
		trianglePath.lineTo(point.x, point.y);
		trianglePath.close();
		return trianglePath;
	}
}