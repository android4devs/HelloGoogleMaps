package pl.froger.hello.googlemaps;

import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MainActivity extends MapActivity {
	private static final int DIALOG_COORDINATES = 1;
	
	private CheckBox cbSatellite;
	private CheckBox cbStreet;
	private CheckBox cbTraffic;
	private Button btnGoTo;
	private Button btnMark;
	private MapView mapView;

	private MapController mapController;
	private ImHereTipOverlay imHereTipOverlay;
	private MyPinOverlay myPinOverlay;
	private List<Overlay> overlays;
	private Double latitude = 0.0;
	private Double longitude = 0.0;

	private OnCheckedChangeListener cbViewListener = new OnCheckedChangeListener() {
		public void onCheckedChanged(CompoundButton button, boolean state) {
			switch (button.getId()) {
			case R.id.cbSatellite:
				mapView.setSatellite(state);
				break;
			case R.id.cbStreet:
				mapView.setStreetView(state);
				break;
			case R.id.cbTraffic:
				mapView.setTraffic(state);
				break;
			default:
				break;
			}
		}
	};

	private OnClickListener btnGoToListener = new OnClickListener() {
		public void onClick(View arg0) {
			showDialog(DIALOG_COORDINATES);
		}
	};
	
	private OnClickListener btnMarkListener = new OnClickListener() {
		public void onClick(View arg0) {
			GeoPoint point = new GeoPoint(latitude.intValue(), longitude.intValue());
			OverlayItem item = new OverlayItem(point, "", "");
			myPinOverlay.addOverlay(item);
			overlays.add(myPinOverlay);
			mapView.invalidate();
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initUiComponents();
		initListeners();

		mapController = mapView.getController();
		mapView.setBuiltInZoomControls(true);

		imHereTipOverlay = new ImHereTipOverlay();
		overlays = mapView.getOverlays();
		overlays.add(imHereTipOverlay);

		Drawable drawable = this.getResources().getDrawable(R.drawable.pin);
		myPinOverlay = new MyPinOverlay(drawable, getApplicationContext());
	}

	private void initUiComponents() {
		cbSatellite = (CheckBox) findViewById(R.id.cbSatellite);
		cbStreet = (CheckBox) findViewById(R.id.cbStreet);
		cbTraffic = (CheckBox) findViewById(R.id.cbTraffic);
		btnGoTo = (Button) findViewById(R.id.btnGoTo);
		btnMark = (Button) findViewById(R.id.btnMark);
		mapView = (MapView) findViewById(R.id.mapview);
	}

	private void initListeners() {
		cbSatellite.setOnCheckedChangeListener(cbViewListener);
		cbStreet.setOnCheckedChangeListener(cbViewListener);
		cbTraffic.setOnCheckedChangeListener(cbViewListener);
		btnGoTo.setOnClickListener(btnGoToListener);
		btnMark.setOnClickListener(btnMarkListener);
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.dialog, (ViewGroup) findViewById(R.id.layout_root));
		builder.setView(layout).setTitle("Coordinates").setCancelable(true);
		builder.setPositiveButton("Go", new Dialog.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				AlertDialog ad = (AlertDialog) dialog;
				EditText etLat = (EditText) ad.findViewById(R.id.etLat);
				EditText etLong = (EditText) ad.findViewById(R.id.etLong);
				latitude = Double.parseDouble(etLat.getText().toString()) * 1E6;
				longitude = Double.parseDouble(etLong.getText().toString()) * 1E6;
				GeoPoint point = new GeoPoint(latitude.intValue(), longitude.intValue());
				mapController.animateTo(point);
				mapController.setZoom(17);
				imHereTipOverlay.setLatitude(latitude);
				imHereTipOverlay.setLongitude(longitude);
			}
		});
		Dialog dialog = builder.create();
		return dialog;
	}
}