package com.rigai.rigeye.util;

import com.alibaba.fastjson.JSONObject;
import com.rigai.rigeye.common.ApolloConfig;
import org.apache.http.HttpHost;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;
import java.io.Serializable;

/**
 * @author yh
 * @date 2018/8/30 15:12
 */
public enum  ElasticUtil implements Serializable {
    INSTANCE;

    public void writeDivideDetailToEs(String indices, String type, Dataset<Row> data){
        String es = ApolloConfig.INSTANCE.getEsIp();
        int port = ApolloConfig.INSTANCE.getEsPort();
        data.toJSON().foreachPartition(t -> {
            RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost(es,port)));
            BulkRequest bulkRequest = new BulkRequest();
            t.forEachRemaining(s -> {
                try {
                    JSONObject jsonObject = JSONObject.parseObject(s);
                    bulkRequest.add(new IndexRequest(indices, type).source(jsonObject));
                }catch (Exception e){
                    System.out.println("error "+e);
                }
            });
            try {
                if (bulkRequest.requests().size() > 0 ) {
                    client.bulk(bulkRequest);
                }
            } catch (IOException e) {
                System.out.println("error "+e);
                //保存错误，说明mapping不正确，删除原索引，重建索引
                DeleteIndexRequest deleteRequest = new DeleteIndexRequest(indices);
                try {
                    client.indices().deleteIndex(deleteRequest);
                    //重新保存新数据。
                    client.bulk(bulkRequest);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }finally {
                //最后关闭连接，一定要关闭。
                client.close();
            }
        });
    }
}
