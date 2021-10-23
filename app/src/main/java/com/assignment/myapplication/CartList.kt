package com.assignment.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.assignment.myapplication.Adapters.CartAdapter
import com.assignment.myapplication.Interface.onItemClickListener2
import com.assignment.myapplication.models.CartData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_cart_list.*

class CartList : AppCompatActivity(), onItemClickListener2{
    lateinit var reference1: DatabaseReference
    private lateinit var valueEventListener: ValueEventListener
    lateinit var data: MutableList<CartData>
    private var total:Double=0.0
    //private var status: String? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_list)

        var resUID= intent.getStringExtra("Code").toString()
        data = mutableListOf()
        var userUID = FirebaseAuth.getInstance().currentUser!!.uid
        reference1 = FirebaseDatabase.getInstance().getReference(userUID).child("CartList")
        valueEventListener=reference1.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(it: DataSnapshot) {
                data.clear()
                total=0.0
                it.children.forEach { i ->
                    var item = i.key.toString()
                    var price = it.child(item).child("Price").value.toString()
                    var quantity = it.child(item).child("Quantity").value.toString()
                    total+=(price.toDouble() * quantity.toDouble())
                    data.add(CartData(item, price, quantity))
                }
                totalPrice.text=total.toString()
                if (data != null) {
                    val linearLayoutManager =
                            LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL, false)
                    cartList.layoutManager = linearLayoutManager
                    var adapter1 = CartAdapter(data, baseContext, this@CartList)
                    cartList.adapter = adapter1
                } else {
                    cartList.visibility = View.INVISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(baseContext, "Check your Internet Connection", Toast.LENGTH_SHORT).show()
            }

        })

        cartBackBtn.setOnClickListener {
            finish()
        }
        pay.setOnClickListener{
            if (total.toString()=="0.0"){
                Toast.makeText(baseContext, "Please add food to cart FIRST", Toast.LENGTH_SHORT).show()
            }else{
                var intent=Intent(applicationContext,PayPage::class.java)
                intent.putExtra("Code",resUID)
                intent.putExtra("Total",total.toString())
                startActivity(intent) 
            }

        }

    }

    override fun onPause() {
        super.onPause()
        reference1.removeEventListener(valueEventListener)
    }
    override fun onDeleteClick(position: Int) {
        reference1.child(data[position].item!!).ref.removeValue()
    }

    override fun onPlusClick(position: Int) {
        var value=data[position].quantity!!.toInt()+1
        reference1.child(data[position].item!!).child("Quantity").setValue(value.toString())
    }

    override fun onMinusClick(position: Int) {
        var value=data[position].quantity!!.toInt()-1
        if (value>0){
        reference1.child(data[position].item!!).child("Quantity").setValue(value.toString())}
    }
}
