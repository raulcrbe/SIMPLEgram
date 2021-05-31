package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;

import java.util.ArrayList;

public class DocumentObject {

    public static class ThemeDocument extends TLRPC.TL_document {

        public TLRPC.TL_themeSettings themeSettings;
        public TLRPC.Document wallpaper;
        public Theme.ThemeInfo baseTheme;
        public Theme.ThemeAccent accent;

        public ThemeDocument(TLRPC.TL_themeSettings settings) {
            themeSettings = settings;
            baseTheme = Theme.getTheme(Theme.getBaseThemeKey(settings));
            accent = baseTheme.createNewAccent(settings);
            if (themeSettings.wallpaper instanceof TLRPC.TL_wallPaper) {
                TLRPC.TL_wallPaper object = (TLRPC.TL_wallPaper) themeSettings.wallpaper;
                wallpaper = object.document;
                id = wallpaper.id;
                access_hash = wallpaper.access_hash;
                file_reference = wallpaper.file_reference;
                user_id = wallpaper.user_id;
                date = wallpaper.date;
                file_name = wallpaper.file_name;
                mime_type = wallpaper.mime_type;
                size = wallpaper.size;
                thumbs = wallpaper.thumbs;
                version = wallpaper.version;
                dc_id = wallpaper.dc_id;
                key = wallpaper.key;
                iv = wallpaper.iv;
                attributes = wallpaper.attributes;
            } else {
                id = Integer.MIN_VALUE;
                dc_id = Integer.MIN_VALUE;
            }
        }
    }

    public static SvgHelper.SvgDrawable getSvgThumb(ArrayList<TLRPC.PhotoSize> sizes, String colorKey, float alpha) {
        int w = 0;
        int h = 0;
        TLRPC.TL_photoPathSize photoPathSize = null;
        for (int a = 0, N = sizes.size(); a < N; a++) {
            TLRPC.PhotoSize photoSize = sizes.get(a);
            if (photoSize instanceof TLRPC.TL_photoPathSize) {
                photoPathSize = (TLRPC.TL_photoPathSize) photoSize;
            } else {
                w = photoSize.w;
                h = photoSize.h;
            }
            if (photoPathSize != null && w != 0 && h != 0) {
                SvgHelper.SvgDrawable pathThumb = SvgHelper.getDrawableByPath(SvgHelper.decompress(photoPathSize.bytes), w, h);
                if (pathThumb != null) {
                    pathThumb.setupGradient(colorKey, alpha);
                }
                return pathThumb;
            }
        }
        return null;
    }

    public static SvgHelper.SvgDrawable getSvgThumb(TLRPC.Document document, String colorKey, float alpha) {
        return getSvgThumb(document, colorKey, alpha, 1.0f);
    }

    public static SvgHelper.SvgDrawable getSvgThumb(TLRPC.Document document, String colorKey, float alpha, float zoom) {
        if (document == null) {
            return null;
        }
        SvgHelper.SvgDrawable pathThumb = null;
        for (int b = 0, N2 = document.thumbs.size(); b < N2; b++) {
            TLRPC.PhotoSize size = document.thumbs.get(b);
            if (size instanceof TLRPC.TL_photoPathSize) {
                int w = 512, h = 512;
                for (int a = 0, N = document.attributes.size(); a < N; a++) {
                    TLRPC.DocumentAttribute attribute = document.attributes.get(a);
                    if (attribute instanceof TLRPC.TL_documentAttributeImageSize) {
                        w = attribute.w;
                        h = attribute.h;
                        break;
                    }
                }
                if (w != 0 && h != 0) {
                    pathThumb = SvgHelper.getDrawableByPath(SvgHelper.decompress(size.bytes), (int) (w * zoom), (int) (h * zoom));
                    if (pathThumb != null) {
                        pathThumb.setupGradient(colorKey, alpha);
                    }
                }
                break;
            }
        }
        return pathThumb;
    }
}
