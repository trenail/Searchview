# Searchview
### 效果图
<iframe height=600 width=800 src="demo.gif">
---
### 使用方法
XML中直接引用

```
<com.whimaggot.SearchView
        android:layout_width="match_parent"
        android:background="#f2e200"
        android:textColor="#ffffff"
        android:padding="3dp"
        app:hint_text="@string/app_name"
        android:layout_height="50dp"
        />
```
设置参数

```
<declare-styleable name="search_view">
        <attr name="search_icon_width" format="dimension" />
        <attr name="search_icon_height" format="dimension" />
        <attr name="hint_text_color" format="color"/>
        <attr name="hint_text_size" format="dimension"/>
        <attr name="delete_icon_width" format="dimension"/>
        <attr name="delete_icon_height" format="dimension"/>
        <attr name="hint_text" format="string|reference"/>
        <attr name="search_icon" format="reference"/>
        <attr name="delete_icon" format="reference"/>
    </declare-styleable>
```

### 引入项目

根目录的build.gradle配置

```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

module的build.gradle配置

```
dependencies {
	compile 'com.github.whimaggot:Searchview:1.0'
}
```