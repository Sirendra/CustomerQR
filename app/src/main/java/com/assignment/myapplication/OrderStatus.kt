package com.assignment.myapplication

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_order_status.*

class OrderStatus : AppCompatActivity() {
    private lateinit var reference: DatabaseReference
    private lateinit var reference1: DatabaseReference
    private lateinit var valueEventListener: ValueEventListener
    private  var tableNumber: String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_status)
        var resUID= intent.getStringExtra("Code").toString()
        var userUID = FirebaseAuth.getInstance().currentUser!!.uid
        reference=FirebaseDatabase.getInstance().getReference(resUID).child("OrderList")
        reference1 = FirebaseDatabase.getInstance().getReference(userUID).child("OrderStatus")
        reference1.get().addOnSuccessListener {
            if (it.hasChildren()){
                orderDetails.visibility=View.VISIBLE
            run loop@{
                  it.children.forEach { i->
                      tableNumber=i.key.toString()
                      orderTableNumber.text=tableNumber
                      orderTotalPrice.text=it.child(tableNumber).value.toString()
                      return@loop
                  }
            }
            }else{
                orderDetails.visibility=View.INVISIBLE
            }
        }.addOnCompleteListener {
        valueEventListener = reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var status=snapshot.child(tableNumber).child("Status").value.toString()
                //Log.d("error1",status)
                if(status=="Received" || status=="Accepted" || status=="Preparing" || status=="Ready"){
                    orderStatus.text =status
                }else{
                    //delete order once order status change to null=served
                    reference1.ref.removeValue()
                    orderDetails.visibility=View.INVISIBLE
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(baseContext, "Check your internet connection", Toast.LENGTH_SHORT).show()
            }

        })
        }
        orderSBack.setOnClickListener {
            finish()
        }
    }
}