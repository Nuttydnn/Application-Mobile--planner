package org.classapp.whatsup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NearMeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NearMeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var latTxt:EditText
    private lateinit var lonTxt:EditText

    private  val  LOCATION_PERMISSION_REQ_CODE: Int = 111
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
        latTxt = v.findViewById(R.id.LatTxt)
        lonTxt = v.findViewById(R.id.LonTxt)

        val gStatus:Int = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(activity)
        if (gStatus == ConnectionResult.SUCCESS)
        {
            Toast.makeText(activity, "Google Play Available", Toast.LENGTH_LONG).show()
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)
            manageLocation()
        }
        else{
            Toast.makeText(activity, "Cannot Use Google Play", Toast.LENGTH_LONG).show()
        }

        return v
    }
    fun manageLocation()
    {
        if (ContextCompat.checkSelfPermission(activity!!, android.Manifest.permission.ACCESS_FINE_LOCATION)
                LOCATION_PERMISSION_PEQ_CODE)
    }
    else
    {
        var locationCb = object : LocationCallback(){
            override fun onLocationResult(p0: LocationResult?) {
                super.onLocationResult(p0)
                var last_location : Location? =p0?.lastLocation
                if (last_location! = null)
                {
                    latTxt.setText(last_location.latitude.toString())
                    lonTxt.setText(last_location.longitude.toString())
                }
            }
        }
        var req = LocationResult()
        req.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        req.interval = 0
        req.fastestInterval = 0
        fusedLocationClient.requestLocationUpdates(req, locationCb, null)
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode==LOCATION_PERMISSION_REQ_CODE && permissions.isNotEmpty())
        {
            var granted : Boolean = false
            for (i in permissions.indices)
            {
                if (permissions[i].equals(android.Manifest.permission.ACCESS_FINE_LOCATION) && grantResults[i] == PackageManager.PERMISSION_GRANTED)
                {
                    granted = true
                    manageLocation()
                    break
                }
            }
        }
        if (!granted){
            Toast.makeText(activity,"No Permission to access location!", Toast.LENGTH_LONG).show()
        }
    }

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
        latTxt = v.findViewById(R.id.LatTxt)
        lonTxt = v.findViewById(R.id.LonTxt)

        val gStatus:Int = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(activity)
        if (gStatus == ConnectionResult.SUCCESS)
        {
            Toast.makeText(activity, "Google Play Available", Toast.LENGTH_LONG).show()
        }
        else{
            Toast.makeText(activity, "Cannot Use Google Play", Toast.LENGTH_LONG).show()
        }

        return v
    }
    fun manageLocation(){

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
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
}