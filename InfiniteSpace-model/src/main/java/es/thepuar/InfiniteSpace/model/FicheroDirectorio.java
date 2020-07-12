package es.thepuar.InfiniteSpace.model;

import java.io.File;

import lombok.Data;

@Data
public class FicheroDirectorio {

    private int Id;
    private File file;
    private String display;


    public FicheroDirectorio(int id, File file) {
        this.Id = id;
        this.file = file;
        this.display = file.getName();
    }

    public FicheroDirectorio(int id, File file, String display) {
        this.Id = id;
        this.file = file;
        this.display = display;
    }

    public long getKB() {
        return file.length() / 1024;
    }

    public double getMB() {
        return (double) file.length() / 1024 / 1024;
    }

    public boolean isVideo(){
        return getTipo().equals("video");
    }
    public boolean isImage(){
        return getTipo().equals("image");
    }
    public boolean isAudio(){
        return getTipo().equals("audio");
    }
    public boolean isUnknown(){
        return getTipo().equals("unknown");
    }


    public String getTipo() {
        if (file.isDirectory())
            return "dir";
        String[] formato = file.getName().split("\\.");
        String extension = formato[formato.length - 1];
        switch (extension.toLowerCase()) {
            case "mov":
            case "avi":
            case "mpg":
            case "mkv":
            case "mpeg":
            case "mp4":
                return "video";
            case "jpg":
            case "png":
            case "jpeg":
            case "bmp":
                return "image";
            case "mp3":
            case "wav":
            case "flac":
                return "audio";
            default:
                return "unknown";

        }
    }

}
