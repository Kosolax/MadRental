package com.formationandroid.madrental

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.formationandroid.madrental.ws.NetworkHelper
import com.formationandroid.madrental.ws.ReturnWSVehicle
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VehiclesAdapter(private var listVehicles: MutableList<ReturnWSVehicle>, private val mainDayPrice: String, private val categoryCo2: String, private val mainActivity: MainActivity,  private val comingFromDb: Boolean) : RecyclerView.Adapter<VehiclesAdapter.VehicleViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleViewHolder
    {
        val viewVehicle = LayoutInflater.from(parent.context).inflate(R.layout.item_vehicle, parent, false)
        return VehicleViewHolder(viewVehicle)
    }

    override fun onBindViewHolder(holder: VehicleViewHolder, position: Int)
    {
        if (comingFromDb) {
            // If it's coming from db we already did the concatenation
            holder.textViewNameVehicle.text = listVehicles[position].nom
            holder.textViewDayPriceVehicle.text = listVehicles[position].prixjournalierbase
            holder.textViewCategoryCO2Vehicle.text = listVehicles[position].categorieco2
        }
        else {
            holder.textViewNameVehicle.text = listVehicles[position].nom
            holder.textViewDayPriceVehicle.text = listVehicles[position].prixjournalierbase + mainDayPrice
            holder.textViewCategoryCO2Vehicle.text = categoryCo2 + listVehicles[position].categorieco2
        }
    }

    override fun getItemCount(): Int
    {
        return listVehicles.size
    }

    inner class VehicleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        // Getting reference of what we need to display informations correctly
        var textViewNameVehicle: TextView = itemView.findViewById(R.id.vehicle_names)
        var textViewDayPriceVehicle: TextView = itemView.findViewById(R.id.vehicle_day_prices)
        var textViewCategoryCO2Vehicle: TextView = itemView.findViewById(R.id.vehicle_category_co2s)

        init
        {
            // On click listener. This code allow us to decide what we want to do ON EVERY vehicle when we click on one of them
            itemView.setOnClickListener {
                val currentId: String = listVehicles[adapterPosition].id;

                if (mainActivity.findViewById<FrameLayout>(R.id.container_fragment) != null) {
                    // We are in landscape mode so we don't need to send the value to the activity but instead we need to create a fragment and set it in the view

                    val fragment = VehicleFragment()
                    val bundle = Bundle()

                    // Setting values to the MainActivity
                    // We should'nt do that but i had a bug when adding in landscape mode
                    // And only static object worked
                    MainActivity.currentId = currentId;
                    MainActivity.textViewNameVehicle = textViewNameVehicle.text.toString();
                    MainActivity.textViewDayPriceVehicle = textViewDayPriceVehicle.text.toString();
                    MainActivity.textViewCategoryCO2Vehicle = textViewCategoryCO2Vehicle.text.toString();

                    // Setting values to the fragment
                    bundle.putString(itemView.context.getString(R.string.currentId), currentId);
                    bundle.putString(itemView.context.getString(R.string.textViewNameVehicle), textViewNameVehicle.text.toString())
                    bundle.putString(itemView.context.getString(R.string.textViewDayPriceVehicle), textViewDayPriceVehicle.text.toString())
                    bundle.putString(itemView.context.getString(R.string.textViewCategoryCO2Vehicle), textViewCategoryCO2Vehicle.text.toString())
                    fragment.arguments = bundle

                    // Replacing the content of the view that has the id container_fragment by our fragment
                    val transaction: FragmentTransaction = mainActivity.supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.container_fragment, fragment)
                    transaction.commit()
                }
                else {
                    // We are in portrait mode so we need to send values to the activity
                    var intent = Intent(it.context, VehicleActivity::class.java);

                    // Setting value to the intent
                    intent.putExtra(itemView.context.getString(R.string.currentId), currentId);
                    intent.putExtra(itemView.context.getString(R.string.textViewNameVehicle), textViewNameVehicle.text);
                    intent.putExtra(itemView.context.getString(R.string.textViewDayPriceVehicle), textViewDayPriceVehicle.text);
                    intent.putExtra(itemView.context.getString(R.string.textViewCategoryCO2Vehicle), textViewCategoryCO2Vehicle.text);

                    // Navigating to the next activity
                    it.context.startActivity(intent);
                }
            }
        }
    }
}