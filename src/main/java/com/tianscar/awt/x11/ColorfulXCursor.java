package com.tianscar.awt.x11;

import jnr.ffi.LibraryLoader;
import jnr.ffi.Pointer;
import jnr.ffi.Runtime;
import jnr.ffi.Struct;
import jnr.ffi.annotations.In;
import sun.awt.AWTAccessor;
import sun.awt.CustomCursor;
import sun.awt.X11.XToolkit;

import java.awt.Point;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Cursor;
import java.awt.HeadlessException;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The ColorfulXCursor factory class to create X cursors
 */
public final class ColorfulXCursor {

    public static final int XC_num_glyphs=154;
    public static final int XC_X_cursor=0;
    public static final int XC_arrow=2;
    public static final int XC_based_arrow_down=4;
    public static final int XC_based_arrow_up=6;
    public static final int XC_boat=8;
    public static final int XC_bogosity=10;
    public static final int XC_bottom_left_corner=12;
    public static final int XC_bottom_right_corner=14;
    public static final int XC_bottom_side=16;
    public static final int XC_bottom_tee=18;
    public static final int XC_box_spiral=20;
    public static final int XC_center_ptr=22;
    public static final int XC_circle=24;
    public static final int XC_clock=26;
    public static final int XC_coffee_mug=28;
    public static final int XC_cross=30;
    public static final int XC_cross_reverse=32;
    public static final int XC_crosshair=34;
    public static final int XC_diamond_cross=36;
    public static final int XC_dot=38;
    public static final int XC_dotbox=40;
    public static final int XC_double_arrow=42;
    public static final int XC_draft_large=44;
    public static final int XC_draft_small=46;
    public static final int XC_draped_box=48;
    public static final int XC_exchange=50;
    public static final int XC_fleur=52;
    public static final int XC_gobbler=54;
    public static final int XC_gumby=56;
    public static final int XC_hand1=58;
    public static final int XC_hand2=60;
    public static final int XC_heart=62;
    public static final int XC_icon=64;
    public static final int XC_iron_cross=66;
    public static final int XC_left_ptr=68;
    public static final int XC_left_side=70;
    public static final int XC_left_tee=72;
    public static final int XC_leftbutton=74;
    public static final int XC_ll_angle=76;
    public static final int XC_lr_angle=78;
    public static final int XC_man=80;
    public static final int XC_middlebutton=82;
    public static final int XC_mouse=84;
    public static final int XC_pencil=86;
    public static final int XC_pirate=88;
    public static final int XC_plus=90;
    public static final int XC_question_arrow=92;
    public static final int XC_right_ptr=94;
    public static final int XC_right_side=96;
    public static final int XC_right_tee=98;
    public static final int XC_rightbutton=100;
    public static final int XC_rtl_logo=102;
    public static final int XC_sailboat=104;
    public static final int XC_sb_down_arrow=106;
    public static final int XC_sb_h_double_arrow=108;
    public static final int XC_sb_left_arrow=110;
    public static final int XC_sb_right_arrow=112;
    public static final int XC_sb_up_arrow=114;
    public static final int XC_sb_v_double_arrow=116;
    public static final int XC_shuttle=118;
    public static final int XC_sizing=120;
    public static final int XC_spider=122;
    public static final int XC_spraycan=124;
    public static final int XC_star=126;
    public static final int XC_target=128;
    public static final int XC_tcross=130;
    public static final int XC_top_left_arrow=132;
    public static final int XC_top_left_corner=134;
    public static final int XC_top_right_corner=136;
    public static final int XC_top_side=138;
    public static final int XC_top_tee=140;
    public static final int XC_trek=142;
    public static final int XC_ul_angle=144;
    public static final int XC_umbrella=146;
    public static final int XC_ur_angle=148;
    public static final int XC_watch=150;
    public static final int XC_xterm=152;

    private static final long NULL = 0L;

    static final class XCursor extends Cursor {
        XCursor(final String name, final long pData) {
            super(name);
            XToolkit.awtLock();
            try {
                AWTAccessor.getCursorAccessor().setPData(this, pData);
            }
            finally {
                XToolkit.awtUnlock();
            }
        }
    }

    static final class XImageCursor extends CustomCursor {

        public XImageCursor(final Image cursor, final Point hotSpot, final String name) throws IndexOutOfBoundsException {
            super(cursor, hotSpot, name);
        }

