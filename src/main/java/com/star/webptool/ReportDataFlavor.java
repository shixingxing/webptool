package com.star.webptool;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;

public class ReportDataFlavor {
    /**
     * swing控件
     */
    public static DataFlavor componentFlavor = createConstant(JComponent.class,
            JComponent.class.getName());
    /**
     * 本地对象类型
     */
    public static DataFlavor localObjectFlavor = createConstant(DataFlavor.javaJVMLocalObjectMimeType);


    /**
     * 数据类型构造
     *
     * @param rc
     * @param prn
     * @return
     */
    static private DataFlavor createConstant(Class rc, String prn) {
        try {
            return new DataFlavor(rc, prn);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    static private DataFlavor createConstant(String mimeType) {
        try {
            return new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType);
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
            return null;
        }
    }
}
