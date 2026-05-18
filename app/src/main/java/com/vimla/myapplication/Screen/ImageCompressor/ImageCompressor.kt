package com.vimla.myapplication.Screen.ImageCompressor

import android.media.ExifInterface
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object ImageCompressor {

    /**
     * Compress image from URI
     * @param context Application context
     * @param imageUri URI of the image to compress
     * @param maxWidth Maximum width (default 1024px)
     * @param maxHeight Maximum height (default 1024px)
     * @param quality Compression quality 0-100 (default 75)
     * @return ByteArray of compressed image
     */
    suspend fun compressImage(
        context: Context,
        imageUri: Uri,
        maxWidth: Int = 1024,
        maxHeight: Int = 1024,
        quality: Int = 75
    ): ByteArray? {
        return try {
            val inputStream = context.contentResolver.openInputStream(imageUri)

            // First decode with inJustDecodeBounds=true to check dimensions
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            BitmapFactory.decodeStream(inputStream, null, options)
            inputStream?.close()

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight)

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false
            val newInputStream = context.contentResolver.openInputStream(imageUri)
            var bitmap = BitmapFactory.decodeStream(newInputStream, null, options)
            newInputStream?.close()

            bitmap = bitmap?.let {
                // Handle image rotation
                fixImageOrientation(context, imageUri, it)
            }

            // Compress to ByteArray
            val outputStream = ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            bitmap?.recycle()

            outputStream.toByteArray()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Compress image and save to file
     */
    suspend fun compressImageToFile(
        context: Context,
        imageUri: Uri,
        maxWidth: Int = 1024,
        maxHeight: Int = 1024,
        quality: Int = 75
    ): File? {
        return try {
            val compressedBytes = compressImage(context, imageUri, maxWidth, maxHeight, quality)
            compressedBytes?.let {
                val file = File(context.cacheDir, "compressed_${System.currentTimeMillis()}.jpg")
                FileOutputStream(file).use { fos ->
                    fos.write(it)
                }
                file
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2

            while ((halfHeight / inSampleSize) >= reqHeight &&
                (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    private fun fixImageOrientation(context: Context, uri: Uri, bitmap: Bitmap): Bitmap {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val exif = inputStream?.let { ExifInterface(it) }
            inputStream?.close()

            val orientation = exif?.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            ) ?: ExifInterface.ORIENTATION_NORMAL

            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(bitmap, 90f)
                ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(bitmap, 180f)
                ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(bitmap, 270f)
                else -> bitmap
            }
        } catch (e: IOException) {
            bitmap
        }
    }

    private fun rotateBitmap(bitmap: Bitmap, degrees: Float): Bitmap {
        val matrix = Matrix().apply { postRotate(degrees) }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }
}
