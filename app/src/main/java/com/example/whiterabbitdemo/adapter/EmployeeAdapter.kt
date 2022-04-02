package com.example.whiterabbitdemo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.whiterabbitdemo.R
import com.example.whiterabbitdemo.model.EmployeeResponseItem
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*
import kotlin.collections.ArrayList

internal class EmployeeAdapter(
    val mItemClickListener: ItemClickListener,
    private var list: List<EmployeeResponseItem?>?,
    val searchValueListener: SearchValue,

    ) :
    RecyclerView.Adapter<EmployeeAdapter.MyViewHolder>(), Filterable {

    var ecomList = list

    interface ItemClickListener{
        fun onItemClick(
            position: Int,
            detailsItem: EmployeeResponseItem?
        )
    }

    interface SearchValue {
        fun onSearch(
            type: String, data: ArrayList<EmployeeResponseItem>
        )
    }

    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.findViewById(R.id.name)
        var companyName: TextView = view.findViewById(R.id.company_name)
        var employee_image: CircleImageView = view.findViewById(R.id.employee_image)

        init {
            view.setOnClickListener{
                mItemClickListener.onItemClick(adapterPosition, list?.get(adapterPosition))
            }
        }
    }

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.employee_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val employeeResponseItem = this.list?.get(position)
        holder.name.text = employeeResponseItem!!.name.toString()
        holder.companyName.text = employeeResponseItem.username.toString()

        if(employeeResponseItem.profile_image.toString()!=""){
            Picasso.get().load(employeeResponseItem.profile_image.toString()).into(holder.employee_image)
        }

    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                val resultList = ArrayList<EmployeeResponseItem>()

                if (charSearch.isEmpty()) {
                    ecomList = list
                    resultList.clear()
                } else {
                    for (row in list!!) {
                        if (row!!.name!!.toLowerCase(Locale.ROOT).contains(
                                charSearch.toLowerCase(
                                    Locale.ROOT
                                )
                            )
                        ) {
                            resultList.add(row)
                        }
                    }
                    ecomList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = resultList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                ecomList = results?.values as ArrayList<EmployeeResponseItem>

                if ((ecomList as ArrayList<EmployeeResponseItem>).isEmpty()) {
                    searchValueListener.onSearch(
                        "empty",
                        results.values as ArrayList<EmployeeResponseItem>
                    )
                } else {
                    searchValueListener.onSearch(
                        "value",
                        results.values as ArrayList<EmployeeResponseItem>
                    )
                }
            }

        }
    }

}