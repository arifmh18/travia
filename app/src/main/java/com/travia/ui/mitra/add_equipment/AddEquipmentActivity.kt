package com.travia.ui.mitra.add_equipment

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.travia.EquipmentModel
import com.travia.R
import com.travia.databinding.ActivityAddEquipmentBinding
import com.travia.utils.LoadingDialogUtil
import kotlinx.android.synthetic.main.item_set_requirement.view.*
import kotlinx.android.synthetic.main.layout_set_requirement.view.*

class AddEquipmentActivity : AppCompatActivity(), AdapterRequirement.Listener {

    private lateinit var binding: ActivityAddEquipmentBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    private lateinit var loadingDialogUtil: LoadingDialogUtil

    private lateinit var adapter: AdapterRequirement

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEquipmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()

    }

    private fun init() {
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        loadingDialogUtil = LoadingDialogUtil(this)

        adapter = AdapterRequirement(this, this)

        binding.apply {
            btnAddEquipment.setOnClickListener {
                checkForm()
            }

            rvEquipmentRequirement.layoutManager = LinearLayoutManager(this@AddEquipmentActivity)
            rvEquipmentRequirement.adapter = adapter

            btnAddRequirementEquipment.setOnClickListener {
                setRequirement(
                    type = 1,
                    mName = null,
                    mRequired = null,
                    position = null
                )
            }
        }

    }

    private fun checkForm() {
        binding.apply {
            when {
                !tieEquipmentName.text.toString().isNotBlank() -> {
                    tilEquipmentName.error = "Isi nama peralatan"
                }
                !tieEquipmentDescription.text.toString().isNotBlank() -> {
                    tilEquipmentDescription.error = "Isi deskripsi peralatan"
                }
                !tieEquipmentStock.text.toString().isNotBlank() -> {
                    tilEquipmentStock.error = "Isi stok peralatan"
                }
                !tieEquipmentPrice.text.toString().isNotBlank() -> {
                    tilEquipmentPrice.error = "Isi harga sewa peralatan"
                }
                else -> {
                    submitEquipment(
                        name = tieEquipmentName.text.toString(),
                        description = tieEquipmentDescription.text.toString(),
                        stock = tieEquipmentStock.text.toString(),
                        price = tieEquipmentPrice.text.toString()
                    )
                }
            }
        }
    }

    private fun submitEquipment(name: String, description: String, stock: String, price: String) {
        loadingDialogUtil.show()

        val equipmentModel = EquipmentModel(
            uuid = auth.currentUser?.uid,
            nama = name,
            deskripsi = description,
            stok = stock,
            harga = price,
            syarat = if (adapter.getRequirement().isEmpty()) null else adapter.getRequirement()
        )

        database.reference.child("peralatan").push()
            .setValue(equipmentModel)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    Toast.makeText(this, "Berhasil tambah peralatan", Toast.LENGTH_SHORT).show()
                    loadingDialogUtil.dismiss()
                    finish()
                }else {
                    Toast.makeText(this, "Gagal ${task.exception}", Toast.LENGTH_SHORT).show()
                    loadingDialogUtil.dismiss()
                }
            }
    }

    private fun setRequirement(type: Int, mName: String?, mRequired: Boolean?, position: Int?){

        val view = layoutInflater.inflate(R.layout.layout_set_requirement, binding.parentViewAddEquipment, false)

        if (type == 2){
            view.apply {
                setRequirementName.setText(mName)
                cbSetRequiredRequirement.isChecked = mRequired!!
            }
        }

        val builder = AlertDialog.Builder(this)
            .setView(view)
            .setPositiveButton(if (type == 1) "Tambah" else "Edit") { dialogInterface, _ ->
                view.apply {

                    val name = setRequirementName.text.toString()
                    val required = cbSetRequiredRequirement.isChecked

                    if (type == 2){
                        adapter.editRequirement(
                            name = name, required = required, position = position!!
                        )
                    }else {
                        adapter.addRequirement(
                            name = name, required = required
                        )
                    }

                    dialogInterface.dismiss()
                }
            }
            .setNegativeButton("Batal") { dialogInterface, i ->
                dialogInterface.dismiss()
            }
            .setCancelable(false)

        builder.show()
    }

    override fun onEditRequirement(name: String, required: Boolean, position: Int) {
        setRequirement(
            type = 2,
            mName = name,
            mRequired = required,
            position = position
        )
    }
}