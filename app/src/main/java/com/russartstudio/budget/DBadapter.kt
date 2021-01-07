package com.russartstudio.budget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class DBadapter(options: FirestoreRecyclerOptions<ListObject>)  : FirestoreRecyclerAdapter<ListObject, DBadapter.ListHolder>(
    options
) {

    class ListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val TVname=itemView.findViewById<TextView>(R.id.list_name)
        val TVsum=itemView.findViewById<TextView>(R.id.list_sum)
        val IVdollar=itemView.findViewById<ImageView>(R.id.listIVdollar)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListHolder {
       val v:View=LayoutInflater.from(parent.context).inflate(
           R.layout.universal_list,
           parent,
           false
       )
        return ListHolder(v)
    }

    override fun onBindViewHolder(holder: ListHolder, position: Int, model: ListObject) {
        holder.TVname.text=snapshots.getSnapshot(position).id
        holder.TVsum.text=model.Amount.toString()

    }
}