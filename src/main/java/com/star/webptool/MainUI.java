package com.star.webptool;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MainUI {
    private JButton select_file;
    private JList file_list;
    private JButton create_webp_anim;
    private JPanel root_panel;

    public MainUI() {
        String REGEX = "[^(0-9)]";
        final Pattern pattern = Pattern.compile(REGEX);
        select_file.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileFilter filter = new FileNameExtensionFilter("图像文件", "JPG", "PNG");

                JFileChooser addChooser = new JFileChooser();
                addChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                addChooser.setMultiSelectionEnabled(true);
                addChooser.setFileFilter(filter);
                int returnVal = addChooser.showDialog(root_panel, "图片选择");
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File[] files = addChooser.getSelectedFiles();

                    List<String> fileNameList = Arrays.stream(files)
                            .sorted(new Comparator<File>() {
                                @Override
                                public int compare(File o1, File o2) {
                                    String file1Name = o1.getName();
                                    String file2Name = o2.getName();

                                    int a = Integer.valueOf(pattern.matcher(file1Name).replaceAll("").trim());
                                    int b = Integer.valueOf(pattern.matcher(file2Name).replaceAll("").trim());
                                    return a - b;
                                }
                            })
                            .map(new Function<File, String>() {
                                @Override
                                public String apply(File file) {
                                    return file.getAbsolutePath();
                                }
                            }).collect(Collectors.toList());
                    ListModel<String> filePath = new DefaultComboBoxModel(fileNameList.toArray());
                    file_list.setModel(filePath);
                }
            }
        });
        create_webp_anim.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int size = file_list.getModel().getSize();

            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("MainUI");
        try {
            frame.setContentPane(new MainUI().root_panel);
        } catch (Exception e) {
            e.printStackTrace();
        }
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}
