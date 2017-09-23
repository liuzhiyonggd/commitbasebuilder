package sysu.commitbasebuilder.dao;


import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import sysu.commitbasebuilder.bean.CodeComment;
import sysu.commitbasebuilder.bean.CommentType;


public class CodeCommentDAO {
	
	public static Document codeComment2Document(CodeComment ccomment){
		Document obj = new Document();
		
		String type="";
		if(ccomment.getType().equals(CommentType.Javadoc)){
			type = "Javadoc";
		}else if(ccomment.getType().equals(CommentType.Block)){
			type = "Block";
		}else if(ccomment.getType().equals(CommentType.Line)){
			type = "line";
		}
		
		obj.put("type", type);
		obj.put("start_line", ccomment.getStartLine());
		obj.put("end_line", ccomment.getEndLine());
		
		return obj;
	}
	
	public static CodeComment Document2CodeComment(Document obj){
		CodeComment ccomment = new CodeComment();
		String type = (String)obj.get("type");
		CommentType cType=null;
		if(type.equals("Javadoc")){
			cType = CommentType.Javadoc;
		}else if(type.equals("Block")){
			cType = CommentType.Block;
		}else if(type.equals("line")){
			cType = CommentType.Line;
		}
		ccomment.setType(cType);
		ccomment.setStartLine((Integer)obj.get("start_line"));
		ccomment.setEndLine((Integer)obj.get("end_line"));
		
		return ccomment;

	}
	
	public static List<Document> codeCommentList2DocumentList(List<CodeComment> commentList){
		
		List<Document> dbList = new ArrayList<Document>();
		if(commentList!=null){
			for(CodeComment ccomment:commentList){
				Document obj = codeComment2Document(ccomment);
				dbList.add(obj);
			}
		}
		
		return dbList;
	}
	
	public static List<CodeComment> DocumentList2CodeCommentList(List<Document> dbList){
		List<CodeComment> commentList = new ArrayList<CodeComment>();
		if(dbList!=null){
			for(Document obj : dbList){
				CodeComment ccomment = Document2CodeComment(obj);
				commentList.add(ccomment);
			}
		}
		return commentList;
	}

}
