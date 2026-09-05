package com.tihasg.crypto.exchanges.presentation.common

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

fun formatUsd(value: Double?): String {
    if (value == null) return "--"
    return NumberFormat.getCurrencyInstance(Locale.US).format(value)
}

fun formatPercent(value: Double?): String {
    if (value == null) return "--"
    return String.format(Locale.US, "%.2f%%", value)
}

fun formatDate(iso: String?): String {
    if (iso.isNullOrBlank()) return "--"
    val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    val parsed = runCatching { parser.parse(iso) }.getOrNull() ?: return iso
    return SimpleDateFormat("MMM d, yyyy", Locale.US).format(parsed)
}

private val markdownHeadingRegex = Regex("(?m)^#{1,6}\\s+")
private val markdownLinkRegex = Regex("\\[([^]]+)]\\(([^)]+)\\)")

fun formatDescription(raw: String): String = raw
    .replace(markdownHeadingRegex, "")
    .replace(markdownLinkRegex, "$1")
