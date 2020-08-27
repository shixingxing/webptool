package com.star.webptool;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class ReportObjTransferable implements Transferable {
    Object object;
    static DataFlavor[] supportedFlavors = {ReportDataFlavor.localObjectFlavor};

    public ReportObjTransferable(Object obj) {
        this.object = obj;
    }

    public Object getTransferData(DataFlavor df) throws UnsupportedFlavorException, IOException {
        if (isDataFlavorSupported(df))
            return object;
        else
            throw new UnsupportedFlavorException(df);
    }

    public boolean isDataFlavorSupported(DataFlavor df) {
        return (df.equals(ReportDataFlavor.localObjectFlavor));
    }

    public DataFlavor[] getTransferDataFlavors() {
        return supportedFlavors;
    }

}
