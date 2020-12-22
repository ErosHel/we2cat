package com.we2cat.gui;

import com.intellij.openapi.project.Project;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by hel on 2020/12/16 18:21
 */
public class MybatisLogFilterSetting extends JDialog {

    private static final String filterName = "过滤设置";

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField preparingTextField;
    private JTextField parametersTextField;
    private JCheckBox startupCheckBox;

    /**
     * 窗口初始
     *
     * @param project 项目
     */
    public MybatisLogFilterSetting(Project project) {
        setTitle(filterName);
        setContentPane(contentPane);
        setModal(Boolean.TRUE);
        getRootPane().setDefaultButton(buttonOK);
        buttonOK.addActionListener(e -> onOK(project));
        buttonCancel.addActionListener(e -> onCancel());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    /**
     * 点击确认按钮处理
     *
     * @param project 项目
     */
    private void onOK(Project project) {
        setVisible(false);
    }

    private void onCancel() {
        setVisible(false);
    }

}
