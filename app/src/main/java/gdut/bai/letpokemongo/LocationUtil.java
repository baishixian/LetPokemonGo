package gdut.bai.letpokemongo;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

/**
 * Created by baishixian on 16-7-21.
 */
public class LocationUtil {

    final static double MODIFY = 0.00005;

    public static final double CAPITAL_LATITUDE = -35.302747;
    public static final double CAPITAL_LONGITUDE = 149.125318;


    private static Context mContext;
    private static HookLocationProvider localMockLocationProvider;

    public static void setContext(Context context) {
        mContext = context;
        if (localMockLocationProvider == null){
            localMockLocationProvider = new HookLocationProvider("gps", context.getApplicationContext());
        }
    }

    public static Context getContext() {
        return mContext;
    }


    private static Location getLocation(Context context){

        if (localMockLocationProvider == null){
        localMockLocationProvider = new HookLocationProvider("gps", context.getApplicationContext());
        }

        Location location = localMockLocationProvider.getMockLocation();

        if (location == null){
            LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location == null){
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
            if (location ==  null){
                location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            }
        }

        if (location == null){
            localMockLocationProvider.pushLocation(CAPITAL_LATITUDE,CAPITAL_LONGITUDE);
            localMockLocationProvider.shutdown();
            location = localMockLocationProvider.getMockLocation();
        }
        if (location != null){
            Log.i("getLocation","location getLongitude = " +  location.getLongitude() + " getLatitude = " + location.getLatitude());
        }else{
            Log.i("getLocation","location is null");
        }

        return location;
    }

    public static void moveUp() {
        Location location = getLocation(mContext);
        if (location != null){
            sendMockLocation(mContext,location.getLatitude() + 0.00005,location.getLongitude());
        }else{
            Log.i("Move Location","location is null" );
        }
    }


    public static void moveLeft() {
        Location location = getLocation(mContext);
        if (location != null){
            sendMockLocation(mContext,location.getLatitude(),location.getLongitude() - 0.00005);
        }else{
            Log.i("Move Location","location is null" );
        }
    }

    public static void moveDown() {
        Location location = getLocation(mContext);
        if (location != null){
            sendMockLocation(mContext,location.getLatitude() - 0.00005,location.getLongitude());
        }else{
            Log.i("Move Location","location is null" );
        }
    }

    public static void moveRight() {
        Location location = getLocation(mContext);
        if (location != null){
            sendMockLocation(mContext,location.getLatitude(),location.getLongitude() + 0.00005);
        }else{
            Log.i("Move Location","location is null" );
        }
    }

    public  static void sendMockLocation(Context context, double lat, double lon) {
        if (context == null){
            Log.i("sendMockLocation","context is null" );
            return;
        }
        localMockLocationProvider = new HookLocationProvider("gps", context.getApplicationContext());
        localMockLocationProvider.pushLocation(lat, lon);
        localMockLocationProvider.shutdown();
    }
}
