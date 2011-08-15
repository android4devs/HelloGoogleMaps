package pl.froger.hello.googlemaps;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class MyPinOverlay extends ItemizedOverlay<OverlayItem> {
	private ArrayList<OverlayItem> overlays = new ArrayList<OverlayItem>();
	private Context context;

	public MyPinOverlay(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker));
		this.context = context;
	}

	@Override
	protected OverlayItem createItem(int i) {
		return overlays.get(i);
	}

	@Override
	public int size() {
		return overlays.size();
	}

	@Override
	protected boolean onTap(int index) {
		OverlayItem item = overlays.get(index);
		GeoPoint point = item.getPoint();
		showLocationToast(point);
		return true;
	}

	private void showLocationToast(GeoPoint point) {
		Toast.makeText(context,
				"Lat: " + point.getLatitudeE6() / 1E6 + 
				"\nLon: " + point.getLongitudeE6() / 1E6, 
				Toast.LENGTH_LONG).show();
	}
	
	public void addOverlay(OverlayItem overlay) {
		overlays.add(overlay);
		populate();
	}
}