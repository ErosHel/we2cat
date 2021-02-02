package com.we2cat.gui;

import javax.swing.*;

/**
 * Created by hel on 2021/2/1 16:49
 */
public class Entity2JsonGui extends BaseDialog {

    private JPanel contentPane;

    /**
     * 标题
     */
    private JLabel titleLabel;

    private JTextPane jsonTextPane;

    public Entity2JsonGui() {
        super("实体类转Json", 600, 440);
        setContentPane(contentPane);
    }

    /**
     * 设置json字符串到文本框
     *
     * @param className 类名
     * @param json      json字符串
     */
    public void setJson(String className, String json) {
        titleLabel.setText("类名: " + className);
        jsonTextPane.setText(json);
    }

}
