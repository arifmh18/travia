package com.travia.ui.pemandu

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.travia.MainActivity
import com.travia.R
import com.travia.WisataModel
import com.travia.database.entity.WisataEntity
import com.travia.databinding.ActivityDetailPemanduBinding
import com.travia.model.PemanduModel
import com.travia.model.Users
import com.travia.utils.*
import com.travia.viewModel.WisataViewModel
import kotlinx.android.synthetic.main.activity_detail_pemandu.*
import java.io.ByteArrayOutputStream
import java.io.IOException

class DetailPemanduActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPemanduBinding
    private val viewModel by viewModels<WisataViewModel>()
    private var wisatas: ArrayList<WisataEntity> = ArrayList()
    private var ref: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var user: FirebaseUser
    private var nama = mutableListOf<String>("Pilih Wisata")
    private var id_wisata = mutableListOf<String>("Pilih Wisata")
    private var status = arrayOf("Pilih Status", "Online", "Offline", "Berkerja")
    private var filePath: Uri? = null
    private var storageReference: StorageReference =
        FirebaseStorage.getInstance().getReference("users/pemandu")
    private lateinit var img: String
    private lateinit var loadingDialog: LoadingDialogUtil
    private lateinit var userData: Users

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPemanduBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = auth.currentUser!!
        syncData()
        initSync()
    }

    private fun syncData() {
        loadingDialog = LoadingDialogUtil(this)
        viewModel.init(baseContext)
        ref.getReference("wisata/").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (value in snapshot.children) {
                        val item = value.getValue(WisataModel::class.java)
                        val data = WisataEntity(
                            item!!.uuid,
                            item.nama,
                            item.deskripsi,
                            item.kategory,
                            item.harga,
                            item.video_link,
                            item.location.name,
                            item.rekomendasi
                        )
                        nama.add(item.nama)
                        id_wisata.add(item.uuid)
                        wisatas.add(data)
                    }
                    viewModel.insertAll(wisatas)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }


    private fun initSync() {
        val adter = ArrayAdapter(this, R.layout.list_item, nama)
        val adterSts = ArrayAdapter(this, R.layout.list_item, status)
        binding.apply {
            spinWisata.adapter = adter
            spinStatus.adapter = adterSts
            BPick_img.setOnClickListener {
                launchImage()
            }
            edtPSertifikasi.setOnClickListener {
                launchImage()
            }
            if (filePath != null) {
                edtPSertifikasi.setText(filePath.toString())
            }
            btnPSimpan.setOnClickListener {
                formValidasi()
            }
            imgPick.hide()
        }
    }

    private fun formValidasi() {
        binding.apply {
            when {
                edtPMoto.text.isNullOrEmpty() -> {
                    showToast(this@DetailPemanduActivity, "Moto harus Di isi!!")
                }
                edtPHarga.text.isNullOrEmpty() -> {
                    showToast(this@DetailPemanduActivity, "Harga harus Di isi!!")
                }
                edtPwaktu.text.isNullOrEmpty() -> {
                    showToast(this@DetailPemanduActivity, "Waktu harus Di isi!!")
                }
                spinWisata.selectedItemPosition == 0 -> {
                    showToast(this@DetailPemanduActivity, "Nama Wisata harus Di isi!!")
                }
                spinStatus.selectedItemPosition == 0 -> {
                    showToast(this@DetailPemanduActivity, "Status harus Di isi!!")
                }
                else -> {
                    loadingDialog.show()
                    if (filePath != null) {
                        uploadImg()
                    } else {
                        img = "Tidak"
                        getData()
                    }
                }
            }
        }
    }

    private fun getData() {
        ref.getReference("users/").child(user.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        userData = snapshot.getValue(Users::class.java)!!
                        simpanData()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    showToast(applicationContext, "Data Not found!!")
                }

            })
    }


    private fun uploadImg() {
        val rand = getRandomString(12)
        val storRef = storageReference.child(rand)
        binding.imgPick.isDrawingCacheEnabled = true
        binding.imgPick.buildDrawingCache()
        val bitmap = (binding.imgPick.drawable as BitmapDrawable).bitmap
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
                getData()
            }
        }
    }

    private fun simpanData() {
        val data = PemanduModel(
            user.uid,
            userData.nama,
            img,
            binding.edtPMoto.text.toString(),
            binding.edtPHarga.text.toString(),
            binding.edtPwaktu.text.toString(),
            id_wisata[binding.spinWisata.selectedItemPosition],
            binding.spinStatus.selectedItem.toString(),
            userData.foto
        )
        ref.getReference("pemandu/").child(auth.uid!!).setValue(data)
            .addOnCompleteListener { rest ->
                if (rest.isSuccessful) {
                    startActivity(Intent(this@DetailPemanduActivity, MainActivity::class.java))
                    finish()
                }
                loadingDialog.dismiss()
            }
            .addOnFailureListener { err ->
                showToast(this@DetailPemanduActivity, "error : " + err.message)
                loadingDialog.dismiss()
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
                    binding.imgPick.setImageBitmap(bitmap)
                    val file = filePath.toString().split("/")
                    binding.edtPSertifikasi.setText(file[file.lastIndex])

                } catch (e: IOException) {
                    Log.d("Error pap: ", e.printStackTrace().toString())
                }
            }
        }
    }
}