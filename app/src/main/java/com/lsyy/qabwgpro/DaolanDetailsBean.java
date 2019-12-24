package com.lsyy.qabwgpro;

import java.util.List;

public class DaolanDetailsBean {

    /**
     * data : [{"id":99,"uuid":"16399564738544a1aae222e6ce12a28f","updateTime":"2019-12-18 17:53:53","childName":"ss","childPic":"http://www.guolaiwan.net/file//20191218/1576637416899.jpg,","chineseGirl":"http://www.guolaiwan.net/file//20190309/1552096031675.mp3","chineseBoy":"/20190309/1552096031675.mp3","englishGirl":"/20190309/1552096031675.mp3","englishBoy":"/20190309/1552096031675.mp3","childLongitude":"1","childLatitude":"1","wxChildLongitude":"1","wxChildLatitude":"1","productID":2452,"pics":null,"webUrl":null,"lanId":1,"content":"222","childScale":"小","childRoad":null,"isCen":0,"chineseContent":"222","englishContent":"222","scope":"1","isTaught":0,"linkedPoint":"dd","relevance":"","region":1,"layer":1,"startAngle":null,"endAngle":null},{"id":100,"uuid":"7922fb4ed8b049f1b7b357a1f7a94465","updateTime":"2019-12-18 17:58:43","childName":"www","childPic":"http://www.guolaiwan.net/file//20191218/1576637416899.jpg,http://www.guolaiwan.net/file//20191217/1576573768844.jpg,","chineseGirl":"http://www.guolaiwan.net/file//20190309/1552096025007.mp3","chineseBoy":"/20190309/1552096025007.mp3","englishGirl":"/20190309/1552096025007.mp3","englishBoy":"/20190309/1552096025007.mp3","childLongitude":"11","childLatitude":"11","wxChildLongitude":"11","wxChildLatitude":"11","productID":2452,"pics":null,"webUrl":null,"lanId":1,"content":"","childScale":"小","childRoad":null,"isCen":0,"chineseContent":"","englishContent":"","scope":"1","isTaught":0,"linkedPoint":"q","relevance":"11","region":1,"layer":1,"startAngle":null,"endAngle":null},{"id":101,"uuid":"10b7505a13e6478b8396fe78fc33a11c","updateTime":"2019-12-18 17:59:01","childName":"444","childPic":"http://www.guolaiwan.net/file//20191218/1576637416899.jpg,http://www.guolaiwan.net/file//20191217/1576573768844.jpg,http://www.guolaiwan.net/file//20191217/1576567429441.jpg,","chineseGirl":"http://www.guolaiwan.net/file//20190309/1552096031675.mp3","chineseBoy":"/20190309/1552096031675.mp3","englishGirl":"/20190309/1552096031675.mp3","englishBoy":"/20190309/1552096031675.mp3","childLongitude":"22","childLatitude":"22","wxChildLongitude":"22","wxChildLatitude":"22","productID":2452,"pics":null,"webUrl":null,"lanId":1,"content":"","childScale":"小","childRoad":null,"isCen":0,"chineseContent":"","englishContent":"","scope":"22","isTaught":0,"linkedPoint":"3","relevance":"","region":1,"layer":1,"startAngle":null,"endAngle":null},{"id":102,"uuid":"c4c6ed7734a94cd88881b4470627928a","updateTime":"2019-12-18 18:09:30","childName":"dsad","childPic":"http://www.guolaiwan.net/file//20191218/1576637416899.jpg,http://www.guolaiwan.net/file//20191217/1576573768844.jpg,http://www.guolaiwan.net/file//20191217/1576567429441.jpg,http://www.guolaiwan.net/file//20191218/1576637416879.jpg,","chineseGirl":"http://www.guolaiwan.net/file//20190406/1554520838601.mp3","chineseBoy":"/20190406/1554520838601.mp3","englishGirl":"/20190406/1554520838601.mp3","englishBoy":"/20190406/1554520838601.mp3","childLongitude":"11","childLatitude":"11","wxChildLongitude":"11","wxChildLatitude":"11","productID":2452,"pics":null,"webUrl":null,"lanId":1,"content":"111","childScale":"小","childRoad":null,"isCen":0,"chineseContent":"111","englishContent":"111","scope":"11","isTaught":0,"linkedPoint":"asd","relevance":"","region":1,"layer":1,"startAngle":null,"endAngle":null}]
     * message : 请求成功！
     * status : 200
     */

    private String message;
    private int status;
    private List<DataBean> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 99
         * uuid : 16399564738544a1aae222e6ce12a28f
         * updateTime : 2019-12-18 17:53:53
         * childName : ss
         * childPic : http://www.guolaiwan.net/file//20191218/1576637416899.jpg,
         * chineseGirl : http://www.guolaiwan.net/file//20190309/1552096031675.mp3
         * chineseBoy : /20190309/1552096031675.mp3
         * englishGirl : /20190309/1552096031675.mp3
         * englishBoy : /20190309/1552096031675.mp3
         * childLongitude : 1
         * childLatitude : 1
         * wxChildLongitude : 1
         * wxChildLatitude : 1
         * productID : 2452
         * pics : null
         * webUrl : null
         * lanId : 1
         * content : 222
         * childScale : 小
         * childRoad : null
         * isCen : 0
         * chineseContent : 222
         * englishContent : 222
         * scope : 1
         * isTaught : 0
         * linkedPoint : dd
         * relevance :
         * region : 1
         * layer : 1
         * startAngle : null
         * endAngle : null
         */

