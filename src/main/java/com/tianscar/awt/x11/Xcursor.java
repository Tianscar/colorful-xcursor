package com.tianscar.awt.x11;

import jnr.ffi.LibraryLoader;
import jnr.ffi.Runtime;
import jnr.ffi.Struct;
import jnr.ffi.annotations.In;

final class Xcursor {

    private Xcursor() {
        throw new UnsupportedOperationException();
    }

    private static final Library LIBRARY = LibraryLoader.create(Library.class).load("Xcursor");

    protected interface Library {
        XCursorImage XcursorImageCreate(@In int width, @In int height);
        void XcursorImageDestroy(@In XCursorImage image);
        long XcursorImageLoadCursor(@In long dpy, @In XCursorImage image);
    }

    public static XCursorImage XcursorImageCreate(final int width, final int height) {
        return LIBRARY.XcursorImageCreate(width, height);
    }

    public static void XcursorImageDestroy(final XCursorImage image) {
        LIBRARY.XcursorImageDestroy(image);
    }

    public static long XcursorImageLoadCursor(final long dpy, final XCursorImage image) {
        return LIBRARY.XcursorImageLoadCursor(dpy, image);
    }

    public final static class XCursorImage extends Struct {

        private final Unsigned32     version = new Unsigned32();	/* version of the image data */
        private final Unsigned32     size    = new Unsigned32();   	/* nominal size for matching */
        private final Unsigned32     width   = new Unsigned32();  	/* actual width */
        private final Unsigned32     height  = new Unsigned32();  	/* actual height */
        private final Unsigned32     xhot    = new Unsigned32();	/* hot spot x (must be inside image) */
        private final Unsigned32     yhot    = new Unsigned32();   	/* hot spot y (must be inside image) */
        private final Unsigned32     delay   = new Unsigned32();  	/* animation delay to next frame (ms) */
        private final Pointer        pixels  = new Pointer();    	/* pointer to pixels */

        public XCursorImage(Runtime runtime) {
            super(runtime);
        }

        public void setXhot(long xhot) {
            this.xhot.set(xhot);
        }

        public long getXhot() {
            return xhot.get();
        }

        public void setYhot(long yhot) {
            this.yhot.set(yhot);
        }

        public long getYhot() {
            return yhot.get();
        }

        public void setWidth(long width) {
            this.width.set(width);
        }

        public long getWidth() {
            return width.get();
        }

        public void setHeight(long height) {
            this.height.set(height);
        }

        public long getHeight() {
            return height.get();
        }

        public void setPixels(jnr.ffi.Pointer pixels) {
            this.pixels.set(pixels);
        }

        public jnr.ffi.Pointer getPixels() {
            return pixels.get();
        }

    }

}
