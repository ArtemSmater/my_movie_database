package com.example.mymoviedatabase.data.mappers

import com.example.mymoviedatabase.domain.entity.MovieCategories
import java.time.LocalDate
import java.time.format.DateTimeFormatter


val popularParams = hashMapOf(
    "sort_by" to "popularity.desc",
)

val ratedParams = hashMapOf(
    "sort_by" to "vote_average.desc",
    "vote_count.gte" to "200",
)

val playingParam = hashMapOf(
    "sort_by" to "popularity.desc",
    "with_release_type" to "2|3",
    "release_date.gte" to getDateForPeriod(false),
    "release_date.lte" to getCurrentDate()
)

val upcomingParam = hashMapOf(
    "sort_by" to "popularity.desc",
    "with_release_type" to "2|3",
    "release_date.gte" to getCurrentDate(),
    "release_date.lte" to getDateForPeriod(true)
)

fun returnQueryMapByCategory(categories: MovieCategories): Map<String, String> {
    return when (categories) {
        MovieCategories.POPULAR -> popularParams
        MovieCategories.TOP_RATED -> ratedParams
        MovieCategories.NOW_PLAYING -> playingParam
        MovieCategories.UPCOMING -> upcomingParam
    }
}

private fun getCurrentDate(): String {
    val currentDate = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return formatter.format(currentDate)
}

private fun getDateForPeriod(isUpcoming: Boolean): String {
    val currentDate = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val temporal = if (isUpcoming) {
        currentDate.plusMonths(2)
    } else {
        currentDate.minusMonths(2)
    }
    return formatter.format(temporal)
}


