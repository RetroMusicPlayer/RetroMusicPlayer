package code.name.monkey.retromusic.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import androidx.core.graphics.scale
import com.bumptech.glide.util.Util.assertBackgroundThread


internal object MergedImageUtils {

    private const val IMAGE_SIZE = 1600
    private const val PARTS = 3
    private const val DEGREES = 9f

    fun joinImages(list: List<Bitmap>): Bitmap {
        assertBackgroundThread()

        val arranged = arrangeBitmaps(list)

        val mergedImage = create(
            arranged,
            IMAGE_SIZE,
            PARTS
        )
        val finalImage = rotate(
            mergedImage,
            IMAGE_SIZE,
            DEGREES
        )
        mergedImage.recycle()
        return finalImage
    }

    private fun arrangeBitmaps(list: List<Bitmap>): List<Bitmap> {
        return when {
            list.size == 1 -> {
                val item = list[0]
                listOf(item, item, item, item, item, item, item, item, item)
            }
            list.size == 2 -> {
                val item1 = list[0]
                val item2 = list[1]
                listOf(item1, item2, item1, item2, item1, item2, item1, item2, item1)
            }
            list.size == 3 -> {
                val item1 = list[0]
                val item2 = list[1]
                val item3 = list[2]
                listOf(item1, item2, item3, item3, item1, item2, item2, item3, item1)
            }
            list.size == 4 -> {
                val item1 = list[0]
                val item2 = list[1]
                val item3 = list[2]
                val item4 = list[3]
                listOf(item1, item2, item3, item4, item1, item2, item3, item4, item1)
            }
            list.size < 9 -> { // 5 to 8
                val item1 = list[0]
                val item2 = list[1]
                val item3 = list[2]
                val item4 = list[3]
                val item5 = list[4]
                listOf(item1, item2, item3, item4, item5, item2, item3, item4, item1)
            }
            else -> list // case 9
        }
    }

    private fun create(images: List<Bitmap>, imageSize: Int, parts: Int): Bitmap {
        val result = Bitmap.createBitmap(imageSize, imageSize, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(result)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        val onePartSize = imageSize / parts

        images.forEachIndexed { i, bitmap ->
            val bit = bitmap.scale(onePartSize, onePartSize)
            canvas.drawBitmap(
                bit,
                (onePartSize * (i % parts)).toFloat() + (i % 3) * 50,
                (onePartSize * (i / parts)).toFloat() + (i / 3) * 50,
                paint
            )
            bit.recycle()
        }
        return result
    }

    private fun rotate(bitmap: Bitmap, imageSize: Int, degrees: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degrees)

        val rotated = Bitmap.createBitmap(bitmap, 0, 0, imageSize, imageSize, matrix, true)
        bitmap.recycle()
        val cropStart = imageSize * 25 / 100
        val cropEnd: Int = (cropStart * 1.5).toInt()
        val cropped = Bitmap.createBitmap(
            rotated,
            cropStart,
            cropStart,
            imageSize - cropEnd,
            imageSize - cropEnd
        )
        rotated.recycle()

        return cropped
    }


}