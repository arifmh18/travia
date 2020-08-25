package com.travia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.travia.databinding.ActivityLoginBinding
import com.travia.utils.LoadingDialogUtil
import com.travia.utils.showToast

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var loadingDialog: LoadingDialogUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding =  ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {

        //init Firebase Auth
        auth = FirebaseAuth.getInstance()

        loadingDialog = LoadingDialogUtil(this)

        //View binding with ktx in action
        binding.apply {
            btnLogin.setOnClickListener {
                checkForm()
            }
            cvLoginWithGoogle.setOnClickListener {
                Log.d(TAG, "init: login by gmail")
            }
        }
    }

    //Check / validate form for login (email and password)
    private fun checkForm() {
        binding.apply {
            when {
                !edtEmailLogin.text.isNotBlank() -> {
                    showToast(this@LoginActivity, "Isi email anda !")
                }
                !edtPasswordLogin.text.isNotBlank() -> {
                    showToast(this@LoginActivity, "Isi password anda")
                }
                else -> {
                    loginByEmailPassword(
                        email = edtEmailLogin.text.toString(),
                        password = edtPasswordLogin.text.toString()
                    )
                }
            }
        }
    }

    //process login with email and password
    private fun loginByEmailPassword(email: String, password: String) {
        loadingDialog.show()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    Log.d(TAG, "loginByEmailPassword: login success")
                    showToast(this, "Login berhasil")
                    loadingDialog.dismiss()

                    /**
                     * TODO : Check user login role
                     *          if user ->  then go to wisatawan layout
                     *          else -> then go to mitra layout
                     */

                } else{
                    Log.d(TAG, "loginByEmailPassword: failed to login : ${task.exception}")
                    showToast(this, "Login gagal")
                    loadingDialog.dismiss()
                }
            }
    }

    companion object {
        var TAG = LoginActivity::class.java.simpleName
    }
}