        @Override
        protected void createNativeCursor(final Image im, final int[] pixels, final int width, final int height, final int xHotSpot, final int yHotSpot) {
            XToolkit.awtLock();
            try {
                final long pNativePixels = UNSAFE.allocateMemory(pixels.length * 4L);
                final Pointer nativePixels = Pointer.wrap(Runtime.getSystemRuntime(), pNativePixels);
                nativePixels.put(0, pixels, 0, pixels.length);
                final XcursorImage xCursorImage = XCURSOR.XcursorImageCreate(width, height);
                xCursorImage.setXhot(xHotSpot);
                xCursorImage.setYhot(yHotSpot);
                xCursorImage.setPixels(nativePixels);
                final long pData = XCURSOR.XcursorImageLoadCursor(XToolkit.getDisplay(), xCursorImage);
                XCURSOR.XcursorImageDestroy(xCursorImage);
                UNSAFE.freeMemory(pNativePixels);
                AWTAccessor.getCursorAccessor().setPData(this, pData);
            }
            finally {
                XToolkit.awtUnlock();
            }
        }

    }

    /**
     * Creates a new image cursor object.
     * If the image to display is invalid, the cursor will be hidden (made
     * completely transparent), and the hotspot will be set to (0, 0).
     *
     * <p>Note that multi-frame images are invalid and may cause this
     * method to hang.
     *
     * @param cursor the image to display when the cursor is activated
     * @param hotSpot the X and Y of the large cursor's hot spot; the
     *   hotSpot values must be less than the Dimension returned by
     *   <code>java.awt.Toolkit.getBestCursorSize</code>
     * @param     name a localized description of the cursor, for Java Accessibility use
     * @exception IndexOutOfBoundsException if the hotSpot values are outside
     *   the bounds of the cursor
     * @exception HeadlessException if GraphicsEnvironment.isHeadless()
     * returns true
     * @see       java.awt.GraphicsEnvironment#isHeadless
     *
     * @return the colorful image cursor, or the original 2-color image cursor if {@link ColorfulXCursor#isARGBSupported()} returns false
     */
    public static Cursor createImageCursor(final Image cursor, final Point hotSpot, final String name) throws IndexOutOfBoundsException, HeadlessException {
        if (isARGBSupported()) return new XImageCursor(cursor, hotSpot, name);
        else return Toolkit.getDefaultToolkit().createCustomCursor(cursor, hotSpot, name);
    }

    private static final Map<Integer, Cursor> fontCursors = new ConcurrentHashMap<>();

    /**
     * Gets font cursor object by type.
     * @see <a href="https://www.x.org/releases/X11R7.5/doc/man/man3/XCreateFontCursor.3.html">XCreateFontCursor</a>
     *
     * @param type the font cursor type
     *
     * @exception HeadlessException if GraphicsEnvironment.isHeadless()
     * returns true
     * @see       java.awt.GraphicsEnvironment#isHeadless
     *
     * @return the font cursor, or the default cursor if there's no cursor could be found by the type
     */
    public static Cursor getFontCursor(final int type) throws HeadlessException {
        if (type < XC_X_cursor || type > XC_xterm || type % 2 != 0)
            throw new IllegalArgumentException("Invalid type");
        final long pData = XLIB.XCreateFontCursor(XToolkit.getDisplay(), type);
        if (pData == NULL) return Cursor.getDefaultCursor();
        if (!fontCursors.containsKey(type)) fontCursors.put(type, new XCursor(getFontCursorName(type), pData));
        return fontCursors.get(type);
    }

    private static String getFontCursorName(final int type) {
        switch (type) {
            case 0: return "XC_X_cursor";
            case 2: return "XC_arrow";
            case 4: return "XC_based_arrow_down";
            case 6: return "XC_based_arrow_up";
            case 8: return "XC_boat";
            case 10: return "XC_bogosity";
            case 12: return "XC_bottom_left_corner";
            case 14: return "XC_bottom_right_corner";
            case 16: return "XC_bottom_side";
            case 18: return "XC_bottom_tee";
            case 20: return "XC_box_spiral";
            case 22: return "XC_center_ptr";
            case 24: return "XC_circle";
            case 26: return "XC_clock";
            case 28: return "XC_coffee_mug";
            case 30: return "XC_cross";
            case 32: return "XC_cross_reverse";
            case 34: return "XC_crosshair";
            case 36: return "XC_diamond_cross";
            case 38: return "XC_dot";
            case 40: return "XC_dotbox";
            case 42: return "XC_double_arrow";
            case 44: return "XC_draft_large";
            case 46: return "XC_draft_small";
            case 48: return "XC_draped_box";
            case 50: return "XC_exchange";
            case 52: return "XC_fleur";
            case 54: return "XC_gobbler";
            case 56: return "XC_gumby";
            case 58: return "XC_hand1";
            case 60: return "XC_hand2";
            case 62: return "XC_heart";
            case 64: return "XC_icon";
            case 66: return "XC_iron_cross";
            case 68: return "XC_left_ptr";
            case 70: return "XC_left_side";
            case 72: return "XC_left_tee";
            case 74: return "XC_leftbutton";
            case 76: return "XC_ll_angle";
            case 78: return "XC_lr_angle";
            case 80: return "XC_man";
            case 82: return "XC_middlebutton";
            case 84: return "XC_mouse";
            case 86: return "XC_pencil";
            case 88: return "XC_pirate";
            case 90: return "XC_plus";
            case 92: return "XC_question_arrow";
            case 94: return "XC_right_ptr";
            case 96: return "XC_right_side";
            case 98: return "XC_right_tee";
            case 100: return "XC_rightbutton";
            case 102: return "XC_rtl_logo";
            case 104: return "XC_sailboat";
            case 106: return "XC_sb_down_arrow";
            case 108: return "XC_sb_h_double_arrow";
            case 110: return "XC_sb_left_arrow";
            case 112: return "XC_sb_right_arrow";
            case 114: return "XC_sb_up_arrow";
            case 116: return "XC_sb_v_double_arrow";
            case 118: return "XC_shuttle";
            case 120: return "XC_sizing";
            case 122: return "XC_spider";
            case 124: return "XC_spraycan";
            case 126: return "XC_star";
            case 128: return "XC_target";
            case 130: return "XC_tcross";
            case 132: return "XC_top_left_arrow";
            case 134: return "XC_top_left_corner";
            case 136: return "XC_top_right_corner";
            case 138: return "XC_top_side";
            case 140: return "XC_top_tee";
            case 142: return "XC_trek";
            case 144: return "XC_ul_angle";
            case 146: return "XC_umbrella";
            case 148: return "XC_ur_angle";
            case 150: return "XC_watch";
            case 152: return "XC_xterm";
            default: throw new IllegalArgumentException("Invalid type");
        }
    }

