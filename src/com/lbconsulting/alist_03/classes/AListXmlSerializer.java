package com.lbconsulting.alist_03.classes;

public class AListXmlSerializer {
	private StringBuilder sb;

	private enum Status {
		NOT_STARTED,
		IN_PROGRESS,
		COMPLETED
	}

	private Status status = Status.NOT_STARTED;
	private int level = 0;
	private int numberOfIndentationSpaces = 3;
	private String indentation = "";
	private String leftCarrot = "<";
	private String rightCarrot = ">";
	private String endCarrot = "</";

	public AListXmlSerializer() {
		// Constructor
		for (int i = 0; i < numberOfIndentationSpaces; i++) {
			indentation = indentation + " ";
		}
	}

	private String indent() {
		String result = "";
		if (level > 0) {
			for (int i = 0; i < level; i++) {
				result = result + indentation;
			}
		}
		return result;
	}

	public void startDocument(String encoding, boolean standalone) {
		// <?xml version='1.0' encoding='UTF-8' standalone='yes' ?>
		if (status == Status.NOT_STARTED) {
			status = Status.IN_PROGRESS;

			sb = new StringBuilder();
			level = 0;
			String standaloneValue = "";
			if (standalone) {
				standaloneValue = "yes";
			} else {
				standaloneValue = "no";
			}

			sb.append("<?xml version='1.0' encoding='" + encoding + "' standalone='" + standaloneValue + "' ?>").append("\r\n");
		}
	}

	public void endDocument() {
		status = Status.COMPLETED;
	}

	public void startTag(String Tag) {
		if (status == Status.IN_PROGRESS) {
			sb.append(indent()).append(leftCarrot).append(Tag).append(rightCarrot).append("\r\n");
			level++;
		}
	}

	public void endTag(String Tag) {
		if (status == Status.IN_PROGRESS) {
			level--;
			sb.append(indent()).append(endCarrot).append(Tag).append(rightCarrot).append("\r\n");
		}
	}

	public void text(String Tag, String Text) {
		if (status == Status.IN_PROGRESS) {
			sb.append(indent()).append(leftCarrot).append(Tag).append(rightCarrot);
			sb.append(Text);
			sb.append(endCarrot).append(Tag).append(rightCarrot).append("\r\n");
		}
	}

	public String getXml() {
		String result = "";
		if (status == Status.COMPLETED) {
			result = sb.toString();
		}
		return result;
	}

}
