/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package utils;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

/**
 *
 * @author chang
 */
public class JTreeUtils {

    public static void expandAll(JTree tree, TreePath path){
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
        if(!node.isLeaf()){
            tree.expandPath(path);
            for(int i=0; i<node.getChildCount(); i++){
                expandAll(tree, new TreePath(((DefaultMutableTreeNode)node.getChildAt(i)).getPath()));
            }
        }
    }
}
