package com.russartstudio.budget

import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_debt.*
import kotlinx.android.synthetic.main.universal_list.*
import java.util.*

class DebtActivity: AppCompatActivity() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val c: Calendar = Calendar.getInstance()
    private val strYear=c.get(Calendar.YEAR).toString()
    private val strDate="${c.get(Calendar.MONTH)+1}.${strYear.takeLast(2)}"    //Date format
    private val docRef=db.collection("Months").document(strDate).collection("Debt")
    private var adapter: DBadapter? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debt)
        setUprecyclerView()
    }
    private fun setUprecyclerView(){
        val query:Query=docRef
        val options= FirestoreRecyclerOptions.Builder<ListObject>().setQuery(query, ListObject::class.java).build()
        adapter = DBadapter(options)

        DebtLoanRecyclerView.adapter = adapter
        DebtLoanRecyclerView.layoutManager = LinearLayoutManager(this)

    }
    override fun onStart() {
        super.onStart()
        adapter!!.startListening()
    }

    override fun onStop() {
        super.onStop()

        if (adapter != null) {
            adapter!!.stopListening()
        }
    }
}