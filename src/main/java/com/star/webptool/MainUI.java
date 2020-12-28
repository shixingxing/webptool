package com.star.webptool;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
    private JTextField frame_text;
    private JTextField zip_text;


    private File defaultDir;

    public MainUI() {
        String REGEX = "[^(0-9)]";
        final Pattern pattern = Pattern.compile(REGEX);
        select_file.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileFilter filter = new FileNameExtensionFilter("图像文件", "JPG", "PNG");

                JFileChooser addChooser = new JFileChooser();
                addChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                if (defaultDir != null) {
                    addChooser.setCurrentDirectory(defaultDir);
                }
                addChooser.setMultiSelectionEnabled(true);
                addChooser.setFileFilter(filter);
                int returnVal = addChooser.showDialog(root_panel, "图片选择");
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File[] files = addChooser.getSelectedFiles();

                    if (files != null && files.length > 0) {
                        //把选中的目录作为默认值
                        defaultDir = files[0].getParentFile();
                    }

                    List<String> fileNameList = Arrays.stream(files)
                            .sorted(new Comparator<File>() {
                                @Override
                                public int compare(File o1, File o2) {
                                    String file1Name = o1.getName();
                                    String file2Name = o2.getName();

                                    try {
                                        int a = Integer.parseInt(pattern.matcher(file1Name).replaceAll("").trim());
                                        int b = Integer.parseInt(pattern.matcher(file2Name).replaceAll("").trim());
                                        return a - b;
                                    } catch (NumberFormatException numberFormatException) {
                                        numberFormatException.printStackTrace();
                                    }
                                    return 0;
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
                String frame = frame_text.getText();
                //压缩率
                String zip = zip_text.getText();
                int frameInt;
                try {
                    frameInt = Integer.parseInt(frame);
                } catch (NumberFormatException numberFormatException) {
                    numberFormatException.printStackTrace();
                    JOptionPane.showMessageDialog(null, "帧率数据错误，只能为数字", "错误", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (frameInt <= 0) {
                    JOptionPane.showMessageDialog(null, "帧率数据错误，必须大于0", "错误", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int zipInt;
                try {
                    zipInt = Integer.parseInt(zip);
                } catch (NumberFormatException numberFormatException) {
                    numberFormatException.printStackTrace();
                    JOptionPane.showMessageDialog(null, "压缩率数据错误，只能为数字", "错误", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (zipInt <= 0 || zipInt > 100) {
                    JOptionPane.showMessageDialog(null, "压缩率数据错误，必须大于0,小于100", "错误", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                String timePerFrame = String.valueOf(1000 / frameInt);
                int size = file_list.getModel().getSize();

                if (size > 0) {

                    StringBuilder builder = new StringBuilder();
                    builder.append("img2webp -loop 0");
                    //有损压缩配置
                    builder.append(" -lossy -q ");
                    builder.append(zip);

                    for (int i = 0; i < size; i++) {
                        builder.append(" ");
                        builder.append("\"");
                        builder.append(file_list.getModel().getElementAt(i));
                        builder.append("\"");
                        //默认1000/24 = 42毫秒每帧
                        builder.append(" -d ");
                        builder.append(timePerFrame);
                    }
                    builder.append(" -o output.webp");


                    Runtime rt = Runtime.getRuntime();
                    try {
                        Process process = rt.exec(builder.toString());
                        InputStream inputStream = process.getErrorStream();
                        InputStreamReader isr = new InputStreamReader(inputStream);
                        BufferedReader buff = new BufferedReader(isr);

                        StringBuilder message = new StringBuilder();
                        String line;
                        while ((line = buff.readLine()) != null) {
                            message.append(line);
                        }

                        if (message.length() > 0) {
                            JOptionPane.showMessageDialog(null, message.toString(), "错误", JOptionPane.WARNING_MESSAGE);
                        }
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                        JOptionPane.showMessageDialog(null, ioException.getMessage(), "错误", JOptionPane.WARNING_MESSAGE);
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
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        root_panel.add(select_file, gbc);
        create_webp_anim = new JButton();
        create_webp_anim.setText("生成webp动图");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        root_panel.add(create_webp_anim, gbc);
        final JScrollPane scrollPane1 = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        root_panel.add(scrollPane1, gbc);
        file_list = new JList();
        file_list.setDragEnabled(true);
        file_list.setDropMode(DropMode.ON);
        final DefaultListModel defaultListModel1 = new DefaultListModel();
        file_list.setModel(defaultListModel1);
        file_list.setPreferredSize(new Dimension(0, 0));
        file_list.setRequestFocusEnabled(true);
        file_list.setSelectionMode(1);
        scrollPane1.setViewportView(file_list);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        root_panel.add(panel1, gbc);
        final JLabel label1 = new JLabel();
        label1.setText("帧率：");
        panel1.add(label1);
        frame_text = new JTextField();
        frame_text.setMinimumSize(new Dimension(60, 30));
        frame_text.setPreferredSize(new Dimension(60, 30));
        frame_text.setText("24");
        panel1.add(frame_text);
        final JLabel label2 = new JLabel();
        label2.setText("压缩率：");
        panel1.add(label2);
        zip_text = new JTextField();
        zip_text.setMinimumSize(new Dimension(60, 30));
        zip_text.setPreferredSize(new Dimension(60, 30));
        zip_text.setText("90");
        panel1.add(zip_text);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return root_panel;
    }

    public void setData(MainUI data) {
    }

    public void getData(MainUI data) {
    }
}
