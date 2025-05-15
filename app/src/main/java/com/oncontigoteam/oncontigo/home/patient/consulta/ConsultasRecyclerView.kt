package     com.oncontigoteam.oncontigo.home.patient.consulta

import      android.view.LayoutInflater
import      android.view.View
import      android.view.ViewGroup
import      android.widget.TextView
import      androidx.recyclerview.widget.DiffUtil
import      androidx.recyclerview.widget.ListAdapter
import      androidx.recyclerview.widget.RecyclerView
import      com.oncontigoteam.oncontigo.R

public final class ConsultasAdapter : ListAdapter<Consulta, ConsultasAdapter.ViewHolder>(DiffCallback()) {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val description: TextView = itemView.findViewById(R.id.consulta_description)
        private val date: TextView  = itemView.findViewById(R.id.consulta_date)

        fun bind(item: Consulta) {
            description.text    = item.description
            date.text           = item.dateTime

            itemView.setOnClickListener {

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.consulta_item, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<Consulta>() {
        override fun areItemsTheSame(old: Consulta, new: Consulta) =
            old.id == new.id

        override fun areContentsTheSame(old: Consulta, new: Consulta) =
            old == new
    }
}