package com.vanyscore.app.utils

import android.content.ContentResolver
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.core.net.toFile
import androidx.core.net.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

object FileUtil {
    suspend fun saveFileToInternalStorage(
        contentResolver: ContentResolver,
        uri: Uri,
        fileName: String,
        dir: File
    ): Uri? {
        return withContext(Dispatchers.IO) {
            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null

            try {
                // Open the input stream from the URI
                inputStream = contentResolver.openInputStream(uri)

                // Create the output file in the internal storage directory
                val internalFile = File(dir, fileName)

                // Create an output stream to the internal file
                outputStream = FileOutputStream(internalFile)

                // Buffer to read the data
                val buffer = ByteArray(1024)
                var bytesRead: Int

                // Read from input stream and write to output stream
                while (inputStream?.read(buffer).also { bytesRead = it ?: -1 } != -1) {
                    outputStream.write(buffer, 0, bytesRead)
                }

                // Successfully saved the file
                internalFile.toUri()
            } catch (e: IOException) {
                e.printStackTrace()
                // Failed to save the file
                null
            } finally {
                // Close streams
                try {
                    inputStream?.close()
                    outputStream?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    suspend fun copyFileByUri(uri: Uri, dir: File): Uri? {
        return withContext(Dispatchers.IO) {
            try {
                val file = uri.toFile()
                val newFile = File(dir, file.name)
                file.copyRecursively(newFile, overwrite = true)
                file.delete()
                newFile.toUri()
            } catch (ex: Exception) {
                ex.printStackTrace()
                null
            }
        }
    }

    suspend fun removeFileByUri(uri: Uri) {
        withContext(Dispatchers.IO) {
            try {
                val file = uri.toFile()
                if (file.exists()) {
                    file.delete()
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    fun getFileExtensionFromUri(contentResolver: ContentResolver, uri: Uri): String? {
        // Get MIME type from the content resolver
        val mimeType = contentResolver.getType(uri)

        // Use MimeTypeMap to get the file extension based on the MIME type
        return mimeType?.let {
            MimeTypeMap.getSingleton().getExtensionFromMimeType(it)
        }
    }
}
