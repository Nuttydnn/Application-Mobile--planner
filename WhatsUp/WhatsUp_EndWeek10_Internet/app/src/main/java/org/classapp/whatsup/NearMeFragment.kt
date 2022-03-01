package org.classapp.whatsup

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.jar.Manifest

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NearMeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NearMeFragment : Fragment(), OnMapReadyCallback {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var latTxt:EditText
    private lateinit var lonTxt:EditText

    private lateinit var mMap:GoogleMap

    private val LOCATION_PERMISSION_REQ_CODE:Int = 111

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var v:View = inflater.inflate(R.layout.fragment_near_me, container, false)
        latTxt = v.findViewById(R.id.latTxt)
        lonTxt = v.findViewById(R.id.lonTxt)


        val gStatus:Int = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(activity)

        if (gStatus == ConnectionResult.SUCCESS)
        {
            Toast.makeText(activity, "Google Play Available", Toast.LENGTH_LONG).show()
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)
            manageLocation()
            var mapFragment = childFragmentManager.findFragmentById(R.id.mapNearMe) as SupportMapFragment
            mapFragment.getMapAsync(this)
        }
        else {
            Toast.makeText(activity, "Cannot Use Google Play", Toast.LENGTH_LONG).show()
        }
        return  v
    }

    fun manageLocation()
    {
        if (ContextCompat.checkSelfPermission(activity!!, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED)
        {
            //permission missing, request it
            requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQ_CODE)
        }
        else
        {
            //permission granted
            var locationCb = object : LocationCallback() {
                override fun onLocationResult(p0: LocationResult?) {
                    super.onLocationResult(p0)
                    var last_location:Location? = p0?.lastLocation
                    if (last_location!=null)
                    {
                        latTxt.setText(last_location.latitude.toString())
                        lonTxt.setText(last_location.longitude.toString())
                    }
                }
            }
            val req = LocationRequest()
            req.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            req.interval = 0
            req.fastestInterval = 0
            fusedLocationClient.requestLocationUpdates(req, locationCb, null)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode==LOCATION_PERMISSION_REQ_CODE && permissions.isNotEmpty())
        {
            var granted:Boolean = false
            for (i in permissions.indices)
            {
                if (permissions[i].equals(android.Manifest.permission.ACCESS_FINE_LOCATION)
                    && grantResults[i]==PackageManager.PERMISSION_GRANTED)
                {
                    // user grant permission
                    granted = true
                    manageLocation()
                    break
                }
            }
            if (!granted) {
                Toast.makeText(activity, "No permission to access location!", Toast.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NearMeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NearMeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun refreshMap(lat:Double, lon:Double, title:String, subtitle:String)
    {
        var o = BitmapDescriptorFactory.fromResource(R.drawable.front_sign)
        var markerPos = LatLng(lat, lon)
        var mOptions = MarkerOptions().position(markerPos)
            .title(title)

        mMap.addMarker(mOptions)
        mMap.clear()

    }

    override fun onMapReady(p0: GoogleMap?) {
        if (p0!=null)
        {
            mMap = p0
            refreshMap(13.75493, 100.49402, "Sanam Luang", "I'm Here")
        }
    }
}