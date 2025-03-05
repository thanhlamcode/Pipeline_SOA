package core;

public class Note {
    private int noteId;
    private String content;
    private String type;
    private String status;

    public Note() {}

    public Note(int noteId, String content, String type, String status) {
        this.noteId = noteId;
        this.content = content;
        this.type = type;
        this.status = status;
    }

    // Getters and setters
    public int getNoteId() { return noteId; }
    public void setNoteId(int noteId) { this.noteId = noteId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
