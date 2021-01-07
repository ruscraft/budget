package com.russartstudio.budget

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatDialogFragment
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.util.*


class MainDialog(private val resId: Int,
                 private val themeDialog:Int,
                 private val vt:String,
                 private val DBtitle:String,
                 private val strDate:String): AppCompatDialogFragment() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val docRef= db.collection("Months").document(strDate)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val  builder = AlertDialog.Builder(activity, themeDialog)
        val view = LayoutInflater.from(activity).inflate(R.layout.dialog_main, null, false)
        val textView : TextView = view.findViewById(R.id.textView)
        val  editText : EditText=view.findViewById(R.id.editText)
        textView.text=vt
        builder.setView(view).setTitle(getString(resId)).setNegativeButton(
            "cancel"
        ) { dialogInterface: DialogInterface, i: Int ->

        }.setPositiveButton(
            "ok"
        ) { dialogInterface: DialogInterface, i: Int ->
            if(editText.text.toString().trim().isNotEmpty()){
                val inc =editText.text.toString().toDouble()
                val cv= hashMapOf(
                    DBtitle to FieldValue.increment(inc)
                )
                docRef.set(cv, SetOptions.merge())
                    if(resId==R.string.expenses) {
                        docRef.set("ExpensesSum" to FieldValue.increment(inc), SetOptions.merge())
                    }
            }

        }


        return builder.create()
    }
}