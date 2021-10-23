package com.assignment.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_pay_page.*

class PayPage : AppCompatActivity() {

    lateinit var resUID:String
    lateinit var reference1: DatabaseReference
    lateinit var reference: DatabaseReference
    lateinit var reference2: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay_page)
        var userUID = FirebaseAuth.getInstance().currentUser!!.uid
        resUID=intent.getStringExtra("Code").toString()
        //references
        reference1 = FirebaseDatabase.getInstance().getReference(userUID).child("CartList")
        reference = FirebaseDatabase.getInstance().getReference(resUID).child("OrderList")
        reference2 = FirebaseDatabase.getInstance().getReference(userUID).child("OrderStatus")

        //Creating Order Status
        payCash.setOnClickListener {
            start()
        }
        payOnline.setOnClickListener {
            start()
        }
    }

    private fun start() {
        var tableNumber = tableNumber.text.toString().trim()
        if(tableNumber.isEmpty()) {
            Toast.makeText(this, "Please Enter Table Number", Toast.LENGTH_SHORT).show()
        }else{
            //Creating orderStatus for tracking order
            reference2.child(tableNumber).setValue(intent.getStringExtra("Total").toString())

            //updating order in restaurant database
            reference1.get().addOnSuccessListener {
                it.children.forEach{i->
                    var item = i.key.toString()
                    //var price = it.child(item).child("Price").value.toString()
                    var quantity = it.child(item).child("Quantity").value.toString()
                    reference.child(tableNumber).child("Item").child(item).setValue(quantity)
                }
                reference.child(tableNumber).child("Status").setValue("Received")

            }.addOnFailureListener {
                Toast.makeText(baseContext, "Check your Internet Connection", Toast.LENGTH_SHORT).show()
            }.addOnCompleteListener {
                reference1.ref.removeValue()
                var intent =Intent(applicationContext,HomePage::class.java)
                intent.putExtra("Code",resUID)
                startActivity(intent)
            }

        }


    }



}