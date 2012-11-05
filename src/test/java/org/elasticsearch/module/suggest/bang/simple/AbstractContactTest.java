package org.elasticsearch.module.suggest.bang.simple;
import static org.elasticsearch.module.suggest.bang.simple.ContactTestHelper.*;
import static org.elasticsearch.module.suggest.bang.simple.NodeContactTestHelper.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.elasticsearch.common.collect.Lists;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.node.Node;
import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import  static  org.elasticsearch.common.settings.ImmutableSettings.*;
//import org.elasticsearch.module.suggest.test.NodeContactTestHelper.*;

public abstract class AbstractContactTest {

	protected final String clusterName = "SuggestTest_" + Math.random();
	protected Node node;
	protected List<Node> nodes = Lists.newArrayList();
	public static final String DEFAULT_INDEX = "contacts";

	@Parameters
	public static Collection<Object[]> data() {
		// first argument: number of shards, second argument: number of nodes
		// Object[][] data = new Object[][] { { 1,1 } };
//		Object[][] data = new Object[][] { { 1, 1 }, { 1, 2 }, { 1, 3 },
//				{ 1, 4 } };
//		Object[][] data = new Object[][] { { 1, 1 }, { 2, 1 }, { 3, 1 },
//        { 4, 1 } };
		
//	  Object[][] data = new Object[][] { { 1, 1 }, { 4, 1 }, { 10, 1 },
//        { 4, 4 } };
		
		Object[][] data = new Object[][] { { 1, 1 } };
		return Arrays.asList(data);
	}

	public AbstractContactTest(int shards, int nodeCount)
			throws Exception {
	  
	  
//	  Builder settings = ImmutableSettings.settingsBuilder();
//	  settings.build();
//	  NodeBuilder nBuilder = node
	  
		ExecutorService executor = Executors.newFixedThreadPool(nodeCount);
		List<Future<Node>> nodeFutures = Lists.newArrayList();
		for (int i = 0; i < nodeCount; i++) {
			Future<Node> nodeFuture = executor.submit(createNode(clusterName,
					shards));
			nodeFutures.add(nodeFuture);
		}
		for (Future<Node> nodeFuture : nodeFutures) {
			nodes.add(nodeFuture.get());
		}
		node = nodes.get(0);
		createIndexWithMapping("contacts", node);
	}

	@Test
	public void testThatSimpleSuggestionWorks() throws Exception {
		List<Map<String, Object>> contacts = createContacts(4);
		contacts.get(0).put("ContactName", "bang");
		contacts.get(1).put("ContactName", "bao");
		contacts.get(2).put("ContactName", "bang");
		contacts.get(3).put("ContactName", "huong");
		indexContacts(contacts, node);
		List<String> suggestions = getSuggestions("_all", "ba", 10);
		assertThat(suggestions.toString(), suggestions, hasSize(2));
	}
	

	private List<String> getSuggestions(String field, String term, Integer size)
			throws Exception {
		return getSuggestions(DEFAULT_INDEX, field, term, size);
	}

	abstract public List<String> getSuggestions(String index, String field,
			String term, Integer size) throws Exception;
	

}
