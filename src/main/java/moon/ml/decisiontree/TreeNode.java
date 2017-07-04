package moon.ml.decisiontree;

import java.util.List;

public class TreeNode {
	private String name;
	private Integer index;
	//父节点
	private TreeNode parent;
	//指向父节点的属性
    private String parentAttribute;
    //属性集合
    private List<String> attributes;
    private List<TreeNode> children;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getIndex() {
		return index;
	}
	public void setIndex(Integer index) {
		this.index = index;
	}
	public TreeNode getParent() {
		return parent;
	}
	public void setParent(TreeNode parent) {
		this.parent = parent;
	}
	public String getParentAttribute() {
		return parentAttribute;
	}
	public void setParentAttribute(String parentAttribute) {
		this.parentAttribute = parentAttribute;
	}
	public List<String> getAttributes() {
		return attributes;
	}
	public void setAttributes(List<String> attributes) {
		this.attributes = attributes;
	}
	public List<TreeNode> getChildren() {
		return children;
	}
	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}
	@Override
	public String toString() {
		return "TreeNode [name=" + name + ", parentAttribute=" + parentAttribute + "]";
	}
    
    
}
