package sysu.commitbasebuilder.dao;


import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import sysu.commitbasebuilder.bean.DiffType;


public class DiffTypeDAO {
	
	public static Document diffType2Document(DiffType diff){
		Document obj = new Document();
		
		obj.put("type", diff.getType());
		obj.put("new_start_line", diff.getNewStartLine());
		obj.put("new_end_line", diff.getNewEndLine());
		obj.put("old_start_line", diff.getOldStartLine());
		obj.put("old_end_line", diff.getOldEndLine());
		obj.put("new_hashs", diff.getNewHashList());
		obj.put("old_hashs", diff.getOldHashList());
		obj.put("new_keywords", diff.getNewKeywordList());
		obj.put("old_keywords", diff.getOldKeywordList());
		obj.put("new_tokens", TokenDAO.tokenList2DocumentList(diff.getNewTokenList()));
		obj.put("old_tokens", TokenDAO.tokenList2DocumentList(diff.getOldTokenList()));
		return obj;
	}
	
	public static DiffType Document2DiffType(Document obj){
		DiffType diff = new DiffType();
		diff.setType((String)obj.get("type"));
		diff.setNewStartLine((Integer)obj.get("new_start_line"));
		diff.setNewEndLine((Integer)obj.get("new_end_line"));
		diff.setOldStartLine((Integer)obj.get("old_start_line"));
		diff.setOldEndLine((Integer)obj.get("old_end_line"));
		diff.setNewHashList((List<Long>)obj.get("new_hashs"));
		diff.setOldHashList((List<Long>)obj.get("old_hashs"));
		diff.setNewKeywordList((List<String>)obj.get("new_keywords"));
		diff.setOldKeywordList((List<String>)obj.get("old_keywords"));
		diff.setNewTokenList(TokenDAO.DocumentList2TokenList((List<Document>)obj.get("new_tokens")));
		diff.setOldTokenList(TokenDAO.DocumentList2TokenList((List<Document>)obj.get("old_tokens")));
		return diff;
	}
	
	public static List<Document> diffTypeList2DocumentList(List<DiffType> diffList){
		List<Document> dbList = new ArrayList<Document>();
		if(diffList!=null){
			for(DiffType diff:diffList){
				Document obj = diffType2Document(diff);
				dbList.add(obj);
			}
		}
		
		return dbList;
	}
	
	public static List<DiffType> DocumentList2DiffTypeList(List<Document> dbList){
		List<DiffType> diffList = new ArrayList<DiffType>();
		if(dbList!=null){
			for(Document obj:dbList){
				DiffType diff = Document2DiffType(obj);
				diffList.add(diff);
			}
		}
		return diffList;
	}

}
