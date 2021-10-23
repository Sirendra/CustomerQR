package com.assignment.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.assignment.myapplication.Adapters.MenuAdapter
import com.assignment.myapplication.Interface.OnItemClickListener
import com.assignment.myapplication.models.MenuData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_home_page.*

class HomePage : AppCompatActivity() , OnItemClickListener {
    private lateinit var reference: DatabaseReference
    lateinit var reference2: DatabaseReference
    lateinit var data: MutableList<MenuData>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)


        data = mutableListOf()
        var resUID= intent.getStringExtra("Code").toString()
        //var resUID="ObV9w2k34ThdrGvJpkabpAsbGA73"
        var userUID = FirebaseAuth.getInstance().currentUser!!.uid
        //var userUID = "IlClyNkOUmZ2ooBZMbboigbp7W22"


        reference = FirebaseDatabase.getInstance().getReference(resUID).child("MenuList")
        reference2 = FirebaseDatabase.getInstance().getReference(userUID).child("CartList")

        reference.get().addOnSuccessListener {
            data.clear()
            it.children.forEach { i ->
                var item = i.key.toString()
                var price = it.child(item).value.toString()
                data.add(MenuData(item, price))
            }
            if (data != null) {
                val linearLayoutManager =
                    LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL, false)
                menuList.layoutManager = linearLayoutManager
                var adapter1 = MenuAdapter(data, baseContext,this)
                menuList.adapter = adapter1
            } else {
                menuList.visibility = View.INVISIBLE
            }

        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)
        }
        //CartList
        openCart.setOnClickListener {
            var intent= Intent(this,CartList::class.java)
            intent.putExtra("Code",resUID)
            startActivity(intent)
        }
        openOrder.setOnClickListener {
            var intent= Intent(this,OrderStatus::class.java)
            intent.putExtra("Code",resUID)
            startActivity(intent)
        }
    }
    override fun onItemClick(position: Int) {
        reference2.child(data[position].item!!).child("Price").setValue(data[position].price)
        reference2.child(data[position].item!!).child("Quantity").setValue("1")
        Toast.makeText(this, "Added To Cart", Toast.LENGTH_SHORT).show()
    }
}