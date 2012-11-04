package org.elasticsearch.module.suggest.test;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.commons.io.IOUtils;
import org.elasticsearch.action.admin.indices.exists.IndicesExistsResponse;
import org.elasticsearch.common.RandomStringGenerator;
import org.elasticsearch.common.collect.Lists;
import org.elasticsearch.common.collect.Maps;
import org.elasticsearch.common.logging.log4j.LogConfigurator;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.ImmutableSettings.Builder;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;

public class NodeContactTestHelper {
	public static Callable<Node> createNode(final String clusterName,
			final int numberOfShards) throws IOException {
		return new Callable<Node>() {
			@Override
			public Node call() throws Exception {
				Builder settingsBuilder = ImmutableSettings.settingsBuilder();
				String config = IOUtils.toString(NodeContactTestHelper.class
						.getResourceAsStream("/elasticsearch.yml"));
				settingsBuilder = settingsBuilder.loadFromSource(config);
				settingsBuilder.put("gateway.type", "none");
				settingsBuilder.put("cluster.name", clusterName);
				settingsBuilder.put("index.number_of_shards", numberOfShards);
				settingsBuilder.put("index.number_of_replicas", 0);
				LogConfigurator.configure(settingsBuilder.build());
				Node node = NodeBuilder.nodeBuilder()
						.settings(settingsBuilder.build()).node();
				return node;
			}
		};
	}

	public static void createIndexWithMapping(String index, Node node)
			throws IOException {
		IndicesExistsResponse existsResponse = node.client().admin().indices()
				.prepareExists(index).execute().actionGet();
		if (!existsResponse.exists()) {
			String mapping = IOUtils.toString(NodeContactTestHelper.class
					.getResourceAsStream("/contact.json"));
			node.client().admin().indices().prepareCreate(index).execute()
					.actionGet();
			node.client().admin().indices().preparePutMapping(index)
					.setType("contact").setSource(mapping).execute()
					.actionGet();
		}
	}

	public static List<Map<String, Object>> createContacts(int count) {
		List<Map<String, Object>> contacts = Lists.newArrayList();
		for (int i = 0; i < count; i++) {
			Map<String, Object> contact = Maps.newHashMap();
			contact.put("ContactName",
					RandomStringGenerator.randomAlphabetic(10));
			contact.put("Contactid",
					i + RandomStringGenerator.randomAlphanumeric(10));
			contacts.add(contact);
		}
		return contacts;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}