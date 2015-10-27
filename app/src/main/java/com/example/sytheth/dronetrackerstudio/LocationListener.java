package com.example.sytheth.dronetrackerstudio;

import android.location.Location;
import android.os.Bundle;

/**
 * Created by Sytheth on 10/7/2015.
 */
public class LocationListener implements android.location.LocationListener {
    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProvidpackage com.example.sytheth.dronetrackerstudio;

    import android.location.Location;
    import android.os.Bundle;

    /**
     * Created by Sytheth on 10/7/2015.
     */
    public class LocationListener implements android.location.LocationListener {
        /**
         * Acts when the location is change.
         * @param location The new location, as a Location object.
         */
        @Override
        public void onLocationChanged(Location location) {

        }
        /**
         *
         * @param provider The name of the location provider associated with this update.
         * @param Status 0 if the provider is out of service, and this is not expected to change in the near future; 1 if the provider is temporarily unavailable but is expected to be available shortly; and 2 if the provider is currently available.
         * @param Extras an optional Bundle which will contain provider specific status variables.
         */
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
        /**
         *
         * @param provider The name of the location provider associated with this update.
         */
        @Override
        public void onProviderEnabled(String provider) {

        }
        /**
         *
         * @param provider The name of the location provider associated with this update.
         */
        @Override
        public void onProviderDisabled(String provider) {

        }
    }
    erEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
