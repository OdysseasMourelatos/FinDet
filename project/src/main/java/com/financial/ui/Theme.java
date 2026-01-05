package com.financial.ui;

/**
 * Centralized theme constants for the FinDet application.
 *
 * Design Philosophy:
 * - Apple-like minimalism with clean, spacious layouts
 * - Greek governmental theming with subtle blue accents (inspired by Greek flag)
 * - Gamified elements for engagement without overwhelming colors
 * - Dark mode optimized for extended use
 */
public final class Theme {

    private Theme() {} // Prevent instantiation

    // ═══════════════════════════════════════════════════════════════════════════
    // CORE COLORS - Greek Government Inspired Palette
    // ═══════════════════════════════════════════════════════════════════════════

    /** Deep background - creates depth and focus */
    public static final String BG_BASE = "#0D1117";

    /** Primary surface - cards, panels */
    public static final String BG_SURFACE = "#161B22";

    /** Elevated surface - modals, dropdowns, hover states */
    public static final String BG_ELEVATED = "#1C2128";

    /** Subtle surface - input fields, nested elements */
    public static final String BG_SUBTLE = "#21262D";

    /** Muted surface - disabled states */
    public static final String BG_MUTED = "#30363D";

    // ═══════════════════════════════════════════════════════════════════════════
    // TEXT COLORS
    // ═══════════════════════════════════════════════════════════════════════════

    /** Primary text - high emphasis */
    public static final String TEXT_PRIMARY = "#F0F6FC";

    /** Secondary text - medium emphasis */
    public static final String TEXT_SECONDARY = "#8B949E";

    /** Muted text - low emphasis, hints */
    public static final String TEXT_MUTED = "#6E7681";

    /** Disabled text */
    public static final String TEXT_DISABLED = "#484F58";

    // ═══════════════════════════════════════════════════════════════════════════
    // ACCENT COLORS - Greek Blue Theme
    // ═══════════════════════════════════════════════════════════════════════════

    /** Primary accent - Greek flag blue */
    public static final String ACCENT_PRIMARY = "#0D47A1";

    /** Light accent - for hover, highlights */
    public static final String ACCENT_LIGHT = "#1976D2";

    /** Bright accent - for active states, focus */
    public static final String ACCENT_BRIGHT = "#42A5F5";

    /** Subtle accent - for backgrounds */
    public static final String ACCENT_SUBTLE = "rgba(13, 71, 161, 0.15)";

    // ═══════════════════════════════════════════════════════════════════════════
    // SEMANTIC COLORS
    // ═══════════════════════════════════════════════════════════════════════════

    /** Success - revenues, positive values */
    public static final String SUCCESS = "#2EA043";
    public static final String SUCCESS_LIGHT = "#3FB950";
    public static final String SUCCESS_SUBTLE = "rgba(46, 160, 67, 0.15)";

    /** Warning - caution states */
    public static final String WARNING = "#D29922";
    public static final String WARNING_LIGHT = "#E3B341";
    public static final String WARNING_SUBTLE = "rgba(210, 153, 34, 0.15)";

    /** Error - expenses, negative values */
    public static final String ERROR = "#DA3633";
    public static final String ERROR_LIGHT = "#F85149";
    public static final String ERROR_SUBTLE = "rgba(218, 54, 51, 0.15)";

    /** Info - neutral information */
    public static final String INFO = "#58A6FF";
    public static final String INFO_SUBTLE = "rgba(88, 166, 255, 0.15)";

    // ═══════════════════════════════════════════════════════════════════════════
    // BORDERS
    // ═══════════════════════════════════════════════════════════════════════════

    public static final String BORDER_DEFAULT = "#30363D";
    public static final String BORDER_MUTED = "#21262D";
    public static final String BORDER_EMPHASIS = "#8B949E";

    // ═══════════════════════════════════════════════════════════════════════════
    // GAMIFICATION COLORS - Achievement & Progress
    // ═══════════════════════════════════════════════════════════════════════════

    public static final String GOLD = "#FFD700";
    public static final String GOLD_SUBTLE = "rgba(255, 215, 0, 0.15)";
    public static final String BRONZE = "#CD7F32";
    public static final String SILVER = "#C0C0C0";

