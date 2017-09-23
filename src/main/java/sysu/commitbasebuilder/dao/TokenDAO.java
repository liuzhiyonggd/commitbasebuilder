package sysu.commitbasebuilder.dao;


import java.util.ArrayList;
import java.util.List;

import org.bson.Document;


import sysu.commitbasebuilder.bean.Token;


public class TokenDAO {
	public static Document token2Document(Token token){
		Document obj = new Document();
		obj.put("token_name", token.getTokenName());
		obj.put("keyword", token.getKeyword());
		obj.put("start_line", token.getStartLine());
		obj.put("end_line", token.getEndLine());
		obj.put("hash_number", token.getHashNumber());
		return obj;
	}
	
	public static Token Document2Token(Document obj){
		Token token = new Token();
		
		token.setTokenName((String)obj.get("token_name"));
		token.setKeyword((String)obj.get("keyword"));
		token.setStartLine((Integer)obj.get("start_line"));
		token.setEndLine((Integer)obj.get("end_line"));
		token.setHashNumber((Long)obj.get("hash_number"));

		return token;
	}
	
	public static List<Document> tokenList2DocumentList(List<Token> tokenList){
		List<Document> dbList = new ArrayList<Document>();
		if(tokenList!=null){
			for(Token token : tokenList){
				Document obj = token2Document(token);
				dbList.add(obj);
			}
		}
		
		return dbList;
	}
	
	public static List<Token> DocumentList2TokenList(List<Document> dbList){
		List<Token> tokenList = new ArrayList<Token>();
		if(dbList!=null){
			for(Document obj:dbList){
				Token token = Document2Token(obj);
				tokenList.add(token);
			}
		}
		return tokenList;
		
	}
}
