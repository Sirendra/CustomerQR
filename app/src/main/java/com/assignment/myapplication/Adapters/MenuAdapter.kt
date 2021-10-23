package com.assignment.myapplication.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.assignment.myapplication.Interface.OnItemClickListener
import com.assignment.myapplication.R
import com.assignment.myapplication.models.MenuData


class MenuAdapter(
        private val rssObject: MutableList<MenuData>,
        private val mContext: Context,
        private val listener: OnItemClickListener
) : RecyclerView.Adapter<MenuAdapter.FeedViewHolders>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolders {

        val itemView = inflater.inflate(R.layout.card_menu, parent, false)
        return FeedViewHolders(itemView)
    }

    private val inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(mContext)
    }
    //giving data to card
    override fun onBindViewHolder(holder: FeedViewHolders, position: Int) {
        holder.txtTitle.text = rssObject[position].item
        holder.txtTitle1.text = rssObject[position].price
        holder.cartBtn.setOnClickListener { listener.onItemClick(position) }

    }

    override fun getItemCount(): Int {
        return rssObject.size
    }
    //binding
    inner class FeedViewHolders(itemView: View) : RecyclerView.ViewHolder(itemView){

        var txtTitle: TextView = itemView.findViewById(R.id.itemName)
        var txtTitle1: TextView = itemView.findViewById(R.id.cartItemPrice)
        var cartBtn: ImageButton = itemView.findViewById(R.id.cart)

        //to get position of user's clicked data
    }
}