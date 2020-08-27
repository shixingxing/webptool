package com.star.webptool;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;

public class ReorderableList extends JList implements
        DragSourceListener,
        DropTargetListener,
        DragGestureListener {

    private static final long serialVersionUID = -961840807598972987L;
    /**
     * 拖源
     */
    DragSource dragSource;
    /**
     * 放置目标
     */
    DropTarget dropTarget;
    /**
     * 放置对象
     */
    Object dropTargetCell;
    /**
     * 被拖对象index
     */
    int draggedIndex = -1;


    /**
     * 获取放置对象
     *
     * @return
     */
    public Object getDropTargetCell() {
        return dropTargetCell;
    }

    public ReorderableList() {
        super();
        dragSource = DragSource.getDefaultDragSource();
        dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_MOVE, this);
        dropTarget = new DropTarget(this, this);
    }

    public void dragGestureRecognized(DragGestureEvent dge) {
        Point clickPoint = dge.getDragOrigin();
        int index = locationToIndex(clickPoint);
        if (index == -1) return;
        Object target = getModel().getElementAt(index);
        Transferable trans = new ReportObjTransferable(target);
        draggedIndex = index;
        dragSource.startDrag(dge, Cursor.getDefaultCursor(), trans, this);
    }

    public void dragEnter(DropTargetDragEvent dtde) {
        if (dtde.getSource() != dropTarget)
            dtde.rejectDrag();
        else {
            dtde.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);
        }
    }

    public void dragOver(DropTargetDragEvent dtde) {

        if (dtde.getSource() != dropTarget) dtde.rejectDrag();
        // 拿到拖放的目标位置
        Point dragPoint = dtde.getLocation();
        int index = locationToIndex(dragPoint);
        if (index == -1)
            dropTargetCell = null;
        else
            dropTargetCell = getModel().getElementAt(index);
        repaint();
    }

    public void dropActionChanged(DropTargetDragEvent dtde) {
    }

    public void dragExit(DropTargetEvent dte) {
    }

    public void drop(DropTargetDropEvent dtde) {
        if (dtde.getSource() != dropTarget) {
            dtde.rejectDrop();
            return;
        }
        Point dropPoint = dtde.getLocation();
        int index = locationToIndex(dropPoint);
        boolean dropped = false;
        try {
            DefaultListModel mod = (DefaultListModel) getModel();
            Object dragged = dtde.getTransferable().getTransferData(ReportDataFlavor.localObjectFlavor);
            // 判断拖拽的对象是否在JList内以及是否已含有当前拖拽对象，不满足条件则不允许放置
            if ((index == -1 && mod.size() > 0) || (index == draggedIndex && index != -1) || (draggedIndex == -1 && mod.contains(dragged))) {
                dtde.rejectDrop();
                return;
            }
            dtde.acceptDrop(DnDConstants.ACTION_MOVE);
            // 不是从当前面板拖拽的对象，则添加
            if (draggedIndex == -1) {
                mod.add(mod.getSize(), dragged);
            } else {
                // 从当前面板拖拽的对象，则进行顺序调整
                boolean sourceBeforeTarget = (draggedIndex < index);
                mod.remove(draggedIndex);
                mod.add((sourceBeforeTarget ? index - 1 : index), dragged);
            }
            dropped = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        dtde.dropComplete(dropped);
    }

    public void add(Object obj) {
        DefaultListModel mod = (DefaultListModel) getModel();
        mod.addElement(obj);
    }

    public void dragEnter(DragSourceDragEvent dsde) {
    }

    public void dragOver(DragSourceDragEvent dsde) {
    }

    public void dropActionChanged(DragSourceDragEvent dsde) {
    }

    public void dragExit(DragSourceEvent dse) {
    }

    public void dragDropEnd(DragSourceDropEvent dsde) {
        dropTargetCell = null;
        draggedIndex = -1;
        repaint();
    }


}
