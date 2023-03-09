package com.applid.musicbox.services.i18n

import com.applid.musicbox.Symphony

class Translator(private val symphony: Symphony) {
    var t: Translations

    init {
        t = symphony.settings.getLanguage()?.let { Translations.of(it) } ?: Translations.default
    }
}
