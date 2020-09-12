package com.travia.ui.mitra.add_destination

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.travia.R
import com.travia.WisataModel
import com.travia.databinding.ActivitySetRequirementBinding
import com.travia.ui.mitra.add_equipment.AdapterRequirement
import kotlinx.android.synthetic.main.layout_set_requirement.view.*

class SetRequirementActivity : AppCompatActivity(), AdapterRequirement.Listener {

    private lateinit var binding: ActivitySetRequirementBinding

    private lateinit var adapter: AdapterRequirement

    private var wisataModel: WisataModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetRequirementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {

        val intent = intent.extras
        if (intent != null){

            Log.d(SetScheduleDestinationActivity.TAG, "init: ${intent.getString(SetRequirementActivity.TAG_DESTINATION_DETAIL)}")

            val wisataString = Gson().fromJson(intent.getString(SetScheduleDestinationActivity.TAG_DESTINATION_DETAIL), WisataModel::class.java)
            wisataModel = wisataString
        }

        adapter = AdapterRequirement(this, this)

        binding.apply {
            tbSetRequirementDestination.setOnClickListener {
                finish()
            }

            rvDestinationRequirement.layoutManager = LinearLayoutManager(this@SetRequirementActivity)
            rvDestinationRequirement.adapter = adapter

            btnAddRequirementDestination.setOnClickListener {
                setRequirement(
                    type = 1,
                    mName = null,
                    mRequired = null,
                    position = null
                )
            }

            btnAddDestination.setOnClickListener {

                checkRequirement()
            }
        }
    }

    private fun checkRequirement() {

        wisataModel?.requirement = adapter.getRequirement()

        val wisataString = Gson().toJson(wisataModel)

        val intent = Intent(this@SetRequirementActivity, SetScheduleDestinationActivity::class.java)
        intent.putExtra(SetScheduleDestinationActivity.TAG_DESTINATION_DETAIL, wisataString)
        startActivityForResult(intent, 1)
    }

    private fun setRequirement(type: Int, mName: String?, mRequired: Boolean?, position: Int?){

        val view = layoutInflater.inflate(R.layout.layout_set_requirement, binding.parentViewSetRequirementDestination, false)

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1){
            if (resultCode == RESULT_OK){
                setResult(RESULT_OK)
                finish()
            }else {
                Toast.makeText(this, "Gagal menambah wisata", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        var TAG_DESTINATION_DETAIL = "TAG_DESTINATION_DETAIL"
    }
}