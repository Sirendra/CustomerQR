package com.assignment.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import android.widget.Toast.makeText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class PhoneNoVerify : AppCompatActivity() {
    lateinit var phoneNo:EditText
    lateinit var generateBtn:Button
    lateinit var progressBar:ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_no_verify)

        phoneNo = findViewById(R.id.phoneNo)
        generateBtn = findViewById(R.id.generateBtn)
        progressBar = findViewById(R.id.progressBar)

        generateBtn.setOnClickListener {
            var getNo=phoneNo.text.toString()
            if (!getNo.trim().isEmpty()){
                if (getNo.trim().length==10){

                    progressBar.visibility=View.VISIBLE
                    generateBtn.visibility=View.INVISIBLE
                    PhoneAuthProvider.getInstance().verifyPhoneNumber("+91"+getNo, 60, TimeUnit.SECONDS,this, object :PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                        override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                            progressBar.visibility=View.GONE
                            generateBtn.visibility=View.VISIBLE
                        }

                        override fun onVerificationFailed(p0: FirebaseException) {
                            progressBar.visibility=View.GONE
                            generateBtn.visibility=View.VISIBLE
                            Toast.makeText(baseContext,p0.message,Toast.LENGTH_SHORT).show()
                        }

                        override fun onCodeSent(
                            p0: String,
                            p1: PhoneAuthProvider.ForceResendingToken
                        ) {
                            super.onCodeSent(p0, p1)
                            progressBar.visibility=View.GONE
                            generateBtn.visibility=View.VISIBLE
                            var intent= Intent(applicationContext,OtpVerify::class.java)
                            intent.putExtra("PhoneNo", getNo)
                            intent.putExtra("OTP", p0)
                            startActivity(intent)
                        }
                    })

                }else{
                    makeText(this,"Please enter 10 digit valid number", LENGTH_SHORT).show()
                }
            }else{
                makeText(this,"Please enter your mobile number", LENGTH_SHORT).show()
            }
        }





    }
}