    // ═══════════════════════════════════════════════════════════════════════════
    // CHART COLORS - Monochromatic Blue Scale
    // ═══════════════════════════════════════════════════════════════════════════

    public static final String[] CHART_PALETTE = {
        "#0D47A1", "#1565C0", "#1976D2", "#1E88E5", "#2196F3",
        "#42A5F5", "#64B5F6", "#90CAF9", "#BBDEFB", "#E3F2FD"
    };

    // ═══════════════════════════════════════════════════════════════════════════
    // TYPOGRAPHY
    // ═══════════════════════════════════════════════════════════════════════════

    public static final String FONT_FAMILY = "-apple-system, BlinkMacSystemFont, 'Segoe UI', system-ui, sans-serif";

    public static final int FONT_TITLE = 28;
    public static final int FONT_HEADING = 20;
    public static final int FONT_SUBHEADING = 16;
    public static final int FONT_BODY = 14;
    public static final int FONT_SMALL = 12;
    public static final int FONT_TINY = 11;

    // ═══════════════════════════════════════════════════════════════════════════
    // SPACING (Apple HIG inspired)
    // ═══════════════════════════════════════════════════════════════════════════

    public static final int SPACE_XS = 4;
    public static final int SPACE_SM = 8;
    public static final int SPACE_MD = 16;
    public static final int SPACE_LG = 24;
    public static final int SPACE_XL = 32;
    public static final int SPACE_XXL = 48;

    // ═══════════════════════════════════════════════════════════════════════════
    // BORDER RADIUS
    // ═══════════════════════════════════════════════════════════════════════════

    public static final int RADIUS_SM = 6;
    public static final int RADIUS_MD = 10;
    public static final int RADIUS_LG = 14;
    public static final int RADIUS_XL = 20;
    public static final int RADIUS_FULL = 9999;

    // ═══════════════════════════════════════════════════════════════════════════
    // SHADOWS
    // ═══════════════════════════════════════════════════════════════════════════

    public static final String SHADOW_SM = "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 4, 0, 0, 2);";
    public static final String SHADOW_MD = "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 8, 0, 0, 4);";
    public static final String SHADOW_LG = "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 16, 0, 0, 8);";

    // ═══════════════════════════════════════════════════════════════════════════
    // COMPONENT STYLES
    // ═══════════════════════════════════════════════════════════════════════════

    /** Style for primary buttons */
    public static String buttonPrimary() {
        return "-fx-background-color: " + ACCENT_PRIMARY + ";" +
               "-fx-text-fill: white;" +
               "-fx-font-size: " + FONT_BODY + "px;" +
               "-fx-font-weight: 600;" +
               "-fx-padding: 10 20;" +
               "-fx-background-radius: " + RADIUS_SM + ";" +
               "-fx-cursor: hand;";
    }

    public static String buttonPrimaryHover() {
        return "-fx-background-color: " + ACCENT_LIGHT + ";" +
               "-fx-text-fill: white;" +
               "-fx-font-size: " + FONT_BODY + "px;" +
               "-fx-font-weight: 600;" +
               "-fx-padding: 10 20;" +
               "-fx-background-radius: " + RADIUS_SM + ";" +
               "-fx-cursor: hand;";
    }

    /** Style for secondary/ghost buttons */
    public static String buttonSecondary() {
        return "-fx-background-color: transparent;" +
               "-fx-text-fill: " + TEXT_SECONDARY + ";" +
               "-fx-font-size: " + FONT_BODY + "px;" +
               "-fx-padding: 10 16;" +
               "-fx-background-radius: " + RADIUS_SM + ";" +
               "-fx-border-color: " + BORDER_DEFAULT + ";" +
               "-fx-border-radius: " + RADIUS_SM + ";" +
               "-fx-cursor: hand;";
    }

    public static String buttonSecondaryHover() {
        return "-fx-background-color: " + BG_ELEVATED + ";" +
               "-fx-text-fill: " + TEXT_PRIMARY + ";" +
               "-fx-font-size: " + FONT_BODY + "px;" +
               "-fx-padding: 10 16;" +
               "-fx-background-radius: " + RADIUS_SM + ";" +
               "-fx-border-color: " + BORDER_DEFAULT + ";" +
               "-fx-border-radius: " + RADIUS_SM + ";" +
               "-fx-cursor: hand;";
    }

