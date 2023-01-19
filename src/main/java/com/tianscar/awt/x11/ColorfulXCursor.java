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
import java.awt.GraphicsEnvironment;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The ColorfulXCursor factory class to create X cursors
 */
public final class ColorfulXCursor {

    private static final long NULL = 0L;

    // Cursor classes

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
                xCursorImage.xhot.set(xHotSpot);
                xCursorImage.yhot.set(yHotSpot);
                xCursorImage.pixels.set(nativePixels);
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

    private static void checkHeadless() throws HeadlessException {
        if (GraphicsEnvironment.isHeadless()) throw new HeadlessException();
    }

    /**
     * <p>Creates a new image cursor object.
     * <p>If {@link #isARGBSupported()} returns false, it fallbacks to {@link Toolkit#createCustomCursor(Image, Point, String)}
     * <p>If the image to display is invalid, the cursor will be hidden (made
     * completely transparent), and the hotspot will be set to (0, 0).
     *
     * <p>Note that multi-frame images are invalid and may cause this
     * method to hang.
     *
     * @param cursor the image to display when the cursor is activated
     * @param hotSpot the X and Y of the large cursor's hot spot; the
     *   hotSpot values must be less than the Dimension returned by
     *   {@link Toolkit#getBestCursorSize(int, int)}
     * @param     name a localized description of the cursor, for Java Accessibility use
     * @exception IndexOutOfBoundsException if the hotSpot values are outside
     *   the bounds of the cursor
     * @exception HeadlessException if GraphicsEnvironment.isHeadless()
     * returns true
     * @see       java.awt.GraphicsEnvironment#isHeadless
     * @see       Toolkit#createCustomCursor(Image, Point, String)
     *
     * @return the colorful image cursor, or the original 2-color image cursor if {@link ColorfulXCursor#isARGBSupported()} returns false
     */
    public static Cursor createImageCursor(final Image cursor, final Point hotSpot, final String name) throws IndexOutOfBoundsException, HeadlessException {
        checkHeadless();
        if (isARGBSupported()) return new XImageCursor(cursor, hotSpot, name);
        else return Toolkit.getDefaultToolkit().createCustomCursor(cursor, hotSpot, name);
    }

    private static final Map<String, Cursor> systemCursors = new ConcurrentHashMap<>(1);

    /**
     * Gets font cursor object by type.
     * @see <a href="https://www.x.org/releases/X11R7.5/doc/man/man3/XCreateFontCursor.3.html">XCreateFontCursor</a>
     *
     * @param type the font cursor type
     *
     * @throws IllegalArgumentException if the specified font cursor type
     * is invalid
     * @exception HeadlessException if GraphicsEnvironment.isHeadless()
     * returns true
     * @see       java.awt.GraphicsEnvironment#isHeadless
     *
     * @return the font cursor, or the default cursor if there's no cursor could be found by the type
     */
    public static Cursor getFontCursor(final int type) throws IllegalArgumentException, HeadlessException {
        checkHeadless();
        final String name = getFontCursorName(type);
        final long pData = XLIB.XCreateFontCursor(XToolkit.getDisplay(), type);
        if (pData == NULL) return Cursor.getDefaultCursor();
        if (!systemCursors.containsKey(name)) systemCursors.put(name, new XCursor(name, pData));
        return systemCursors.get(name);
    }

