# Colorful Xcursor
Colorful custom cursor support for Java XAWT (AWT/X11)

## Add the library to your project (gradle)
1. Add the Maven Central repository (if not exist) to your build file:
```groovy
repositories {
    mavenCentral()
}
```

2. Add the dependency:
```groovy
dependencies {
    implementation 'com.tianscar:colorful-xcursor:1.0.0'
}
```

## Usage
```
Cursor colorfulXcursor = new ColorfulXCursor(Image image, Point hotspot, String name);
```
[A simple example](src/test/java/CursorComparison.java)

## Comparison
![Original XAWT Custom Cursor](img0.png)
![Colorful Xcursor](img1.png)

## License
[MIT](https://github.com/Tianscar/colorful-xcursor/blob/main/LICENSE) (c) Tianscar  

### Dependencies
[jnr-ffi](https://github.com/jnr/jnr-ffi) - [Apache-2.0](https://github.com/jnr/jnr-ffi/blob/master/LICENSE)

### Resources be used for test
[pencils.jpg](/src/test/resources/pencils.png) is cropped from https://en.wikipedia.org/wiki/Color#/media/File:Colouring_pencils.jpg,
the source image licensed under CC-BY-SA.