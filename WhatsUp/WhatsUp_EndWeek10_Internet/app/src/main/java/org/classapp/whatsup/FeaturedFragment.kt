package org.classapp.whatsup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FeaturedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FeaturedFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var mRecyclerView:RecyclerView? = null
    private var mAdapter:RecyclerView.Adapter<RecyclerView.ViewHolder>? = null
    private var  mLayoutManager:RecyclerView.LayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v:View = inflater.inflate(R.layout.fragment_featured, container, false)

        mRecyclerView = v.findViewById(R.id.eventRecyclerView)
        mRecyclerView?.setHasFixedSize(true)
        mLayoutManager = LinearLayoutManager(activity)
        mRecyclerView?.layoutManager = mLayoutManager

        GlobalScope.launch(Dispatchers.Main) {
            val result = httpGetEvents("https://whatsup.machigar.com/all_events.php")

            if (result!=null) parseJsonEvent(result)
        }
        return v
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FeaturedFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                FeaturedFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    //****** HTTP Request ******
    private suspend fun httpGetEvents(eventUrlStr: String):String? = withContext(Dispatchers.IO) {
            val eventUrl:URL = URL(eventUrlStr)
            val conn:HttpURLConnection = eventUrl.openConnection() as HttpURLConnection
            conn.requestMethod = "GET"
            conn.connect()

            val inStream:InputStream = conn.inputStream
            val inStreamReader:InputStreamReader = InputStreamReader(inStream, "UTF-8")
            val buffReader:BufferedReader = BufferedReader(inStreamReader)

            var sb:StringBuilder = StringBuilder()
            var line_read:String? = buffReader.readLine()
            while (line_read!=null) {
                sb.append(line_read)
                line_read = buffReader.readLine()
            }
            inStream.close()
            sb.toString()
    }

    //****** JSON Parsing ******
    private var eventObjects = ArrayList<JSONObject>()
    private fun parseJsonEvent(jsonStr:String)
    {
        eventObjects.clear()
        val eventArray = JSONArray(jsonStr)
        for (i in 0..eventArray.length()-1)
        {
            val eventObj = eventArray.getJSONObject(i)
            eventObjects.add(eventObj)
        }
        mAdapter = EventListAdapter(eventObjects)
        mRecyclerView?.adapter = mAdapter
    }

    //****** RecyclerView Management ******
    private class EventItemViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)
    {
        val eventNameTxt:TextView = itemView.findViewById(R.id.eventNameTxt)
        val venueTxt:TextView = itemView.findViewById(R.id.venueTxt)
        val fromTxt:TextView = itemView.findViewById(R.id.fromTxt)
        val toTxt:TextView = itemView.findViewById(R.id.toTxt)
    }

    private class EventListAdapter(var eventObjects:ArrayList<JSONObject>):RecyclerView.Adapter<RecyclerView.ViewHolder>()
    {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val viewInflater:LayoutInflater = LayoutInflater.from(parent.context)
            val entryView:View = viewInflater.inflate(R.layout.event_entry, parent, false)
            val entryViewHolder:EventItemViewHolder = EventItemViewHolder(entryView)
            return entryViewHolder
        }

        override fun getItemCount(): Int {
            return eventObjects.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val eventObj = eventObjects.get(position)
            val eventName = eventObj.getString("event_name")
            val venue = eventObj.getString("venue")
            val eventStart = eventObj.getString("event_start_date")
            val eventEnd = eventObj.getString("event_end_date")

            if (holder is EventItemViewHolder)
            {
                val eventViewHolder:EventItemViewHolder = holder
                eventViewHolder.eventNameTxt.text = eventName
                eventViewHolder.venueTxt.text = venue
                eventViewHolder.fromTxt.text = eventStart
                eventViewHolder.toTxt.text = eventEnd
            }
        }
    }

    private class EventListAdapter2(var eventObjects:ArrayList<JSONObject>):RecyclerView.Adapter<EventItemViewHolder>()
    {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventItemViewHolder {
            val viewInflater:LayoutInflater = LayoutInflater.from(parent.context)
            val entryView:View = viewInflater.inflate(R.layout.event_entry, parent, false)
            val entryViewHolder:EventItemViewHolder = EventItemViewHolder(entryView)
            return entryViewHolder
        }

        override fun getItemCount(): Int {
            return eventObjects.size
        }

        override fun onBindViewHolder(holder: EventItemViewHolder, position: Int) {
            val eventObj = eventObjects.get(position)
            val eventName = eventObj.getString("event_name")
            val venue = eventObj.getString("venue")
            val eventStart = eventObj.getString("event_start_date")
            val eventEnd = eventObj.getString("event_end_date")
            holder.eventNameTxt.text = eventName
            holder.venueTxt.text = venue
            holder.fromTxt.text = eventStart
            holder.toTxt.text = eventEnd
        }
    }
}