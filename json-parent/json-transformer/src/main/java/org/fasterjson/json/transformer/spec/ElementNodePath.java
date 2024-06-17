package org.fasterjson.json.transformer.spec;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ElementNodePath {
	private static final String ROOT="_root_";
	private static final String PATH_SEP = "\\/";
	private static final String NAME_SEP = "\\|";
	
	private String path;
	
	private ElementNodePath parent;
	
	private List<ElementNodePath> childern;
	
	public ElementNodePath(String path, ElementNodePath parent) {
		this.path = path;
		this.parent = parent;
	}
	
	public ElementNodePath() {
		this(ROOT, null);
	}
	
	public ElementNodePath(String[] pathes) {
		this();
		
	}
	
	public ElementNodePath addSubElementPath(String subPath) {
		ElementNodePath child = new ElementNodePath(subPath, this);
		addChild(child);
		return child;
	}
	
	private void addChild(ElementNodePath child) {
		if(childern == null) {
			childern = new LinkedList<>();
		}
		this.childern.add(child);
	}
	
	public boolean isRoot() {
		return this.parent == null;
	}
	
	public boolean hasChild() {
		return this.childern != null && !this.childern.isEmpty();
	}
	
	public String getPath() {
		return this.path;
	}
	
	public void addSpecPath(String specPath) {
		if(specPath == null || specPath.isEmpty()) {
			return;
		}
		String[] pathes = specPath.split(PATH_SEP);
		List<ElementNodePath> parents = new ArrayList<>();
		List<ElementNodePath> newParents = new ArrayList<>();
		newParents.add(this);
		
		for(String path : pathes) {
			String[] names = path.split(NAME_SEP);
			
			parents.clear();
			parents.addAll(newParents);
			newParents.clear();
			
			for(String name : names) {
				for(ElementNodePath parent : parents) {
					ElementNodePath child = parent.addSubElementPath(name);
					newParents.add(child);
				}
			}
		}
	}

	public List<ElementNodePath> getChildern() {
		return childern;
	}

	@Override
	public String toString() {
		return "ElementPath [path=" + path + "]";
	}

	public static void main(String argv[]) {
		String rawLhsStr = "p11|p12/p21|p22/p31|p32|p33";
	
		ElementNodePath root = new ElementNodePath();
		root.addSpecPath(rawLhsStr);
		
		rawLhsStr = "p13/p23|p24/p35";
		root.addSpecPath(rawLhsStr);
	}
}