    private static final Map<String, Cursor> libraryCursors = new ConcurrentHashMap<>();

    /**
     * Gets library cursor object by name.
     * @see <a href=https://www.x.org/releases/X11R7.7/doc/man/man3/Xcursor.3.xhtml>XcursorLibraryLoadCursor</a>
     *
     * @param     name the library cursor name
     *
     * @exception HeadlessException if GraphicsEnvironment.isHeadless()
     * returns true
     * @see       java.awt.GraphicsEnvironment#isHeadless
     *
     * @return the library cursor, or the default cursor if there's no cursor could be found by the name
     */
    public static Cursor getLibraryCursor(final String name) throws HeadlessException {
        final long pData = XCURSOR.XcursorLibraryLoadCursor(XToolkit.getDisplay(), Objects.requireNonNull(name));
        if (pData == 0) return Cursor.getDefaultCursor();
        else {
            if (!libraryCursors.containsKey(name)) libraryCursors.put(name, new XCursor(name, pData));
            return libraryCursors.get(name);
        }
    }

    static final sun.misc.Unsafe UNSAFE;
    static {
        sun.misc.Unsafe unsafe = null;
        try {
            @SuppressWarnings("DiscouragedPrivateApi")
            final Field field = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (sun.misc.Unsafe) field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException ignored) {
        }
        UNSAFE = unsafe;
    }

    private ColorfulXCursor() {
        throw new UnsupportedOperationException();
    }

    private static final Xlib XLIB = LibraryLoader.create(Xlib.class).load("X11");

    protected interface Xlib {
        long XCreateFontCursor(@In long display, @In int shape);
    }

    private static final Xcursor XCURSOR = LibraryLoader.create(Xcursor.class).load("Xcursor");

    /**
     * Returns whether current display supports ARGB-colored cursor
     * @return true if ARGB-colored cursor is supported by current display, false not
     */
    public static boolean isARGBSupported() {
        return XCURSOR.XcursorSupportsARGB(XToolkit.getDisplay());
    }

    protected interface Xcursor {
        XcursorImage XcursorImageCreate(@In int width, @In int height);
        void XcursorImageDestroy(@In XcursorImage image);
        long XcursorImageLoadCursor(@In long dpy, @In XcursorImage image);
        long XcursorLibraryLoadCursor(@In long dpy, @In String name);
        boolean XcursorSupportsARGB (@In long dpy);
    }

    protected final static class XcursorImage extends Struct {
        private final Unsigned32     version = new Unsigned32();	/* version of the image data */
        private final Unsigned32     size    = new Unsigned32();   	/* nominal size for matching */
        private final Unsigned32     width   = new Unsigned32();  	/* actual width */
        private final Unsigned32     height  = new Unsigned32();  	/* actual height */
        private final Unsigned32     xhot    = new Unsigned32();	/* hot spot x (must be inside image) */
        private final Unsigned32     yhot    = new Unsigned32();   	/* hot spot y (must be inside image) */
        private final Unsigned32     delay   = new Unsigned32();  	/* animation delay to next frame (ms) */
        private final Pointer        pixels  = new Pointer();    	/* pointer to pixels */
        public XcursorImage(Runtime runtime) {
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
        public void setPixels(final jnr.ffi.Pointer pixels) {
            this.pixels.set(pixels);
        }
        public jnr.ffi.Pointer getPixels() {
            return pixels.get();
        }
    }

}
