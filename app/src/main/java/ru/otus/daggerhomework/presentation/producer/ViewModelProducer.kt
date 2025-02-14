package ru.otus.daggerhomework.presentation.producer

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import ru.otus.daggerhomework.di.ActivityContext
import ru.otus.daggerhomework.util.ColorGenerator
import ru.otus.daggerhomework.util.EventFlow
import ru.otus.daggerhomework.util.IntEvent
import java.lang.ref.WeakReference

class ViewModelProducer @AssistedInject constructor(
    @Assisted savedStateHandle: SavedStateHandle,
    @Assisted context: Context,
    val colorGenerator: ColorGenerator,
    @ActivityContext val context: Context,
    val eventFlow: EventFlow
) : ViewModel() {

    private var contextReference: WeakReference<Context> = WeakReference(context)

    fun updateContext(context: Context) {
        contextReference = WeakReference(context)
    }

    fun generateColor() {
        contextReference.get()?.let { context ->
            if (context !is FragmentActivity) throw RuntimeException("Здесь нужен контекст активити")
            viewModelScope.launch {
                val color = colorGenerator.generateColor()
                eventFlow.send(IntEvent(color))
                Toast.makeText(context, "Color sent", Toast.LENGTH_LONG).show()
            }
        }
    }

    @AssistedFactory
    interface Factory {

        fun create(savedStateHandle: SavedStateHandle, context: Context): ViewModelProducer
    }
}