    /** Style for navigation buttons */
    public static String navButton() {
        return "-fx-background-color: transparent;" +
               "-fx-text-fill: " + TEXT_SECONDARY + ";" +
               "-fx-font-size: " + FONT_BODY + "px;" +
               "-fx-padding: 12 16;" +
               "-fx-background-radius: " + RADIUS_SM + ";" +
               "-fx-cursor: hand;" +
               "-fx-alignment: CENTER-LEFT;";
    }

    public static String navButtonHover() {
        return "-fx-background-color: " + BG_SUBTLE + ";" +
               "-fx-text-fill: " + TEXT_PRIMARY + ";" +
               "-fx-font-size: " + FONT_BODY + "px;" +
               "-fx-padding: 12 16;" +
               "-fx-background-radius: " + RADIUS_SM + ";" +
               "-fx-cursor: hand;" +
               "-fx-alignment: CENTER-LEFT;";
    }

    public static String navButtonActive() {
        return "-fx-background-color: " + ACCENT_SUBTLE + ";" +
               "-fx-text-fill: " + ACCENT_BRIGHT + ";" +
               "-fx-font-size: " + FONT_BODY + "px;" +
               "-fx-font-weight: 600;" +
               "-fx-padding: 12 16;" +
               "-fx-background-radius: " + RADIUS_SM + ";" +
               "-fx-cursor: hand;" +
               "-fx-alignment: CENTER-LEFT;";
    }

    /** Style for text fields */
    public static String textField() {
        return "-fx-background-color: " + BG_SUBTLE + ";" +
               "-fx-text-fill: " + TEXT_PRIMARY + ";" +
               "-fx-prompt-text-fill: " + TEXT_MUTED + ";" +
               "-fx-font-size: " + FONT_BODY + "px;" +
               "-fx-padding: 10 14;" +
               "-fx-background-radius: " + RADIUS_SM + ";" +
               "-fx-border-color: " + BORDER_DEFAULT + ";" +
               "-fx-border-radius: " + RADIUS_SM + ";";
    }

    public static String textFieldFocused() {
        return "-fx-background-color: " + BG_SUBTLE + ";" +
               "-fx-text-fill: " + TEXT_PRIMARY + ";" +
               "-fx-prompt-text-fill: " + TEXT_MUTED + ";" +
               "-fx-font-size: " + FONT_BODY + "px;" +
               "-fx-padding: 10 14;" +
               "-fx-background-radius: " + RADIUS_SM + ";" +
               "-fx-border-color: " + ACCENT_LIGHT + ";" +
               "-fx-border-radius: " + RADIUS_SM + ";";
    }

    /** Style for combo boxes */
    public static String comboBox() {
        return "-fx-background-color: " + BG_SUBTLE + ";" +
               "-fx-text-fill: " + TEXT_PRIMARY + ";" +
               "-fx-font-size: " + FONT_BODY + "px;" +
               "-fx-background-radius: " + RADIUS_SM + ";" +
               "-fx-border-color: " + BORDER_DEFAULT + ";" +
               "-fx-border-radius: " + RADIUS_SM + ";";
    }

    /** Style for cards */
    public static String card() {
        return "-fx-background-color: " + BG_SURFACE + ";" +
               "-fx-background-radius: " + RADIUS_LG + ";" +
               "-fx-border-color: " + BORDER_MUTED + ";" +
               "-fx-border-radius: " + RADIUS_LG + ";";
    }

    public static String cardHover() {
        return "-fx-background-color: " + BG_ELEVATED + ";" +
               "-fx-background-radius: " + RADIUS_LG + ";" +
               "-fx-border-color: " + BORDER_DEFAULT + ";" +
               "-fx-border-radius: " + RADIUS_LG + ";";
    }

    /** Style for elevated cards */
    public static String cardElevated() {
        return "-fx-background-color: " + BG_ELEVATED + ";" +
               "-fx-background-radius: " + RADIUS_LG + ";" +
               "-fx-border-color: " + BORDER_DEFAULT + ";" +
               "-fx-border-radius: " + RADIUS_LG + ";" +
               SHADOW_SM;
    }

