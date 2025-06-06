package npa.op.vo;

import com.aspose.words.SaveFormat;

public class FileType {
	private static FileType instance = null;
	
	private String subFileType;
	private String contentType;
	private int savedFormat;
	
	/**
	 * instance FileType.
	 * 
	 * @return FileType
	 */
	public static FileType getInstance() {
		if (instance == null) {
			instance = new FileType();
		}
		
		return instance;
	}

	public String getsubFileType() {
		return subFileType;
	}

	public void setsubFileType(String subFileType) {
		this.subFileType = subFileType;
		
		switch (subFileType) {
		case "pdf":
			setcontentType(subFileType);
			setsavedFormat(SaveFormat.PDF);
			break;

		case "doc":
			setcontentType("ms-word");
			setsavedFormat(SaveFormat.DOC);
			break;
		}
	}
	
	public String getcontentType() {
		return contentType;
	}

	private void setcontentType(String contentType) {
		this.contentType = contentType;
	}
	
	public int getsavedFormat() {
		return savedFormat;
	}

	private void setsavedFormat(int savedFormat) {
		this.savedFormat = savedFormat;
	}
}
