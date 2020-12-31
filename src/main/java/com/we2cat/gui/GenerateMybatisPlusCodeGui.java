package com.we2cat.gui;

import com.intellij.openapi.project.Project;
import com.intellij.ui.JBColor;
import com.we2cat.codeauto.AutoCode;
import com.we2cat.entity.GenerateMybatisPlusCodeConfig;
import com.we2cat.exception.AlertException;
import com.we2cat.utils.ConfigUtilsKt;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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

    /**
     * 保存
     */
    private JButton save;

    /**
     * 实体包名
     */
    private JTextField domainPag;

    /**
     * 领域层包名
     */
    private JTextField domainName;

    /**
     * 控制层包名
     */
    private JTextField controllerPag;

    /**
     * 服务层包名
     */
    private JTextField servicePag;

    /**
     * 持久层包名
     */
    private JTextField mapperPag;

    private static final int WIDTH = 600;

    private static final int HEIGHT = 440;

    private final Project project;

    private String mapperPagName = "mapper";

    private String mapperEnd = "Mapper";

    private String domainPagName = "entity";

    private String domainEnd = "";

    private String lastControllerPag = null;
    private String lastServicePag = null;
    private String lastMapperPag = null;
    private String lastDomainPag = null;

    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    public GenerateMybatisPlusCodeGui(Project project) {
        this.project = project;
        setDefaultValue();
        setListener();
        getAndSetConfig();
    }

    /**
     * 设置监听事件
     */
    private void setListener() {
        start.addActionListener(e -> start());
        save.addActionListener(e -> saveConfig());

        pagName.addCaretListener(e -> changePag());
        modelName.addCaretListener(e -> changePag());
        childModelName.addCaretListener(e -> changePag());
        domainName.addActionListener(e -> changePag());
    }

    /**
     * 设置默认值
     */
    private void setDefaultValue() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = screenSize.width / 2 - WIDTH / 2;
        int y = screenSize.height / 2 - HEIGHT / 2 - 30;
        setBounds(x, y, WIDTH, HEIGHT);
        setFocusable(true);
        setAlwaysOnTop(true);
        setTitle("mybatis-plus代码生成");
        setContentPane(contentPane);
        setModal(Boolean.TRUE);
        this.dbUser.setName("用户名");
        this.dbPw.setName("密码");
        this.dbUrl.setName("数据库地址");
        this.pagName.setName("包名");
        this.modelName.setName("模块名");
        this.childModelName.setName("子模块");
        this.dbName.setName("数据库");
        this.outPath.setName("输出目录");
        projectName.setText(project.getName());
    }

    /**
     * 开始生成代码
     */
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
                            .setDaoEnd(mapperEnd)
                            .setDaoPag(appName + ".mapper." + childModelName)
                            .setMapperPag(appName + ".mapper." + childModelName + ".xml")
                            .setEntityPag("model.entity." + childModelName)
                            .setEntityEnd(domainEnd)
                            .setActiveRecord(false)
                            .start();
                }
                setAlert("生成完成", JBColor.GREEN, () -> start.setEnabled(true));
            } catch (AlertException e) {
                setAlert(e.getMessage(), JBColor.RED, () -> start.setEnabled(true));
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
    private void setAlert(String text, JBColor color, Runnable runnable) {
        alert.setForeground(color);
        alert.setText(text);
        executor.execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException ignored) {
            } finally {
                runnable.run();
                alert.setText("");
            }
        });
    }

    /**
     * 保存配置
     */
    private void saveConfig() {
        save.setEnabled(false);
        GenerateMybatisPlusCodeConfig gmc = new GenerateMybatisPlusCodeConfig();
        gmc.setDbUser(dbUser.getText());
        gmc.setDbPw(new String(dbPw.getPassword()));
        gmc.setDbUrl(dbUrl.getText());
        gmc.setDbName(dbName.getText());
        gmc.setPagName(pagName.getText());
        gmc.setModelName(modelName.getText());
        gmc.setChildModelName(childModelName.getText());
        gmc.setTable(table.getText());
        gmc.setOutPath(outPath.getText());
        ConfigUtilsKt.saveGenMpcLocalConfig(gmc);
        setAlert("保存成功", JBColor.GREEN, () -> save.setEnabled(true));
    }

    /**
     * 获取并设置本地配置
     */
    private void getAndSetConfig() {
        GenerateMybatisPlusCodeConfig gmc = ConfigUtilsKt.getGenMpcLocalConfig();
        if (gmc != null) {
            dbUser.setText(gmc.getDbUser());
            dbPw.setText(gmc.getDbPw());
            dbName.setText(gmc.getDbName());
            dbUrl.setText(gmc.getDbUrl());
            pagName.setText(gmc.getPagName());
            modelName.setText(gmc.getModelName());
            childModelName.setText(gmc.getChildModelName());
            table.setText(gmc.getTable());
            outPath.setText(gmc.getOutPath());
            changePag();
        }
    }

    /**
     * 改变其他包名
     */
    private void changePag() {
        String pag = pagName.getText();
        String model = modelName.getText().split(",")[0];
        String childModel = Optional.of(childModelName.getText())
                .filter(s -> s.length() > 0)
                .map(s -> "." + s)
                .orElse("");
        String domainText = Optional.ofNullable(domainName.getText())
                .filter(s -> s.length() > 0)
                .orElse(model);

        lastControllerPag = pag + ".%s.controller" + childModel;
        controllerPag.setText(String.format(lastControllerPag, model));

        lastServicePag = pag + ".%s.service" + childModel;
        servicePag.setText(String.format(lastServicePag, model));

        String mapperPagText = mapperPag.getText();
        if (!"".equals(mapperPagText)) {
            String[] pagName = mapperPagText.split(":");
            mapperEnd = pagName[1];
            String[] pagArr = pagName[0].split("\\.");
            mapperPagName = pagArr[pagArr.length - 1];
        }
        lastMapperPag = pag + ".%s." + mapperPagName + childModel + ":" + mapperEnd;
        mapperPag.setText(String.format(lastMapperPag, model));

        String domainPagText = domainPag.getText();
        if (!"".equals(domainPagText)) {
            if (domainPagText.contains(":")) {
                String[] pagName = domainPagText.split(":");
                domainEnd = pagName[1];
                String[] pagArr = pagName[0].split("\\.");
                domainPagName = pagArr[pagArr.length - 1];
            }
        }
        lastDomainPag = pag + ".%s." + domainPagName + childModel +
                Optional.of(domainEnd).filter(s -> s.length() > 0).map(s -> ":" + s).orElse("");
        domainPag.setText(String.format(lastDomainPag, domainText));
    }

    private void changeMapperPag(String pag, String childModel) {
        String mapperPagText = mapperPag.getText();
        if (!"".equals(mapperPagText)) {
            String[] pagName = mapperPagText.split(":");
            mapperEnd = pagName[1];
            String[] pagArr = pagName[0].split("\\.");
            mapperPagName = pagArr[pagArr.length - 1];
        }
        lastMapperPag = pag + ".%s." + mapperPagName + childModel + ":" + mapperEnd;
    }

}
