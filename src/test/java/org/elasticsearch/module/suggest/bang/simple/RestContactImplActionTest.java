package org.elasticsearch.module.suggest.bang.simple;

import static org.elasticsearch.module.suggest.bang.simple.ContactTestHelper.createContacts;
import static org.elasticsearch.module.suggest.bang.simple.ContactTestHelper.indexContacts;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.common.xcontent.json.JsonXContent;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import static org.elasticsearch.index.query.FilterBuilders.*;
import static org.elasticsearch.index.query.QueryBuilders.*;
@RunWith(value = Parameterized.class)
public class RestContactImplActionTest extends AbstractContactTest {
	private final AsyncHttpClient httpClient = new AsyncHttpClient();

	public RestContactImplActionTest(int shards, int nodeCount)
			throws Exception {
		super(shards, nodeCount);
		// TODO Auto-generated constructor stub
	}

	@After
	public void closeHttpClient() {
		httpClient.close();
	}
	
	
	
	
	
	

	
	
	
	@Override
	public List<String> getSuggestions(String index, String field, String term,
			Integer size) throws IllegalArgumentException,
			InterruptedException, ExecutionException, IOException {
		// TODO Auto-generated method stub
		return getSuggestions(index, field, term, size, null);
	}

	public List<String> getSuggestions(String index, String field, String term,
			Integer size, Float similarity) throws IllegalArgumentException,
			InterruptedException, ExecutionException, IOException {
		String json = createJSONQuery(field, term, size, similarity);
		String url = "http://localhost:9200/" + index + "/contact/_suggest";
		Response r = httpClient.preparePost(url).setBody(json).execute().get();
		assertThat(r.getStatusCode(), is(200));
		
		SearchResponse response = node.client().prepareSearch("ContactName")
//		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setQuery(termQuery("multi", "b"))
		        .setFrom(0).setSize(60).setExplain(true)
		        .execute()
		        .actionGet();
		
		System.out.println("---------------"+response.getHits().getTotalHits());
		
		
		
		
		// System.out.println("REQ : " + json);
		// System.out.println("RESP: " + r.getResponseBody());

		return getSuggestionsFromResponse(r.getResponseBody());
	}


	
	@SuppressWarnings("unchecked")
	private List<String> getSuggestionsFromResponse(String response)
			throws IOException {
		XContentParser parser = JsonXContent.jsonXContent
				.createParser(response);
		Map<String, Object> jsonResponse = parser.map();
		assertThat(jsonResponse, hasKey("suggestions"));
		return (List<String>) jsonResponse.get("suggestions");
	}

	private String createJSONQuery(String field, String term, Integer size,
			Float similarity) {
		StringBuilder query = new StringBuilder("{");
		query.append(String.format("\"field\": \"%s\", ", field));
		query.append(String.format("\"term\": \"%s\"", term));
		if (size != null) {
			query.append(String.format(", \"size\": \"%s\"", size));
		}
		if (similarity != null && similarity > 0.0 && similarity < 1.0) {
			query.append(String.format(", \"similarity\": \"%s\"", similarity));
		}
		query.append("}");

		return query.toString();
	}
	
	



}
