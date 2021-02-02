package com.we2cat.gui;

import javax.swing.*;
import java.awt.*;

/**
 * 基础的dialog
 * <p>
 * Created by hel on 2021/2/1 17:19
 */
public abstract class BaseDialog extends JDialog {

    /**
     * 创建时进行设置宽和高
     *
     * @param title  标题
     * @param width  宽
     * @param height 高
     */
    public BaseDialog(String title, int width, int height) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = screenSize.width / 2 - width / 2;
        int y = screenSize.height / 2 - height / 2 - 30;
        setBounds(x, y, width, height);
        setAlwaysOnTop(Boolean.TRUE);
        setResizable(Boolean.FALSE);
        setTitle(title);
    }

}
