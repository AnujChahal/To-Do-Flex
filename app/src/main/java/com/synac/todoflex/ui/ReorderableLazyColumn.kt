/*
 * Copyright 2025 Kyriakos Georgiopoulos
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.synac.todoflex.ui

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.synac.todoflex.model.Task
import com.synac.todoflex.ui.screens.components.TaskCard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Collections
import kotlin.math.roundToInt

@Composable
fun <T> ReorderableLazyColumn( //Generic type parameter(list can hold any type)
    modifier: Modifier = Modifier,
    items: SnapshotStateList<T>, //State-backed list of items
    key: (T) -> Any, //function mapping item = unique key
    itemPxHeight: Int, //fixed height of each row in pixels
    itemContent: @Composable (item: T, isDragging: Boolean) -> Unit, //composable lambda to render each item, with flag if dragging
    onDragEnd: (List<T>) -> Unit
) {
    val listState = rememberLazyListState() //Controls scroll position
    val scope = rememberCoroutineScope() //Coroutine scope for scrolling
    val state = remember(items, listState) { ReorderState(items, listState, scope) } //custom drag/reorder state object

    val autoScrollEdgePx = with(LocalDensity.current) {84.dp.toPx()} //Converts 84dp to pixels. Defines edge zone for auto-scroll

    val placementSpring = spring<IntOffset>( //Defines spring animation for item placement
        stiffness = Spring.StiffnessMediumLow,
        dampingRatio = Spring.DampingRatioNoBouncy,
        visibilityThreshold = IntOffset.VisibilityThreshold
    )

    LazyColumn(
        modifier = modifier,
        state = listState
    ) {
        //iterates with index, key = Provides stable identity
        itemsIndexed(items, key = {_, it -> key(it)}) { index, item ->
            val itemKey = key(item)  //unique identifier
            val isDragging = state.isDragging(itemKey)  //whether this item is being dragged
            val translationY = if(isDragging) state.draggedOffset else 0f  //vertical offset while dragging

            val rowModifier = Modifier
                .fillMaxWidth()
                .height(with(LocalDensity.current) {itemPxHeight.toDp()})
                .let{ base ->
                    if(isDragging) {  //if dragging: bring item to front(zIndex = 1), lock gesture, apply offset.
                        base
                            .zIndex(1f)
                            //.pointerInput(itemKey, state.dragSession) {}
                            .offset{ IntOffset(0, translationY.roundToInt())}
                    } else {  //else normal item with animation
                        base
                            .zIndex(0f)
                            .animateItem(placementSpec = placementSpring)
                    }
                }
                .pointerInput(itemKey, state.dragSession) {
                    detectDragGesturesAfterLongPress(
                        onDragStart = { state.startDragIfIdle(index, itemKey) },  //begin drag
                        onDrag = { change, dragAmount ->  //move item
                            change.consume()
                            state.dragBy(dragAmount.y, autoScrollEdgePx)
                        },
                        onDragCancel = { state.endDrag() },  //reset state
                        onDragEnd = {
                            state.endDrag()
                            onDragEnd(items)
                        }  //reset state
                    )
                }
//            TaskCard(
//                task = item as Task,
//                modifier = rowModifier
//            )
        }
    }
}


//Tracks drag state and reordering logic
private class ReorderState<T>(  //Holds list, scroll state, coroutine scope
    private val items: SnapshotStateList<T>,
    private val lazyListState: LazyListState,
    private val scope: CoroutineScope
) {
    private var draggedKey: Any? by mutableStateOf(null) //which item is dragged
    var draggedIndex by mutableStateOf<Int?>(null); private set //index of dragged item
    var draggedOffset by mutableStateOf(0f); private set  //vertical offset
    var dragSession by mutableStateOf(0); private set  //incremented each drag (forces recomposition)

    fun isDragging(itemKey: Any) = draggedKey == itemKey  //checks if item is being dragged

    fun startDragIfIdle(index: Int, itemKey: Any) {  //starts drag if none active
        if(draggedKey != null) return
        draggedKey = itemKey
        draggedIndex = index
        draggedOffset = 0f
    }

    fun dragBy(deltaY: Float, autoScrollEdgePx: Float) {  //moves item by drag amount
        val currentIndex = draggedIndex ?: return
        draggedOffset += deltaY

        //gets layout info and visible items
        val layout = lazyListState.layoutInfo
        val visible = layout.visibleItemsInfo
        val currentInfo = visible.firstOrNull { it.index == currentIndex } ?: return

        //calculate center of dragged item
        val draggedCenter = currentInfo.offset + draggedOffset + currentInfo.size / 2f

        //Swap logic
        //swap items when dragged past neighbors
        var moved: Boolean
        do {
            moved = false
            val ci = draggedIndex ?: break
            val below = visible.firstOrNull { it.index > ci }
            if (below != null && draggedCenter > below.centerY()) {
                items.swap(ci, ci + 1)
                draggedIndex = ci + 1
                draggedOffset -= below.size.toFloat()
                moved = true
                continue
            }
            val above = visible.lastOrNull { it.index < ci }
            if (above != null && draggedCenter < above.centerY()) {
                items.swap(ci, ci - 1)
                draggedIndex = ci - 1
                draggedOffset += above.size.toFloat()
                moved = true
            }
        } while (moved)

        val viewportStart = layout.viewportStartOffset
        val viewportEnd = layout.viewportEndOffset
        val upEdge = viewportStart + autoScrollEdgePx
        val downEdge = viewportEnd - autoScrollEdgePx

        val scrollDelta = when {
            draggedCenter < upEdge -> {
                val t = ((upEdge - draggedCenter) / autoScrollEdgePx).coerceIn(0f, 1f)
                -lerp(0f, 60f, t * t)
            }
            draggedCenter > downEdge -> {
                val t = ((draggedCenter - downEdge) / autoScrollEdgePx).coerceIn(0f, 1f)
                lerp(0f, 60f, t * t)
            }
            else -> 0f
        }
        if (scrollDelta != 0f) {
            scope.launch {
                val consumed = lazyListState.scrollBy(scrollDelta)
                if (consumed != 0f) draggedOffset += consumed
            }
        }
    }

    fun endDrag() {
        draggedKey = null
        draggedIndex = null
        draggedOffset = 0f
        dragSession++
    }
}

private fun LazyListItemInfo.centerY(): Float = offset + size / 2f

private fun <T> MutableList<T>.swap(i: Int, j: Int) {
    if (i == j) return
    if (i !in indices || j !in indices) return
    Collections.swap(this, i, j)
}

private fun lerp(a: Float, b: Float, t: Float) = a * (1 - t) + b * t
