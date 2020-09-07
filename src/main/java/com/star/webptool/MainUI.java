package com.star.webptool;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
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

                if (size > 0) {

                    StringBuilder builder = new StringBuilder();
                    builder.append("img2webp -loop 0");
                    for (int i = 0; i < size; i++) {
                        builder.append(" ");
                        builder.append("\"");
                        builder.append(file_list.getModel().getElementAt(i));
                        builder.append("\"");
                    }
                    builder.append(" -o output.webp");


                    Runtime rt = Runtime.getRuntime();
                    try {
                        rt.exec(builder.toString());

                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }

                }
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("WebpTool");
        try {
            frame.setContentPane(new MainUI().root_panel);
        } catch (Exception e) {
            e.printStackTrace();
        }
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        root_panel = new JPanel();
        root_panel.setLayout(new GridBagLayout());
        root_panel.setMinimumSize(new Dimension(800, 600));
        root_panel.setPreferredSize(new Dimension(800, 600));
        select_file = new JButton();
        select_file.setText("选择文件");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        root_panel.add(select_file, gbc);
        file_list = new JList();
        file_list.setDropMode(DropMode.ON);
        final DefaultListModel defaultListModel1 = new DefaultListModel();
        file_list.setModel(defaultListModel1);
        file_list.setSelectionMode(1);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        root_panel.add(file_list, gbc);
        create_webp_anim = new JButton();
        create_webp_anim.setText("生成webp动图");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        root_panel.add(create_webp_anim, gbc);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return root_panel;
    }

}
