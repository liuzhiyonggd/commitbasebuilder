package sysu.commitbasebuilder.dao;


import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import sysu.commitbasebuilder.bean.Line;


public class LineDAO {
	public static Document line2Document(Line line){
		Document obj = new Document();
		obj.put("line", line.getLine());
		obj.put("line_number", line.getLineNumber());
		
		return obj;
	}
	
	public static Line Document2Line(Document obj){
		Line line = new Line();
		line.setLine((String)obj.get("line"));
		line.setLineNumber((Integer)obj.get("line_number"));
		return line;
	}
	
	public static List<Document> lineList2DocumentList(List<Line> lineList){
		List<Document> dbList = new ArrayList<Document>();
		if(lineList!=null){
			for(Line line:lineList){
				Document obj = line2Document(line);
				dbList.add(obj);
			}
		}
		
		return dbList;
	}
	
	public static List<Line> DocumentList2LineList(List<Document> dbList){
		List<Line> lineList = new ArrayList<Line>();
		if(dbList!=null){
			for(Document obj:dbList){
				Line line = Document2Line(obj);
				lineList.add(line);
			}
		}
		
		return lineList;
	}
}
