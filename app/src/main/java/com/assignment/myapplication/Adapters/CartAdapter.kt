package com.assignment.myapplication.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.assignment.myapplication.Interface.onItemClickListener2
import com.assignment.myapplication.R
import com.assignment.myapplication.models.CartData


class CartAdapter(
        private val rssObject: MutableList<CartData>,
        private val mContext: Context,
        private val listener: onItemClickListener2
) : RecyclerView.Adapter<CartAdapter.FeedViewHolders>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolders {

        val itemView = inflater.inflate(R.layout.card_food_cart, parent, false)
        return FeedViewHolders(itemView)
    }

    private val inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(mContext)
    }
    //giving data to card
    override fun onBindViewHolder(holder: FeedViewHolders, position: Int) {
        holder.txtTitle.text = rssObject[position].item
        holder.txtTitle1.text = rssObject[position].quantity
        holder.txtTitle2.text = rssObject[position].price
        //listeners
        holder.addBtn.setOnClickListener { listener.onPlusClick(position) }
        holder.minusBtn.setOnClickListener { listener.onMinusClick(position) }
        holder.deleteBtn.setOnClickListener { listener.onDeleteClick(position) }

    }

    override fun getItemCount(): Int {
        return rssObject.size
    }
    //binding
    inner class FeedViewHolders(itemView: View) : RecyclerView.ViewHolder(itemView){

        var txtTitle: TextView = itemView.findViewById(R.id.cartItemName)
        var txtTitle1: TextView = itemView.findViewById(R.id.cartItemQuantity)
        var txtTitle2: TextView = itemView.findViewById(R.id.cartItemPrice)
        //img btn
        var addBtn:ImageButton=itemView.findViewById(R.id.quantityPlus)
        var minusBtn:ImageButton=itemView.findViewById(R.id.quantityMinus)
        var deleteBtn:ImageButton=itemView.findViewById(R.id.cartDelete)
        //to get position of user's clicked data
    }
}