package app.presentation

import com.github.ajalt.mordant.rendering.TextColors.*
import com.github.ajalt.mordant.rendering.TextStyles.*
import java.awt.event.ActionEvent
import java.io.File
import java.nio.file.Paths
import javax.swing.JFileChooser


fun creditsOut(): String {
    return buildString {
        append(brightWhite("CubeStudio"))
        append(" / ")
        append(brightWhite("by fadegor05 (Lyroq1s)"))
    }
}

fun titleOut(): String {
    val title = """
  ____      _          ____           _             _   
 / ___|   _| |__   ___|  _ \ ___  ___| |_ __ _ _ __| |_ 
| |  | | | | '_ \ / _ \ |_) / _ \/ __| __/ _` | '__| __|
| |__| |_| | |_) |  __/  _ <  __/\__ \ || (_| | |  | |_ 
 \____\__,_|_.__/ \___|_| \_\___||___/\__\__,_|_|   \__|   
""".trim('\n')
    return buildString {
        append(brightWhite(title))
        append("\n")
    }
}

fun requestFolderFromUser(): File? {
    val fileChooser = JFileChooser()
    fileChooser.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
    fileChooser.dialogTitle = "Выбрать папку"
    fileChooser.selectedFile = File(Paths.get("%appdata%").toString())

    val result = fileChooser.showDialog(null, "Выбрать папку")
    return if (result == JFileChooser.APPROVE_OPTION && fileChooser.selectedFile.isDirectory) {
        fileChooser.selectedFile
    } else {
        null
    }
}