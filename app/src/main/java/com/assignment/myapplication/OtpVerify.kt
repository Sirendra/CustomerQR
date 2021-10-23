package com.assignment.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit


class OtpVerify : AppCompatActivity() {

    lateinit var otp1: EditText
    lateinit var otp2: EditText
    lateinit var otp3: EditText
    lateinit var otp4: EditText
    lateinit var otp5: EditText
    lateinit var otp6: EditText

    private lateinit var verifyBtn:Button
    lateinit var progressBarVerify:ProgressBar
    lateinit var resendOtp:TextView
    lateinit var phoneNo:TextView
    lateinit var otp:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_verify)

        verifyBtn = findViewById(R.id.verifyBtn)
        progressBarVerify = findViewById(R.id.progressBarVerify)
        resendOtp=findViewById(R.id.resend_Otp)
        phoneNo=findViewById(R.id.phoneNo_given)

        otp1 = findViewById(R.id.otp_1)
        otp2 = findViewById(R.id.otp_2)
        otp3 = findViewById(R.id.otp_3)
        otp4 = findViewById(R.id.otp_4)
        otp5 = findViewById(R.id.otp_5)
        otp6 = findViewById(R.id.otp_6)

        otp=intent.getStringExtra("OTP").toString()
        phoneNo.text= "+91-"+intent.getStringExtra("PhoneNo").toString()

        verifyBtn.setOnClickListener {
            var otp11= otp1.text.toString().trim().isNotEmpty()
            var otp22= otp2.text.toString().trim().isNotEmpty()
            var otp33= otp3.text.toString().trim().isNotEmpty()
            var otp44= otp4.text.toString().trim().isNotEmpty()
            var otp55= otp5.text.toString().trim().isNotEmpty()
            var otp66= otp6.text.toString().trim().isNotEmpty()

            if(otp11 && otp22 && otp33 && otp44 && otp55 && otp66){
                progressBarVerify.visibility=View.VISIBLE
                verifyBtn.visibility=View.INVISIBLE
                var code= otp1.text.toString()+otp2.text.toString()+otp3.text.toString()+otp4.text.toString()+otp5.text.toString()+otp6.text.toString()
                FirebaseAuth.getInstance().signInWithCredential(PhoneAuthProvider.getCredential(otp,code))
                    .addOnCompleteListener {
                        progressBarVerify.visibility=View.INVISIBLE
                        verifyBtn.visibility=View.VISIBLE
                        if(it.isSuccessful){
                            var intent = Intent(applicationContext,MainActivity::class.java)
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(intent)
                        }
                }

            }else{
                Toast.makeText(this,"Please enter 6 digit OTP",Toast.LENGTH_SHORT).show()
            }
        }

        resendOtp.setOnClickListener {
            PhoneAuthProvider.getInstance().verifyPhoneNumber("+91"+intent.getStringExtra("PhoneNo").toString(), 60, TimeUnit.SECONDS,this, object :PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    Toast.makeText(baseContext,p0.message,Toast.LENGTH_SHORT).show()
                }

                override fun onCodeSent(
                    p0: String,
                    p1: PhoneAuthProvider.ForceResendingToken
                ) {
                    super.onCodeSent(p0, p1)
                    otp=p0
                    Toast.makeText(baseContext,"New Otp sent",Toast.LENGTH_SHORT).show()
                }
            })
        }
        


    }
}