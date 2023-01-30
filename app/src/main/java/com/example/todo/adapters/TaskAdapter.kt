package com.example.todo.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R
import com.example.todo.databinding.ItemTaskBinding
import com.example.todo.models.Task


class TaskAdapter(
    private val dataSet: List<Task>,
//    private val changeTodoDone: (position: Long) -> Unit
) :
    RecyclerView.Adapter<TaskAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView
        val statusView: TextView

        val card: CardView

        init {

            textView = view.findViewById(R.id.text_task)
            statusView = view.findViewById(R.id.status)
            card = view.findViewById(R.id.card_task)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_task, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        val task: Task = dataSet[position]

        viewHolder.textView.text = task.text

        viewHolder.statusView.text = if (task.status == Task.TODO) "To Do" else "Done"

//        viewHolder.card.setOnLongClickListener {
//            changeTodoDone(task.id)
//            true
//        }
    }

    override fun getItemCount() = dataSet.size

}