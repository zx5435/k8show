package com.zx5435.idea.kubernetes.node.volume;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.JBMenuItem;
import com.intellij.openapi.ui.JBPopupMenu;
import com.zx5435.idea.kubernetes.node.ATreeNode;
import io.fabric8.kubernetes.api.model.storage.StorageClass;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;

/**
 * @author zx5435
 */
@Slf4j
public class StorageClassNode extends ATreeNode {

    StorageClass ins;

    public StorageClassNode(StorageClass ns) {
        this.ins = ns;
    }

    @Override
    public String getLabel() {
        return ins.getMetadata().getName();
    }

    @Override
    public Icon getIcon() {
        return AllIcons.Actions.GroupByClass;
    }

    @Override
    public JBPopupMenu getMenu(Project project) {
        JBPopupMenu menu = new JBPopupMenu();

        JBMenuItem b1 = new JBMenuItem("Load", AllIcons.Actions.Show);
        b1.addActionListener(e -> {
            log.warn("todo");
        });
        menu.add(b1);

        JBMenuItem b2 = new JBMenuItem("Delete", AllIcons.Actions.Close);
        b2.addActionListener(e -> {
            log.warn("todo");
        });
        menu.add(b2);

        return menu;
    }

}