    /** Style for tables */
    public static String table() {
        return "-fx-background-color: " + BG_SURFACE + ";" +
               "-fx-background-radius: " + RADIUS_MD + ";" +
               "-fx-border-color: " + BORDER_MUTED + ";" +
               "-fx-border-radius: " + RADIUS_MD + ";";
    }

    /** Style for stat cards with semantic colors */
    public static String statCard(String accentColor) {
        return "-fx-background-color: " + BG_SURFACE + ";" +
               "-fx-background-radius: " + RADIUS_LG + ";" +
               "-fx-border-color: " + BORDER_MUTED + ";" +
               "-fx-border-radius: " + RADIUS_LG + ";" +
               "-fx-border-width: 1 1 1 3;" +
               "-fx-border-insets: 0;" +
               "-fx-border-style: solid solid solid solid;";
    }

    /** Style for badges/chips */
    public static String badge(String bgColor, String textColor) {
        return "-fx-background-color: " + bgColor + ";" +
               "-fx-text-fill: " + textColor + ";" +
               "-fx-font-size: " + FONT_TINY + "px;" +
               "-fx-font-weight: 600;" +
               "-fx-padding: 4 10;" +
               "-fx-background-radius: " + RADIUS_FULL + ";";
    }

    /** Style for section headers */
    public static String sectionHeader() {
        return "-fx-font-size: " + FONT_HEADING + "px;" +
               "-fx-font-weight: 600;" +
               "-fx-text-fill: " + TEXT_PRIMARY + ";";
    }

    /** Style for page titles */
    public static String pageTitle() {
        return "-fx-font-size: " + FONT_TITLE + "px;" +
               "-fx-font-weight: bold;" +
               "-fx-text-fill: " + TEXT_PRIMARY + ";";
    }

    /** Style for subtitles */
    public static String subtitle() {
        return "-fx-font-size: " + FONT_SUBHEADING + "px;" +
               "-fx-text-fill: " + TEXT_SECONDARY + ";";
    }

    /** Style for body text */
    public static String bodyText() {
        return "-fx-font-size: " + FONT_BODY + "px;" +
               "-fx-text-fill: " + TEXT_PRIMARY + ";";
    }

    /** Style for muted text */
    public static String mutedText() {
        return "-fx-font-size: " + FONT_SMALL + "px;" +
               "-fx-text-fill: " + TEXT_MUTED + ";";
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // GREEK GOVERNMENTAL ELEMENTS
    // ═══════════════════════════════════════════════════════════════════════════

    /** Greek flag blue stripe pattern (for decorative elements) */
    public static final String GREEK_BLUE = "#0D5EAF";
    public static final String GREEK_WHITE = "#FFFFFF";

    /** Official government style header */
    public static String governmentHeader() {
        return "-fx-background-color: linear-gradient(to right, " + ACCENT_PRIMARY + ", " + ACCENT_LIGHT + ");" +
               "-fx-padding: 16 24;";
    }

    /** Greek meander/key pattern border (simplified) */
    public static String greekBorder() {
        return "-fx-border-color: " + ACCENT_PRIMARY + ";" +
               "-fx-border-width: 0 0 2 0;" +
               "-fx-border-style: solid;";
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // UTILITY METHODS
    // ═══════════════════════════════════════════════════════════════════════════

    /** Format amount in Greek style */
    public static String formatAmount(long amount) {
        if (Math.abs(amount) >= 1_000_000_000) {
            return String.format("%.2f δισ. €", amount / 1_000_000_000.0);
        } else if (Math.abs(amount) >= 1_000_000) {
            return String.format("%.2f εκ. €", amount / 1_000_000.0);
        } else if (Math.abs(amount) >= 1_000) {
            return String.format("%.1f χιλ. €", amount / 1_000.0);
        }
        return String.format("%,d €", amount);
    }

    /** Get color for positive/negative amounts */
    public static String amountColor(long amount) {
        return amount >= 0 ? SUCCESS_LIGHT : ERROR_LIGHT;
    }
}
