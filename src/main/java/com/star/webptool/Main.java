package com.star.webptool;


import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Main {

    public static void main(String[] args) {
        //img2webp -loop 0 1.png 2.png 3.png 4.png 5.png 6.png 7.png 8.png 9.png 10.png -o outaaa.webp
// Create Display
        Display display = new Display();
        // Create Shell (Window) from diplay
        Shell shell = new Shell(display);

        shell.open();

        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
        display.dispose();
    }
}
