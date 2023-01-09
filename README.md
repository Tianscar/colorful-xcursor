# Colorful Xcursor
Colorful custom cursor support for Java XAWT (AWT/X11)

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
[Apache-2.0](https://github.com/jnr/jnr-ffi/blob/master/LICENSE) [jnr-ffi](https://github.com/jnr/jnr-ffi)

### Resources be used for test
[pencils.jpg](/src/test/resources/pencils.png) is cropped from https://en.wikipedia.org/wiki/Color#/media/File:Colouring_pencils.jpg,
the source image licensed under CC-BY-SA.