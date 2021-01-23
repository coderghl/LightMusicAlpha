package com.ghl.lightmusicalpha.pojo;

public class MusicPOJO {
  private int id;
  private String songName;
  private String singerName;
  private String songUri;
  private String songPic;

  public MusicPOJO(int id, String songName, String singerName, String songUri, String songPic) {
    this.id = id;
    this.songName = songName;
    this.singerName = singerName;
    this.songUri = songUri;
    this.songPic = songPic;
  }

  public MusicPOJO(String songName, String singerName, String songUri, String songPic) {
    this.songName = songName;
    this.singerName = singerName;
    this.songUri = songUri;
    this.songPic = songPic;
  }

  public MusicPOJO() {

  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getSongName() {
    return songName;
  }

  public void setSongName(String songName) {
    this.songName = songName;
  }

  public String getSingerName() {
    return singerName;
  }

  public void setSingerName(String singerName) {
    this.singerName = singerName;
  }

  public String getSongUri() {
    return songUri;
  }

  public void setSongUri(String songUri) {
    this.songUri = songUri;
  }

  public String getSongPic() {
    return songPic;
  }

  public void setSongPic(String songPic) {
    this.songPic = songPic;
  }

  @Override
  public String toString() {
    return "MusicPOJO{" +
      "id=" + id +
      ", songName='" + songName + '\'' +
      ", singerName='" + singerName + '\'' +
      ", songUri='" + songUri + '\'' +
      ", songPic='" + songPic + '\'' +
      '}';
  }
}
