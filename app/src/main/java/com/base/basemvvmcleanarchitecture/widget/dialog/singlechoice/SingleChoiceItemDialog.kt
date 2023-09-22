package com.base.basemvvmcleanarchitecture.widget.dialog.singlechoice

import android.app.AlertDialog
import android.app.Dialog
import android.content.ComponentName
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.basemvvmcleanarchitecture.BuildConfig
import com.base.basemvvmcleanarchitecture.R
import com.base.basemvvmcleanarchitecture.base.BaseActivity
import com.base.basemvvmcleanarchitecture.databinding.DialogSingleChoiceItemBinding
import com.base.basemvvmcleanarchitecture.utils.helper.CamouflageIconHelper
import com.base.data.local.datastore.DataStoreRepository
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class SingleChoiceItemDialog : DialogFragment() {

    companion object {
        const val BUNDLE_TYPE = "BUNDLE_TYPE"

        @JvmStatic
        fun newInstance(bundle: Bundle? = null): SingleChoiceItemDialog {
            val dialogFragment = SingleChoiceItemDialog()
            dialogFragment.arguments = bundle
            return dialogFragment
        }
    }


    @Inject
    lateinit var dataStoreRepository: DataStoreRepository

    private lateinit var camouflageIconAdapter: CamouflageIconAdapter

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { fragmentActivity ->
            val builder = MaterialAlertDialogBuilder(fragmentActivity)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater
            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            val binding = DialogSingleChoiceItemBinding.inflate(inflater)

            arguments?.getString(BUNDLE_TYPE)?.let { it ->
                if (it == SingleChoiceType.CAMOUFLAGE_ICON.name) {
                    binding.textTitle.text = getString(R.string.camouflage_icon)

                    val launcherAlias = runBlocking {
                        val name = dataStoreRepository.getCamouflageIconName()
                        name.ifEmpty {
                            CamouflageIconHelper.CamouflageIconType.DEFAULT.key
                        }
                    }
                    val camouflageIconList = CamouflageIconHelper().getCamouflageIconList()
                    camouflageIconList.singleOrNull { it.key == launcherAlias }?.isChecked = true

                    var itemSelected: CamouflageIconHelper.CamouflageIconModel? = null

                    camouflageIconAdapter = CamouflageIconAdapter { item ->
                        itemSelected = item
                        val tempList =
                            camouflageIconList.map { it.copy(isChecked = item.key == it.key) }
                        camouflageIconAdapter.submitList(tempList)
                    }
                    camouflageIconAdapter.submitList(camouflageIconList)

                    binding.recycleView.apply {
                        layoutManager = LinearLayoutManager(context)
                        setHasFixedSize(true)
                        adapter = camouflageIconAdapter
                        itemAnimator = null    // disable animation for ListAdapter
                    }

                    binding.textOK.setOnClickListener {
                        itemSelected?.key?.let {
                            runBlocking {
                                val pm = requireActivity().applicationContext.packageManager
                                pm.setComponentEnabledSetting(
                                    ComponentName(
                                        requireActivity(),
                                        "${BuildConfig.APPLICATION_ID}$launcherAlias"
                                    ),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                                    PackageManager.DONT_KILL_APP
                                )

                                dataStoreRepository.setCamouflageIconName(it)

                                pm.setComponentEnabledSetting(
                                    ComponentName(
                                        requireActivity(),
                                        "${BuildConfig.APPLICATION_ID}$it"
                                    ),
                                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                                    PackageManager.DONT_KILL_APP
                                )
                            }
                        }
                    }
                }
            }



            binding.textCancel.setOnClickListener {
                dismiss()
            }

            builder.setView(binding.root)
            val dialog = builder.create()
//            if (dialog.window != null) {
//                dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//            }
//            dialog.setCancelable(false)
//            dialog.setCanceledOnTouchOutside(false)
            return dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }

//    override fun onResume() {
//        super.onResume()
//        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
//        params?.width = (resources.displayMetrics.widthPixels * 0.9).toInt()
//        params?.height = ViewGroup.LayoutParams.WRAP_CONTENT
//        dialog?.window?.attributes = params as WindowManager.LayoutParams
//    }
}

enum class SingleChoiceType(val value: Int) {
    CAMOUFLAGE_ICON(0)
}