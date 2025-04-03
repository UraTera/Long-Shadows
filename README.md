## An example of creating a long shadow for text and an icon.

![Long-Shadows](https://github.com/user-attachments/assets/e1f0d918-b25d-4a2c-8a87-3800c48a2014)

To use the ready-made library, add the dependency:
```
dependencies {

    implementation("io.github.uratera:long_shadows:1.0.1")
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
|Attributes      |Description |
|----------------|------------|
|ls_arrayColor   |Array of colors
|ls_blurWidth    |Blur width
|ls_enabledAlpha |Enable A-channel
|ls_fontFamily   |Font
|ls_multiColor   |Multi color
|ls_shadow_angle |Shadow angle
|ls_shadowColor  |Shadow color
|ls_shadowLength |Shadow length
|ls_text         |Text
|ls_textColor    |Text color
|ls_textSize     |Text size

### Attributes LongShadowImage
|Attributes       |Description |
|-----------------|------------|
|lsi_enabledAlpha |Enable A-channel
|lsi_icon         |Icon
|lsi_iconColor    |Icon color
|lsi_iconSize     |Icon size
|lsi_shadow_angle |Shadow angle
|lsi_shadowColor  |Shadow color
|lsi_shadowLength |Shadow length

