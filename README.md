## An example of creating a long shadow for text and an icon.

![Long-Shadows](https://github.com/user-attachments/assets/e1f0d918-b25d-4a2c-8a87-3800c48a2014)

To use the ready-made library, add the dependency:
```
dependencies {

    implementation("io.github.uratera:long_shadows:1.0.2")
}
```
**Usage:**
```
<com.tera.long_shadows.LongShadowText
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:ls_text="Hello" />

<com.tera.long_shadows.LongShadowImage
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:lsi_icon="@drawable/ic_circle" />
```

### Attributes LongShadowText
|Attributes      |Description     |Related methods|
|----------------|----------------|---------------|
|ls_arrayColor   |Array of colors |setArrayColor
|ls_blurWidth    |Blur width      |setBlurWidth
|ls_enabledAlpha |Enable A-channel|setEnabledAlpha
|ls_fontFamily   |Font            |setFontFamily
|ls_multiColor   |Multi color     |setMulticolor
|ls_shadow_angle |Shadow angle    |setShadowAngle
|ls_shadowColor  |Shadow color    |setShadowColor
|ls_shadowLength |Shadow length   |setShadowLength
|ls_text         |Text            |setText
|ls_textColor    |Text color      |setTextColor
|ls_textSize     |Text size       |setTextSize

### Attributes LongShadowImage
|Attributes       |Description     |Related methods|
|-----------------|----------------|---------------|
|lsi_enabledAlpha |Enable A-channel|setEnabledAlpha
|lsi_icon         |Icon            |setIcon
|lsi_iconColor    |Icon color      |setIconColor
|lsi_iconSize     |Icon size       |setIconSize
|lsi_shadow_angle |Shadow angle    |setShadowAngle
|lsi_shadowColor  |Shadow color    |setShadowColor
|lsi_shadowLength |Shadow length   |setShadowLength


