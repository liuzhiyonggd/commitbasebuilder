package sysu.commitbasebuilder.dao;


import java.util.List;

import org.bson.Document;

import com.mongodb.client.MongoCollection;

import sysu.commitbasebuilder.bean.ClassMessageBean;
import sysu.commitbasebuilder.bean.Code;
import sysu.commitbasebuilder.bean.CodeComment;
import sysu.commitbasebuilder.bean.DiffType;
import sysu.commitbasebuilder.bean.Line;
import sysu.commitbasebuilder.bean.Token;
import sysu.commitbasebuilder.db.ConnectionFactory;

public class ClassMessageDAO {
	
	public static Document classMessage2Document(ClassMessageBean classMessage){
		Document obj = new Document();
		obj.put("class_id", classMessage.getClassId());
		obj.put("project", classMessage.getProject());
		obj.put("commit_id", classMessage.getCommitID());
		obj.put("class_name", classMessage.getClassName());
		
		obj.put("type", classMessage.getType());
		
		obj.put("old_code", LineDAO.lineList2DocumentList(classMessage.getCode().getOldLines()));
		obj.put("new_code", LineDAO.lineList2DocumentList(classMessage.getCode().getNewLines()));
		
		obj.put("new_tokens", TokenDAO.tokenList2DocumentList(classMessage.getNewTokenList()));
		obj.put("old_tokens", TokenDAO.tokenList2DocumentList(classMessage.getOldTokenList()));
		
		obj.put("diffs", DiffTypeDAO.diffTypeList2DocumentList(classMessage.getDiffList()));
		
		obj.put("new_comments", CodeCommentDAO.codeCommentList2DocumentList(classMessage.getNewComment()));
		obj.put("old_comments", CodeCommentDAO.codeCommentList2DocumentList(classMessage.getOldComment()));
		
		obj.put("iscore_probability", classMessage.getIsCoreProbability());
		
		return obj;
	}
	
	public static ClassMessageBean Document2ClassMessage(Document obj){
		ClassMessageBean classMessage = new ClassMessageBean();
		
		classMessage.setProject((String)obj.get("project"));
		classMessage.setCommitID((String)obj.get("commit_id"));
		classMessage.setClassName((String)obj.get("class_name"));
		classMessage.setType((String)obj.get("type"));
		Code code = new Code();
		List<Line> newCode = LineDAO.DocumentList2LineList((List<Document>)obj.get("new_code"));
		List<Line> oldCode = LineDAO.DocumentList2LineList((List<Document>)obj.get("old_code"));
		code.setNewLines(newCode);
		code.setOldLines(oldCode);
		classMessage.setCode(code);
		
		List<Token> newTokens = TokenDAO.DocumentList2TokenList((List<Document>)obj.get("new_tokens"));
		List<Token> oldTokens = TokenDAO.DocumentList2TokenList((List<Document>)obj.get("old_tokens"));
		
		classMessage.setNewTokenList(newTokens);
		classMessage.setOldTokenList(oldTokens);
		
		List<DiffType> diffList = DiffTypeDAO.DocumentList2DiffTypeList((List<Document>)obj.get("diffs"));
		classMessage.setDiffList(diffList);
		
		List<CodeComment> newComments = CodeCommentDAO.DocumentList2CodeCommentList((List<Document>)obj.get("new_comments"));
		List<CodeComment> oldComments = CodeCommentDAO.DocumentList2CodeCommentList((List<Document>)obj.get("old_comments"));
		
		classMessage.setNewComment(newComments);
		classMessage.setOldComment(oldComments);
		
		return classMessage;
	}
	
	public static void insertOne(ClassMessageBean classMessage){
		MongoCollection<Document> classMessages = ConnectionFactory.getClassCollection();
		int maxId = getClassMaxId();
		Document DBClassMessage = classMessage2Document(classMessage);
		DBClassMessage.put("class_id", maxId);
		classMessages.insertOne(DBClassMessage);
		
		
	}
	
	public static void update(String project,int commitID,String classname,ClassMessageBean classMessage){
		MongoCollection<Document> classMessages = ConnectionFactory.getClassCollection();
		
		Document DBClassMessage = classMessage2Document(classMessage);
		Document query = new Document();
		query.put("project", project);
		query.put("commit_id", commitID);
		query.put("class_name",classname);
		classMessages.updateOne(query, DBClassMessage);
		
	}
	
	public static int getClassMaxId() {
		MongoCollection<Document> classMaxIDCollection = ConnectionFactory.getClassMaxId();
		Document doc = classMaxIDCollection.find().first();
		int maxId;
		if(doc.get("maxid") instanceof Double) {
			double id = doc.getDouble("maxid");
			maxId = (int)id;
		}else {
			maxId = doc.getInteger("maxid");
		}
		maxId++;
		doc.put("maxid", maxId);
		classMaxIDCollection.findOneAndReplace(new Document(),doc);
		return maxId;
	}	
	
}
