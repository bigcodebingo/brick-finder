package com.bigbingo.brickfinder.ui.screens.setsbytheme.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bigbingo.brickfinder.data.Set
import com.bigbingo.brickfinder.ui.screens.PaginationBar

@Composable
fun SetsList(
    sets: List<Set>,
    listState: LazyListState,
    currentPage: Int,
    totalPages: Int,
    onSetClick: (String) -> Unit,
    onPageChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        state = listState,
        modifier = modifier
    ) {
        item {
            ColumnHeader()
        }

        itemsIndexed(sets) { index, set ->
            val backgroundColor = if (index % 2 == 0) Color.White else Color(0xfff1f5f8)
            SetCard(set = set, backgroundColor = backgroundColor){ onSetClick(set.set_num)}
        }

        if (sets.size >= 9) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                PaginationBar(
                    currentPage = currentPage,
                    totalPages = totalPages,
                    onPageChange = onPageChange,
                    modifier = Modifier.padding(bottom = 20.dp)
                )
            }
        }
    }
}
