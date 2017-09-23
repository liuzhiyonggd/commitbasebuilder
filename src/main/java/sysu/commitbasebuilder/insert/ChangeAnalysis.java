package sysu.commitbasebuilder.insert;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import ch.uzh.ifi.seal.changedistiller.ChangeDistiller;
import ch.uzh.ifi.seal.changedistiller.ChangeDistiller.Language;
import ch.uzh.ifi.seal.changedistiller.ast.FileUtils;
import ch.uzh.ifi.seal.changedistiller.distilling.FileDistiller;
import ch.uzh.ifi.seal.changedistiller.model.classifiers.ChangeType;
import ch.uzh.ifi.seal.changedistiller.model.entities.Delete;
import ch.uzh.ifi.seal.changedistiller.model.entities.Insert;
import ch.uzh.ifi.seal.changedistiller.model.entities.Move;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;
import ch.uzh.ifi.seal.changedistiller.model.entities.Update;
import sysu.commitbasebuilder.bean.DiffType;
import sysu.commitbasebuilder.bean.Token;


public class ChangeAnalysis {
	
	
	public static List<DiffType> changeDistill(String file1, String file2,List<Token> newTokenList,List<Token> oldTokenList) throws Exception {
		
		List<DiffType> diffList = new ArrayList<DiffType>();
		File left = new File(file1);
		File right = new File(file2);
		if(!left.exists() || !right.exists()){
			System.err.println("File do not exists:");
		}
		
		FileDistiller distiller = ChangeDistiller.createFileDistiller(Language.JAVA);
		try {
		    distiller.extractClassifiedSourceCodeChanges(left, right);
		} catch(Exception e) {
		    /* An exception most likely indicates a bug in ChangeDistiller. Please file a
		       bug report at https://bitbucket.org/sealuzh/tools-changedistiller/issues and
		       attach the full stack trace along with the two files that you tried to distill. */
		    System.out.println("Warning: error while change distilling. " + e.getMessage());
		    System.out.println(file1);
		    System.out.println(file2);
		}

		List<SourceCodeChange> changes = distiller.getSourceCodeChanges();
		if(changes != null) {
		    for(SourceCodeChange change : changes) {
		    	
		    	//�仯�����λ��
		    	int oldStart = -1,oldEnd = -1,newStart = -1,newEnd = -1;
		    	
		    	//�仯���������
		    	int newStartLine = 0, newEndLine = 0,oldStartLine = 0,oldEndLine=0;
		    	
		    	
		    	
		    	//���������ģ���ֻ��¼��������
		    	if (change instanceof Insert) {
		    		Insert insert = (Insert)change;
		    		newStart = insert.getChangedEntity().getStartPosition();
		    		newEnd = insert.getChangedEntity().getEndPosition();
		    		newStartLine = getLineNumber(file2, newStart);
		    		newEndLine = getLineNumber(file2, newEnd);
		    		
		    	} 
		    	//���Ǹı�ģ������ļ��;��ļ��ı仯������ж�Ҫ��¼
		    	else if (change instanceof Update){
		    		Update update = (Update)change;
		    		newStart = update.getNewEntity().getStartPosition();
		    		newEnd = update.getNewEntity().getEndPosition();
		    		newStartLine = getLineNumber(file2, newStart);
		    		newEndLine = getLineNumber(file2, newEnd);
		    		
		    		oldStart = update.getChangedEntity().getStartPosition();
		    		oldEnd = update.getChangedEntity().getEndPosition();
		    		oldStartLine = getLineNumber(file1,oldStart);
		    		oldEndLine = getLineNumber(file1,oldEnd);
		    	
		    	}
		    	//����ɾ���ģ���ֻ��¼���ļ�����
		    	else if(change instanceof Delete){
		    		Delete delete = (Delete)change;
		    		oldStart = delete.getChangedEntity().getStartPosition();
		    		oldEnd = delete.getChangedEntity().getEndPosition();
		    		oldStartLine = getLineNumber(file1,oldStart);
		    		oldEndLine = getLineNumber(file1,oldEnd);
		    	}
		    	//�����ƶ��ģ������ļ��;��ļ��ı仯������ж�Ҫ��¼
		    	else if( change instanceof Move){
		    		Move move = (Move)change;
		    		newStart = move.getNewEntity().getStartPosition();
		    		newEnd = move.getNewEntity().getEndPosition();
		    		newStartLine = getLineNumber(file2, newStart);
		    		newEndLine = getLineNumber(file2, newEnd);
		    		
		    		oldStart = move.getChangedEntity().getStartPosition();
		    		oldEnd = move.getChangedEntity().getEndPosition();
		    		oldStartLine = getLineNumber(file1,oldStart);
		    		oldEndLine = getLineNumber(file1,oldEnd);
		    	} else {
		    		System.out.println("Error no type");
		    	}
		    	
		    	//���仯ʵ����ӵ������б���
		    	ChangeType ct = change.getChangeType();
		    	DiffType diff = new DiffType();
		    	diff.setType(ct.name());
		    	diff.setNewStartLine(newStartLine);
		    	diff.setNewEndLine(newEndLine);
		    	diff.setOldStartLine(oldStartLine);
		    	diff.setOldEndLine(oldEndLine);
		    	
		    	//newHashList:�����еı仯����Ĺ�ϣ�б�
		    	//oldHashList:�����еı仯����Ĺ�ϣ�б�
		    	//newKeywordList:�����еĹؼ����б�
		    	//oldKeywordList:�����еĹؼ����б�
		    	List<Long> newHashList =new ArrayList<Long>();
		    	List<Long> oldHashList = new ArrayList<Long>();
		    	List<String> newKeywordList = new ArrayList<String>();
		    	List<String> oldKeywordList = new ArrayList<String>();
		    	for(Token token:newTokenList){
		    		if(token.getStartLine()>=newStartLine&&token.getEndLine()<=newEndLine){
		    			if(token.getKeyword()!=null){
		    				newKeywordList.add(token.getKeyword());
		    			}
		    		}
		    	}
		    	for(Token token:oldTokenList){
		    		if(token.getStartLine()>=oldStartLine&&token.getEndLine()<=oldEndLine){
		    			if(token.getKeyword()!=null){
		    				oldKeywordList.add(token.getKeyword());
		    			}
		    		}
		    	}
		    	
		    	
		    	List<Token> diffNewTokenList = new ArrayList<Token>();
				List<Token> diffOldTokenList = new ArrayList<Token>();
				
				for(Token token:newTokenList){
					if(token.getStartLine()>=newStartLine&&token.getEndLine()<=newEndLine){
						diffNewTokenList.add(token);
					}
				}
				
				for(Token token:oldTokenList){
					if(token.getStartLine()>=oldStartLine&&token.getEndLine()<=oldEndLine){
						diffOldTokenList.add(token);
					}
				}
				final int prime = 31;
				
				for(int i=0,n=diffNewTokenList.size();i<n;i++){
					
				    long result = 1;  
				    int index = i;
				    result = prime * result + diffNewTokenList.get(i).getHashNumber();
					for(int j=i+1;j<n&&diffNewTokenList.get(j).getStartLine()==diffNewTokenList.get(i).getStartLine();j++){
						result = prime * result + diffNewTokenList.get(j).getHashNumber();
						index = j;
					}
					i = index+1;
					newHashList.add(result);
				}
				
                for(int i=0,n=diffOldTokenList.size();i<n;i++){
					
				    long result = 1;
				    int index = i;
				    result = prime * result + diffOldTokenList.get(i).getHashNumber();
					for(int j=i+1;j<n&&diffOldTokenList.get(j).getStartLine()==diffOldTokenList.get(i).getStartLine();j++){
						result = prime * result + diffOldTokenList.get(j).getHashNumber();
						index = j;
					}
					i=index+1;
					oldHashList.add(result);
				}
                diff.setNewHashList(newHashList);
		    	diff.setOldHashList(oldHashList);
		    	diff.setNewKeywordList(newKeywordList);
		    	diff.setOldKeywordList(oldKeywordList);
                diff.setNewTokenList(diffNewTokenList);
                diff.setOldTokenList(diffOldTokenList);
		    	
		    	diffList.add(diff);
		    }
		    
		}
		return diffList;
	}

