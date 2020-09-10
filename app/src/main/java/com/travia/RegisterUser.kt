package com.travia

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.travia.databinding.ActivityRegisterUserBinding
import com.travia.model.Users
import com.travia.ui.mitra.add_destination.AddDestinationActivity
import com.travia.ui.pemandu.DetailPemanduActivity
import com.travia.utils.LoadingDialogUtil
import com.travia.utils.TAG_GOOGLE_CLIENT_ID
import com.travia.utils.showToast
import kotlinx.android.synthetic.main.activity_register_user.*

class RegisterUser : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterUserBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var ref: FirebaseDatabase
    private lateinit var loadingDialog: LoadingDialogUtil

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var gso: GoogleSignInOptions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        deklarasi
        auth = FirebaseAuth.getInstance()
        ref = FirebaseDatabase.getInstance()
        loadingDialog = LoadingDialogUtil(this)
        //init Google sign in client
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(TAG_GOOGLE_CLIENT_ID)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        init()
    }

    private fun init() {
//        spinner
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.jk_string,
            R.layout.list_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        val role = ArrayAdapter.createFromResource(
            this,
            R.array.role,
            R.layout.list_item
        )
        role.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.apply {
            edtJk.adapter = adapter
            edtRole.adapter = role
            masuk.setOnClickListener {
                startActivity(Intent(this@RegisterUser, LoginActivity::class.java))
            }
            googleLogin.setOnClickListener {
                GoogleWithGoogle()
            }
            btn_register.setOnClickListener {
                formValidasi()
            }
        }
    }

    private fun formValidasi() {
        loadingDialog.show()
        binding.apply {
            when {
                edtNama.text.isNullOrBlank() -> {
                    showToast(this@RegisterUser, "Isikan Nama Anda!!")
                }
                edtEmail.text.isNullOrBlank() -> {
                    showToast(this@RegisterUser, "Isikan Email Anda!!")
                }
                edtTelp.text.isNullOrBlank() -> {
                    showToast(this@RegisterUser, "Isikan No Telp Anda!!")
                }
                edtPassword.text.isNullOrBlank() -> {
                    showToast(this@RegisterUser, "Isikan Password Anda!!")
                }
                edtPassword.text.toString().length < 6 -> {
                    showToast(this@RegisterUser, "Password harus lebih dari 6 Karakter!!")
                }
                edtJk.selectedItemPosition == 0 -> {
                    showToast(this@RegisterUser, "Pilih Jenis Kelamin Anda!!")
                }
                edtRole.selectedItemPosition == 0 -> {
                    showToast(this@RegisterUser, "Pilih Jenis User anda!!")
                }
                else -> {
                    registerEmail(edtEmail.text.toString(), edtPassword.text.toString())
                }
            }
        }
    }

    private fun registerEmail(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val data = Users(
                        auth.currentUser!!.uid,
                        edtNama.text.toString(),
                        edtEmail.text.toString(),
                        edtTelp.text.toString(),
                        edtJk.selectedItem.toString(),
                        edtRole.selectedItem.toString(),
                        edtPassword.text.toString(),
                        "Tidak"
                    )
                    ref.getReference("users").child(auth.currentUser!!.uid).setValue(data)
                        .addOnCompleteListener { rest ->
                            if (rest.isSuccessful) {
                                loadingDialog.dismiss()
                                val i: Intent = if (binding.edtRole.selectedItemPosition == 3) {
                                    Intent(this@RegisterUser, DetailPemanduActivity::class.java)
                                } else if (binding.edtRole.selectedItemPosition == 2) {
                                    Intent(
                                        this@RegisterUser,
                                        AddDestinationActivity::class.java
                                    )
                                } else {
                                    Intent(this@RegisterUser, MainActivity::class.java)
                                }
                                startActivity(i)
                                finish()
                            }
                        }
                        .addOnFailureListener { err ->
                            showToast(this@RegisterUser, "error : " + err.message)
                            loadingDialog.dismiss()
                        }
                }
            }
            .addOnFailureListener {
                showToast(this@RegisterUser, "Error : " + it.message)
                loadingDialog.dismiss()
            }
    }

    private fun GoogleWithGoogle() {
        loadingDialog.show()
        val signInGoogleIntent = googleSignInClient.signInIntent
        startActivityForResult(signInGoogleIntent, REQ_SIGN_IN_GOOGLE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQ_SIGN_IN_GOOGLE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account = task.getResult(ApiException::class.java)
                Log.d(TAG, "onActivityResult: ${account?.id}")
                firebaseAuthWithGoogle(account?.idToken!!)
            } catch (e: ApiException) {
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
                if (task.isSuccessful) {
                    Log.d(TAG, "firebaseAuthWithGoogle: login success")
                    showToast(this, "Login dengan Google berhasil")
                    validasi()

                } else {
                    Log.d(TAG, "firebaseAuthWithGoogle: login failed ${task.exception}")
                    showToast(this, "Login dengan Google gagal")
                    loadingDialog.dismiss()
                }
            }
    }

    private fun validasi() {
        ref.getReference("users").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var user = false
                for (h in snapshot.children) {
                    if (auth.currentUser!!.uid == h.key) {
                        user = true
                    }
                }
//                loadingDialog.dismiss()
                if (!user) {
                    startActivity(
                        Intent(
                            this@RegisterUser,
                            RegisterMitra::class.java
                        )
                    )
                } else {
                    startActivity(Intent(this@RegisterUser, MainActivity::class.java))
                }
                finish()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    companion object {
        var TAG = RegisterUser::class.java.simpleName
        var REQ_SIGN_IN_GOOGLE = 22
    }
}