package com.example.simplehero.utils

class ImageUtils {
    companion object {
        fun buildImageUrl(path: String, variant: String, extension: String): String {
            return "$path/$variant.$extension"
        }
    }
}
