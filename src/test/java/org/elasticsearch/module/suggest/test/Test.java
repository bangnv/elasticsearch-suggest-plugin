package org.elasticsearch.module.suggest.test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.elasticsearch.common.collect.Lists;
import org.elasticsearch.node.Node;
import org.junit.runners.Parameterized.Parameters;

public class Test {
	
	
protected final String clusterName="bang";
protected Node node;
protected List<Node> nodes = Lists.newArrayList();
public static final String DEFAULT_INDEX="persons";

@Parameters
public static Collection<Object[]> data(){
	Object[][] data = new Object[][] { { 1, 1 }, { 4, 1 }, { 10, 1 }, { 4, 4 } };
    return Arrays.asList(data);
}




























}