        private int id;
        private String uuid;
        private String updateTime;
        private String childName;
        private String childPic;
        private String chineseGirl;
        private String chineseBoy;
        private String englishGirl;
        private String englishBoy;
        private String childLongitude;
        private String childLatitude;
        private String wxChildLongitude;
        private String wxChildLatitude;
        private int productID;
        private Object pics;
        private Object webUrl;
        private int lanId;
        private String content;
        private String childScale;
        private Object childRoad;
        private int isCen;
        private String chineseContent;
        private String englishContent;
        private String scope;
        private int isTaught;
        private String linkedPoint;
        private String relevance;
        private int region;
        private int layer;
        private Object startAngle;
        private Object endAngle;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getChildName() {
            return childName;
        }

        public void setChildName(String childName) {
            this.childName = childName;
        }

        public String getChildPic() {
            return childPic;
        }

        public void setChildPic(String childPic) {
            this.childPic = childPic;
        }

        public String getChineseGirl() {
            return chineseGirl;
        }

        public void setChineseGirl(String chineseGirl) {
            this.chineseGirl = chineseGirl;
        }

        public String getChineseBoy() {
            return chineseBoy;
        }

        public void setChineseBoy(String chineseBoy) {
            this.chineseBoy = chineseBoy;
        }

        public String getEnglishGirl() {
            return englishGirl;
        }

        public void setEnglishGirl(String englishGirl) {
            this.englishGirl = englishGirl;
        }

        public String getEnglishBoy() {
            return englishBoy;
        }

        public void setEnglishBoy(String englishBoy) {
            this.englishBoy = englishBoy;
        }

        public String getChildLongitude() {
            return childLongitude;
        }

        public void setChildLongitude(String childLongitude) {
            this.childLongitude = childLongitude;
        }

        public String getChildLatitude() {
            return childLatitude;
        }

        public void setChildLatitude(String childLatitude) {
            this.childLatitude = childLatitude;
        }

        public String getWxChildLongitude() {
            return wxChildLongitude;
        }

        public void setWxChildLongitude(String wxChildLongitude) {
            this.wxChildLongitude = wxChildLongitude;
        }

        public String getWxChildLatitude() {
            return wxChildLatitude;
        }

        public void setWxChildLatitude(String wxChildLatitude) {
            this.wxChildLatitude = wxChildLatitude;
        }

        public int getProductID() {
            return productID;
        }

        public void setProductID(int productID) {
            this.productID = productID;
        }

        public Object getPics() {
            return pics;
        }

        public void setPics(Object pics) {
            this.pics = pics;
        }

        public Object getWebUrl() {
            return webUrl;
        }

        public void setWebUrl(Object webUrl) {
            this.webUrl = webUrl;
        }

        public int getLanId() {
            return lanId;
        }

        public void setLanId(int lanId) {
            this.lanId = lanId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getChildScale() {
            return childScale;
        }

        public void setChildScale(String childScale) {
            this.childScale = childScale;
        }

        public Object getChildRoad() {
            return childRoad;
        }

        public void setChildRoad(Object childRoad) {
            this.childRoad = childRoad;
        }

        public int getIsCen() {
            return isCen;
        }

        public void setIsCen(int isCen) {
            this.isCen = isCen;
        }

        public String getChineseContent() {
            return chineseContent;
        }

        public void setChineseContent(String chineseContent) {
            this.chineseContent = chineseContent;
        }

        public String getEnglishContent() {
            return englishContent;
        }

        public void setEnglishContent(String englishContent) {
            this.englishContent = englishContent;
        }

        public String getScope() {
            return scope;
        }

        public void setScope(String scope) {
            this.scope = scope;
        }

        public int getIsTaught() {
            return isTaught;
        }

        public void setIsTaught(int isTaught) {
            this.isTaught = isTaught;
        }

        public String getLinkedPoint() {
            return linkedPoint;
        }

        public void setLinkedPoint(String linkedPoint) {
            this.linkedPoint = linkedPoint;
        }

        public String getRelevance() {
            return relevance;
        }

        public void setRelevance(String relevance) {
            this.relevance = relevance;
        }

        public int getRegion() {
            return region;
        }

        public void setRegion(int region) {
            this.region = region;
        }

        public int getLayer() {
            return layer;
        }

        public void setLayer(int layer) {
            this.layer = layer;
        }

        public Object getStartAngle() {
            return startAngle;
        }

        public void setStartAngle(Object startAngle) {
            this.startAngle = startAngle;
        }

        public Object getEndAngle() {
            return endAngle;
        }

        public void setEndAngle(Object endAngle) {
            this.endAngle = endAngle;
        }
    }
}
