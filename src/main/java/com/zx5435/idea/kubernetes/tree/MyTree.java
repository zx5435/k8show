package com.zx5435.idea.kubernetes.tree;

import com.intellij.ide.util.treeView.AbstractTreeStructure;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import com.intellij.ui.tree.AsyncTreeModel;
import com.intellij.ui.tree.StructureTreeModel;
import com.intellij.ui.treeStructure.Tree;
import com.zx5435.idea.kubernetes.dom.res.ClusterNode;
import com.zx5435.idea.kubernetes.dom.res.ResNode;
import com.zx5435.idea.kubernetes.tree.descriptor.FolderDescriptor;
import com.zx5435.idea.kubernetes.tree.model.ResourceModelImpl;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Constructor;

/**
 * @author 913332
 */
@Slf4j
public class MyTree {

    @SneakyThrows
    public static Tree bindAction(Project project) {
        ResourceModelImpl root = new ResourceModelImpl();

        MyTreeStructure myTreeStructure = new MyTreeStructure(root);
        Constructor<StructureTreeModel> constructor = StructureTreeModel.class.getConstructor(AbstractTreeStructure.class, Disposable.class);
        StructureTreeModel<?> tm = constructor.newInstance(myTreeStructure, project);
        AsyncTreeModel treeModel = new AsyncTreeModel(tm, project);
        Tree tree = new Tree(treeModel);

//        treeModel.addTreeModelListener(new TreeModelListener() {
//            @Override
//            public void treeNodesChanged(TreeModelEvent treeModelEvent) {
//                log.warn("treeNodesChanged");
//            }
//
//            @Override
//            public void treeNodesInserted(TreeModelEvent treeModelEvent) {
//                // log.warn("t");
//            }
//
//            @Override
//            public void treeNodesRemoved(TreeModelEvent treeModelEvent) {
//                log.warn("treeNodesRemoved");
//            }
//
//            @Override
//            public void treeStructureChanged(TreeModelEvent treeModelEvent) {
//                log.warn("treeStructureChanged");
//            }
//        });

        tree.addTreeExpansionListener(new TreeExpansionListener() {
            @Override
            public void treeExpanded(TreeExpansionEvent event) {
                Object o = ((DefaultMutableTreeNode) event.getPath().getLastPathComponent()).getUserObject();
                if (o instanceof FolderDescriptor) {
                    ((FolderDescriptor<?>) o).watchUpdate();
                }
            }

            @Override
            public void treeCollapsed(TreeExpansionEvent event) {
                Object o = ((DefaultMutableTreeNode) event.getPath().getLastPathComponent()).getUserObject();
                if (o instanceof FolderDescriptor) {
                    ((FolderDescriptor<?>) o).stopWatchUpdate();
                }
            }
        });

        tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    myPopupEvent(e);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    myPopupEvent(e);
                }
            }

            void myPopupEvent(MouseEvent e) {
                super.mousePressed(e);
                int x = e.getX();
                int y = e.getY();
                JTree tree = (JTree) e.getSource();
                TreePath path = tree.getPathForLocation(x, y);
                if (path == null) {
                    return;
                }

                Object dom = path.getLastPathComponent();

                if (dom instanceof ResNode) {
                    JPopupMenu menu = ((ResNode) dom).getMenu();
                    if (menu != null) {
                        menu.show(tree, x, y);
                    }
                }
            }
        });

        tree.setModel(treeModel);
        tree.setRootVisible(false);
//        tree.setCellRenderer(new NodeRenderer());

        return tree;
    }

}
