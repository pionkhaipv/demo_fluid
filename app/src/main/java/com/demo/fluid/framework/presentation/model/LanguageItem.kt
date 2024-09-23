package com.demo.fluid.framework.presentation.model

data class LanguageItem(
    val languageName: String,
    val languageCode: String,
    var isChecked: Boolean = false
)

val listLanguage = mutableListOf(
    LanguageItem(
        languageName = "English",
        languageCode = "en",
        isChecked = false
    ),
    LanguageItem(
        languageName = "عربي",
        languageCode = "ar",
        isChecked = false
    ),
    LanguageItem(
        languageName = "Français",
        languageCode = "fr",
        isChecked = false
    ),


    LanguageItem(
        languageName = "Deutsch",
        languageCode = "de",
        isChecked = false
    ),
    LanguageItem(
        languageName = "Español",
        languageCode = "es",
        isChecked = false
    ),
    LanguageItem(
        languageName = "हिंदी",
        languageCode = "hi",
        isChecked = false
    ),
    LanguageItem(
        languageName = "日本人",
        languageCode = "ja",
        isChecked = false
    ),
    LanguageItem(
        languageName = "한국인",
        languageCode = "ko",
        isChecked = false
    ),
    LanguageItem(
        languageName = "Português",
        languageCode = "pt",
        isChecked = false
    ),
    LanguageItem(
        languageName = "Pусский",
        languageCode = "ru",
        isChecked = false
    ),
    LanguageItem(
        languageName = "ไทย",
        languageCode = "th",
        isChecked = false
    ),
    LanguageItem(
        languageName = "Türkçe",
        languageCode = "tr",
        isChecked = false
    ),
    LanguageItem(
        languageName = "O'zbek",
        languageCode = "uz",
        isChecked = false
    ),
    LanguageItem(
        languageName = "Việt Nam",
        languageCode = "vi",
        isChecked = false
    ),
    LanguageItem(
        languageName = "中國人",
        languageCode = "zh",
        isChecked = false
    )

)
