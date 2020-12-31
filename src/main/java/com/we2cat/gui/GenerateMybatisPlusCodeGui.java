package com.we2cat.gui;

import com.intellij.openapi.project.Project;
import com.intellij.ui.JBColor;
import com.we2cat.codeauto.AutoCode;
import com.we2cat.entity.GenerateMybatisPlusCodeConfig;
import com.we2cat.exception.AlertException;
import com.we2cat.utils.ConfigUtilsKt;
import com.we2cat.utils.StringUtilsKt;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    /**
     * 生成作者
     */
    private JTextField author;

    private static final int WIDTH = 600;

    private static final int HEIGHT = 440;

    private final Project project;

    /**
     * 持久层包名
     */
    private String mapperPagName = ".mapper";

    /**
     * 持久层后缀
     */
    private String mapperEnd = "Mapper";

    /**
     * 领域层包名
     */
    private String domainPagName = ".entity";

    /**
     * 领域层后缀
     */
    private String domainEnd = "";

    private String lastControllerPag = null;
    private String lastServicePag = null;
    private String lastMapperPag = null;
    private String lastDomainPag = null;

    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    public GenerateMybatisPlusCodeGui(Project project) {
        this.project = project;
        setListener();
        getAndSetConfig();
        setDefaultValue();
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
        domainName.addCaretListener(e -> changePag());
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
        this.author.setName("生成作者");
        projectName.setText(project.getName());
        if (outPath.getText().length() == 0) {
            String dir = ConfigUtilsKt.isWindows() ? "D:/tmp"
                    : StringUtilsKt.join(ConfigUtilsKt.getUserDir(), "/tmp");
            outPath.setText(dir);
        }
    }

    /**
     * 开始生成代码
     */
    private void start() {
        start.setEnabled(false);
        executor.execute(() -> {
            try {
                for (String appName : getText(modelName).split(",")) {
                    String model = Optional.ofNullable(domainName.getText())
                            .filter(s -> s.length() > 0)
                            .orElse(appName);
                    String servicePag = StringUtilsKt.join(appName, lastServicePag);
                    String mapperPag = StringUtilsKt.join(appName, lastMapperPag);
                    AutoCode.of(getText(pagName))
                            .setAuthor(getText(author))
                            .setDeleteName("deleted")
                            .setTable(Optional.ofNullable(table.getText())
                                    .filter(t -> t.length() > 0)
                                    .map(t -> t.split(","))
                                    .orElse(null))
                            .setDbUser(getText(dbUser))
                            .setOutPath(StringUtilsKt.join(getText(outPath), "/", project.getName()))
                            .setDbUrl(getText(dbUrl))
                            .setDbPw(getText(dbPw))
                            .setDbName(getText(dbName))
                            .setFileOverride(true)
                            .setControllerPag(StringUtilsKt.join(appName, lastControllerPag))
                            .setServicePag(servicePag)
                            .setServiceImplPag(StringUtilsKt.join(servicePag, ".impl"))
                            .setDaoEnd(mapperEnd)
                            .setDaoPag(mapperPag)
                            .setMapperPag(StringUtilsKt.join(mapperPag, ".xml"))
                            .setEntityPag(StringUtilsKt.join(model, lastDomainPag))
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
                Thread.sleep(2500);
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
        gmc.setAuthor(author.getText());
        gmc.setDomainName(domainName.getText());
        gmc.setMapperPagName(mapperPagName);
        gmc.setMapperEnd(mapperEnd);
        gmc.setDomainPagName(domainPagName);
        gmc.setDomainEnd(domainEnd);
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
            author.setText(gmc.getAuthor());
            domainName.setText(gmc.getDomainName());
            mapperPagName = StringUtilsKt.getOrElse(gmc.getMapperPagName(), mapperPagName);
            mapperEnd = StringUtilsKt.getOrElse(gmc.getMapperEnd(), mapperEnd);
            domainPagName = StringUtilsKt.getOrElse(gmc.getDomainPagName(), domainPagName);
            domainEnd = StringUtilsKt.getOrElse(gmc.getDomainEnd(), domainEnd);
        }
        changePag();
    }

    /**
     * 改变其他包名
     */
    private void changePag() {
        String pag = pagName.getText();
        String model = modelName.getText().split(",")[0];
        String childModel = getChildModel();
        StringBuilder sbModel = new StringBuilder().append(pag).append(".");

        lastDomainPag = StringUtilsKt.join(domainPagName, childModel);
        domainPag.setText(StringUtilsKt.join(sbModel.toString(), StringUtilsKt.getOrElse(domainName.getText(), model),
                lastDomainPag));

        String modelBeforeUnify = sbModel.append(model).toString();

        lastControllerPag = StringUtilsKt.join(".controller", childModel);
        controllerPag.setText(StringUtilsKt.join(modelBeforeUnify, lastControllerPag));

        lastServicePag = StringUtilsKt.join(".service", childModel);
        servicePag.setText(StringUtilsKt.join(modelBeforeUnify, lastServicePag));

        lastMapperPag = StringUtilsKt.join(mapperPagName, childModel);
        mapperPag.setText(StringUtilsKt.join(modelBeforeUnify, lastMapperPag));
    }

    /**
     * 获得子模块
     *
     * @return 子模块
     */
    private String getChildModel() {
        return Optional.ofNullable(childModelName.getText())
                .filter(s -> s.length() > 0)
                .map(s -> "." + s)
                .orElse("");
    }

}
