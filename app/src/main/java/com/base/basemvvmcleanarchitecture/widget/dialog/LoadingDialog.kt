package com.base.basemvvmcleanarchitecture.widget.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.base.basemvvmcleanarchitecture.R

class LoadingDialog : DialogFragment() {
    private var isShowing = false

    fun showDialog(fragmentManager: FragmentManager) {
        if (!isShowing) {
            isShowing = true
            show(fragmentManager, null)
        }
    }

    fun hiddenDialog() {
        if (isShowing) {
            isShowing = false
            dismiss()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        isCancelable = false
        return activity?.let {
            val builder = AlertDialog.Builder(it, R.style.StyleLoadingDialog)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater
            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(inflater.inflate(R.layout.layout_loading, null))
            val dialog = builder.create()
            if (dialog.window != null) {
                dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(false)
            return dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        params?.width = (resources.displayMetrics.widthPixels * 0.95).toInt()
        params?.height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }

    override fun onDestroy() {
        isShowing = false
        super.onDestroy()
    }
}
