package org.elasticsearch.module.suggest.bang.test;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import junit.framework.Assert;

import org.elasticsearch.action.admin.indices.refresh.RefreshRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.node.Node;

public class ContactTestHelper {
	
	
	static void indexContacts(List<Map<String,Object>> contacts, Node node) throws Exception {
		indexContacts(contacts, "contacts",node);
	}
	static void indexContacts(List<Map<String,Object>> contacts, String index,Node node   ) throws InterruptedException, ExecutionException{
		long currentCount ;
	BulkRequest bulkRequest = new BulkRequest ();
	for (Map<String, Object> contact : contacts ) {
		IndexRequest indexRequest = new IndexRequest(index, "contact", (String) contact.get("ContactId"));
		indexRequest.source(contact);
		bulkRequest.add(indexRequest);
	}
	BulkResponse response = node.client().bulk(bulkRequest).actionGet();
	if (response.hasFailures()){
		Assert.fail("Error in creating contacts" + response.buildFailureMessage());
	}
	refreshIndex(index,node);
	}
	private static void refreshIndex(String index, Node node) throws InterruptedException, ExecutionException {
		node.client().admin().indices().refresh(new RefreshRequest(index)).get();
		
	}

}
