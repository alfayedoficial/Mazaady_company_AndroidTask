package com.afapps.mazaadyAndroidTask.data.helper
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun <T : Any> PagingData<T>.collectDataForTest(
    diffCallback: DiffUtil.ItemCallback<T>
): List<T> {
    val snapshotItems = mutableListOf<T>()

    val differ = AsyncPagingDataDiffer(
        diffCallback = diffCallback,
        updateCallback = NoopListCallback(),
        workerDispatcher = Dispatchers.Default
    )

    withContext(Dispatchers.Main.immediate) {
        differ.submitData(this@collectDataForTest)
    }

    for (i in 0 until differ.itemCount) {
        differ.getItem(i)?.let { snapshotItems.add(it) }
    }

    return snapshotItems
}


class NoopListCallback : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}
