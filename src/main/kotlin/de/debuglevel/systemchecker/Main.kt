import com.github.sarxos.webcam.Webcam
import java.awt.image.BufferedImage

fun countEqualPixels(image1: BufferedImage, image2: BufferedImage): Int {
    // convert images to pixel arrays
    val p1 = image1.getRGB(0, 0, image1.width, image1.height, null, 0, image1.width)
    val p2 = image2.getRGB(0, 0, image1.width, image1.height, null, 0, image1.width)

    // compare img1 to img2, pixel by pixel
    return p1.indices.count { p1[it] == p2[it] }
}

fun checkWebcam(webcam: Webcam): Boolean {
    webcam.open()

    val totalPixels = webcam.viewSize.height * webcam.viewSize.width
    val image1 = webcam.image
    val image2 = webcam.image
    val equalPixels = countEqualPixels(image1, image2)
    val equalPixelRatio = equalPixels.toDouble() / totalPixels
    //println("Equal pixels ratio: $equalPixelRatio")

    webcam.close()

    return equalPixelRatio != 1.0
}

fun main(args: Array<String>) {
    val webcams = Webcam.getWebcams().filterNot { webcam ->
        listOf("screen-capture-recorder").any { brokenDevice -> webcam.name.contains(brokenDevice) }
    }

    for (webcam in webcams) {
        print("Checking webcam ${webcam.name}... ")
        val works = checkWebcam(webcam)
        when {
            works -> println("working")
            else -> println("broken")
        }
    }
}