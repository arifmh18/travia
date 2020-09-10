package com.travia

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ArrayAdapter
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.travia.databinding.ActivityEditProfileBinding
import com.travia.model.Users
import com.travia.utils.LoadingDialogUtil
import com.travia.utils.PICK_IMAGE_REQUEST
import com.travia.utils.getRandomString
import com.travia.utils.showToast
import java.io.ByteArrayOutputStream
import java.io.IOException

class ActivityEditProfile : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private var filePath: Uri? = null
    private lateinit var loadingDialog: LoadingDialogUtil
    private lateinit var img: String
    lateinit var mref: DatabaseReference
    lateinit var storageReference: StorageReference
    private lateinit var user: FirebaseUser
    private var i = Bundle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = FirebaseAuth.getInstance().currentUser!!
        mref = FirebaseDatabase.getInstance().getReference("users/").child(user.uid)
        storageReference = FirebaseStorage.getInstance().getReference("users/foto/")
        initSync()
    }

    private fun initSync() {
        loadingDialog = LoadingDialogUtil(this)
        val adpter = ArrayAdapter.createFromResource(this, R.array.jk_string, R.layout.list_item)
        i = intent.extras!!

        binding.apply {
            edpNama.setText(i.getString("nama"))
            edpEmail.setText(i.getString("email"))
            edpPhone.setText(i.getString("phone"))
            val jk = when (i.getString("jk")) {
                "Laki-Laki" -> 1
                "Perempuan" -> 2
                else -> 0
            }
            img = i.getString("img")!!
            if (img != "Tidak" && img != "") {
                Glide.with(this@ActivityEditProfile).load(img).into(pickFoto)
            }
            edpJk.adapter = adpter
            edpJk.setSelection(jk)
            gantiPP.setOnClickListener {
                launchImage()
            }
            tbEditProfile.setNavigationOnClickListener {
                finish()
            }
            tbEditProfile.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.btn_simpan -> {
                        loadingDialog.show()
                        uploadData()
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
        }
    }

    private fun uploadData() {
        if (filePath != null) {
            uploadImg()
        } else {
            pushData()
        }
    }

    private fun pushData() {
        var datas:Users
        binding.apply {
            datas = Users(
                user.uid,
                edpNama.text.toString(),
                edpEmail.text.toString(),
                edpPhone.text.toString(),
                edpJk.selectedItem.toString(),
                i.getString("role")!!,
                i.getString("pass")!!,
                img
            )
        }
        mref.setValue(datas).addOnSuccessListener {
            loadingDialog.dismiss()
            finish()
        }.addOnFailureListener {
            loadingDialog.dismiss()
            showToast(this, it.message.toString())
        }
    }

    private fun uploadImg() {
        val rand = getRandomString(12)
        val storRef = storageReference.child(rand)
        binding.pickFoto.isDrawingCacheEnabled = true
        binding.pickFoto.buildDrawingCache()
        val bitmap = (binding.pickFoto.drawable as BitmapDrawable).bitmap
        val resize = Bitmap.createScaledBitmap(
            bitmap,
            (bitmap.width * 0.3).toInt(),
            (bitmap.height * 0.3).toInt(), true
        )
        val baos = ByteArrayOutputStream()
        resize.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val dt = baos.toByteArray()

        storRef.putBytes(dt).addOnFailureListener {
            loadingDialog.dismiss()
            showToast(this, it.message.toString())
        }.addOnSuccessListener {
            showToast(this, "Berhasil Upload Foto!")
            it.metadata?.reference!!.downloadUrl.addOnSuccessListener { uri ->
                img = uri.toString()
                pushData()
            }
        }
    }

    private fun launchImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST) {
            if (resultCode == Activity.RESULT_OK && data!!.data != null) {
                filePath = data.data!!
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, filePath)
                    binding.pickFoto.setImageBitmap(bitmap)
                } catch (e: IOException) {
                    print("Error pap: ${e.printStackTrace()}")
                }
            }
        }
    }
}