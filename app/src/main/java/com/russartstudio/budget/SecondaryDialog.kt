package com.russartstudio.budget

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialogFragment
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class SecondaryDialog(private val resId: Int,
                      private val themeDialog:Int,
                      private val strDate:String,
                      private val cont: Context
                                                 ): AppCompatDialogFragment() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val docRef= db.collection("Months").document(strDate)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val  builder = AlertDialog.Builder(activity, themeDialog)
        val view = LayoutInflater.from(activity).inflate(R.layout.dialog_secondary, null, false)
        val autoComp:AutoCompleteTextView=view.findViewById(R.id.autoCompSD)
        val editText:EditText=view.findViewById(R.id.editTextSD)
        val arraylist = ArrayList<String>()
        docRef.collection(getString(resId)).get().addOnSuccessListener { documents ->

            for (document in documents) {
                arraylist.add(document.id)
            }
        }
        val adapter = ArrayAdapter<String>(cont, android.R.layout.simple_list_item_1, arraylist)
        autoComp.setAdapter(adapter)

        builder.setView(view).setTitle(getString(resId)).setNegativeButton(
            "cancel"
        ) { dialogInterface: DialogInterface, i: Int ->

        }.setPositiveButton(
            "ok"
        ) {dialogInterface: DialogInterface, i: Int ->
            if(editText.text.toString().trim().isNotEmpty() && autoComp.text.toString().trim().isNotEmpty()){
                val inc =editText.text.toString().toDouble()
                val DBtitle = autoComp.text.toString()
                val specRef=docRef.collection(getString(resId)).document(DBtitle)
                val cv= hashMapOf(
                    "Amount" to FieldValue.increment(inc)
                )
                specRef.set(cv, SetOptions.merge())

                if(resId==R.string.debt) {
                   docRef.set("Debt" to FieldValue.increment(inc), SetOptions.merge() )
                }
                else   docRef.set("Loan" to FieldValue.increment(inc), SetOptions.merge() )
            }
            else Toast.makeText(cont, "Failed!", Toast.LENGTH_SHORT).show()
        }

        return builder.create()
    }
}