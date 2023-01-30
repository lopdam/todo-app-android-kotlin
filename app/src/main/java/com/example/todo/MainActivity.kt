package com.example.todo

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.adapters.TaskAdapter
import com.example.todo.models.Task
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var shimmer: ShimmerFrameLayout
    private lateinit var recycler: RecyclerView
    private lateinit var empty_tasks: LinearLayout

    private lateinit var mAdapter: TaskAdapter
    private val mTasks = mutableListOf<Task>()
    private val mTasksFilter = mutableListOf<Task>()

    private lateinit var floatingActionButton: FloatingActionButton

    private lateinit var search: EditText

    private var searchText: String = ""

    private lateinit var status: Spinner

    private var mStatusSelected: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        status = findViewById(R.id.status)


        val adapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            this,
            R.array.spinner_status,
            R.layout.color_status_layout
        )

        adapter.setDropDownViewResource(R.layout.status_dropdown_layout);
        status.adapter = adapter;

        floatingActionButton = findViewById(R.id.add_task)

        floatingActionButton.setOnClickListener {
            inputTask()
        }

        search = findViewById(R.id.search)

        search.setOnEditorActionListener { textView, i, keyEvent ->
            if (i === EditorInfo.IME_ACTION_DONE) {
                searchText = search.text.toString()
                filterTask()
                false
            }
            false
        }

        shimmer = findViewById(R.id.shimmer_layout)
        shimmer.startShimmer()

        recycler = findViewById(R.id.recycler)
        empty_tasks = findViewById(R.id.empty_tasks)

        Handler().postDelayed({
            empty_tasks.visibility = View.VISIBLE
            shimmer.stopShimmer()
            shimmer.visibility = View.GONE
            status.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>,
                    view: View,
                    position: Int,
                    l: Long
                ) {
                    mStatusSelected = when (adapterView.getItemAtPosition(position)) {
                        "All" -> Task.ALL
                        "To Do" -> Task.TODO
                        "Done" -> Task.DONE
                        else -> Task.ALL
                    }
                    filterTask()
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
            }
        }, 2000)

        mAdapter = TaskAdapter(mTasksFilter);

        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)

        recycler.layoutManager = mLayoutManager
        recycler.adapter = mAdapter


        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedTask: Task =
                    mTasksFilter[viewHolder.adapterPosition]
                mTasks.filter { it.id == deletedTask.id }.forEach {
                    it.status = Task.DONE
                }
                filterTask()
            }

        }).attachToRecyclerView(recycler)




        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedTask: Task =
                    mTasksFilter[viewHolder.adapterPosition]
                mTasks.removeIf { e -> e.id == deletedTask.id }
                filterTask()
            }
        }).attachToRecyclerView(recycler)

    }


    fun filterTask() {
        mTasksFilter.clear()
        mTasksFilter.addAll(mTasks.filter { task -> (task.text.startsWith(searchText) and ((mStatusSelected == Task.ALL) or (task.status == mStatusSelected))) })
        mAdapter.notifyDataSetChanged()

        if (mTasks.size == 0) {
            empty_tasks.visibility = View.VISIBLE
            recycler.visibility = View.GONE
        }
    }


    fun inputTask() {
        var inputEditTextField = EditText(this);

        var dialog: AlertDialog = AlertDialog.Builder(this)
            .setTitle("Nueva Tarea")
            .setMessage("Ingrese el texto de la nueva tarea")
            .setView(inputEditTextField)
            .setPositiveButton("OK") { _, _ ->
                val editTextInput: String = inputEditTextField.text.toString()
                val newTask: Task =
                    Task(System.currentTimeMillis(), editTextInput.trim(), Task.TODO)
                mTasks.add(newTask)
                filterTask();
                empty_tasks.visibility = View.GONE
                recycler.visibility = View.VISIBLE
            }
            .setNegativeButton("Cancel", null)
            .create();
        dialog.show();
    }
}