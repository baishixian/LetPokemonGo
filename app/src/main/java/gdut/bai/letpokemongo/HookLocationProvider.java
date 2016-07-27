package gdut.bai.letpokemongo;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.SystemClock;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by baishixian on 16-7-21.
 */
public class HookLocationProvider {
    Context ctx;
    String providerName;
    LocationManager locationManager;
    private Location mLocation;

    public HookLocationProvider(String paramString, Context paramContext)
    {
        this.providerName = paramString;
        this.ctx = paramContext;
        locationManager = (LocationManager)paramContext.getSystemService(Context.LOCATION_SERVICE);
        try
        {
            if (this.providerName.equalsIgnoreCase(LocationManager.GPS_PROVIDER)) {
                locationManager.addTestProvider(this.providerName, false, false, false, false, false, false, false, 1, 1);
            }else{
                locationManager.addTestProvider(this.providerName, true, false, true, false, false, false, false, 1, 2);
            }

            locationManager.setTestProviderStatus(this.providerName, 2, null, System.currentTimeMillis());
            locationManager.setTestProviderEnabled(this.providerName, true);
            return;
        }
        catch (Exception ex)
        {
            Toast.makeText(paramContext, "Please allow mock location to use PokeControls", Toast.LENGTH_SHORT).show();
        }
    }

    public void pushLocation(double paramDouble1, double paramDouble2)
    {
        LocationManager localLocationManager = (LocationManager)this.ctx.getSystemService(Context.LOCATION_SERVICE);
        Location localLocation = new Location(this.providerName);
        localLocation.setLatitude(paramDouble1);
        localLocation.setLongitude(paramDouble2);
        localLocation.setAltitude(0.0D);
        localLocation.setTime(System.currentTimeMillis());
        if (Build.VERSION.SDK_INT >= 17) {
            localLocation.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
        }
        try
        {
            Method localMethod = Location.class.getMethod("makeComplete", new Class[0]);
            if (localMethod != null) {
                localMethod.invoke(localLocation, new Object[0]);
            }
        }
        catch (NoSuchMethodException localNoSuchMethodException)
        {
            for (;;)
            {
                Toast.makeText(this.ctx, "Error " + localNoSuchMethodException.getMessage(), 1).show();
            }
        }
        catch (InvocationTargetException localInvocationTargetException)
        {
            for (;;)
            {
                localInvocationTargetException.printStackTrace();
            }
        }
        catch (IllegalAccessException localIllegalAccessException)
        {
            for (;;)
            {
                localIllegalAccessException.printStackTrace();
            }
        }
        setMockLocation(localLocation);
        localLocationManager.setTestProviderLocation(this.providerName, localLocation);
    }

    private void setMockLocation(Location localLocation) {
        mLocation = localLocation;
    }

    public Location getMockLocation() {
        return mLocation;
    }

    public void shutdown()
    {
        try
        {
            locationManager = (LocationManager) (this.ctx.getSystemService(Context.LOCATION_SERVICE));
            locationManager.removeTestProvider(this.providerName);
            return;
        }
        catch (Exception localException) {}
    }
}
