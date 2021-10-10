package com.adverity.etl.util


fun <T: Any, R: Any> Collection<T?>.whenAllNotNull(block: (List<T>)->R) {
    if (this.all { it != null }) {
        block(this.filterNotNull())
    }
}