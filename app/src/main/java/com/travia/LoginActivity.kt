package com.travia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.travia.databinding.ActivityLoginBinding
import com.travia.ui.mitra.AddDestinationActivity
import com.travia.ui.mitra.add_equipment.AddEquipmentActivity
import com.travia.utils.LoadingDialogUtil
import com.travia.utils.TAG_GOOGLE_CLIENT_ID
import com.travia.utils.showToast

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var loadingDialog: LoadingDialogUtil

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var gso: GoogleSignInOptions

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

        if (auth.currentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        //init Google sign in client
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(TAG_GOOGLE_CLIENT_ID)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)


        loadingDialog = LoadingDialogUtil(this)

        //View binding with ktx in action
        binding.apply {
            btnLogin.setOnClickListener {
                checkForm()
            }
            cvLoginWithGoogle.setOnClickListener {
                loginWithGoogle()
            }
            tvToRegister.setOnClickListener {
                startActivity(Intent(this@LoginActivity, Register_User::class.java))
            }
        }
    }

    //launch login with google using
    private fun loginWithGoogle() {
        loadingDialog.show()
        val signInGoogleIntent = googleSignInClient.signInIntent
        startActivityForResult(signInGoogleIntent, REQ_SIGN_IN_GOOGLE)
    }

    //Check / validate form for login by email and password
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

                    startActivity(Intent(this, MainActivity::class.java))
                    finish()

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQ_SIGN_IN_GOOGLE){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account = task.getResult(ApiException::class.java)
                Log.d(TAG, "onActivityResult: ${account?.id}")
                firebaseAuthWithGoogle(account?.idToken!!)
            } catch (e: ApiException){
                Log.d(TAG, "onActivityResult: failed to login with google : ${e.message}")
                showToast(this, "Login dengan Google gagal")
                loadingDialog.dismiss()
            }
        }
    }

    //proceed firebase auth with token provided by google sign in client
    private fun firebaseAuthWithGoogle(idToken: String) {
        val userCredential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(userCredential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    Log.d(TAG, "firebaseAuthWithGoogle: login success")
                    showToast(this, "Login dengan Google berhasil")
                    loadingDialog.dismiss()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else{
                    Log.d(TAG, "firebaseAuthWithGoogle: login failed ${task.exception}")
                    showToast(this, "Login dengan Google gagal")
                    loadingDialog.dismiss()
                }
            }
    }

    companion object {
        var TAG = LoginActivity::class.java.simpleName
        var REQ_SIGN_IN_GOOGLE = 22
    }
}