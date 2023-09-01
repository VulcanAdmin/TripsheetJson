package com.dispatch.tripsheet.Exception

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.dispatch.tripsheet.R
import com.dispatch.tripsheet.model.Cell2
import kotlinx.android.synthetic.main.part_list_item.view.*

internal class ReturnFormAdapter(
    var partsList: ArrayList<Cell2>,
    val context: DelException,
    val adapter: ArrayAdapter<String>,
    var hdrcbExc: CheckBox,

    ) :
    RecyclerView.Adapter<ReturnFormAdapter.ReturnFormViewHolder>(){


    inner class ReturnFormViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemcbExc: CheckBox = itemView.findViewById(R.id.itemcbExc)
        val itemtxtpartName: TextView = itemView.findViewById(R.id.itemtxtpartName)
        val itemspnReason: Spinner = itemView.findViewById(R.id.itemspnReason)
        val itemspnQTY: Spinner = itemView.findViewById(R.id.itemspnQTY)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReturnFormViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.part_list_item, parent, false)
        return ReturnFormViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return partsList.size
    }

    private  fun updateData(data: List<Cell2>) {
        partsList = data as ArrayList<Cell2>
        notifyDataSetChanged()
    }


//    class RowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
//        var itemcbExc: CheckBox = itemView.findViewById(R.id.itemcbExc)
//        val itemtxtpartName: TextView = itemView.findViewById<TextView>(R.id.itemtxtpartName)
//        var itemspnReason: Spinner = itemView.findViewById(R.id.itemspnReason)
//        var itemspnQTY: Spinner = itemView.findViewById(R.id.itemspnQTY)
//    }

     override fun onBindViewHolder(holder: ReturnFormViewHolder, position: Int) {


        val modal = partsList[position]

         var i = 1
         val spnlist: MutableList<Int> = ArrayList()

             while (i <=  modal.QTY_DELV){ spnlist.add(i)
                 i++}
         holder.itemspnQTY.adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, spnlist)


        holder.apply {

            hdrcbExc.setOnClickListener(View.OnClickListener {
                Log.e("Clicked", "Exception on all delivery")
                partsList.forEach { it.Exception = hdrcbExc.isChecked}
                notifyDataSetChanged()
            })

            if (modal.Exception){itemcbExc.isChecked = true

            }

            itemcbExc.setOnClickListener(View.OnClickListener {
                Log.e("Clicked", "Exception on delivery")
                modal.Exception = itemcbExc.isChecked
              //  notifyDataSetChanged()

                holder.itemView.apply {
                    setBackgroundResource(
                        if (modal.Exception) {
                            R.color.yellow
                        } else {
                             android.R.color.transparent
                        } )
                }
            })

        }

        holder.itemView.apply {

            itemtxtpartName.text = modal.DESCRIPTION.toString()
            itemtxtpartQTY.text = modal.QTY_DELV.toString()
            itemcbExc.isChecked = modal.Exception

            setBackgroundResource( when (modal.Exception ){
                true -> R.color.yellow
                false -> android.R.color.transparent
            })
        }



        holder.itemView.apply {
            itemspnReason.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    //modal.EReason= list[p2].toString()
                    modal.EReason= p2
                }
                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }

            itemspnQTY.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    modal.EQTY  =  p2 + 1 }
                override fun onNothingSelected(p0: AdapterView<*>?) {}
            } }


    }


}