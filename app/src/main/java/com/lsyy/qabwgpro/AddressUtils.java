package com.lsyy.qabwgpro;

public class AddressUtils {
    /**
     * 地球半径，单位：公里/千米
     */
    private static final double EARTH_RADIUS = 6378.137;

    /**
     * 经纬度转化成弧度
     * @param d  经度/纬度
     * @return  经纬度转化成的弧度
     */
    private static double radian(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 返回两个地理坐标之间的距离
     */
    public static double distance(double x, double y,
                                  double Childx, double Childy) {
        double d =Math.sqrt(Math.pow(Math.abs(x - Childx),2) + Math.pow(Math.abs(y - Childy), 2));
        return d;
    }
    public static int disrange(double x, double y,
                                  double Childx, double Childy) { 
        int xx=Math.abs((int)(x-Childx));
        int yy=Math.abs((int)(y-Childy));
        double zz=Math.sqrt(xx*xx+yy*yy);
        int jiaodu=Math.round((float)(Math.asin(yy/zz)/Math.PI*180));
        if (Childx>x){
            if (Childy>y){
                //第四象限
                return jiaodu+270;
            }else{
                //第三象限
                return 270-jiaodu;
            }
        }else{
            if (Childy>y){
                //第二象限
                return jiaodu+90;
            }else{
                //第一象限
                return 90-jiaodu;
            }
        }
    }
   

}
