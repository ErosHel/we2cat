package com.we2cat.gui;

import com.intellij.openapi.project.Project;
import com.intellij.ui.JBColor;
import com.we2cat.codeauto.AutoCode;
import com.we2cat.exception.AlertException;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by hel on 2020/12/30 10:03
 */
public class GenerateMybatisPlusCodeGui extends JDialog {

    /**
     * 画布
     */
    private JPanel contentPane;

    /**
     * 数据库用户
     */
    private JTextField dbUser;

    /**
     * 数据库密码
     */
    private JPasswordField dbPw;

    /**
     * 数据库连接
     */
    private JTextField dbUrl;

    /**
     * 当前项目名称
     */
    private JLabel projectName;

    /**
     * 包名
     */
    private JTextField pagName;

    /**
     * 模块名称 逗号分隔
     */
    private JTextField modelName;

    /**
     * 子模块
     */
    private JTextField childModelName;

    /**
     * 数据库名称
     */
    private JTextField dbName;

    /**
     * 表名 逗号分隔
     */
    private JTextField table;

    /**
     * 输出目录
     */
    private JTextField outPath;

    /**
     * 开始按钮
     */
    private JButton start;

    /**
     * 提示
     */
    private JLabel alert;

    private static final int WIDTH = 600;

    private static final int HEIGHT = 380;

    private final Project project;

    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    public GenerateMybatisPlusCodeGui(Project project) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = screenSize.width / 2 - WIDTH / 2;
        int y = screenSize.height / 2 - HEIGHT / 2 - 30;
        setBounds(x, y, WIDTH, HEIGHT);
        setFocusable(true);
        setAlwaysOnTop(true);
        setTitle("mybatis-plus代码生成");
        setContentPane(contentPane);
        setModal(Boolean.TRUE);

        this.project = project;
        //System.getProperty("user.dir")
        this.dbUser.setName("用户名");
        this.dbPw.setName("密码");
        this.dbUrl.setName("ip地址");
        this.pagName.setName("包名");
        this.modelName.setName("模块名");
        this.childModelName.setName("子模块");
        this.dbName.setName("数据库");
        this.outPath.setName("输出目录");

        projectName.setText(project.getName());
        start.addActionListener(event -> start());
    }

    private void start() {
        start.setEnabled(false);
        executor.execute(() -> {
            try {
                for (String appName : getText(modelName).split(",")) {
                    String childModelName = getText(this.childModelName);
                    AutoCode.of("", getText(pagName))
                            .setDeleteName("deleted")
                            .setTable(Optional.ofNullable(table.getText())
                                    .filter(t -> t.length() > 0)
                                    .map(t -> t.split(","))
                                    .orElse(null))
                            .setDbUser(getText(dbUser))
                            .setOutPath(getText(outPath) + "/" + project.getName())
                            .setDbUrl(getText(dbUrl))
                            .setDbPw(getText(dbPw))
                            .setDbName(getText(dbName))
                            .setFileOverride(true)
                            .setControllerPag(appName + ".controller." + childModelName)
                            .setServicePag(appName + ".service." + childModelName)
                            .setServiceImplPag(appName + ".service." + childModelName + ".impl")
                            .setDaoEnd("Mapper")
                            .setDaoPag(appName + ".mapper." + childModelName)
                            .setMapperPag(appName + ".mapper." + childModelName + ".xml")
                            .setEntityPag("model.entity." + childModelName)
                            .setEntityEnd("")
                            .setActiveRecord(false)
                            .start();
                }
                setAlert("生成完成", JBColor.GREEN);
            } catch (AlertException e) {
                setAlert(e.getMessage(), JBColor.RED);
            } finally {
                start.setEnabled(true);
            }
        });

    }

    /**
     * 获取指定文本框文本
     *
     * @param component 文本框
     * @return 文本
     */
    private String getText(JTextComponent component) {
        String text = component.getText();
        if (text == null || text.length() == 0) {
            throw new AlertException(component.getName() + "<-不能为空");
        }
        return text;
    }

    /**
     * 设置提示信息
     *
     * @param text 信息
     */
    private void setAlert(String text, JBColor color) {
        alert.setForeground(color);
        alert.setText(text);
        executor.execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException ignored) {
            }
            alert.setText("");
        });
    }

}