    private static String getFontCursorName(final int type) {
        switch (type) {
            case 0: return "X_cursor";
            case 2: return "arrow";
            case 4: return "based_arrow_down";
            case 6: return "based_arrow_up";
            case 8: return "boat";
            case 10: return "bogosity";
            case 12: return "bottom_left_corner";
            case 14: return "bottom_right_corner";
            case 16: return "bottom_side";
            case 18: return "bottom_tee";
            case 20: return "box_spiral";
            case 22: return "center_ptr";
            case 24: return "circle";
            case 26: return "clock";
            case 28: return "coffee_mug";
            case 30: return "cross";
            case 32: return "cross_reverse";
            case 34: return "crosshair";
            case 36: return "diamond_cross";
            case 38: return "dot";
            case 40: return "dotbox";
            case 42: return "double_arrow";
            case 44: return "draft_large";
            case 46: return "draft_small";
            case 48: return "draped_box";
            case 50: return "exchange";
            case 52: return "fleur";
            case 54: return "gobbler";
            case 56: return "gumby";
            case 58: return "hand1";
            case 60: return "hand2";
            case 62: return "heart";
            case 64: return "icon";
            case 66: return "iron_cross";
            case 68: return "left_ptr";
            case 70: return "left_side";
            case 72: return "left_tee";
            case 74: return "leftbutton";
            case 76: return "ll_angle";
            case 78: return "lr_angle";
            case 80: return "man";
            case 82: return "middlebutton";
            case 84: return "mouse";
            case 86: return "pencil";
            case 88: return "pirate";
            case 90: return "plus";
            case 92: return "question_arrow";
            case 94: return "right_ptr";
            case 96: return "right_side";
            case 98: return "right_tee";
            case 100: return "rightbutton";
            case 102: return "rtl_logo";
            case 104: return "sailboat";
            case 106: return "sb_down_arrow";
            case 108: return "sb_h_double_arrow";
            case 110: return "sb_left_arrow";
            case 112: return "sb_right_arrow";
            case 114: return "sb_up_arrow";
            case 116: return "sb_v_double_arrow";
            case 118: return "shuttle";
            case 120: return "sizing";
            case 122: return "spider";
            case 124: return "spraycan";
            case 126: return "star";
            case 128: return "target";
            case 130: return "tcross";
            case 132: return "top_left_arrow";
            case 134: return "top_left_corner";
            case 136: return "top_right_corner";
            case 138: return "top_side";
            case 140: return "top_tee";
            case 142: return "trek";
            case 144: return "ul_angle";
            case 146: return "umbrella";
            case 148: return "ur_angle";
            case 150: return "watch";
            case 152: return "xterm";
            default: throw new IllegalArgumentException("Invalid type");
        }
    }

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
        checkHeadless();
        final long pData = XCURSOR.XcursorLibraryLoadCursor(XToolkit.getDisplay(), name);
        if (pData == NULL) return Cursor.getDefaultCursor();
        else {
            if (!systemCursors.containsKey(name)) systemCursors.put(name, new XCursor(name, pData));
            return systemCursors.get(name);
        }
    }

    /**
     * Returns whether current display supports ARGB-colored cursor
     * @return true if ARGB-colored cursor is supported by current display, false not
     */
    public static boolean isARGBSupported() {
        return XCURSOR.XcursorSupportsARGB(XToolkit.getDisplay());
    }

    private ColorfulXCursor() {
        throw new UnsupportedOperationException();
    }

    // UNSAFE
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

    // JNR-FFI

    private static final Xlib XLIB = LibraryLoader.create(Xlib.class).load("X11");
    private static final Xcursor XCURSOR = LibraryLoader.create(Xcursor.class).load("Xcursor");

    // Xlib

    protected interface Xlib {
        long XCreateFontCursor(@In long display, @In int shape);
    }

    // Xcursor

    protected interface Xcursor {
        XcursorImage XcursorImageCreate(@In int width, @In int height);
        void XcursorImageDestroy(@In XcursorImage image);
        long XcursorImageLoadCursor(@In long dpy, @In XcursorImage image);
        long XcursorLibraryLoadCursor(@In long dpy, @In String name);
        boolean XcursorSupportsARGB(@In long dpy);
    }
    protected final static class XcursorImage extends Struct {
        public final Unsigned32     version = new Unsigned32();	/* version of the image data */
        public final Unsigned32     size    = new Unsigned32();   	/* nominal size for matching */
        public final Unsigned32     width   = new Unsigned32();  	/* actual width */
        public final Unsigned32     height  = new Unsigned32();  	/* actual height */
        public final Unsigned32     xhot    = new Unsigned32();	/* hot spot x (must be inside image) */
        public final Unsigned32     yhot    = new Unsigned32();   	/* hot spot y (must be inside image) */
        public final Unsigned32     delay   = new Unsigned32();  	/* animation delay to next frame (ms) */
        public final Pointer        pixels  = new Pointer();    	/* pointer to pixels */
        public XcursorImage(Runtime runtime) {
            super(runtime);
        }
    }

    // X cursor constants

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

}
