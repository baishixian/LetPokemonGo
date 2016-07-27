package gdut.bai.letpokemongo;

import android.content.ContentResolver;
import android.location.Location;
import android.os.Build;
import android.util.Log;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by baishixian on 16-7-20.
 */
public class Tutorial implements IXposedHookLoadPackage {

    static final double CAPITAL_LATITUDE = -35.302747;
    static final double CAPITAL_LONGITUDE = 149.125318;
    private double MODIFY_LONG;
    final String TAG = "XposedHook";
    private double MODIFY_LAT;
    private double CORRECTED;

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {

        Log.i(TAG, "Loaded Package: " + loadPackageParam.packageName);
        XposedBridge.log("Loaded Package: " + loadPackageParam.packageName);

        XposedHelpers.findAndHookMethod("android.location.LocationManager", loadPackageParam.classLoader,
                "getLastKnownLocation", String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("location provider is" + param.args[0].toString());
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Location location = (Location) param.getResult();
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    XposedBridge.log("latitude = " + latitude + " longitude =  " + longitude);
                } else {
                    LocationUtil.sendMockLocation(LocationUtil.getContext(), CAPITAL_LATITUDE,CAPITAL_LONGITUDE);
                    XposedBridge.log("location is null");
                }
            }
        });

        XposedHelpers.findAndHookMethod("android.location.Location", loadPackageParam.classLoader, "getLatitude", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("location getLatitude");
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                double localLatitude = (double) param.getResult();

                XposedBridge.log("getLatitude() result = " + localLatitude);
                XposedBridge.log("----hook getLatitude start!----");

                localLatitude = localLatitude - MODIFY_LAT + CORRECTED;
                String result = String.format("%7f", localLatitude);
                param.setResult(Double.parseDouble(result));

                XposedBridge.log("----hook getLatitude result = " + result);
                XposedBridge.log("----hook getLatitude end!----");


            }
        });


        XposedHelpers.findAndHookMethod("android.location.Location", loadPackageParam.classLoader, "getLongitude", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("location getLongitude");
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                double localLongitude = (double) param.getResult();

                XposedBridge.log("getLongitude() result = " + localLongitude);
                XposedBridge.log("----hook getLongitude start!----");

                localLongitude = localLongitude + MODIFY_LONG + CORRECTED;
                String result = String.format("%7f", localLongitude);
                param.setResult(Double.parseDouble(result));

                XposedBridge.log("----hook getLongitude result = " + result);
                XposedBridge.log("----hook getLongitude end!----");


            }
        });

        XposedHelpers.findAndHookMethod("android.provider.Settings.Secure", loadPackageParam.classLoader, "getString",
                new Object[] { ContentResolver.class, String.class, new XC_MethodHook() {
                    protected void beforeHookedMethod(XC_MethodHook.MethodHookParam paramAnonymousMethodHookParam) throws Throwable {
                        if (((String)paramAnonymousMethodHookParam.args[1]).equals("mock_location")) {
                            paramAnonymousMethodHookParam.setResult("0");
                            XposedBridge.log("1 Mocked GPS for: " + loadPackageParam.packageName + " done!");
                        }
                    }
                }});

        if (Build.VERSION.SDK_INT >= 17) {
            XposedHelpers.findAndHookMethod("android.provider.Settings.Secure", loadPackageParam.classLoader, "getStringForUser",
                    new Object[] { ContentResolver.class, String.class, Integer.TYPE, new XC_MethodHook() {
                        protected void beforeHookedMethod(XC_MethodHook.MethodHookParam paramAnonymousMethodHookParam) throws Throwable {
                            if (((String)paramAnonymousMethodHookParam.args[1]).equals("mock_location")) {
                                paramAnonymousMethodHookParam.setResult("0");
                                XposedBridge.log("2 Mocked GPS for: " + loadPackageParam.packageName + " done!");
                            }
                        }
                    }});
        }

        if (Build.VERSION.SDK_INT >= 18) {
            XposedHelpers.findAndHookMethod("android.location.Location", loadPackageParam.classLoader, "isFromMockProvider",
                    new Object[] { new XC_MethodHook() {
                        protected void beforeHookedMethod(XC_MethodHook.MethodHookParam paramAnonymousMethodHookParam) throws Throwable {
                            paramAnonymousMethodHookParam.setResult(Boolean.valueOf(false));
                            XposedBridge.log("3 Mocked GPS for: " + loadPackageParam.packageName + " done!");
                        }
                    } });
        }
    }
}
