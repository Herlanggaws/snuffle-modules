package com.logixmates.snuffle.core.presentation.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.webkit.URLUtil
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import com.logixmates.snuffle.core.presentation.themes.SnuffleColors

fun String.isValidEmail(): Boolean {
    val emailRegex = """[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}""".toRegex()
    return matches(emailRegex)
}

fun String.checkEmail(): String? {
    val result: Result<String?> = runCatching {
        require(!isNullOrBlank()) { "Email cannot be blank" }
        require(isValidEmail()) { "Invalid email address" }
        null
    }
    return result.getOrElse { it.message }
}

fun String.intentToDefaultBrowser(context: Context) {
    require(URLUtil.isValidUrl(this))
    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(this))
    context.startActivity(browserIntent)
}

fun String.checkPassword(
    confirmPassword: String? = null,
    confirmPasswordOnly: Boolean = false
): String? {
    val result: Result<String?> = runCatching {
        if (!confirmPasswordOnly) {
            require(length >= 8) { "Password must have at least eight characters!" }
            require(none { it.isWhitespace() }) { "Password must not contain whitespace!" }
            require(any { it.isDigit() }) { "Password must contain at least one digit!" }
            require(any { it.isUpperCase() }) { "Password must have at least one uppercase letter!" }
            require(any { !it.isLetterOrDigit() }) { "Password must have at least one special character, such as: _%-=+#@" }
        }
        if (confirmPassword != null) {
            require(this == confirmPassword) { "Password is not the same!" }
        }
        null
    }
    return result.getOrElse { it.message }
}

const val ANNOTATED_CLICKABLE = "Clickable"

@Composable
fun String.toAnnotatedClickableText(actions: List<String>): AnnotatedString {
    val annotatedString = AnnotatedString.Builder()
    var currentIndex = 0

    actions.forEach { phrase ->
        var foundIndex = this.indexOf(phrase, currentIndex)
        while (foundIndex >= 0) {
            // Append text before the phrase
            annotatedString.append(this.substring(currentIndex, foundIndex))

            // Style and append the phrase
            annotatedString.withStyle(
                style = SpanStyle(
                    color = SnuffleColors.RoyalBlue,
                    textDecoration = TextDecoration.Underline
                )
            ) {
                append(phrase)
            }

            // Add clickable annotation
            annotatedString.addStringAnnotation(
                tag = ANNOTATED_CLICKABLE,
                annotation = phrase,
                start = foundIndex,
                end = foundIndex + phrase.length
            )

            // Update current index
            currentIndex = foundIndex + phrase.length

            // Find next occurrence
            foundIndex = this.indexOf(phrase, currentIndex)
        }
    }

    // Append any remaining text after the last phrase
    if (currentIndex < this.length) {
        annotatedString.append(this.substring(currentIndex))
    }

    return annotatedString.toAnnotatedString()
}
