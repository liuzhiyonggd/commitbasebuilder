package sysu.commitbasebuilder.db;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * 连接工厂类，获得commit集合和class集合的对象
 * @author zhiyong
 *
 */
public class ConnectionFactory {
	
	private static MongoDatabase db = new MongoClient("localhost",27018).getDatabase("commitbase");
	private static MongoCollection<Document> classCollection = db.getCollection("class_message2");
	private static MongoCollection<Document> classMaxIDCollection = db.getCollection("classmaxid");
	private static MongoCollection<Document> commitCollection = db.getCollection("commit");
	
	
	public static MongoCollection<Document> getClassCollection(){
		if(classCollection!=null){
			return classCollection;
		}else{
			return db.getCollection("class_message2");
		}
	}
	
	public static MongoCollection<Document> getClassMaxId() {
		if(classMaxIDCollection==null) {
			classMaxIDCollection = db.getCollection("classmaxid");
		}
		return classMaxIDCollection;
	}
	
	public static MongoCollection<Document> getCommitCollection(){
		if(commitCollection!=null){
			return commitCollection;
		}else{
			return db.getCollection("commit");
		}
	}
	
	public static MongoDatabase getDB(){
		if(db!=null){
			return db;
		}else{
			return new MongoClient("localhost",27018).getDatabase("commitbase");
		}
	}

}
