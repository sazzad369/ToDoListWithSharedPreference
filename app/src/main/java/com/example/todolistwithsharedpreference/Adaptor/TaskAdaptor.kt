import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.recyclerview.widget.RecyclerView
import com.example.todolistwithsharedpreference.Data.Task
import com.example.todolistwithsharedpreference.databinding.ListItemBinding

class TaskAdapter (private val tasklist:MutableList<Task>, private val clicklisten:TaskClickLister )
    : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>()
{
    interface TaskClickLister {
        fun onEditClick(position:Int)
        fun onDeleteClick(position:Int)
    }

    class TaskViewHolder(val binding:ListItemBinding):RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task){
            binding.txt.text = task.title
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return TaskViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return tasklist.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        var task =tasklist[position]
        holder.bind(task)
        holder.binding.add.setOnClickListener{
            clicklisten.onEditClick(position)
        }
        holder.binding.delete.setOnClickListener{
            clicklisten.onDeleteClick(position)
        }
        holder.binding.cb.isChecked= task.isCompleted
        holder.binding.cb.setOnCheckedChangeListener { _, isChecked ->
            task.isCompleted = isChecked
        }
    }

}