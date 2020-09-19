package com.travia.ui.mitra.add_destination

import android.app.TimePickerDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import com.travia.DayScheduleModel
import com.travia.ScheduleModel
import com.travia.WisataModel
import com.travia.databinding.ActivitySetScheduleDestinationBinding
import com.travia.utils.LoadingDialogUtil
import com.travia.utils.getRandomString
import com.travia.utils.setLeadingZero
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.ArrayList

class SetScheduleDestinationActivity : AppCompatActivity(), AdapterSetSchedule.Listener {

    private lateinit var binding: ActivitySetScheduleDestinationBinding
    private lateinit var adapter: AdapterSetSchedule

    private lateinit var database : FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage

    private lateinit var loadingDialogUtil: LoadingDialogUtil

    private var wisataModel: WisataModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetScheduleDestinationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()

    }

    private fun init() {

        val intent = intent.extras
        if (intent != null){

            Log.d(TAG, "init: ${intent.getString(TAG_DESTINATION_DETAIL)}")

            val wisataString = Gson().fromJson(intent.getString(TAG_DESTINATION_DETAIL), WisataModel::class.java)
            wisataModel = wisataString
        }

        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()

        adapter = AdapterSetSchedule(this, this)

        loadingDialogUtil = LoadingDialogUtil(this)

        binding.apply {
            rvSetSchedule.layoutManager = LinearLayoutManager(this@SetScheduleDestinationActivity)
            rvSetSchedule.adapter = adapter

            btnAddDestination.setOnClickListener {
                setScheduleToDestination()
            }

            tbSetSchedule.setNavigationOnClickListener {
                setResult(RESULT_CANCELED)
                finish()
            }
        }

        setScheduleList()
    }

    private fun setScheduleToDestination() {
        loadingDialogUtil.show()
        val listSchedule = adapter.getScheduleList()

        val scheduleModel = ScheduleModel()

        for ((index, schedule) in listSchedule.withIndex()){

            Log.d(TAG, "setScheduleToDestination: buka ${schedule.buka}")
            Log.d(TAG, "setScheduleToDestination: tutup ${schedule.tutup}")

            if (!schedule.buka!!.isNotBlank()){
                schedule.buka = "tutup"
            }

            if (!schedule.tutup!!.isNotBlank()){
                schedule.tutup = "tutup"
            }

            when (index) {

                0 -> {
                    scheduleModel.senin = schedule
                }
                1 -> {
                    scheduleModel.selasa = schedule
                }
                2 -> {
                    scheduleModel.rabu = schedule
                }
                3 -> {
                    scheduleModel.kamis = schedule
                }
                4 -> {
                    scheduleModel.jumat = schedule
                }
                5 -> {
                    scheduleModel.sabtu = schedule
                }
                6 -> {
                    scheduleModel.minggu = schedule
                }
            }
        }

        wisataModel?.jadwal = scheduleModel

        Log.d(TAG, "setScheduleToDestination: $wisataModel")

        submitDestination()
    }

    private fun submitDestination() {

        var photoUrl = ArrayList<String>()

        for ((i, path) in wisataModel?.gambar!!.withIndex()){

            var limit = i+1

            Log.d(TAG, "submitDestination: limit $limit")

            Log.d(TAG, "submitDestination: gambar size ${wisataModel?.gambar!!.size}")

            Log.d(TAG, "submitDestination: index in $i")

            BitmapFactory.decodeFile(path).also { bitmap ->
                val bos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 30, bos)
                val bitmapData = bos.toByteArray()

                val uid = getRandomString(5)

                val url = storage.reference.child("wisata/photos/$uid")

                val upload = url.putBytes(bitmapData)

                val urlTask = upload.continueWithTask { uploadTask ->
                    if (!uploadTask.isSuccessful){
                        uploadTask.exception?.let {exception ->
                            throw exception
                        }
                    }
                    url.downloadUrl
                }.addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        Log.d(TAG, "submitDestination: ${task.result.toString()} successfull")
                        photoUrl.add(task.result.toString())

                        if (limit == wisataModel!!.gambar!!.size){
                            Log.d(TAG, "submitDestination: 2 index in $i")
                            Log.d(TAG, "submitDestination: $wisataModel")
                            wisataModel!!.gambar = photoUrl
                            addDestination()
                        }
                    }
                }
                Log.d(TAG, "submitDestination: ${urlTask.toString()}")
            }
        }
    }

    private fun addDestination() {
        database.reference.child("wisata").child(auth.currentUser!!.uid)
            .setValue(wisataModel)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    Toast.makeText(this, "Berhasil tambah wisata", Toast.LENGTH_SHORT).show()
                    loadingDialogUtil.dismiss()
                    setResult(RESULT_OK)
                    finish()
                }else {
                    Toast.makeText(this, "${task.exception}", Toast.LENGTH_SHORT).show()
                    loadingDialogUtil.dismiss()
                }
            }
    }

    private fun setScheduleList() {
        val listSchedule = ArrayList<DayScheduleModel>()

        for (i in 0..6){
            listSchedule.add(
                DayScheduleModel(buka = "", tutup = "")
            )
        }

        adapter.setListSchedule(listSchedule = listSchedule)
    }

    private fun showTimePickerDialog(position: Int, type : Int) {

        val calendar = Calendar.getInstance()

        val timePickerDialog = TimePickerDialog(this, {
                _, hour, minute ->

                if (type == 1){
                    adapter.setOpenTime(open = "${setLeadingZero(hour)}:${setLeadingZero(minute)}", position = position)
                }else {
                    adapter.setCloseTime(close = "${setLeadingZero(hour)}:${setLeadingZero(minute)}", position = position)
                }

        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true )

        timePickerDialog.show()

    }

    override fun onChooseOpenTime(position: Int) {
        showTimePickerDialog(position = position, type = 1)
    }

    override fun onChooseCloseTime(position: Int) {
        showTimePickerDialog(position = position, type = 2)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResult(RESULT_CANCELED)
        finish()
    }

    companion object {
        var TAG = SetScheduleDestinationActivity::class.java.simpleName
        var TAG_DESTINATION_DETAIL = "TAG_DESTINATION_DETAIL"
    }
}