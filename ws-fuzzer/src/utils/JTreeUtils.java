/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package utils;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**
 *
 * @author chang
 */
public class JTreeUtils {

    public static void expandAll(JTree tree, TreePath path){
        
        DefaultMutableTreeNode node = null;
        
        if(path == null){
            node = (DefaultMutableTreeNode)((DefaultTreeModel)tree.getModel()).getRoot();
        }else{
            node = (DefaultMutableTreeNode)path.getLastPathComponent();
        }
        
        if(!node.isLeaf()){
            tree.expandPath(path);
            for(int i=0; i<node.getChildCount(); i++){
                expandAll(tree, new TreePath(((DefaultMutableTreeNode)node.getChildAt(i)).getPath()));
            }
        }
    }
}
