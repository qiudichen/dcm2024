package com.demo.lib.generator.graph.common;

import java.util.List;
import java.util.Set;

public interface Graph extends Scorable {
	public Set<Integer> getStartNodes();
    public List<Integer> getEdgesBy(int index);
    public boolean hasChild(int index);
    public boolean isEndNode(Integer index);
    public Node getNodeBy(int index);
    public Node[] getNodes();
}