	public static int getLineNumber(String file, int position) throws Exception{
		int lineNum = 1;
		String fileContent = FileUtils.getContent(new File(file));
		char[] charArray = fileContent.toCharArray();
		for(int i=0; i<position&&i<charArray.length; i++){
			if(charArray[i]=='\n'){
				lineNum++;
			}
		}		
		return lineNum;
	}
	
	public static void main(String[] args) throws IOException{
		
		String newPath = "/home/angel/log/test/Test2.java";
		String oldPath = "/home/angel/log/test/Test.java";
		
		String str = FileUtils.getContent(new File(newPath));
		System.out.println(str.length());
		str = FileUtils.getContent(new File(oldPath));
		System.out.println(str.length());
		FileDistiller distiller = ChangeDistiller.createFileDistiller(Language.JAVA);
		try {
		    distiller.extractClassifiedSourceCodeChanges(new File(newPath),new File(oldPath));
		    
		} catch(Exception e) {
		    /* An exception most likely indicates a bug in ChangeDistiller. Please file a
		       bug report at https://bitbucket.org/sealuzh/tools-changedistiller/issues and
		       attach the full stack trace along with the two files that you tried to distill. */
		    System.out.println("Warning: error while change distilling. " + e.getMessage());
		    System.out.println(newPath);
		    System.out.println(oldPath);
		}

		List<SourceCodeChange> changes = distiller.getSourceCodeChanges();
		for(SourceCodeChange change: changes) {
			System.out.println(change.getLabel());
		}
	}
	
	
	
	
}
