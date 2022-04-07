package net.golbarg.engtoper.util;

public class UtilController {
    public static String removeHTMLTags(String trans) {
        trans = trans.replace("<br/>", "");
        trans = trans.replace("<br>", "");
        trans = trans.replace("');", "");
        return trans;
    }
}
