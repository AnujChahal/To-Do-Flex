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

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.zIndex

@Composable
fun <T : Any> ReorderableLazyColumn2(
    items: SnapshotStateList<T>,
    key: (T) -> Any,
    onDragEnd: (List<T>) -> Unit,
    modifier: Modifier = Modifier,
    itemContent: @Composable (item: T, isDragging: Boolean) -> Unit
) {
    val listState = rememberLazyListState()

    // We hold the dragging state here
    var draggedKey by remember { mutableStateOf<Any?>(null) }
    var draggedOffset by remember { mutableFloatStateOf(0f) }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = listState
    ) {
        itemsIndexed(items = items, key = {_, item -> key(item)}) { index, item ->
            val itemKey = key(item)
            val isDragging = draggedKey == itemKey

            // Simple animation for the scale when dragging
            val scale by animateFloatAsState(if (isDragging) 1.05f else 1f, label = "scale")
            val zIndex = if (isDragging) 1f else 0f
            val alpha = if(isDragging) 0.9f else 1f

            Box(
                modifier = Modifier
                    .zIndex(zIndex)
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        this.alpha = alpha
                        translationY = if (isDragging) draggedOffset else 0f
                    }
                    .pointerInput(itemKey) { //Key the pointerInput to the Item ID
                        detectDragGesturesAfterLongPress(
                            onDragStart = {
                                draggedKey = itemKey
                                draggedOffset = 0f
                            },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                draggedOffset += dragAmount.y

                                // Basic swap logic
                                val currentIdx = items.indexOfFirst { key(it) == draggedKey }
                                if(currentIdx == -1) return@detectDragGesturesAfterLongPress

                                val currentItemInfo = listState.layoutInfo.visibleItemsInfo
                                    .firstOrNull { it.index == currentIdx } ?: return@detectDragGesturesAfterLongPress

                                val currentCenter = currentItemInfo.offset + draggedOffset + currentItemInfo.size / 2

                                // Move Down
                                if (dragAmount.y > 0) {
                                    val nextItem = listState.layoutInfo.visibleItemsInfo
                                        .firstOrNull { it.index == currentIdx + 1 }
                                    if (nextItem != null) {
                                        val nextItemCenter = nextItem.offset + nextItem.size / 2
                                        if (currentCenter > nextItemCenter) {
                                            items.swap(currentIdx, currentIdx + 1)
                                            draggedOffset -= nextItem.size
                                        }
                                    }
                                }
                                // Move Up
                                else if (dragAmount.y < 0) {
                                    val prevItem = listState.layoutInfo.visibleItemsInfo
                                        .firstOrNull { it.index == currentIdx - 1 }
                                    if (prevItem != null) {
                                        val prevCenter = prevItem.offset + prevItem.size / 2
                                        if (currentCenter < prevCenter) {
                                            items.swap(currentIdx, currentIdx - 1)
                                            draggedOffset += prevItem.size
                                        }
                                    }
                                }
                            },
                            onDragEnd = {
                                draggedKey = null
                                draggedOffset = 0f
                                onDragEnd(items.toList())
                            },
                            onDragCancel = {
                                draggedKey = null
                                draggedOffset = 0f
                            }
                        )
                    }
            ) {
                itemContent(item, isDragging)
            }
        }
    }
}

// Helper extension to swap items in the MutableList
private fun <T> MutableList<T>.swap(index1: Int, index2: Int) {
    if (index1 >= 0 && index2 >= 0 && index1 < size && index2 < size) {
        val tmp = this[index1]
        this[index1] = this[index2]
        this[index2] = tmp
    }
}
