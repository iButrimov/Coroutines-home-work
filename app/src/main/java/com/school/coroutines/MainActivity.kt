package com.school.coroutines

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.annotations.SerializedName
import com.school.coroutines.databinding.ActivityMainBinding
import com.school.coroutines.databinding.ItemHolderBinding

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<ActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (checkConnection()) {
            Toast.makeText(this, "Internet is connected", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Internet is not connected", Toast.LENGTH_SHORT).show()
        }

        val adapter = Adapter()
        ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)
            recyclerView.adapter = adapter
            viewModel.state.observe(this@MainActivity) { state ->
                when (state) {
                    State.Loading -> root.isRefreshing = true
                    is State.Loaded -> {
                        root.isRefreshing = false
                        //adapter.submitList(state.content)
                        adapter.submitList(listOf(state.content))
                    }
                }
            }
            root.setOnRefreshListener { viewModel.refreshData() }
        }
    }

    private fun checkConnection():Boolean {
        val connMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo? = connMgr.activeNetworkInfo
        return networkInfo?.isConnected == true
    }

    /*
    //second way to check connection

    private fun checkConnection() {
        var isWifiConn: Boolean = false
        var isMobileConn: Boolean = false

        val connectivityManager: ConnectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        connectivityManager.allNetworks.forEach { network ->
            connectivityManager.getNetworkInfo(network)?.apply {
                if (type == ConnectivityManager.TYPE_WIFI) {
                    isWifiConn = isWifiConn or isConnected
                }
                if (type == ConnectivityManager.TYPE_MOBILE) {
                    isMobileConn = isMobileConn or isConnected
                }
            }
        }
        Toast.makeText(this, "Wifi connected: $isWifiConn", Toast.LENGTH_SHORT).show()
        Toast.makeText(this, "Mobile connected: $isMobileConn", Toast.LENGTH_SHORT).show()
    }
    */

    class Adapter : ListAdapter<Adapter.Item, Adapter.Holder>(DiffCallback) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            return Holder(parent)
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {
            holder.bind(getItem(position))
        }

        class Holder(private val binding: ItemHolderBinding) : RecyclerView.ViewHolder(binding.root) {
            constructor(parent: ViewGroup) : this(ItemHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false))

            fun bind(item: Item) {
                binding.apply {
                    titleTV.text = item.name
                    bodyTV.text = item.main.temp.toString()
                }
            }
        }

        data class Item(
                @SerializedName("name")
                val name: String,
                @SerializedName("main")
                val main: Main
        ) {
            data class Main(
                    @SerializedName("temp")
                    val temp: Double,
                    @SerializedName("feels_like")
                    val feels_like: Double,
                    @SerializedName("temp_min")
                    val temp_min: Double,
                    @SerializedName("temp_max")
                    val temp_max: Double,
                    @SerializedName("pressure")
                    val pressure: Int,
                    @SerializedName("humidity")
                    val humidity: Int,
            )
        }

        object DiffCallback : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem.main == newItem.main
            }

            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem == newItem
            }
        }
    }
}
