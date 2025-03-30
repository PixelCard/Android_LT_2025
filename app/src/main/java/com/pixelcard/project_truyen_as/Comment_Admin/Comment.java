package com.pixelcard.project_truyen_as.Comment_Admin;

public class Comment {

        private String content,uid,uimg,uname;
        private Object timestamp;

        public Comment(String content, Object timestamp, String uname, String uimg, String uid) {
            this.content = content;
            this.timestamp = timestamp;
            this.uname = uname;
            this.uimg = uimg;
            this.uid = uid;
        }

        public Comment(){

        }
        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getUimg() {
            return uimg;
        }

        public void setUimg(String uimg) {
            this.uimg = uimg;
        }

        public String getUname() {
            return uname;
        }

        public void setUname(String uname) {
            this.uname = uname;
        }

        public Object getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Object timestamp) {
            this.timestamp = timestamp;
        }

        public Comment(String content, String uid, String uimg, String uname, Object timestamp) {
            this.content = content;
            this.uid = uid;
            this.uimg = uimg;
            this.uname = uname;
            this.timestamp = timestamp;
        }
    }
